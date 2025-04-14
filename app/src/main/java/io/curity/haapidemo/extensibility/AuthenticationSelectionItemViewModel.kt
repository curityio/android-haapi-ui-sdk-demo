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

import se.curity.identityserver.haapi.android.ui.widget.models.SelectorItemModel

class AuthenticationSelectionItemViewModel(val selectorItem: SelectorItemModel) {

    fun getDescriptiveText(): String {

        if (selectorItem.type == "passkeys") {
            return "Passkeys provide modern usability and strong security to protect your account"
        }

        if (selectorItem.type == "html-form") {
            return "You can also sign in with a traditional username and password"
        }

        return ""
    }

    fun getButtonText(): String {

        if (selectorItem.type == "passkeys") {
            return "Sign in with a passkey"
        }

        if (selectorItem.type == "html-form") {
            return "Sign in with a password"
        }

        return ""
    }
}
