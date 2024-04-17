package com.example.aitravelplanner.ui.travel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.aitravelplanner.databinding.FragmentGenerateTravelBinding
import com.google.android.material.switchmaterial.SwitchMaterial

class TravelFragment : Fragment() {

    private var _binding: FragmentGenerateTravelBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val travelViewModel =
            ViewModelProvider(this).get(TravelViewModel::class.java)

        _binding = FragmentGenerateTravelBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val source: EditText = binding.sourceInput
        val currentPosition: SwitchMaterial = binding.actualPositionSwitch
        val destination: EditText = binding.destInput
        val autoDestination: SwitchMaterial = binding.automaticDestSwitch
        val days: EditText = binding.daysInput
        val hotel: CheckBox = binding.hotelCheck
        val budgetGroup: RadioGroup = binding.budgetRadioGroup
        val highBudget: RadioButton = binding.highBudgetRadio
        val midBudget: RadioButton = binding.mediumBudgetRadio
        val lowBudget: RadioButton = binding.smallBudgetRadio

        travelViewModel.text.observe(viewLifecycleOwner) {

        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}