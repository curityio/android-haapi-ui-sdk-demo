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
    val bannerData: String,
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
                bannerData = "Enter your SecureBank credentials",
                interactionItems = formModel.interactionItems.filter { !it.key.contains("Login") },
                linkItems = formModel.linkItems.filter { !it.href.contains("forgot-account-id") },
                messageItems = formModel.messageItems,
                templateArea = formModel.templateArea,
                formModel.viewName
            )
        }
    }
}
