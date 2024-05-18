package com.example.aitravelplanner.ui.interests

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.example.aitravelplanner.MainActivity
import com.example.aitravelplanner.R
import com.example.aitravelplanner.databinding.FragmentInterestsBinding
import com.example.aitravelplanner.ui.BaseFragment

class InterestsFragment : BaseFragment<FragmentInterestsBinding, InterestsViewModel>() {

    override val layoutId: Int = R.layout.fragment_interests

    override val viewModel: InterestsViewModel by viewModels()

    override fun onReady(savedInstanceState: Bundle?) {
        viewModel.setUser((activity as MainActivity).user!!)
    }
}