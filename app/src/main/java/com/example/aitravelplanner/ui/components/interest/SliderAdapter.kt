package com.example.aitravelplanner.ui.components.interest

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.google.android.material.slider.Slider

/**
 * Adapter per ottenere il valore dello slider in modo inverso.
 *
 */
@InverseBindingAdapter(attribute = "android:value")
fun getSliderValue(slider: Slider) = slider.value

/**
 * Adapter per impostare i listener dello slider.
 *
 */
@BindingAdapter("android:valueAttrChanged")
fun setSliderListeners(slider: Slider, attrChange: InverseBindingListener) {
    slider.addOnChangeListener { _, _, _ ->
        attrChange.onChange()
    }
}
