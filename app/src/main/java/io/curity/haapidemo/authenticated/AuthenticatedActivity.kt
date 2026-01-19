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

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import io.curity.haapidemo.R
import se.curity.identityserver.haapi.android.ui.widget.models.OauthModel

class AuthenticatedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_authenticated)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.authenticatorLayout)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userInfoURIString = intent.getStringExtra(EXTRA_AUTHENTICATED_ACTIVITY_USERINFO_URI_STRING)

        @Suppress("DEPRECATION")
        val tokenResponse = intent.getParcelableExtra<OauthModel.Token>(
            EXTRA_AUTHENTICATED_ACTIVITY_TOKEN_RESPONSE
        )

        if (userInfoURIString == null || tokenResponse == null) {
            throw IllegalArgumentException("You need to use AuthenticatedActivity.newIntent(...)")
        }

        val tokensFragment = TokensFragment.newInstance(tokenResponse, userInfoURIString)

        supportFragmentManager.commit {
            replace(R.id.fragment_container, tokensFragment)
        }
    }

    companion object {

        private const val EXTRA_AUTHENTICATED_ACTIVITY_USERINFO_URI_STRING = "io.curity.haapidemo.authenticatedActivity.userinfo_uri_string"
        private const val EXTRA_AUTHENTICATED_ACTIVITY_TOKEN_RESPONSE = "io.curity.haapidemo.authenticatedActivity.extra_token_response"

        fun newIntent(context: Context,
                      userInfoURIString: String,
                      tokenResponse: OauthModel.Token
        ): Intent {
            val intent = Intent(context, AuthenticatedActivity::class.java)
            intent.putExtra(EXTRA_AUTHENTICATED_ACTIVITY_USERINFO_URI_STRING, userInfoURIString)
            intent.putExtra(EXTRA_AUTHENTICATED_ACTIVITY_TOKEN_RESPONSE, tokenResponse)
            return intent
        }
    }
}