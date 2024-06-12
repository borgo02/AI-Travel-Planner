package com.example.aitravelplanner.ui.travel

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.aitravelplanner.R
import com.example.aitravelplanner.databinding.FragmentTravelFormBinding
import com.example.aitravelplanner.ui.BaseFragment

class TravelFormFragment : BaseFragment<FragmentTravelFormBinding, TravelFormViewModel>() {
    override val layoutId: Int = R.layout.fragment_travel_form
    override val viewModel: TravelFormViewModel by activityViewModels()

    override fun onReady(savedInstanceState: Bundle?) {

        viewModel.isActualPosition.observe(viewLifecycleOwner){
            binding.sourceInput.isEnabled = !it
            viewModel.sourceInput.value = ""
        }

        viewModel.isAutomaticDestination.observe(viewLifecycleOwner){
            binding.destInput.isEnabled = !it
            viewModel.destinationInput.value = ""
        }

        viewModel.isFormEmpty.observe(viewLifecycleOwner){
            if(it) {
                Toast.makeText(requireContext(), "Inserisci tutti i campi", Toast.LENGTH_SHORT).show()
                viewModel.isFormEmpty.value = false
            }
        }

        viewModel.hasJsonError.observe(viewLifecycleOwner){it ->
            if(it)
            {
                Toast.makeText(requireContext(), "Errore nel caricamento. Riprova", Toast.LENGTH_SHORT).show()
                viewModel.hasJsonError.value = false
            }

        }
    }
}