package com.example.aitravelplanner.ui.travel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aitravelplanner.databinding.FragmentTravelBinding
import com.example.aitravelplanner.ui.components.StageCard
import com.example.aitravelplanner.ui.components.StageCardAdapter
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
        viewModel.travelName.observe(viewLifecycleOwner){newValue -> binding.travelName.text = newValue}
        viewModel.userName.observe(viewLifecycleOwner){newValue -> binding.usernameText.text = newValue}
        viewModel.affinityPercentage.observe(viewLifecycleOwner){newValue -> binding.affinityPercentageTravel.text = newValue}
        viewModel.likesNumber.observe(viewLifecycleOwner){newValue -> binding.likesNumber.text = newValue}
        viewModel.travelImage.observe(viewLifecycleOwner){newValue -> Picasso
            .get()
            .load(newValue)
            .into(binding.travelImage)}
        viewModel.description.observe(viewLifecycleOwner){newValue -> binding.travelDescription.text = newValue}
        viewModel.stageCardList.observe(viewLifecycleOwner){newValue -> stageCardRecyclerView = binding.stageTravelRecyclerView
                stageCardRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL, false)
                stageCardRecyclerView.setHasFixedSize(true)
                stageCardList = newValue
                stageCardRecyclerView.adapter = StageCardAdapter(stageCardList)}


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}