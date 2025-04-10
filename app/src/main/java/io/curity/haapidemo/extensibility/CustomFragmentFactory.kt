package io.curity.haapidemo.extensibility

import se.curity.identityserver.haapi.android.ui.widget.ExperimentalHaapiApi
import se.curity.identityserver.haapi.android.ui.widget.FragmentResolver
import se.curity.identityserver.haapi.android.ui.widget.HaapiFlowFragmentResolver
import se.curity.identityserver.haapi.android.ui.widget.layouts.FormFragment
import se.curity.identityserver.haapi.android.ui.widget.layouts.HaapiFlowFragment
import se.curity.identityserver.haapi.android.ui.widget.models.FormModel
import se.curity.identityserver.haapi.android.ui.widget.models.UIModel

/*
 * Create custom fragments for particular login screens
 */
@OptIn(ExperimentalHaapiApi::class)
class CustomFragmentFactory : FragmentResolver {

    private val defaultResolver = HaapiFlowFragmentResolver.Builder().build()

    /*
     * Override particular types of models
     */
    override fun <T : UIModel> getFragment(uiModel: T): HaapiFlowFragment<T> {

        if (uiModel is FormModel) {
            @Suppress("UNCHECKED_CAST")
            return createFormFragment(uiModel) as HaapiFlowFragment<T>
        }

        return defaultResolver.getFragment(uiModel)
    }

    /*
     * Override particular form screens
     */
    private fun createFormFragment(model: FormModel): FormFragment {

        if (model is HtmlFormLoginModel) {
            // TODO: Create a custom fragment
            return FormFragment.newInstance(model)
        }

        return FormFragment.newInstance(model)
    }
}