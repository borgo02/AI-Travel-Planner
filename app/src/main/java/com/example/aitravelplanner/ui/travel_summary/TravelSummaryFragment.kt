package com.example.aitravelplanner.ui.travel_summary

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aitravelplanner.R
import com.example.aitravelplanner.databinding.FragmentTravelSummaryBinding
import com.example.aitravelplanner.ui.BaseFragment
import com.example.aitravelplanner.ui.components.stageCard.StageCardAdapter
import com.example.aitravelplanner.ui.components.stageCard.StageCard
import com.example.aitravelplanner.ui.travel.TravelFormViewModel

/**
 * Fragment che visualizza un riepilogo del viaggio, mostrando le tappe generate.
 */
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
        // Gestisce il click sul pulsante di navigazione della toolbar
        toolbar.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
            viewModel.clearViewModel()
            viewModel.isFormCompleted.value = false
        }

        // Osserva le modifiche nella lista delle CardStage selezionate nel ViewModel
        viewModel.stageSelectedCardList.observe(viewLifecycleOwner) { newValue: ArrayList<StageCard> ->
            stageSelectedCardRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            stageSelectedCardRecyclerView.setHasFixedSize(true)
            stageSelectedCardList = newValue
            stageSelectedCardRecyclerView.adapter = StageCardAdapter(stageSelectedCardList, viewModel::deleteStage)
        }

        // Osserva le modifiche nella lista delle CardStage cercate nel ViewModel
        viewModel.stageSearchedCardList.observe(viewLifecycleOwner) { newValue: ArrayList<StageCard> ->
            stageSearchedCardRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            stageSearchedCardRecyclerView.setHasFixedSize(true)
            stageSearchedCardList = newValue
            stageSearchedCardRecyclerView.adapter = StageCardAdapter(stageSearchedCardList, viewModel::addStage)
        }

        // Osserva il flag che indica se il viaggio è stato creato con successo
        viewModel.isTravelCreated.observe(viewLifecycleOwner) { newValue: Boolean ->
            if (newValue)
                requireActivity().supportFragmentManager.popBackStack()
        }

        // Osserva il flag che indica se si è verificato un errore durante il caricamento delle informazioni
        viewModel.hasJsonError.observe(viewLifecycleOwner) { hasError: Boolean ->
            if (hasError)
                Toast.makeText(requireContext(), "Errore nel caricamento. Riprova", Toast.LENGTH_SHORT).show()
        }
    }
}
