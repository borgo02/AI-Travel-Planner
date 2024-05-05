package com.example.aitravelplanner.ui.travel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aitravelplanner.R
import com.example.aitravelplanner.databinding.FragmentTravelBinding
import com.example.aitravelplanner.ui.components.stageCard.StageCard
import com.example.aitravelplanner.ui.components.stageCard.StageCardAdapter
import com.squareup.picasso.Picasso

class TravelFragment : Fragment() {
    private var _binding: FragmentTravelBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: TravelViewModel by viewModels()

    private lateinit var stageCardRecyclerView: RecyclerView
    private lateinit var stageCardList: ArrayList<StageCard>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTravelBinding.inflate(inflater,container, false)
        _binding!!.viewmodel = viewModel
        binding.lifecycleOwner = this
        binding.likesIcon

        viewModel.likedTravel.observe(viewLifecycleOwner){newValue ->
            if (newValue){
                binding.likesIcon.setImageResource(R.drawable.dashboard_heart_selected)
                binding.likesIcon.contentDescription = R.string.content_description_full_heart_icon.toString()
            } else {
                binding.likesIcon.setImageResource(R.drawable.dashboard_heart_not_selected)
                binding.likesIcon.contentDescription = R.string.content_description_empty_heart_icon.toString()
            }
        }

        viewModel.travelImage.observe(viewLifecycleOwner){newValue -> Picasso
            .get()
            .load(newValue)
            .into(binding.travelImage)}
        viewModel.stageCardList.observe(viewLifecycleOwner){newValue -> stageCardRecyclerView = binding.stageTravelRecyclerView
                stageCardRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL, false)
                stageCardRecyclerView.setHasFixedSize(true)
                stageCardList = newValue
                stageCardRecyclerView.adapter = StageCardAdapter(stageCardList)
        }


        val toolbar = binding.travelTopBar
        toolbar.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack() // Pop this fragment from back stack
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}