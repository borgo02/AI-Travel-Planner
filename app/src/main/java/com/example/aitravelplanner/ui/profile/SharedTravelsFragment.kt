package com.example.aitravelplanner.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aitravelplanner.databinding.FragmentSharedTravelsProfileBinding
import com.example.aitravelplanner.ui.components.travelCard.CardAdapter

class SharedTravelsFragment : Fragment() {

    private var _binding: FragmentSharedTravelsProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by activityViewModels()
    private lateinit var cardTravelRecyclerView: RecyclerView
    private lateinit var cardAdapter: CardAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSharedTravelsProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        cardTravelRecyclerView = binding.sharedTravelRecyclerView

        viewModel.sharedTravelList.observe(viewLifecycleOwner){ newValue ->
            cardTravelRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            cardAdapter = CardAdapter(newValue, viewModel::isLiked,this, viewModel::loadSelectedTravel)
            cardTravelRecyclerView.adapter = cardAdapter
        }

        val toolbar = binding.travelTopBar
        toolbar.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
