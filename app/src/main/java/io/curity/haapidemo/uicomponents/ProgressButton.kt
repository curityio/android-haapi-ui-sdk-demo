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

package io.curity.haapidemo.uicomponents

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Space
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import io.curity.haapidemo.R
import io.curity.haapidemo.databinding.ProgressBtnLayoutBinding

/**
 * `ProgressButton` is an UI component that is similar to a `Button` with a "spinner" `ProgressBar` in the center.
 *
 * To display the spinner, it is mandatory to call [ProgressButton.setLoading] with `true`.
 */
class ProgressButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ProgressBtnLayoutBinding
    private var isLoading: Boolean = false

    init {
        binding = ProgressBtnLayoutBinding.inflate(LayoutInflater.from(context), rootView as ViewGroup, true)

        setTextAttribute(attrs)

        // Set default styles
        binding.progressBar.indeterminateDrawable.setTint(Color.parseColor("#FFFFFF"))
        background = AppCompatResources.getDrawable(context, R.drawable.primary_progress_btn_shape)
        background.setStroke(Color.BLACK, (1).toDpi(resources))

        binding.progressBar.visibility = View.GONE
    }

    @SuppressLint("Recycle")
    private fun setTextAttribute(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressButton)
        val text = typedArray.getText(R.styleable.ProgressButton_android_text)
        binding.textView.text = text
        typedArray.recycle()
    }

    /**
     * Set a text to the ProgressButton
     *
     * @param text A String
     */
    @Suppress("Unused")
    fun setText(text: String) {
        binding.textView.text = text
    }

    /**
     * Returns the text of ProgressButton
     */
    @Suppress("Unused")
    fun getText(): String {
        return binding.textView.text.toString()
    }

    /**
     * Sets an image with a @DrawableRes [image]
     */
    fun setImage(@DrawableRes image: Int) {
        binding.imageView.setImageResource(image)
        binding.imageView.visibility = VISIBLE
        binding.spacer.visibility = VISIBLE
    }

    /**
     * Set the loading state to `ProgressButton`. If loading is `true` then the text is hidden, the spinner is shown and
     * ProgressButton is not clickable unless calling `setLoading(false)`.
     *
     * @param loading A Boolean for the loading state of ProgressButton
     */
    fun setLoading(loading: Boolean) {
        isLoading = loading
        if (isLoading) {
            binding.progressBar.visibility = VISIBLE
            binding.textView.visibility = GONE
            binding.imageView.visibility = GONE
            isClickable = false
        } else {
            binding.progressBar.visibility = GONE
            binding.textView.visibility = VISIBLE
            binding.imageView.visibility = VISIBLE
            isClickable = true
        }
    }
}

/**
 * Sets stroke by using [colorInt] with a [width]
 *
 * @param colorInt An [Int] value that represents a color
 * @param width An [Int] value that represents the expected width
 */
fun Drawable.setStroke(@ColorInt colorInt: Int, width: Int) {
    when (this) {
        is GradientDrawable -> {
            mutate()
            setStroke(width, colorInt)
        }
    }
}

/**
 * Computes this [Int] value to a Dpi as [Int] by multiplying [resources].displayMetrics.density
 *
 * @param resources Resources
 * @return Int An integer that represents a Dpi
 */
fun Int.toDpi(resources: Resources): Int {
    return (resources.displayMetrics.density * this).toInt()
}