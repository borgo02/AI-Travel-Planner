package com.example.aitravelplanner.ui.travel_summary

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
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
            viewModel.clearViewModel()
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
            if(newValue)
                requireActivity().supportFragmentManager.popBackStack()
        }

        viewModel.hasJsonError.observe(viewLifecycleOwner){it ->
            if(it)
                Toast.makeText(requireContext(), "Errore nel caricamento. Riprova", Toast.LENGTH_SHORT).show()
        }

        viewModel.isTravelLoading.observe(viewLifecycleOwner) {isLoading ->
            if(isLoading) {
                binding.linearLayout.visibility = View.GONE
                binding.secondLinearLayout.visibility = View.GONE
                binding.travelSummaryName.visibility = View.GONE
                binding.textView5.visibility = View.GONE
                binding.textView4.visibility = View.GONE
                binding.filterButton.visibility = View.GONE
                binding.saveTravelButton.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
            }
            else {
                binding.linearLayout.visibility = View.VISIBLE
                binding.secondLinearLayout.visibility = View.VISIBLE
                binding.travelSummaryName.visibility = View.VISIBLE
                binding.textView5.visibility = View.VISIBLE
                binding.textView4.visibility = View.VISIBLE
                binding.filterButton.visibility = View.VISIBLE
                binding.saveTravelButton.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }
        }
    }
}
