package com.example.aitravelplanner.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aitravelplanner.databinding.FragmentDashboardBinding
import com.example.aitravelplanner.ui.components.travelCard.CardAdapter

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val dashboardViewModel: DashboardViewModel by viewModels()
    private lateinit var cardTravelRecyclerView: RecyclerView
    private lateinit var cardAdapter: CardAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.viewmodel = dashboardViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        cardTravelRecyclerView = binding.travelCardsRecyclerView

        dashboardViewModel.cardsList.observe(viewLifecycleOwner){newValue ->
            cardTravelRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            cardAdapter = CardAdapter(newValue, dashboardViewModel::isLiked,this)
            cardTravelRecyclerView.adapter = cardAdapter
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
