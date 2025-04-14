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
            bannerView.text = uiModel.bannerData
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
