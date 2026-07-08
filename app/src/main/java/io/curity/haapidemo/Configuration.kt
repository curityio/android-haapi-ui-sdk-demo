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

import se.curity.identityserver.haapi.android.driver.HaapiLogger

/**
 * Configuration for `HaapiFlowManager`
 *
 * @property [clientId] A String that represents a clientId. This value is important and needs to match with the curity
 * identity server configuration.
 * @property [baseURLString] A String that represents a base URL that points to the Curity Identity Server.
 * @property [tokenEndpointPath] A String that represents the path to the token endpoint.
 * @property [authorizationEndpointPath] A String that represents the path to the authorization endpoint.
 * @property [userInfoEndpointPath] A String that represents the path to the user info endpoint.
 * @property [redirectURI] A String that represents the redirect URI. This should match the setting for the client in the Curity Identity Server.
 * Since HAAPI does not actually use browser redirects, this can be any valid URI.
 * @property [scope] A list of strings that represents the scope requested from the authorization server.
 * @property [useSSL] Set this to true if you have an instance of the Curity Identity Server with
 * TLS certificates that the app can trust (e.g., when exposing the Curity Identity Server with ngrok — see README)
 */

data class Configuration(
    var clientId: String,
    var baseURLString: String,
    var tokenEndpointPath: String,
    var authorizationEndpointPath: String,
    var userInfoEndpointPath: String,
    var redirectURI: String,
    var isAutoAuthorizationChallengedEnabled: Boolean = true,
    var scope: List<String> = emptyList(),
    val useSSL: Boolean,

    // To implement HAAPI fallback, customers must define their own DCR related settings
    // These will vary depending on the type of credential being used
    var dcrTemplateClientId: String? = null,
    var dcrClientRegistrationEndpointPath: String? = null,
    var deviceSecret: String? = null
) {

    val userInfoURI = baseURLString + userInfoEndpointPath

    /*
    // Uncomment to troubleshoot errors
    init {
        // HaapiLogger.enabled = true
        // HaapiLogger.setLevel(HaapiLogger.LogLevel.DEBUG)
        // HaapiLogger.isSensitiveValueMasked = false
    }*/

    companion object {
        fun newInstance(): Configuration =
            Configuration(
                clientId = "haapi-android-client",
                baseURLString = "https://10.0.2.2:8443",
                tokenEndpointPath = "/oauth/v2/oauth-token",
                authorizationEndpointPath = "/oauth/v2/oauth-authorize",
                userInfoEndpointPath = "/oauth/v2/oauth-userinfo",
                redirectURI = "haapi://callback",
                scope = listOf("openid", "profile"),
                useSSL = false,

                // Uncomment these fields to add support for HAAPI DCR fallback with a simple credential
                // dcrTemplateClientId = "haapi-template-client",
                // dcrClientRegistrationEndpointPath = "/token-service/oauth-registration",
                // deviceSecret = "Password1"
            )
    }
}