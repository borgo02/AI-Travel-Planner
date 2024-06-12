package com.example.aitravelplanner.ui.interests

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.example.aitravelplanner.ui.components.interest.InterestComponent

/**
 * Adapter per ottenere il valore dello slider del interest component in modo inverso.
 *
 */
@InverseBindingAdapter(attribute = "app:sliderValue")
fun getSliderValue(slider: InterestComponent): Float {
    return slider.getSliderValue()
}

/**
 * Adapter per impostare i listener dello slider del interest component.
 *
 */
@BindingAdapter("app:sliderValueAttrChanged")
fun setSliderCompListeners(slider: InterestComponent, attrChange: InverseBindingListener) {
    slider.slider.addOnChangeListener { _, _, _ ->
        attrChange.onChange()
    }
}
