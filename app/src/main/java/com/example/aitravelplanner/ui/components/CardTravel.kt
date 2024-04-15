package com.example.aitravelplanner.ui.components

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.aitravelplanner.R

data class CardTravel(val username: String, val userImage: Int,
                      val travelImage: Int, val travelName: String,
                      val affinityPerc: String, val likesNumber: String){

}

/*class CardTravel @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val username: TextView
    private val userImage: ImageView
    private val travelImage: ImageView
    private val travelName: TextView
    private val affinityPerc: TextView
    private val likesNumber: TextView

    init {
        inflate(context, R.layout.interestcomponent, this)
        username = findViewById(R.id.username)
        userImage = findViewById(R.id.userImage)
        travelImage = findViewById(R.id.travelImage)
        travelName = findViewById(R.id.travelName)
        affinityPerc = findViewById(R.id.affinityPerc)
        likesNumber = findViewById(R.id.likesNumber)
        attrs?.let { setAttributes(context, it) }
    }

    private fun setAttributes(context: Context, attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CardTravel)
        val username = typedArray.getString(R.styleable.CardTravel_username)
        val userImageRes = typedArray.getResourceId(R.styleable.CardTravel_userImage, 0)
        val travelImageRes = typedArray.getResourceId(R.styleable.CardTravel_travelImage, 0)
        val travelName = typedArray.getString(R.styleable.CardTravel_travelName)
        val affinityPerc = typedArray.getString(R.styleable.CardTravel_affinityPerc)
        val likesNumber = typedArray.getString(R.styleable.CardTravel_likesNumber)
        typedArray.recycle()

        if (userImageRes != 0) {
            userImage.setImageResource(userImageRes)
        }

        if (travelImageRes != 0) {
            travelImage.setImageResource(travelImageRes)
        }

        if (username != null) {
            this.username.text = username
        }

        if (travelName != null) {
            this.travelName.text = travelName
        }

        if (affinityPerc != null) {
            this.affinityPerc.text = affinityPerc
        }

        if (likesNumber != null) {
            this.likesNumber.text = likesNumber
        }
    }
}*/
