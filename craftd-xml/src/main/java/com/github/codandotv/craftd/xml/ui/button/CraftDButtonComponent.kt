package com.github.codandotv.craftd.xml.ui.button

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.github.codandotv.craftd.androidcore.data.model.button.ButtonProperties
import com.github.codandotv.craftd.androidcore.domain.CraftDAlign
import com.github.codandotv.craftd.androidcore.extensions.getAttrColorRes
import com.github.codandotv.craftd.xml.databinding.ButtonBinding

class CraftDButtonComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : RelativeLayout(context, attrs, defStyleAttr) {
    private var binding: ButtonBinding

    init {
        binding = ButtonBinding.inflate(LayoutInflater.from(context), this)
    }

    fun setProperties(buttonProperties: ButtonProperties) {
        buttonProperties.text?.let { text ->
            binding.button.text = text
        }

        buttonProperties.textColorHex?.let { textColorHex ->
            binding.button.setTextColor(textColorHex.parseColor())
        }

        buttonProperties.backgroundHex?.let { backgroundHex ->
            binding.button.backgroundTintList = ColorStateList.valueOf(backgroundHex.parseColor())
        } ?: run {
            binding.button.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    context,
                    context.getAttrColorRes(android.R.attr.colorPrimary)
                )
            )
        }

        buttonProperties.textAllCaps?.let { isTextAllCaps ->
            binding.button.isAllCaps = isTextAllCaps
        }

        buttonProperties.fillMaxSize?.let { isfillMaxSize ->
            binding.button.layoutParams = LayoutParams(
                if (isfillMaxSize) {
                    ViewGroup.LayoutParams.MATCH_PARENT
                } else {
                    ViewGroup.LayoutParams.WRAP_CONTENT
                }, ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        buttonProperties.textSize?.let { size ->
            binding.button.setTextSize(TypedValue.COMPLEX_UNIT_SP, size.toFloat())
        }

        buttonProperties.align.toGravity()
    }

    private fun CraftDAlign?.toGravity() = when (this) {
        CraftDAlign.CENTER -> {
            gravity = Gravity.CENTER
        }

        CraftDAlign.RIGHT -> {
            gravity = Gravity.RIGHT
        }

        else -> gravity = Gravity.LEFT
    }
}