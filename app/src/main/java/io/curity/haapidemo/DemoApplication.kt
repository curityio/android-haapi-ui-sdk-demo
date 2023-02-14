package io.curity.haapidemo

import android.app.Application
import io.curity.haapidemo.utils.disableSslTrustVerification
import se.curity.identityserver.haapi.android.ui.widget.HaapiUIWidgetApplication
import se.curity.identityserver.haapi.android.ui.widget.WidgetConfiguration
import java.net.HttpURLConnection
import java.net.URI

class DemoApplication: Application(), HaapiUIWidgetApplication {
    val configuration = Configuration.newInstance()
    private val haapiWidgetConfiguration = run {
        println("Preparing config")

        val baseUri = URI(configuration.baseURLString)
        WidgetConfiguration.Builder(
            clientId = configuration.clientId,
            baseUri = baseUri,
            tokenEndpointUri = baseUri.resolve(configuration.tokenEndpointPath),
            authorizationEndpointUri = baseUri.resolve(configuration.authorizationEndpointPath),
            appRedirect = configuration.redirectURI
        )
            .setOauthAuthorizationParamsProvider {
                WidgetConfiguration.OAuthAuthorizationParams(
                    scope = configuration.scope
                )
            }

//            Uncomment the following lines if you need to connect to an instance
//            of the Curity Identity Server that uses self-signed certificates.

//            .setHttpUrlConnectionProvider { url ->
//                val urlConnection = url.openConnection()
//                urlConnection.connectTimeout = 8000
//                    urlConnection.disableSslTrustVerification() as HttpURLConnection
//            }

            .build()
    }
    override val widgetConfiguration: WidgetConfiguration
        get() = haapiWidgetConfiguration
}
