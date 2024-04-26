package com.example.aitravelplanner.ui.travel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aitravelplanner.R
import com.example.aitravelplanner.databinding.FragmentTravelBinding
import com.example.aitravelplanner.ui.components.StageCard
import com.example.aitravelplanner.ui.components.StageCardAdapter

class TravelFragment : Fragment() {
    private var _binding: FragmentTravelBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private lateinit var stageCardRecyclerView: RecyclerView
    private lateinit var stageCardList: ArrayList<StageCard>
    private lateinit var stageNameList: ArrayList<String>
    private lateinit var stageImageList: ArrayList<String>
    private lateinit var stageAffinityList: ArrayList<Int>

    private fun getStageCards(){
        for( i in (stageNameList.indices)){
            val stageCard = StageCard(stageName = stageNameList[i], stageImage = stageImageList[i], stageAffinity = stageAffinityList[i])
            stageCardList.add(stageCard)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTravelBinding.inflate(inflater,container, false)

        stageCardRecyclerView = binding.stageTravelRecyclerView
        stageCardRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        stageCardRecyclerView.setHasFixedSize(true)

        stageCardList = arrayListOf<StageCard>()

        getStageCards()
        return binding.root

        stageCardRecyclerView.adapter = StageCardAdapter(stageCardList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}