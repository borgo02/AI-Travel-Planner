package com.example.aitravelplanner.ui.profile

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aitravelplanner.R
import com.example.aitravelplanner.databinding.FragmentSharedTravelsProfileBinding
import com.example.aitravelplanner.ui.BaseFragment
import com.example.aitravelplanner.ui.components.travelCard.CardAdapter

class SharedTravelsFragment : BaseFragment<FragmentSharedTravelsProfileBinding, SharedTravelsViewModel>() {
    override val layoutId: Int = R.layout.fragment_shared_travels_profile
    override val viewModel: SharedTravelsViewModel by viewModels()

    private lateinit var cardTravelRecyclerView: RecyclerView
    private lateinit var cardAdapter: CardAdapter

    override fun onReady(savedInstanceState: Bundle?) {
        cardTravelRecyclerView = binding.sharedTravelRecyclerView

        viewModel.cardsList.observe(viewLifecycleOwner){newValue ->
            cardTravelRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            cardAdapter = CardAdapter(newValue, viewModel::isLiked,this)
            cardTravelRecyclerView.adapter = cardAdapter
        }
    }
}
