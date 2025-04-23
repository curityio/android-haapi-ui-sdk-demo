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

import android.os.Bundle
import android.view.View
import android.widget.TextView
import io.curity.haapidemo.R
import se.curity.identityserver.haapi.android.ui.widget.layouts.FormFragment
import se.curity.identityserver.haapi.android.ui.widget.models.InteractionAction
import se.curity.identityserver.haapi.android.ui.widget.models.UIModel

/*
 * Customize the fragment for the HTML form's login screen
 */
class HtmlFormLoginFragment : FormFragment(layoutId = R.layout.haapi_html_login_form) {

    companion object {
        fun newInstance(uiModel: UIModel) : HtmlFormLoginFragment {
            return HtmlFormLoginFragment().apply {
                arguments = uiModelBundle(uiModel)
            }
        }
    }

    /*
     * Adjust UI elements when the view loads
     */
    override fun preRender(uiModel: UIModel?, view: View, savedInstanceState: Bundle?) {
        super.preRender(uiModel, view, savedInstanceState)

        if (uiModel is HtmlFormLoginModel) {
            val bannerView = view.findViewById<TextView>(R.id.banner_text)
            bannerView.text = uiModel.extraData
        }
    }

    /*
     * Override and do any custom validation before submitting the login data to the Curity Identity Server
     */
    override fun preSubmit(
        interactionAction: InteractionAction,
        keysValues: Map<String, Any>,
        block: (Boolean, Map<String, Any>) -> Unit
    ) {
        super.preSubmit(interactionAction, keysValues, block)
    }
}
