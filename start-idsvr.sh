#!/bin/bash

####################################################################################################
# A developer deployment to run the Curity Identity Server in Docker on the local computer.
# The deployment provides a working configuration and includes native passkeys automation.
# - https://curity.io/resources/learn/mobile-logins-using-native-passkeys/
####################################################################################################

cd "$(dirname "${BASH_SOURCE[0]}")"

#
# First check for a license
#
if [ ! -f './license.json' ]; then
  echo 'Please copy a license.json file into the root folder of the code example'
  exit 1
fi

#
# Then validate or default parameters
#
EXAMPLE_NAME='haapi'
if [ "$USE_NGROK" != 'true' ]; then
  USE_NGROK='false'
  if [ "$IDSVR_HOST_NAME" == '' ]; then
    echo 'You must supply an IDSVR_HOST_NAME for the Curity Identity Server'
    exit 1
  fi
fi

#
# Download mobile deployment resources
#
rm -rf deployment
git clone https://github.com/curityio/mobile-deployments deployment
if [ $? -ne 0 ]; then
  echo 'Problem encountered downloading deployment resources'
  exit 1
fi

#
# TODO: delete before merging
#
cd deployment
git checkout feature/sdk_update
cd ..

#
# Override the HAAPI default configuration settings
#
cp config/docker-template.xml deployment/haapi/example-config-template.xml

#
# Run an automated deployment of the Curity Identity Server
#
cp ./license.json deployment/resources/license.json
if [ "$USE_NGROK" == 'true' ]; then
  ./deployment/start.sh 'true' '-' "$EXAMPLE_NAME"
else
  ./deployment/start.sh 'false' "https://$IDSVR_HOST_NAME:8443" "$EXAMPLE_NAME"
fi
if [ $? -ne 0 ]; then
  echo 'Problem encountered deploying the Curity Identity Server'
  exit 1
fi

#
# Get the final Curity Identity Server URL
#
export IDSVR_BASE_URL="$(cat ./deployment/output.txt)"
echo "Curity Identity Server is running at $IDSVR_BASE_URL"

#
# Configure the Android app to use the Curity Identity Server's base URL
#
function replaceTextInFile() {

  FROM="$1"
  TO="$2"
  FILE="$3"
  
  if [ "$(uname -s)" == 'Darwin' ]; then
    sed -i '' "s/$FROM/$TO/g" "$FILE"
  else
    sed -i -e "s/$FROM/$TO/g" "$FILE"
  fi
}

IDSVR_HOST_NAME="${IDSVR_BASE_URL#https://}"
replaceTextInFile '10.0.2.2:8443' "$IDSVR_HOST_NAME" './app/src/main/java/io/curity/haapidemo/Configuration.kt'
if [ "$USE_NGROK" == 'true' ]; then
  replaceTextInFile '10.0.2.2:8443' "$IDSVR_HOST_NAME" './app/src/main/res/values/strings.xml'
fi
