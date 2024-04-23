package com.example.aitravelplanner.ui.interests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.aitravelplanner.databinding.FragmentInterestsBinding

class InterestsFragment : Fragment() {

    private var _binding: FragmentInterestsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(InterestsViewModel::class.java)

        _binding = FragmentInterestsBinding.inflate(inflater, container, false)

        val root: View = binding.root
        /*val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun confirmClicked(view: View) {
        // Il tuo codice da eseguire quando il bottone viene premuto
        println("Bottone premuto!")
    }
}