package com.example.aitravelplanner.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.aitravelplanner.BR
import com.example.aitravelplanner.R
import com.example.aitravelplanner.data.model.NavigationCommand
import com.example.aitravelplanner.utils.observeNonNull

/**
 * BaseFragment è una classe astratta che estende Fragment e gestisce le operazioni comuni
 * per tutti i fragment dell'applicazione che la estendono.
 *
 * @param BINDING Tipo di ViewDataBinding specifico per il layout del Fragment.
 * @param VM Tipo di ViewModel associato al Fragment.
 */
abstract class BaseFragment<BINDING : ViewDataBinding, VM : BaseViewModel>() : Fragment() {
    @get:LayoutRes
    protected abstract val layoutId: Int
    protected abstract val viewModel: VM
    protected lateinit var binding: BINDING
    protected lateinit var progressBar: ProgressBar

    protected abstract fun onReady(savedInstanceState: Bundle?)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            layoutId,
            container,
            false
        )

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            setVariable(BR.viewmodel, viewModel)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addObservers()
        progressBar = requireActivity().findViewById(R.id.progressBar)
        onReady(savedInstanceState)
    }

    /**
     * Metodo privato che aggiunge gli observers per la navigazione e il progresso di caricamento
     * dal ViewModel associato.
     */
    private fun addObservers() {
        viewModel.navigation.observeNonNull(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { navigationCommand ->
                handleNavigation(navigationCommand)
            }
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    /**
    * Metodo che gestisce i comandi di navigazione ricevuti dal ViewModel.
    *
    */
    private fun handleNavigation(navCommand: NavigationCommand) {
        when (navCommand) {
            is NavigationCommand.ToDirection -> findNavController().navigate(navCommand.directions)
            is NavigationCommand.Back -> findNavController().navigateUp()
        }
    }
}