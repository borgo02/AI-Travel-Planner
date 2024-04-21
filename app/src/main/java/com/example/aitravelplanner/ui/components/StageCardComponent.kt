package com.example.aitravelplanner.ui.components
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.aitravelplanner.R

class StageCardComponent @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val stageImage: ImageView
    private val stageName: TextView
    private val stagePercentage: TextView

    init {
        inflate(context, R.layout.stage_card_component, this)
        stageImage = findViewById(R.id.stageImage)
        stageName = findViewById(R.id.stageName)
        stagePercentage = findViewById(R.id.affinityPercentage)
        attrs?.let { setAttributes(context, it) }
    }

    private fun setAttributes(context: Context, attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.StageCardComponent)
        val imageRes = typedArray.getResourceId(R.styleable.StageCardComponent_stageImage, 0)
        val labelText = typedArray.getString(R.styleable.StageCardComponent_stageName)
        val percentage = typedArray.getString(R.styleable.StageCardComponent_stageAffinity)
        val labelColor = typedArray.getColor(R.styleable.StageCardComponent_labelColor1, Color.BLACK)
        typedArray.recycle()

        if (imageRes != 0) {
           stageImage.setImageResource(imageRes)
        }

        if (labelText != null) {
            stageName.text = labelText
        }

        if (percentage != null) {
            stagePercentage.text = percentage
        }

        stageName.setTextColor(labelColor)
    }
}