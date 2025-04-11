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
