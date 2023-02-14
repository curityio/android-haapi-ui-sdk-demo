# Demo Android Application with HAAPI UI SDK Integration

[![Quality](https://img.shields.io/badge/quality-demo-red)](https://curity.io/resources/code-examples/status/)
[![Availability](https://img.shields.io/badge/availability-source-blue)](https://curity.io/resources/code-examples/status/)

This is an example Android app that uses the Curity Identity Server's Hypermedia API to perform an OIDC flow. The authentication is done within the app, usually without the need for an external browser (depends on the authentication methods used).

Note: The app needs at least Android 8.0 (*Oreo*, API level 26) to utilize attestation features.

## Code Organization

This is a trivial app that only authenticates the user, then displays the tokens obtained from the authorization server. Some classes worth checking out:

- The `Configuration` class contains all the configuration options. You should tailor these to reflect your installation of the Curity Identity Server.
- The `DemoApplication` class shows how to configure the HAAPI UI SDK so that it can be used with the app.
- The `MainActivity` class shows the code needed to integrate with the HAAPI UI SDK. First, the `haapiFlowLauncher` is created together with the callback that will handle the received tokens. Then a listener is used to launch the HAAPI flow activity when a button is clicked.
- The `authenticated` package contains the activity and components used once the user is authenticated. This displays the received tokens and obtains information about the user from the user info endpoint. The `authenticated/TokensFragment` class also shows a way of utilising the HAAPI SDK to refresh an access token.

## Getting started

Note that gradle tasks require at least Java 11 to run properly. Make sure to have the proper Java SDK
version set in `Preferences / Build,Execution,Deployment / Build Tools / Gradle / Gradle JDK` in Android Studio.

### Docker Automated Setup

The required Curity Identity Server setup is provided through a script. To run the setup, follow these steps:

1. Copy a Curity Identity Server license file to `license.json` in the code example root folder.
2. Run the `./start-idsvr.sh` script to deploy a preconfigured Curity Identity Server via Docker. 
3. Build and run the mobile app from Android Studio using an emulator of your choice.
4. There is a preconfigured user account you can sign-in with: demouser / Password1. Feel free to create additional accounts.
5. Run the `./stop-idsvr.sh` script to free Docker resources.

By default the Curity Identity Server instance runs on the the Android emulator's default host IP. 
If you prefer to expose the Server on the Internet (e.g. to test with a real device), you can use the 
ngrok tool for that. Edit the `USE_NGROK` variable in `start-server.sh` and `stop-server.sh` scripts.
Then change the configuration setting `useSSL` to true, as ngrok provides trusted certificates for the connection.

This [Mobile Setup](https://curity.io/resources/learn/mobile-setup-ngrok/) tutorial further describes
this option.

## Running the App

### Emulator

1. Make sure that the Curity Identity Server is running and configured.
2. Start the demo application on an emulator that has at least Android API level 26.
3. Tap the button `Start Authentication` on the home screen to start the authentication flow.

### Physical Device (API level >= 26)

1. Make sure that the Curity Identity Server is running and reachable on the Internet (e.g., by using [ngrok](https://curity.io/resources/learn/expose-local-curity-ngrok/)).
2. Adjust the settings in the `Configuration` class to reflect the instance of your Curity Identity Server.
3. Build then install and start the demo application on your physical device.
4. Tap the button `Start Authentication` on the home screen to start the authentication flow.

## How to get the App Signature?

To allow HAAPI access, you need to add the app's signature digest to the client's configuration in the Curity Identity Server. This should be added in the `Attestation` section of the client's settings.

To get the signature's digest use the `signingReport` task (`./gradlew SigningReport`). Then copy the `SHA-256` signature and paste it in the signature field
in the admin UI. If you want to paste the signature into an XML file, or use the CLI or RestConf API to add the signature,
then you need to use a base64 version of the signature hash. You can run this command to obtain the encoded signature:

```shell
echo "<sha-256 signature from the signingReport>" | xxd -r -p | base64
```

## Configuring the App

The application needs a few configuration options set to be able to call the instance of the Curity Identity Server. Default configuration is set to work with the dockerized version of the Curity Identity Server which is run with the `start-idsvr.sh` script. Should you need to make the app work with a different environment (e.g., you have your own instance of the Curity Identity Server already working online), then you should adjust  the configuration. Edit the default settings in the `src/main/java/io/curity/haapidemo/Configuration.kt` file.  The default settings are returned by the static `newInstance` method, and this is the one that should be modified.

## Resources

- [HAAPI UI SDK Guide](https://curity.io/resources/learn/haapi-mobile-getting-started) that shows all the aspects of working with the Curity's HAAPI UI SDK.
- [A tutorial](https://curity.io/resources/learn/authentication-api-android-sdk) that shows how to properly configure the Curity Identity Server and a client to use the Hypermedia API from an Android app.
- [An article](https://curity.io/resources/learn/what-is-hypermedia-authentication-api/) that explains the Hypermedia Authentication API.

## More information

Please visit [curity.io](https://curity.io/) for more information about the Curity Identity Server.
