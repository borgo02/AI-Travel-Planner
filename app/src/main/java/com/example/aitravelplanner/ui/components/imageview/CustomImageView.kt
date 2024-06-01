package com.example.aitravelplanner.ui.components.imageview
import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.squareup.picasso.Picasso

class CustomImageView : AppCompatImageView{
    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    fun setURL(url: String) {
        Picasso
            .get()
            .load(url)
            .into(this)
    }
}