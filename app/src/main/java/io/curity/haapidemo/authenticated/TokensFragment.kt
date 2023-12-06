/*
 *  Copyright (C) 2023 Curity AB
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.curity.haapidemo.authenticated

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.*
import io.curity.haapidemo.R
import io.curity.haapidemo.DemoApplication
import io.curity.haapidemo.authenticated.uicomponents.DecodedTokenView
import io.curity.haapidemo.authenticated.uicomponents.DisclosureContent
import io.curity.haapidemo.authenticated.utils.JWTUtils
import io.curity.haapidemo.utils.disableSslTrustVerification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.FileNotFoundException
import java.net.HttpURLConnection
import java.net.URI
import io.curity.haapidemo.databinding.FragmentTokensBinding
import io.curity.haapidemo.uicomponents.ProgressButton
import org.json.JSONObject
import se.curity.identityserver.haapi.android.ui.widget.HaapiFlowViewModel
import se.curity.identityserver.haapi.android.ui.widget.HaapiFlowViewModelFactory
import se.curity.identityserver.haapi.android.ui.widget.models.OauthModel

class TokensFragment: Fragment() {

    private lateinit var binding: FragmentTokensBinding

    private val tokensViewModel: TokensViewModel by activityViewModels {
        TokensViewModelFactory(
            requireActivity().application,
            requireArguments().getString(EXTRA_USERINFO_URI_STRING) ?: throw IllegalStateException("Expecting a configuration"),
            requireArguments().getParcelable(EXTRA_OAUTH_TOKEN_RESPONSE) ?: throw IllegalStateException("Expecting a TokenResponse"),
            requireActivity()
        )
    }

    private val haapiViewModel: HaapiFlowViewModel by activityViewModels {
        HaapiFlowViewModelFactory(tokensViewModel.getApplication<DemoApplication>())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTokensBinding.inflate(layoutInflater)
        binding.model = tokensViewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tokensViewModel.haapiViewModel = haapiViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tokensViewModel.liveUserInfo.observe(viewLifecycleOwner, Observer {
            it?.let {
                DecodedTokenView(requireContext(), R.string.user_info, JSONObject(it), binding.userinfoLayout)
            }
        })


        haapiViewModel.liveOauthModel.observe(viewLifecycleOwner, Observer {
            binding.refreshButton.setLoading(false)
            when (it) {
                is OauthModel.Token -> tokensViewModel.refreshView(it)
                is OauthModel.Error -> println("There was an error: $it")
                else -> {}
            }
        })

        tokensViewModel.liveTokenResponse.observe(viewLifecycleOwner, Observer {
            binding.accessDisclosureView.setContentText(tokensViewModel.accessToken)
            binding.accessDisclosureView.setDisclosureContents(tokensViewModel.disclosureContents)

            val idToken = tokensViewModel.idToken
            if (idToken != null) {
                JWTUtils.extractJwt(idToken)?.let {
                    binding.idTokenView.setContentText(idToken)
                    binding.idTokenView.setDecodedContent(it)
                } ?: run {
                    binding.idTokenView.setContentText(idToken)
                }
            }

            val refreshToken = tokensViewModel.refreshToken
            if (refreshToken != null) {
                binding.refreshDisclosureView.setContentText(refreshToken)
            }
        })
    }

    class TokensViewModel(
        app: Application,
        private val userInfoURI: URI,
        private var tokenResponse: OauthModel.Token,
        private val logoutParent: () -> Unit
    ): AndroidViewModel(app) {
        var haapiViewModel: HaapiFlowViewModel? = null
        private var _tokenResponse: MutableLiveData<OauthModel.Token> = MutableLiveData(tokenResponse)
        val liveTokenResponse: LiveData<OauthModel.Token>
            get() = _tokenResponse

        private var _liveUserInfo: MutableLiveData<String?> = MutableLiveData(null)
        val liveUserInfo: LiveData<String?>
            get() = _liveUserInfo

        val accessToken: String
            get() = tokenResponse.accessToken
        val idToken: String?
            get() = tokenResponse.idToken
        val refreshToken: String?
            get() = tokenResponse.refreshToken

        private var _disclosureContents: MutableList<DisclosureContent> = mutableListOf()
        val disclosureContents: List<DisclosureContent>
            get() = _disclosureContents

        init {
            updateDisclosureContents()
            fetchUserInfo()
        }

        fun refreshView(result: OauthModel.Token) {
            tokenResponse = result
            updateDisclosureContents()
            fetchUserInfo()
            _tokenResponse.postValue(result)
        }

        private fun updateDisclosureContents() {
            val mutableDisclosureContents = mutableListOf<DisclosureContent>()
            tokenResponse.tokenType?.let {
                mutableDisclosureContents.add(
                    DisclosureContent(
                        label = "token_type",
                        description = it
                    )
                )
            }
            tokenResponse.scope?.let {
                mutableDisclosureContents.add(
                    DisclosureContent(
                        label = "scope",
                        description = it
                    )
                )
            }
            mutableDisclosureContents.add(
                DisclosureContent(
                    label = "expires_in",
                    description = tokenResponse.expiresIn.seconds.toString()
                )
            )
            _disclosureContents = mutableDisclosureContents
        }

        fun refreshToken(view: View) {
            (view as ProgressButton).setLoading(true)
            val refreshToken = tokenResponse.refreshToken
            if (refreshToken != null) {

                viewModelScope.launch {
                        haapiViewModel?.refreshAccessToken(refreshToken)
                }
            }
        }

        // Destroy HAAPI view resources on logout
        fun logout(view: View) {
            haapiViewModel = null
            logoutParent()
        }

        private fun fetchUserInfo() {
            viewModelScope.launch {
                val response = withContext(Dispatchers.IO) {
                    val httpURLConnection = userInfoURI
                        .toURL().openConnection().disableSslTrustVerification() as HttpURLConnection

                    httpURLConnection.requestMethod = "GET"
                    httpURLConnection.doInput = true
                    httpURLConnection.doOutput = false

                    httpURLConnection.setRequestProperty("Authorization", "bearer ${tokenResponse.accessToken}")

                    try {
                        httpURLConnection.inputStream.bufferedReader().use { it.readText() }
                    } catch (fileNotFoundException: FileNotFoundException) {
                        httpURLConnection.errorStream.bufferedReader().use { it.readText() }
                    } finally {
                        httpURLConnection.disconnect()
                    }
                }
                _liveUserInfo.postValue(response)
            }
        }
    }

    class TokensViewModelFactory(
        private val app: Application,
        private val userInfoURIString: String,
        private val tokenResponse: OauthModel.Token,
        private val activity: Activity
    ): ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TokensViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return TokensViewModel(app, URI(userInfoURIString), tokenResponse) {
                    activity.finish()
                } as T
            }

            throw IllegalArgumentException("Unknown ViewModel class TokensViewModel")
        }
    }

    companion object {
        private const val EXTRA_OAUTH_TOKEN_RESPONSE = "io.curity.fragment_tokens.extra_oauth_token_response"
        private const val EXTRA_USERINFO_URI_STRING = "io.curity.fragment_tokens.extra_userinfo_uri_string"

        fun newInstance(tokenResponse: OauthModel.Token,
                        userInfoURIString: String
        ): TokensFragment {
            val fragment = TokensFragment()
            fragment.arguments = Bundle().apply {
                putParcelable(EXTRA_OAUTH_TOKEN_RESPONSE, tokenResponse)
                putString(EXTRA_USERINFO_URI_STRING, userInfoURIString)
            }
            return fragment
        }
    }
}