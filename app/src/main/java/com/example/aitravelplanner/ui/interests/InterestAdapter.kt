package com.example.aitravelplanner.ui.interests

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.example.aitravelplanner.ui.components.InterestComponent
import com.google.android.material.slider.Slider

@InverseBindingAdapter(attribute = "app:sliderValue")
fun getSliderValue(slider: InterestComponent): Float {
    return slider.getSliderValue()
}

@BindingAdapter("app:sliderValueAttrChanged")
fun setSliderCompListeners(slider: InterestComponent, attrChange: InverseBindingListener) {
    slider.slider.addOnChangeListener { _, _, _ ->
        attrChange.onChange()
    }
}