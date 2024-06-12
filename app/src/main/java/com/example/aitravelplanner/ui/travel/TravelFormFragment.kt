package com.example.aitravelplanner.ui.travel

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.aitravelplanner.R
import com.example.aitravelplanner.databinding.FragmentTravelFormBinding
import com.example.aitravelplanner.ui.BaseFragment

/**
 * Fragment che si occupa della visualizzazione del form per la generazione del viaggio.
 */
class TravelFormFragment : BaseFragment<FragmentTravelFormBinding, TravelFormViewModel>() {
    override val layoutId: Int = R.layout.fragment_travel_form
    override val viewModel: TravelFormViewModel by activityViewModels()

    override fun onReady(savedInstanceState: Bundle?) {

        // Osserva la variabile isActualPosition che verifica se l'utente ha selezionato "posizione attuale"
        viewModel.isActualPosition.observe(viewLifecycleOwner){
            binding.sourceInput.isEnabled = !it
            viewModel.sourceInput.value = ""
        }

        // Osserva la variabile isAutomaticDestination che verifica se l'utente ha selezionato "destinazione automatica"
        viewModel.isAutomaticDestination.observe(viewLifecycleOwner){
            binding.destInput.isEnabled = !it
            viewModel.destinationInput.value = ""
        }

        // Osserva la variabile isFormCompleted che verifica se il form è stato completato o meno
        viewModel.isFormEmpty.observe(viewLifecycleOwner){
            if(it) {
                Toast.makeText(requireContext(), "Inserisci tutti i campi", Toast.LENGTH_SHORT).show()
                viewModel.isFormEmpty.value = false
            }
        }

        viewModel.hasJsonError.observe(viewLifecycleOwner){it ->
            if(it)
            {
                Toast.makeText(requireContext(), "Errore nel caricamento. Riprova", Toast.LENGTH_SHORT).show()
                viewModel.hasJsonError.value = false
            }

        }
    }
}