package com.example.aitravelplanner.ui.interests

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.example.aitravelplanner.ui.components.InterestComponent
import com.google.android.material.slider.Slider

@BindingAdapter("app:sliderValue")
fun setSliderValue(slider: InterestComponent, value: Float?) {
    value?.let {
        slider.setSliderValue(value)
    }
}

@InverseBindingAdapter(attribute = "app:sliderValue")
fun getSliderValue(slider: InterestComponent): Float {
    return slider.getSliderValue()
}

@BindingAdapter("app:sliderValueAttrChanged")
fun setSliderListeners(slider: Slider, attrChange: InverseBindingListener) {
    slider.addOnChangeListener { _, _, _ ->
        println("value changed")
        attrChange.onChange()
    }
}

@BindingAdapter("app:sliderValueAttrChanged")
fun setSliderCompListeners(slider: InterestComponent, attrChange: InverseBindingListener) {
    slider.addOnLayoutChangeListener { _, _, _,_,_,_,_,_,_ ->
        println("value changed")
        attrChange.onChange()
    }
}