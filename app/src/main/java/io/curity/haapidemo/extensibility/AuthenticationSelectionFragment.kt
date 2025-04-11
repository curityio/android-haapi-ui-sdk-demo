package io.curity.haapidemo.extensibility

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import io.curity.haapidemo.R
import se.curity.identityserver.haapi.android.ui.widget.layouts.HaapiFlowFragment
import se.curity.identityserver.haapi.android.ui.widget.models.InteractionAction
import se.curity.identityserver.haapi.android.ui.widget.models.SelectorModel
import se.curity.identityserver.haapi.android.ui.widget.models.UIModel

/*
 * Demonstrates replacing an entire screen with a completely custom layout
 * This example renders the authentication selection screen in a custom way
 * Passkeys are rendered first to encourage users to choose a modern and secure login method
 */
class AuthenticationSelectionFragment: HaapiFlowFragment<SelectorModel>(R.layout.haapi_authentication_selector) {

    companion object {
        fun newInstance(uiModel: UIModel) : AuthenticationSelectionFragment {
            return AuthenticationSelectionFragment().apply {
                arguments = uiModelBundle(uiModel)
            }
        }
    }

    /*
     * Render a header followed by authentication selection items
     * Each item is a heading and a button
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        println("GJA: Setting adapter for the recycler view")
        val formModel = extractUIModelFromArguments<SelectorModel>()
        val recyclerView = view.findViewById(R.id.selector_items_recycler_view) as? RecyclerView
        recyclerView?.adapter = AuthenticationSelectionArrayAdapter(formModel.selectorItems)
        println("GJA: Adapter set OK")

        /*formModel.selectorItems.forEach( {
            println("GJA: got selector item: ${it.title}")
        })*/
    }

    /*
     * The custom view must submit the correct authentication selection to the server
     */
    override fun preSubmit(
        interactionAction: InteractionAction,
        keysValues: Map<String, Any>,
        block: (Boolean, Map<String, Any>) -> Unit
    ) {
    }
}
