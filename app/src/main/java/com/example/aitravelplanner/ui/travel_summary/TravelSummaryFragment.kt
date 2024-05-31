package com.example.aitravelplanner.ui.travel_summary

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aitravelplanner.R
import com.example.aitravelplanner.databinding.FragmentTravelSummaryBinding
import com.example.aitravelplanner.ui.BaseFragment
import com.example.aitravelplanner.ui.components.stageCard.StageCardAdapter
import com.example.aitravelplanner.ui.components.stageCard.StageCard
import com.example.aitravelplanner.ui.travel.TravelFormViewModel

class TravelSummaryFragment : BaseFragment<FragmentTravelSummaryBinding, TravelFormViewModel>() {
    override val layoutId: Int = R.layout.fragment_travel_summary
    override val viewModel: TravelFormViewModel by activityViewModels()

    private lateinit var stageSelectedCardRecyclerView: RecyclerView
    private lateinit var stageSelectedCardList: ArrayList<StageCard>
    private lateinit var stageSearchedCardRecyclerView: RecyclerView
    private lateinit var stageSearchedCardList: ArrayList<StageCard>

    override fun onReady(savedInstanceState: Bundle?) {
        stageSelectedCardRecyclerView = binding.selectedStageRecyclerView
        stageSearchedCardRecyclerView = binding.searchedStageRecyclerView


        val toolbar = binding.travelSummaryTopBar
        toolbar.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
            viewModel.isFormCompleted.value = false
        }
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

        viewModel.isTravelCreated.observe(viewLifecycleOwner){newValue: Boolean ->
            if(newValue){
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }
}
