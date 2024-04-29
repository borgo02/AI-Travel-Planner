package com.example.aitravelplanner.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aitravelplanner.ui.components.CardAdapter
import com.example.aitravelplanner.databinding.FragmentSharedTravelsProfileBinding
import com.example.aitravelplanner.ui.components.CardTravel

class SharedTravelsFragment : Fragment() {

    private var _binding: FragmentSharedTravelsProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var travelCardsRecyclerView: RecyclerView
    private lateinit var travelCardsList: ArrayList<CardTravel>
    private lateinit var usernames : ArrayList<String>
    private lateinit var userImages : ArrayList<String>
    private lateinit var travelImages : ArrayList<String>
    private lateinit var travelNames : ArrayList<String>
    private lateinit var travelAffinities : ArrayList<String>
    private lateinit var affinityImages : ArrayList<Int>
    private lateinit var travelLikes : ArrayList<Int>
    private lateinit var likesImages : ArrayList<Int>
    private lateinit var shareImages : ArrayList<Int>
    private lateinit var timestamps : ArrayList<String>

    //get data method for storing data into travelCardsList
    private fun getTravelCards(){

        for(i in (travelCardsList.indices)){
            val card = CardTravel(  username = usernames[i], userImage = userImages[i],
                travelImage = travelImages[i], travelName = travelNames[i],
                affinityPerc = travelAffinities[i], travelLikes = travelLikes[i],
                timestamp = timestamps[i], isLiked = false)

            travelCardsList.add(card)
        }

        travelCardsRecyclerView.adapter = CardAdapter(travelCardsList, this)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val sharedTravelsViewModel = ViewModelProvider(this).get(SharedTravelsViewModel::class.java)

        _binding = FragmentSharedTravelsProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        travelCardsList = arrayListOf(
                //insert elements here
        )
        usernames = arrayListOf(
                //insert elements here
        )
        userImages = arrayListOf(
                //insert elements here
        )
        travelImages = arrayListOf(
                //insert elements here
        )
        travelNames = arrayListOf(
                //insert elements here
        )
        travelAffinities = arrayListOf(
                //insert elements here
        )
        travelLikes = arrayListOf(
                //insert elements here
        )

        travelCardsRecyclerView = binding.sharedTravelRecyclerView
        travelCardsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        travelCardsRecyclerView.setHasFixedSize(true)

        travelCardsList = arrayListOf<CardTravel>()

        // populate cards
        getTravelCards()

        sharedTravelsViewModel.shareImage.observe(viewLifecycleOwner) {

        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}