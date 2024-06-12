package com.example.aitravelplanner.ui.interests

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.aitravelplanner.R
import com.example.aitravelplanner.databinding.FragmentInterestsBinding
import com.example.aitravelplanner.ui.BaseFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * Fragment che gestisce la visualizzazione degli interessi dell'utente.
 */
class InterestsFragment : BaseFragment<FragmentInterestsBinding, InterestsViewModel>() {
    override val layoutId: Int = R.layout.fragment_interests
    override val viewModel: InterestsViewModel by activityViewModels()
    private var navBar: BottomNavigationView? = null

    override fun onReady(savedInstanceState: Bundle?) {
        navBar = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        navBar!!.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        navBar!!.visibility = View.VISIBLE
    }
}
