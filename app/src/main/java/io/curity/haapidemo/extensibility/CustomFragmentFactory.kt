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
import se.curity.identityserver.haapi.android.ui.widget.FragmentResolver
import se.curity.identityserver.haapi.android.ui.widget.HaapiFlowFragmentResolver
import se.curity.identityserver.haapi.android.ui.widget.layouts.FormFragment
import se.curity.identityserver.haapi.android.ui.widget.layouts.HaapiFlowFragment
import se.curity.identityserver.haapi.android.ui.widget.layouts.SelectorFragment
import se.curity.identityserver.haapi.android.ui.widget.models.FormModel
import se.curity.identityserver.haapi.android.ui.widget.models.SelectorModel
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

        if (uiModel is SelectorModel) {
            @Suppress("UNCHECKED_CAST")
            return createSelectorFragment(uiModel) as HaapiFlowFragment<T>
        }

        return defaultResolver.getFragment(uiModel)
    }

    /*
     * Override particular form screens
     */
    private fun createFormFragment(model: FormModel): HaapiFlowFragment<FormModel> {

        if (model is HtmlFormLoginModel) {
            return HtmlFormLoginFragment.newInstance(model)
        }

        return FormFragment.newInstance(model)
    }

    /*
     * Override particular selector screens
     */
    private fun createSelectorFragment(model: SelectorModel): HaapiFlowFragment<SelectorModel> {

        if (model.viewName == "views/select-authenticator/index") {
            return AuthenticationSelectionFragment.newInstance(model)
        }

        return SelectorFragment.newInstance(model)
    }
}