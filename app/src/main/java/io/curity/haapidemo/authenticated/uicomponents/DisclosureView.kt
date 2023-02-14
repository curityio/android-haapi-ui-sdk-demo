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

package io.curity.haapidemo.authenticated.uicomponents

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import io.curity.haapidemo.BR
import io.curity.haapidemo.R
import io.curity.haapidemo.authenticated.models.DecodedJwtData
import io.curity.haapidemo.authenticated.utils.CopyHandler
import io.curity.haapidemo.databinding.DisclosureViewBinding
import io.curity.haapidemo.databinding.LabelTextViewBinding

class DisclosureView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: DisclosureViewBinding

    init {
        binding = DisclosureViewBinding.inflate(LayoutInflater.from(context), rootView as ViewGroup, true)
        binding.copyHandler = CopyHandler(binding.contentText.text)

        setTitle(attrs)
    }

    @SuppressLint("Recycle")
    private fun setTitle(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.DisclosureView)
        val text = typedArray.getText(R.styleable.DisclosureView_titleText)
        binding.title.text = text
        typedArray.recycle()
    }

    fun setContentText(contentText: CharSequence) {
        binding.contentText.text = contentText
        binding.copyHandler?.textToCopy = contentText
    }

    @Suppress("Unused")
    fun setDecodedContent(decodedContent: DecodedJwtData) {
        binding.verticalLinearLayout.removeAllViews()

        decodedContent.getHeaderObj()?.let {
            DecodedTokenView(context, R.string.decoded_jwt_header, it, binding.verticalLinearLayout)
        }

        decodedContent.getPayloadObj()?.let {
            DecodedTokenView(context, R.string.decoded_jwt_payload, it, binding.verticalLinearLayout)
        }
    }

    @Suppress("Unused")
    fun setDisclosureContents(contents: List<DisclosureContent>) {
        binding.verticalLinearLayout.removeAllViews()
        binding.entries = contents.map { Pair(it.label, it.description) }
    }
}

@BindingAdapter("entries")
fun setLabelTextEntries(viewGroup: ViewGroup, entries: List<Pair<CharSequence, CharSequence>>?) {
    if (entries != null) {
        viewGroup.removeAllViews()
        val inflater = LayoutInflater.from(viewGroup.context)
        entries.forEach { entry ->
            val binding: LabelTextViewBinding = DataBindingUtil.inflate(inflater, R.layout.label_text_view, viewGroup, true)
            binding.setVariable(BR.data, entry)
        }
    }
}

data class DisclosureContent(val label: CharSequence, val description: CharSequence)