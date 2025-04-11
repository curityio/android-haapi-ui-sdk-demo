package io.curity.haapidemo.extensibility

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import io.curity.haapidemo.R
import se.curity.identityserver.haapi.android.ui.widget.layouts.HaapiFlowFragment
import se.curity.identityserver.haapi.android.ui.widget.models.SelectorItemModel
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

        val formModel = extractUIModelFromArguments<SelectorModel>()
        val selectorItems = formModel.selectorItems.map { it as SelectorItemModel }

        // Sort selector items so that passkeys is shown first
        val sortedItems = selectorItems.sortedWith { first, second ->
            when {
                first.type == "passkeys" -> -1
                second.type == "passkeys" -> 1
                else -> first.title.compareTo(second.title)
            }
        }

        // Set up the authentication selection items for a recycler view
        val recyclerView = view.findViewById(R.id.selector_items_recycler_view) as? RecyclerView
        val viewModels = sortedItems.map { AuthenticationSelectionItemViewModel(it) }

        // Populate the recycler view and handle select events to send the selection to the server
        recyclerView?.adapter = AuthenticationSelectionArrayAdapter(viewModels, {
            select(it)
        })
    }
}
