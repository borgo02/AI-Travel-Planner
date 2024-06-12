package com.example.aitravelplanner.ui.components.imageview
import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.squareup.picasso.Picasso

/**
 * Clazze utilizzata per personalizzare una ImageView che carica un'immagine da un URL utilizzando Picasso.
 * Questa classe viene utilizzata per semplificare il caricamento di immagini da URL nelle viste ImageView.
 */
class CustomImageView : AppCompatImageView{
    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    /**
     * Imposta l'URL dell'immagine da caricare.
     */
    fun setURL(url: String) {
        Picasso
            .get()
            .load(url)
            .into(this)
    }
}