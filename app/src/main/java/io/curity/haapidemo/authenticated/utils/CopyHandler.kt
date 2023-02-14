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
