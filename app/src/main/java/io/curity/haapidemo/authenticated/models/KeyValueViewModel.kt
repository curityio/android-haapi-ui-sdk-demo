package io.curity.haapidemo.authenticated.models

import io.curity.haapidemo.R

class KeyValueViewModel(
    val key: String,
    val value: String,
    isValueString: Boolean,
) {
    val valueStyle = if (isValueString) R.style.KeyValueView_Value_String else R.style.KeyValueView_Value_Other
}
