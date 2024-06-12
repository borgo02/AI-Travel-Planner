package com.example.aitravelplanner.ui.dashboard

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.aitravelplanner.R
import com.example.aitravelplanner.databinding.FragmentDashboardBinding
import com.example.aitravelplanner.ui.BaseFragment
import com.example.aitravelplanner.ui.components.travelCard.CardAdapter

/** Fragment che si occupa della visualizzazione della dashboard in cui sono presenti i viaggi pubblicati dagli utenti
 *
 */
class DashboardFragment : BaseFragment<FragmentDashboardBinding, DashboardViewModel>() {

    override val layoutId: Int = R.layout.fragment_dashboard
    override val viewModel: DashboardViewModel by activityViewModels()
    private lateinit var cardAdapter: CardAdapter

    private val searchTextObserver = Observer<String> { _ ->
        viewModel.search()
    }

    override fun onReady(savedInstanceState: Bundle?) {
        val pullToRefresh: SwipeRefreshLayout = requireActivity().findViewById(R.id.pullToRefresh)
        cardAdapter = CardAdapter(mutableListOf(), viewModel::isLiked, this, viewModel::loadSelectedTravel)
        binding.travelCardsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.travelCardsRecyclerView.adapter = cardAdapter

        // Osserva le modifiche effettuate alla lista searchedCardsList del Dashboard View Model
        viewModel.searchedCardsList.observe(viewLifecycleOwner) { newValue ->
            cardAdapter.updateData(newValue)
        }

        viewModel.checkIfUserHaveInterest()
        viewModel.searchText.observe(viewLifecycleOwner, searchTextObserver)

        pullToRefresh.setOnRefreshListener {
            viewModel.refreshItems()
            pullToRefresh.isRefreshing = false
        }
    }
}
