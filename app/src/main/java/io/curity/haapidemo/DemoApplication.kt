package io.curity.haapidemo

import android.app.Application
import io.curity.haapidemo.utils.disableSslTrustVerification
import se.curity.identityserver.haapi.android.driver.KeyPairAlgorithmConfig
import se.curity.identityserver.haapi.android.driver.Storage
import se.curity.identityserver.haapi.android.driver.TokenBoundConfiguration
import se.curity.identityserver.haapi.android.sdk.util.ExperimentalWebAuthnApi
import se.curity.identityserver.haapi.android.ui.widget.HaapiUIWidgetApplication
import se.curity.identityserver.haapi.android.ui.widget.WidgetConfiguration
import java.net.HttpURLConnection
import java.net.URI

class DemoApplication: Application(), HaapiUIWidgetApplication {
    val configuration = Configuration.newInstance()

    private val tokenBoundConfiguration = TokenBoundConfiguration(
        keyAlias = "tokenBoundKey",
        keyPairAlgorithmConfig = KeyPairAlgorithmConfig.ES256,
        storage = object : Storage {
            override fun set(value: String, key: String) {
            }

            override fun get(key: String): String? {
                return null
            }

            override fun delete(key: String) {
            }

            override fun getAll(): Map<String, String> {
                return mapOf()
            }
        },
        currentTimeMillisProvider = { System.currentTimeMillis() }
    )

    @OptIn(ExperimentalWebAuthnApi::class)
    private val haapiWidgetConfiguration = run {
        val baseUri = URI(configuration.baseURLString)
        val builder = WidgetConfiguration.Builder(
            clientId = configuration.clientId,
            baseUri = baseUri,
            tokenEndpointUri = baseUri.resolve(configuration.tokenEndpointPath),
            authorizationEndpointUri = baseUri.resolve(configuration.authorizationEndpointPath),
            appRedirect = configuration.redirectURI,
        )
        .setUseNativeWebAuthnSupport(true)
        .setTokenBoundConfiguration(tokenBoundConfiguration)
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
}
