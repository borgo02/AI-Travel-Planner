package com.example.aitravelplanner.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aitravelplanner.databinding.FragmentDashboardBinding
import com.example.aitravelplanner.ui.components.travelCard.CardAdapter

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DashboardViewModel by activityViewModels()
    private lateinit var cardTravelRecyclerView: RecyclerView
    private lateinit var cardAdapter: CardAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        cardTravelRecyclerView = binding.travelCardsRecyclerView

        viewModel.searchedCardsList.observe(viewLifecycleOwner){ newValue ->
            cardTravelRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            cardAdapter = CardAdapter(newValue, viewModel::isLiked,this, viewModel::loadSelectedTravel)
            cardTravelRecyclerView.adapter = cardAdapter
        }

        viewModel.searchText.observe(viewLifecycleOwner, searchTextObserver)
        return root
    }

    private val searchTextObserver = Observer<String> { _ ->
        viewModel.search()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
