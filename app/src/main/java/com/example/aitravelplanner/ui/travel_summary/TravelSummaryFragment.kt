package com.example.aitravelplanner.ui.travel_summary

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aitravelplanner.R
import com.example.aitravelplanner.databinding.FragmentTravelSummaryBinding
import com.example.aitravelplanner.ui.BaseFragment
import com.example.aitravelplanner.ui.components.stageCard.StageCardAdapter
import com.example.aitravelplanner.ui.components.stageCard.StageCard

class TravelSummaryFragment : BaseFragment<FragmentTravelSummaryBinding, TravelSummaryViewModel>() {
    override val layoutId: Int = R.layout.fragment_travel
    override val viewModel: TravelSummaryViewModel by viewModels()

    private lateinit var stageSelectedCardRecyclerView: RecyclerView
    private lateinit var stageSelectedCardList: ArrayList<StageCard>
    private lateinit var stageSearchedCardRecyclerView: RecyclerView
    private lateinit var stageSearchedCardList: ArrayList<StageCard>

    override fun onReady(savedInstanceState: Bundle?) {
        stageSelectedCardRecyclerView = binding.selectedStageRecyclerView
        stageSearchedCardRecyclerView = binding.searchedStageRecyclerView

        viewModel.stageSelectedCardList.observe(viewLifecycleOwner){newValue: ArrayList<StageCard> ->
            stageSelectedCardRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL, false)
            stageSelectedCardRecyclerView.setHasFixedSize(true)

            stageSelectedCardList = newValue


            stageSelectedCardRecyclerView.adapter = StageCardAdapter(stageSelectedCardList, viewModel::deleteStage)}


        viewModel.stageSearchedCardList.observe(viewLifecycleOwner){newValue: ArrayList<StageCard> ->
            stageSearchedCardRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL, false)
            stageSearchedCardRecyclerView.setHasFixedSize(true)

            stageSearchedCardList = newValue


            stageSearchedCardRecyclerView.adapter = StageCardAdapter(stageSearchedCardList, viewModel::addStage)
        }
    }
}
