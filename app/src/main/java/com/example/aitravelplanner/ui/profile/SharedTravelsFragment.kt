package com.example.aitravelplanner.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aitravelplanner.databinding.FragmentSharedTravelsProfileBinding
import com.example.aitravelplanner.ui.components.travelCard.CardAdapter

class SharedTravelsFragment : Fragment() {

    private var _binding: FragmentSharedTravelsProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by activityViewModels()
    private lateinit var cardAdapter: CardAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSharedTravelsProfileBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        cardAdapter = CardAdapter(mutableListOf(), viewModel::isLiked, this, viewModel::loadSelectedTravel, viewModel::shareTravel)
        binding.sharedTravelRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.sharedTravelRecyclerView.adapter = cardAdapter

        viewModel.sharedTravelList.observe(viewLifecycleOwner) { newValue ->
            cardAdapter.updateData(newValue)
        }

        binding.travelTopBar.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
