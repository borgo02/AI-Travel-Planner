package com.example.aitravelplanner.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aitravelplanner.R
import com.example.aitravelplanner.databinding.FragmentDashboardBinding
import com.example.aitravelplanner.databinding.FragmentHomeBinding
import com.example.aitravelplanner.ui.BaseFragment
import com.example.aitravelplanner.ui.components.travelCard.CardAdapter
import com.example.aitravelplanner.ui.home.HomeViewModel

class DashboardFragment : BaseFragment<FragmentDashboardBinding, DashboardViewModel>() {

    override val layoutId: Int = R.layout.fragment_dashboard

    override val viewModel: DashboardViewModel by viewModels()
    private lateinit var cardTravelRecyclerView: RecyclerView
    private lateinit var cardAdapter: CardAdapter

    private val searchTextObserver = Observer<String> { _ ->
        viewModel.search()
    }

    override fun onReady(savedInstanceState: Bundle?) {
        cardTravelRecyclerView = binding.travelCardsRecyclerView

        viewModel.searchedCardsList.observe(viewLifecycleOwner){newValue ->
            cardTravelRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            cardAdapter = CardAdapter(newValue, viewModel::isLiked,this)
            cardTravelRecyclerView.adapter = cardAdapter
        }

        viewModel.searchText.observe(viewLifecycleOwner, searchTextObserver)
    }

}
