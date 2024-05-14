package com.example.aitravelplanner.ui.interests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.aitravelplanner.MainActivity
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
        val interestViewModel: InterestsViewModel by viewModels()

        _binding = FragmentInterestsBinding.inflate(inflater, container, false)
        _binding!!.viewmodel = interestViewModel
        interestViewModel.setUser((activity as MainActivity).user!!)
        interestViewModel.fragmentManager = (activity as MainActivity).supportFragmentManager
        interestViewModel.currentFragment = this
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}