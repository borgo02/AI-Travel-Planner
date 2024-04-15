package com.example.aitravelplanner.ui.components
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.aitravelplanner.R

class InterestComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val imageView: ImageView
    private val labelText: TextView

    init {
        inflate(context, R.layout.interestcomponent, this)
        imageView = findViewById(R.id.image_view)
        labelText = findViewById(R.id.label_text)   
        attrs?.let { setAttributes(context, it) }
    }

    private fun setAttributes(context: Context, attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.InterestComponent)
        val imageRes = typedArray.getResourceId(R.styleable.InterestComponent_imageSrc, 0)
        val labelText = typedArray.getString(R.styleable.InterestComponent_labelText)
        val labelColor = typedArray.getColor(R.styleable.InterestComponent_labelColor, Color.BLACK)
        typedArray.recycle()

        if (imageRes != 0) {
            imageView.setImageResource(imageRes)
        }

        if (labelText != null) {
            this.labelText.text = labelText
        }

        this.labelText.setTextColor(labelColor)
    }
}