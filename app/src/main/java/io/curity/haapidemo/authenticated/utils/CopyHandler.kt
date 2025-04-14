/*
 *  Copyright (C) 2023 Curity AB
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

package io.curity.haapidemo.authenticated.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import android.widget.Toast
import io.curity.haapidemo.R

class CopyHandler(var textToCopy: CharSequence) {
    fun copyToClipboard(view: View) {
        view.context?.let {
            it.copyTextToClipboard(textToCopy)
            it.toast(message = it.getString(R.string.copy_to_clipboard_message))
        }
    }
}

fun Context.copyTextToClipboard(textToCopy: CharSequence) {
    val clipboardManager = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = ClipData.newPlainText(getString(R.string.copy_to_clipboard_label), textToCopy)
    clipboardManager.setPrimaryClip(clipData)
}

fun Context.toast(message: CharSequence){
    Toast.makeText(this, message , Toast.LENGTH_LONG).show()
}
