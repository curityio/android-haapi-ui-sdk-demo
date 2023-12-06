#!/bin/bash

####################################################################################################
# Run the Curity Identity Server in Docker on the local computer, preconfigured for the code example
# Please ensure that the following resources are installed before running this script:
# - Docker Desktop
# - The envsubst tool (`brew install gettext` on MacOS)
#
# If you want to expose the local instance of the Curity Identity Server via ngrok, then the following
# need to also be installed:
# - ngrok
# - The jq tool (`brew install jq` on MacOS)
####################################################################################################

cd "$(dirname "${BASH_SOURCE[0]}")"

#
# By default the Curity Identity Server will use the Android emulator's default host IP.
# Set USE_NGROK to true and a dynamic NGROK base URL will be used automatically.
#
export EXAMPLE_NAME='haapi'
export IDSVR_BASE_URL='https://10.0.2.2:8443'
# export USE_NGROK='true'

#
# First check prerequisites
#
if [ ! -f './license.json' ]; then
  echo 'Please copy a license.json file into the root folder of the code example'
  exit 1
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
# TODO: delete after merge
#
cd deployment
git checkout feature/passkeys
cd ..

#
# Run an automated deployment of the Curity Identity Server
#
cp ./license.json deployment/resources/license.json
./deployment/start.sh
if [ $? -ne 0 ]; then
  echo 'Problem encountered deploying the Curity Identity Server'
  exit 1
fi

#
# Inform the user of the Curity Identity Server URL, to be copied to configuration
#
echo "Curity Identity Server is running at $(cat ./deployment/output.txt)"
