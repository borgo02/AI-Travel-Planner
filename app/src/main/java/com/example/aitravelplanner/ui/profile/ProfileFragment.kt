package com.example.aitravelplanner.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aitravelplanner.databinding.FragmentProfileBinding
import com.example.aitravelplanner.databinding.FragmentSharedTravelsProfileBinding
import com.example.aitravelplanner.ui.components.CardAdapter
import com.example.aitravelplanner.ui.components.CardTravel

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var travelCardsRecyclerView: RecyclerView
    private lateinit var travelCardsList: ArrayList<CardTravel>
    private lateinit var usernames : ArrayList<String>
    private lateinit var userImages : ArrayList<Int>
    private lateinit var travelImages : ArrayList<Int>
    private lateinit var travelNames : ArrayList<String>
    private lateinit var travelAffinities : ArrayList<String>
    private lateinit var affinityImages : ArrayList<Int>
    private lateinit var travelLikes : ArrayList<String>
    private lateinit var likesImages : ArrayList<Int>
    private lateinit var shareImages : ArrayList<Int>
    private lateinit var timestamps : ArrayList<String>

    //get data method for storing data into travelCardsList
    private fun getTravelCards(){

        for(i in (travelCardsList.indices)){
            val card = CardTravel(  username = usernames[i], userImage = userImages[i],
                    travelImage = travelImages[i], travelName = travelNames[i],
                    affinityPerc = travelAffinities[i], affinityImage = affinityImages[i],
                    likesNumber = travelLikes[i], shareImage = shareImages[i], likesImage = likesImages[i],
                    timestamp = timestamps[i])

            travelCardsList.add(card)
        }
        travelCardsRecyclerView.adapter = CardAdapter(travelCardsList, this)

    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
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

        travelCardsRecyclerView = binding.cardTravelRecyclerView
        travelCardsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        travelCardsRecyclerView.setHasFixedSize(true)

        travelCardsList = arrayListOf<CardTravel>()

        // populate cards
        getTravelCards()

        profileViewModel.text.observe(viewLifecycleOwner) {

        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}