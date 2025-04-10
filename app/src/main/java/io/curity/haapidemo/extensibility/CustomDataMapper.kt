package io.curity.haapidemo.extensibility

import se.curity.identityserver.haapi.android.sdk.models.HaapiRepresentation
import se.curity.identityserver.haapi.android.ui.widget.models.FormModel
import se.curity.identityserver.haapi.android.ui.widget.models.HaapiDataMappersFactory
import se.curity.identityserver.haapi.android.ui.widget.models.UIModel
import java.time.Duration

/*
 * This custom data mapper overrides model data from HAAPI responses for particular screens
 */
class CustomDataMapper(
    redirectTo: String,
    autoPollingDuration: Duration,
    useDefaultExternalBrowser: Boolean
) : HaapiDataMappersFactory(redirectTo, autoPollingDuration, useDefaultExternalBrowser) {

    override val mapHaapiResponseToModel: (se.curity.identityserver.haapi.android.sdk.models.HaapiResponse) -> UIModel
        get() = {
            println("GJA: in mapper")
            val defaultModel = super.mapHaapiResponseToModel(it)
            val formModel = defaultModel as? FormModel
            println("GJA: in mapper: ${formModel?.viewName}")
            if (formModel?.viewName == "authenticator/html-form/authenticate/get") {
                HtmlFormLoginModel.fromDefaultModel(formModel)
            } else {
                defaultModel
            }
        }

    /*override val mapHaapiRepresentationToInteraction: (HaapiRepresentation) -> UIModel.Interaction
        get() = {
            println("GJA: in mapper")
            val defaultModel = super.mapHaapiRepresentationToInteraction(it)
            val formModel = defaultModel as? FormModel
            if (formModel != null) {
                if (formModel.viewName == "authenticator/html-form/authenticate/get") {
                    println("GJA: transforming2")
                    HtmlFormLoginModel.fromDefaultModel(formModel)
                    println("GJA: transformed2")
                }
            }

            defaultModel
        }*/
}