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

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import io.curity.haapidemo.BR
import io.curity.haapidemo.authenticated.models.KeyValueViewModel
import io.curity.haapidemo.databinding.DecodedTokenViewBinding
import io.curity.haapidemo.databinding.KeyValueViewBinding
import org.json.JSONObject

class DecodedTokenView @JvmOverloads constructor(
    context: Context,
    label: Int?,
    contents: JSONObject?,
    viewGroupParent: ViewGroup?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): ConstraintLayout(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int):
            this(context, null, null, null, attrs, defStyleAttr)

    private val binding: DecodedTokenViewBinding

    init {
        binding = DecodedTokenViewBinding.inflate(LayoutInflater.from(context), viewGroupParent, true)

        label?.let {
            binding.headerLabel = context.getString(label)
        }

        contents?.let {
            binding.entries = contents.keys().asSequence().map { key ->
                KeyValueViewModel(
                    key,
                    contents[key].toString(),
                    contents[key] is String)
            }.toList()
        }
    }
}

@BindingAdapter(value = ["entries", "layout"])
fun setEntries(viewGroup: ViewGroup, entries: List<KeyValueViewModel>?, layoutId: Int) {
    viewGroup.removeAllViews()
    if (entries != null) {
        val inflater = LayoutInflater.from(viewGroup.context)
        entries.forEach { entry ->
            val binding: KeyValueViewBinding = DataBindingUtil.inflate(inflater, layoutId, viewGroup, true)
            binding.setVariable(BR.model, entry)
        }
    }
}