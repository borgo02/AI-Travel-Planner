package com.example.aitravelplanner.ui.travel_summary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aitravelplanner.databinding.FragmentTravelSummaryBinding
import com.example.aitravelplanner.ui.components.stageCard.StageCardAdapter
import com.example.aitravelplanner.ui.components.stageCard.StageCard



class TravelSummaryFragment : Fragment() {
    private var _binding: FragmentTravelSummaryBinding? = null
    private val binding get() = _binding!!

    private lateinit var stageSelectedCardRecyclerView: RecyclerView
    private lateinit var stageSelectedCardList: ArrayList<StageCard>
    private lateinit var stageSearchedCardRecyclerView: RecyclerView
    private lateinit var stageSearchedCardList: ArrayList<StageCard>
    val viewModel: TravelSummaryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTravelSummaryBinding.inflate(inflater,container, false)
        _binding!!.viewmodel = viewModel
        binding.lifecycleOwner = this
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


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
