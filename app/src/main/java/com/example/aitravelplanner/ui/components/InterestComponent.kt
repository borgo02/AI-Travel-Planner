package com.example.aitravelplanner.ui.components

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.aitravelplanner.R
import com.google.android.material.slider.Slider


class InterestComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val imageView: ImageView
    private val labelText: TextView

    private var sliderValue: Float = 0f

    fun getSliderValue(): Float {
        return sliderValue
    }

    fun setSliderValue(newSliderValue: Float) {
        sliderValue = newSliderValue
    }

    init {
        inflate(context, com.example.aitravelplanner.R.layout.interestcomponent, this)
        imageView = findViewById(com.example.aitravelplanner.R.id.image_view)
        labelText = findViewById(com.example.aitravelplanner.R.id.label_text)
        attrs?.let { setAttributes(context, it) }

        val slider = findViewById<Slider>(com.example.aitravelplanner.R.id.slider)

        /*slider = findViewById(com.example.aitravelplanner.R.id.slider)
        slider.addOnSliderTouchListener(touchListener)*/
    }

    private fun setAttributes(context: Context, attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.InterestComponent)
        val imageRes = typedArray.getResourceId(R.styleable.InterestComponent_imageSrc, 0)
        val labelText = typedArray.getString(R.styleable.InterestComponent_labelText)
        typedArray.recycle()

        if (imageRes != 0) {
            imageView.setImageResource(imageRes)
        }

        if (labelText != null) {
            this.labelText.text = labelText
        }
    }
}