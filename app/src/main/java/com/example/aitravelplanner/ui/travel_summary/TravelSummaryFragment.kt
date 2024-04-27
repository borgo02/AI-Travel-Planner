package com.example.aitravelplanner.ui.travel_summary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aitravelplanner.databinding.FragmentTravelSummaryBinding
import com.example.aitravelplanner.ui.components.StageCard
import com.example.aitravelplanner.ui.components.StageCardAdapter


class TravelSummaryFragment : Fragment() {
    private var _binding: FragmentTravelSummaryBinding? = null
    private val binding get() = _binding!!
    private lateinit var stageSelectedCardRecyclerView: RecyclerView
    private lateinit var stageSelectedCardList: ArrayList<StageCard>
    private lateinit var stageSelectedNameList: ArrayList<String>
    private lateinit var stageSelectedImageList: ArrayList<String>
    private lateinit var stageSelectedAffinityList: ArrayList<Int>
    private lateinit var stageSearchedCardRecyclerView: RecyclerView
    private lateinit var stageSearchedCardList: ArrayList<StageCard>
    private lateinit var stageSearchedNameList: ArrayList<String>
    private lateinit var stageSearchedImageList: ArrayList<String>
    private lateinit var stageSearchedAffinityList: ArrayList<Int>

    private fun getStageCards(stageCardList: ArrayList<StageCard>, stageNameList: ArrayList<String>, stageImageList: ArrayList<String>, stageAffinityList: ArrayList<Int>): ArrayList<StageCard>{
        for( i in (stageNameList.indices)){
            val stageCard = StageCard(stageName = stageNameList[i], stageImage = stageImageList[i], stageAffinity = stageAffinityList[i])
            stageCardList.add(stageCard)
        }
        return stageCardList
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTravelSummaryBinding.inflate(inflater,container, false)

        stageSelectedCardRecyclerView = binding.selectedStageRecyclerView
        stageSelectedCardRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        stageSelectedCardRecyclerView.setHasFixedSize(true)

        stageSelectedCardList = arrayListOf<StageCard>()

        stageSelectedCardList = getStageCards(stageCardList = stageSelectedCardList, stageNameList = stageSelectedNameList, stageImageList = stageSelectedImageList, stageAffinityList = stageSelectedAffinityList)

        stageSelectedCardRecyclerView.adapter = StageCardAdapter(stageSelectedCardList)


        stageSearchedCardRecyclerView = binding.searchedStageRecyclerView
        stageSearchedCardRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        stageSearchedCardRecyclerView.setHasFixedSize(true)

        stageSearchedCardList = arrayListOf<StageCard>()

        stageSearchedCardList = getStageCards(stageCardList = stageSearchedCardList, stageNameList = stageSearchedNameList, stageImageList = stageSearchedImageList, stageAffinityList = stageSearchedAffinityList)

        stageSearchedCardRecyclerView.adapter = StageCardAdapter(stageSearchedCardList)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}