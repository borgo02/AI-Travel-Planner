package com.example.aitravelplanner.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.aitravelplanner.R
import com.example.aitravelplanner.databinding.FragmentDashboardBinding
import com.example.aitravelplanner.ui.BaseFragment
import com.example.aitravelplanner.ui.components.travelCard.CardAdapter
import kotlin.math.log

/** Fragment che si occupa della visualizzazione della dashboard in cui sono presenti i viaggi pubblicati dagli utenti
 *
 */
class DashboardFragment : BaseFragment<FragmentDashboardBinding, DashboardViewModel>() {

    override val layoutId: Int = R.layout.fragment_dashboard
    override val viewModel: DashboardViewModel by activityViewModels()
    private lateinit var cardAdapter: CardAdapter
    var count: Int = 0

    private val searchTextObserver = Observer<String> { _ ->
        viewModel.search()
    }

    override fun onReady(savedInstanceState: Bundle?) {
        val pullToRefresh: SwipeRefreshLayout = requireActivity().findViewById(R.id.pullToRefresh)
        cardAdapter = CardAdapter(mutableListOf(), viewModel::isLiked, this, viewModel::loadSelectedTravel)
        binding.travelCardsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.travelCardsRecyclerView.adapter = cardAdapter
        binding.travelCardsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = binding.travelCardsRecyclerView.layoutManager as LinearLayoutManager?
                val totalItemCount = layoutManager!!.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                if (!recyclerView.canScrollVertically(1) || lastVisibleItem >= totalItemCount - 5) {
                    count++
                    Log.e("Count: ", count.toString())
                    //binding.progressBarView.visibility = View.VISIBLE
                    //loadMoreData()
                }
            }
        })

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
