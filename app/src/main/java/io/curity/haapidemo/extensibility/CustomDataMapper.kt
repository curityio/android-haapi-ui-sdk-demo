/*
 *  Copyright (C) 2025 Curity AB
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

package io.curity.haapidemo.extensibility

import se.curity.identityserver.haapi.android.ui.widget.ExperimentalHaapiApi
import se.curity.identityserver.haapi.android.ui.widget.models.FormModel
import se.curity.identityserver.haapi.android.ui.widget.models.HaapiDataMappersFactory
import se.curity.identityserver.haapi.android.ui.widget.models.UIModel
import java.time.Duration

/*
 * This custom data mapper overrides model data from HAAPI responses for particular screens
 */
@OptIn(ExperimentalHaapiApi::class)
class CustomDataMapper(
    redirectTo: String,
    autoPollingDuration: Duration,
    useDefaultExternalBrowser: Boolean
) : HaapiDataMappersFactory(redirectTo, autoPollingDuration, useDefaultExternalBrowser) {

    override val mapHaapiResponseToModel: (se.curity.identityserver.haapi.android.sdk.models.HaapiResponse) -> UIModel
        get() = {
            val defaultModel = super.mapHaapiResponseToModel(it)
            val formModel = defaultModel as? FormModel
            if (formModel?.viewName == "authenticator/html-form/authenticate/get") {
                HtmlFormLoginModel.fromDefaultModel(formModel)
            } else {
                defaultModel
            }
        }
}