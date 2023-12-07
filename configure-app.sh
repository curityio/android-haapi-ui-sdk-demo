#!/bin/bash

####################################################
# When using ngrok, this makes configuration updates
####################################################

cd "$(dirname "${BASH_SOURCE[0]}")"

BASE_URL='https://10.0.2.2:8443'

#
# Update URLs referenced in the code example to match NGROK
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

RUNTIME_BASE_URL=$(curl -s http://localhost:4040/api/tunnels | jq -r '.tunnels[] | select(.proto == "https") | .public_url')
if [ $RUNTIME_BASE_URL != '' ]; then

  BASE_URL=${BASE_URL//\//\\/}
  RUNTIME_BASE_URL=${RUNTIME_BASE_URL//\//\\/}
  
  # Override the configuration URL to enable internet connectivity from any device
  replaceTextInFile $BASE_URL $RUNTIME_BASE_URL './app/src/main/java/io/curity/haapidemo/Configuration.kt'
  
  # Override the asset statements URL to enable passkeys to work
  replaceTextInFile $BASE_URL $RUNTIME_BASE_URL './app/src/main/res/values/strings.xml'
fi
