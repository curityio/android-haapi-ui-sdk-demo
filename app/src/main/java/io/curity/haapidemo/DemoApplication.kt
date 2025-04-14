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

package io.curity.haapidemo

import android.app.Application
import io.curity.haapidemo.extensibility.CustomDataMapper
import io.curity.haapidemo.extensibility.CustomFragmentFactory
import io.curity.haapidemo.utils.SharedPreferenceStorage
import io.curity.haapidemo.utils.disableSslTrustVerification
import se.curity.identityserver.haapi.android.driver.KeyPairAlgorithmConfig
import se.curity.identityserver.haapi.android.driver.TokenBoundConfiguration
import se.curity.identityserver.haapi.android.ui.widget.ExperimentalHaapiApi
import se.curity.identityserver.haapi.android.ui.widget.FragmentResolver
import se.curity.identityserver.haapi.android.ui.widget.HaapiUIWidgetApplication
import se.curity.identityserver.haapi.android.ui.widget.WidgetConfiguration
import se.curity.identityserver.haapi.android.ui.widget.models.DataMappersFactory
import java.net.HttpURLConnection
import java.net.URI

class DemoApplication: Application(), HaapiUIWidgetApplication {
    val configuration = Configuration.newInstance()
    val fragmentFactory = CustomFragmentFactory()

    private val haapiWidgetConfiguration = run {

        val baseUri = URI(configuration.baseURLString)
        val builder = WidgetConfiguration.Builder(
            clientId = configuration.clientId,
            baseUri = baseUri,
            tokenEndpointUri = baseUri.resolve(configuration.tokenEndpointPath),
            authorizationEndpointUri = baseUri.resolve(configuration.authorizationEndpointPath),
            appRedirect = configuration.redirectURI,
        )
            .setTokenBoundConfiguration(createTokenBoundConfiguration())
            .setOauthAuthorizationParamsProvider {
                WidgetConfiguration.OAuthAuthorizationParams(
                    scope = configuration.scope
                )
        }

        if (!configuration.useSSL) {
            builder.setHttpUrlConnectionProvider { url ->
                val urlConnection = url.openConnection()
                urlConnection.connectTimeout = 8000
                urlConnection.disableSslTrustVerification() as HttpURLConnection
            }
        }

        if (configuration.dcrTemplateClientId != null) {
            builder
                .setDcr(WidgetConfiguration.Dcr(
                    templateClientId = configuration.dcrTemplateClientId!!,
                    clientRegistrationEndpointUri = baseUri.resolve(configuration.dcrClientRegistrationEndpointPath),
                    context = this
                ))
                .setClientAuthenticationMethod(
                    WidgetConfiguration.ClientAuthenticationMethod.Secret(secret = configuration.deviceSecret!!)
                )
        }

        builder.build()
    }

    override val widgetConfiguration: WidgetConfiguration
        get() = haapiWidgetConfiguration

    /*
     * Create extensibility objects to override model data
     */
    @OptIn(ExperimentalHaapiApi::class)
    override val dataMappersFactory: DataMappersFactory
        get() = CustomDataMapper(
            redirectTo = configuration.redirectURI,
            autoPollingDuration = widgetConfiguration.autoPollingDuration,
            useDefaultExternalBrowser = widgetConfiguration.useDefaultExternalBrowser
        )

    /*
     * Create extensibility objects to override view logic
     */
    @OptIn(ExperimentalHaapiApi::class)
    override val fragmentResolver: FragmentResolver = fragmentFactory

    /*
     * This object is required in order to use the recommended options to protect OAuth token requests with DPoP JWTs
     * - By default only an authorization server provided DPoP nonce is stored
     * - For devices that do not support attestation, this also stores an ephemeral key used to issue DPoP JWTs
     *
     * This implementation uses shared preferences, so that this data is available across application restarts
     * A different type of storage can be used if preferred
     */
    private fun createTokenBoundConfiguration(): TokenBoundConfiguration {

        return TokenBoundConfiguration(
            keyAlias = "tokenBoundKey",
            keyPairAlgorithmConfig = KeyPairAlgorithmConfig.ES256,
            storage = SharedPreferenceStorage(this),
            currentTimeMillisProvider = { System.currentTimeMillis() }
        )
    }
}
