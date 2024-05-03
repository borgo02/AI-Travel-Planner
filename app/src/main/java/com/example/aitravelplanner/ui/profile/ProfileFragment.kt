package com.example.aitravelplanner.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aitravelplanner.R
import com.example.aitravelplanner.databinding.FragmentProfileBinding
import com.example.aitravelplanner.ui.components.travelCard.CardAdapter

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel: ProfileViewModel by viewModels()
    private lateinit var cardTravelRecyclerView: RecyclerView
    private lateinit var cardAdapter: CardAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.viewmodel = profileViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val textView: TextView = binding.sharedTravels

        textView.setOnClickListener {
            findNavController().navigate(R.id.action_fragment_profile_to_fragment_shared_profile)
        }

        cardTravelRecyclerView = binding.cardTravelRecyclerView
        cardTravelRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        cardAdapter = CardAdapter(profileViewModel.cardsList.value!!, null,this)
        cardTravelRecyclerView.adapter = cardAdapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
