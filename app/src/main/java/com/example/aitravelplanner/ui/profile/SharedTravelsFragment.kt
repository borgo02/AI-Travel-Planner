package com.example.aitravelplanner.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aitravelplanner.databinding.FragmentSharedTravelsProfileBinding
import com.example.aitravelplanner.ui.components.CardAdapter

class SharedTravelsFragment : Fragment() {

    private var _binding: FragmentSharedTravelsProfileBinding? = null
    private val binding get() = _binding!!

    private val sharedTravelViewModel: SharedTravelsViewModel by viewModels()
    private lateinit var cardTravelRecyclerView: RecyclerView
    private lateinit var cardAdapter: CardAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSharedTravelsProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.viewmodel = sharedTravelViewModel
        binding.lifecycleOwner = this

        cardTravelRecyclerView = binding.sharedTravelRecyclerView
        cardTravelRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        cardAdapter = CardAdapter(sharedTravelViewModel.getTravelCards(), sharedTravelViewModel::isLiked,this)
        cardTravelRecyclerView.adapter = cardAdapter

        sharedTravelViewModel.cardsList.observe(viewLifecycleOwner){
            cardAdapter = CardAdapter(sharedTravelViewModel.getTravelCards(), sharedTravelViewModel::isLiked,this)
            cardTravelRecyclerView.adapter = cardAdapter
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
