package com.example.aitravelplanner.ui.dashboard

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aitravelplanner.R
import com.example.aitravelplanner.databinding.FragmentDashboardBinding
import com.example.aitravelplanner.ui.BaseFragment
import com.example.aitravelplanner.ui.components.travelCard.CardAdapter

class DashboardFragment : BaseFragment<FragmentDashboardBinding, DashboardViewModel>() {

    override val layoutId: Int = R.layout.fragment_dashboard
    override val viewModel: DashboardViewModel by activityViewModels()
    private lateinit var cardTravelRecyclerView: RecyclerView
    private lateinit var cardAdapter: CardAdapter

    private val searchTextObserver = Observer<String> { _ ->
        viewModel.search()
    }

    override fun onReady(savedInstanceState: Bundle?) {
        cardTravelRecyclerView = binding.travelCardsRecyclerView

        viewModel.searchedCardsList.observe(viewLifecycleOwner){newValue ->
            cardTravelRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            cardAdapter = CardAdapter(newValue, viewModel::isLiked,this,viewModel::loadSelectedTravel)
            cardTravelRecyclerView.adapter = cardAdapter
        }
        viewModel.checkIfUserHaveInterest()
        viewModel.searchText.observe(viewLifecycleOwner, searchTextObserver)
    }

}
