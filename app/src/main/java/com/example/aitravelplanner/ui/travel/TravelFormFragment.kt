package com.example.aitravelplanner.ui.travel

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.aitravelplanner.R
import com.example.aitravelplanner.databinding.FragmentTravelFormBinding
import com.example.aitravelplanner.ui.BaseFragment
import com.example.aitravelplanner.ui.profile.SharedTravelsFragmentDirections

class TravelFormFragment : BaseFragment<FragmentTravelFormBinding, TravelFormViewModel>() {
    override val layoutId: Int = R.layout.fragment_travel_form
    override val viewModel: TravelFormViewModel by activityViewModels()

    override fun onReady(savedInstanceState: Bundle?) {
        viewModel.isActualPosition.observe(viewLifecycleOwner){it ->
            binding.sourceInput.isEnabled = !it
        }

        viewModel.isAutomaticDestination.observe(viewLifecycleOwner){it ->
            binding.destInput.isEnabled = !it
        }

        viewModel.isFormCompleted.observe(viewLifecycleOwner){it ->
            if(it)
                findNavController().navigate(R.id.action_travelFormFragment_to_travelSummaryFragment)
            else
                Toast.makeText(requireContext(), "Inserisci tutti i campi", Toast.LENGTH_SHORT).show()
        }
    }
}