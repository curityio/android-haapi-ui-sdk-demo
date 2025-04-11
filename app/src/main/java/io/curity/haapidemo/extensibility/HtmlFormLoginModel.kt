package io.curity.haapidemo.extensibility

import kotlinx.parcelize.Parcelize
import se.curity.identityserver.haapi.android.ui.widget.models.FormModel
import se.curity.identityserver.haapi.android.ui.widget.models.InfoMessageModel
import se.curity.identityserver.haapi.android.ui.widget.models.InteractionItem
import se.curity.identityserver.haapi.android.ui.widget.models.LinkItemModel

/*
 * Customize the data for the HTML form's login screen
 */
@Parcelize
data class HtmlFormLoginModel(
    val userMessage: String,
    override val interactionItems: List<InteractionItem>,
    override val linkItems: List<LinkItemModel>,
    override val messageItems: List<InfoMessageModel>,
    override val templateArea: String?,
    override val viewName: String?

) : FormModel {

    companion object {

        /*
         * Remove the Forgot Username link
         * Also replace the Login title with some custom banner text
         */
        fun fromDefaultModel(formModel: FormModel): HtmlFormLoginModel {

            return HtmlFormLoginModel(
                userMessage = "Enter your SecureBank credentials",
                interactionItems = formModel.interactionItems.filter { it.key != "login" },
                linkItems = formModel.linkItems.filter { !it.href.contains("forgot-account-id") },
                messageItems = formModel.messageItems,
                templateArea = formModel.templateArea,
                formModel.viewName
            )
        }
    }
}
