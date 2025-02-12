package io.curity.haapidemo

import android.app.Application
import io.curity.haapidemo.utils.SharedPreferenceStorage
import io.curity.haapidemo.utils.disableSslTrustVerification
import se.curity.identityserver.haapi.android.driver.KeyPairAlgorithmConfig
import se.curity.identityserver.haapi.android.driver.TokenBoundConfiguration
import se.curity.identityserver.haapi.android.ui.widget.HaapiUIWidgetApplication
import se.curity.identityserver.haapi.android.ui.widget.WidgetConfiguration
import java.net.HttpURLConnection
import java.net.URI

class DemoApplication: Application(), HaapiUIWidgetApplication {
    val configuration = Configuration.newInstance()

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
