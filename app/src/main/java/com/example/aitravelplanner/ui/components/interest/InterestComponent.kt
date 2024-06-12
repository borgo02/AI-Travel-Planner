package com.example.aitravelplanner.ui.components.interest

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.aitravelplanner.R
import com.google.android.material.slider.Slider

/**
 * Classe che rappresenta un componente di interesse da visualizzare dopo il login
 */
class InterestComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val imageView: ImageView
    private val labelText: TextView
    val slider: Slider

    /**
     * Funzione per ottenere il valore dello slider
     */
    fun getSliderValue(): Float {
        return slider.value
    }

    /**
     * Funzione per impostare il valore dello slider
     */
    fun setSliderValue(newSliderValue: Float) {
        slider.value = newSliderValue
    }

    init {
        inflate(context, R.layout.component_interest, this)
        imageView = findViewById(R.id.image_view)
        labelText = findViewById(R.id.label_text)
        slider = findViewById(R.id.slider)
        attrs?.let { setAttributes(context, it) }
    }

    /**
     * Funzione per impostare gli attributi del componente
     */
    private fun setAttributes(context: Context, attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.InterestComponent)
        val imageRes = typedArray.getResourceId(R.styleable.InterestComponent_imageSrc, 0)
        val labelText = typedArray.getString(R.styleable.InterestComponent_labelText)
        typedArray.recycle()

        if (imageRes != 0)
            imageView.setImageResource(imageRes)

        if (labelText != null)
            this.labelText.text = labelText
    }
}
