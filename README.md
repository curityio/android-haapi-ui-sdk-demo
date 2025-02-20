# Android Application using the HAAPI UI SDK

[![Quality](https://img.shields.io/badge/quality-test-yellow)](https://curity.io/resources/code-examples/status/)
[![Availability](https://img.shields.io/badge/availability-source-blue)](https://curity.io/resources/code-examples/status/)

An example Android app that uses the Curity Identity Server's Hypermedia API to perform an OIDC flow.\
Authentication uses native screens without the need for an external browser.

## Getting Started

Start with a local automated deployment to ensure that you understand the technical setup.\
You can then apply the same configuration to deployed environments.

### 1. Deploy the the Curity Identity Server

Ensure that the local computer has these prerequisites:

- A Docker engine.
- The `envsubst` tool, e.g with `brew install gettext`.
- The `jq` tool, e.g with `brew install jq`.

First copy a `license.json` file for the Curity Identity Server into the root folder.\
Then run a Docker deployment and indicate how connected simulators or devices call the Curity Identity Server.

For example, run the following commands to connect to a macOS computer using its IP address.\
Or some older Android emulators might require `IDSVR_HOST_NAME` to use the special value `10.0.2.2`.

```bash
export USE_NGROK='false'
export IDSVR_HOST_NAME="$(ipconfig getifaddr en0)"
./start-idsvr.sh
```

### 2. View Security Configuration

The [Mobile Deployments](https://github.com/curityio/mobile-deployments) repository explains further information about the deployed backend infrastructure.\
You can view the [HAAPI Configuration](config/docker-template.xml) to understand the settings to apply to deployed environments.

### 3. Test Basic Logins

Run the app and first test basic logins using an HTML Form authenticator.\
Sign in to the deployed environment and use a pre-shipped test user account.

- Username: `demouser`
- Password: `Password1`

### 4. Test Native Passkey Logins

Passkeys require hosting of assets documents at a trusted internet HTTPS URL.\
You can use ngrok to host assets documents to enable the testing of passkeys logins.\
The following example commands deploy the Curity Identity Server with a passkeys configuration:

```bash
export USE_NGROK='true'
./start-idsvr.sh
```

## Application Code

The following links point you to the most essential areas of the example app's source code.

### Main Source Files

This app only authenticates the user, then displays the tokens obtained from the authorization server.\
See the following source files to understand how that works:

- The [Configuration](app/src/main/java/io/curity/haapidemo/Configuration.kt) object contains all of the OpenID Connect settings.
- The [DemoApplication](app/src/main/java/io/curity/haapidemo/DemoApplication.kt) shows how to create a global object to complete the configuration.
- The [MainActivity](app/src/main/java/io/curity/haapidemo/MainActivity.kt) shows how to use a `haapiFlowLauncher` to implement a login and receive tokens.
- The [AuthenticatedActivity](app/src/main/java/io/curity/haapidemo/authenticated/AuthenticatedActivity.kt) and [TokensFragment](app/src/main/java/io/curity/haapidemo/authenticated/TokensFragment.kt) show how to use tokens to call APIs once authentication completes.

## Customizing the Look and Feel

The [HAAPI Android customization tutorial](https://curity.io/resources/learn/haapi-mobile-android-customization) explains how to change the default theme.\
See also the [Developer Documentation](https://curity.io/docs/haapi-android-ui/latest/) for the finer details of customization options.

## Resources

See the following tutorials for additional developer information:

- The [Kotlin Android App using HAAPI](https://curity.io/resources/learn/kotlin-android-haapi/) tutorial provides an overview of the code example's behaviors.
- The [ngrok tutorials](https://curity.io/resources/learn/mobile-setup-ngrok/) explain how to use an internet URL and [view HAAPI messages](https://curity.io/resources/learn/expose-local-curity-ngrok/#ngrok-inspection-and-status).
- The [Configure Native Passkeys for Mobile Logins](https://curity.io/resources/learn/mobile-logins-using-native-passkeys/) tutorial explains the technical setup when using passkeys.
- The [HAAPI Mobile Guides](https://curity.io/resources/haapi-ui-sdk/) provide further details for HAAPI mobile developers.
- The [Implementing HAAPI Attestation Fallback](https://curity.io/resources/learn/implementing-haapi-fallback/) explains how to manage non-compliant Android devices.

## Further information

Please visit [curity.io](https://curity.io/) for more information about the Curity Identity Server.
