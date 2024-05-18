package com.example.aitravelplanner.ui.travel
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aitravelplanner.R
import com.example.aitravelplanner.databinding.FragmentTravelBinding
import com.example.aitravelplanner.ui.BaseFragment
import com.example.aitravelplanner.ui.components.stageCard.StageCard
import com.example.aitravelplanner.ui.components.stageCard.StageCardAdapter
import com.squareup.picasso.Picasso

class TravelFragment : BaseFragment<FragmentTravelBinding, TravelViewModel>() {
    override val layoutId: Int = R.layout.fragment_travel
    override val viewModel: TravelViewModel by viewModels()

    private lateinit var stageCardRecyclerView: RecyclerView
    private lateinit var stageCardList: ArrayList<StageCard>

    override fun onReady(savedInstanceState: Bundle?) {
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
    }

}