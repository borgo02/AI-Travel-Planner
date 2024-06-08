package com.example.aitravelplanner.ui.profile

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aitravelplanner.LoginActivity
import com.example.aitravelplanner.R
import com.example.aitravelplanner.databinding.FragmentProfileBinding
import com.example.aitravelplanner.ui.BaseFragment
import com.example.aitravelplanner.ui.components.travelCard.CardAdapter
import com.example.aitravelplanner.ui.travel.TravelFormViewModel

class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>() {

    override val layoutId: Int = R.layout.fragment_profile
    override val viewModel: ProfileViewModel by activityViewModels()
    private lateinit var cardTravelRecyclerView: RecyclerView
    private lateinit var cardAdapter: CardAdapter

    override fun onReady(savedInstanceState: Bundle?) {
        val textView: TextView = binding.sharedTravels
        textView.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_profile_to_fragment_shared_profile)
        }

        cardTravelRecyclerView = binding.cardTravelRecyclerView
        cardTravelRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.cardsList.observe(viewLifecycleOwner) { newValue ->
            cardAdapter = CardAdapter(newValue, null, this, viewModel::loadSelectedTravel, viewModel::shareTravel)
            cardTravelRecyclerView.adapter = cardAdapter
        }

        viewModel.logout.observe(viewLifecycleOwner) { newValue ->
            if (newValue) {
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
        }
    }
}
