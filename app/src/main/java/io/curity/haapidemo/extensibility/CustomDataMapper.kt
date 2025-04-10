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