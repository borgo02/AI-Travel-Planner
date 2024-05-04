package com.example.aitravelplanner.ui.travel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.aitravelplanner.databinding.FragmentTravelFormBinding

class TravelFormFragment : Fragment() {
    private var _binding: FragmentTravelFormBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: TravelFormViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTravelFormBinding.inflate(inflater,container, false)
        _binding!!.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.isActualPosition.observe(viewLifecycleOwner){it ->
            binding.sourceInput.isEnabled = !it
        }

        viewModel.isAutomaticDestination.observe(viewLifecycleOwner){it ->
            binding.destInput.isEnabled = !it
        }

        viewModel.isFormCompleted.observe(viewLifecycleOwner){it ->
            if(!it)
                Toast.makeText(requireContext(), "Inserisci tutti i campi", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}