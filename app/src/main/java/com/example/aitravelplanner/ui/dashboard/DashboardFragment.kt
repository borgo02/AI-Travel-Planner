package com.example.aitravelplanner.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aitravelplanner.R
import com.example.aitravelplanner.databinding.FragmentDashboardBinding
import com.example.aitravelplanner.ui.BaseFragment
import com.example.aitravelplanner.ui.components.travelCard.CardAdapter

class DashboardFragment : BaseFragment<FragmentDashboardBinding, DashboardViewModel>() {

    override val layoutId: Int = R.layout.fragment_dashboard
    override val viewModel: DashboardViewModel by activityViewModels()
    private lateinit var cardAdapter: CardAdapter

    private val searchTextObserver = Observer<String> { _ ->
        viewModel.search()
    }

    override fun onReady(savedInstanceState: Bundle?) {
        cardAdapter = CardAdapter(mutableListOf(), viewModel::isLiked, this, viewModel::loadSelectedTravel)
        binding.travelCardsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.travelCardsRecyclerView.adapter = cardAdapter

        viewModel.searchedCardsList.observe(viewLifecycleOwner) { newValue ->
            cardAdapter.updateData(newValue)
        }

        viewModel.isDashboardLoading.observe(viewLifecycleOwner){isLoading ->
            if(isLoading)
                progressBar.visibility = View.VISIBLE
            else
                progressBar.visibility = View.GONE
        }

        viewModel.checkIfUserHaveInterest()
        viewModel.searchText.observe(viewLifecycleOwner, searchTextObserver)
    }
}
