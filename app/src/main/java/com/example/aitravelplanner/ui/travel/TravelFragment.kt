package com.example.aitravelplanner.ui.travel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.navigation.navArgument
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aitravelplanner.R
import com.example.aitravelplanner.TravelViewModel
import com.example.aitravelplanner.databinding.FragmentTravelBinding
import com.example.aitravelplanner.ui.components.stageCard.StageCard
import com.example.aitravelplanner.ui.components.stageCard.StageCardAdapter
import com.example.aitravelplanner.ui.dashboard.DashboardViewModel
import com.example.aitravelplanner.ui.profile.ProfileViewModel


class TravelFragment : Fragment() {
    private val args: TravelFragmentArgs by navArgs()
    private var _binding: FragmentTravelBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModel: TravelViewModel

    private lateinit var stageCardRecyclerView: RecyclerView
    private lateinit var stageCardList: ArrayList<StageCard>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTravelBinding.inflate(inflater,container, false)

        viewModel = when (args.flag){
            0, 1-> ViewModelProvider(requireActivity())[ProfileViewModel::class.java]
            else-> ViewModelProvider(requireActivity())[DashboardViewModel::class.java]
        }

        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.likesIcon

        binding.userImage.setURL(viewModel.selectedTravel.value!!.userImage)

        val toolbar = binding.travelTopBar
        toolbar.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack() // Pop this fragment from back stack
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.selectedTravel.observe(viewLifecycleOwner){newValue ->
            if (newValue.travelImage== "")
                binding.travelImage.setImageResource(R.mipmap.ic_image_not_found)
            else
                binding.travelImage.setURL(newValue.travelImage)
            stageCardRecyclerView = binding.stageTravelRecyclerView
            stageCardRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL, false)
            stageCardRecyclerView.setHasFixedSize(true)
            stageCardList = newValue.stageCardList
            stageCardRecyclerView.adapter = StageCardAdapter(stageCardList)
            when(args.flag){
                0 -> {
                    binding.likesIcon.visibility = View.GONE
                    binding.likesNumber.visibility = View.GONE
                }
                1,2 -> {
                    if (newValue.isLiked){
                        binding.likesIcon.setImageResource(R.drawable.dashboard_heart_selected)
                        binding.likesIcon.contentDescription = R.string.content_description_full_heart_icon.toString()
                    } else {
                        binding.likesIcon.setImageResource(R.drawable.dashboard_heart_not_selected)
                        binding.likesIcon.contentDescription = R.string.content_description_empty_heart_icon.toString()
                    }
                }
            }
        }
        if(args.flag != 0){
            binding.likesIcon.setOnClickListener(){
                viewModel.clickLike()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}