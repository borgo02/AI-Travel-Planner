package com.example.aitravelplanner.ui.components.travelCard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutParams
import com.example.aitravelplanner.R
import com.example.aitravelplanner.ui.components.imageview.CustomImageView
import com.example.aitravelplanner.ui.dashboard.DashboardFragmentDirections
import com.example.aitravelplanner.ui.profile.ProfileFragment
import com.example.aitravelplanner.ui.profile.ProfileFragmentDirections
import com.example.aitravelplanner.ui.profile.SharedTravelsFragment
import com.example.aitravelplanner.ui.profile.SharedTravelsFragmentDirections

class CardAdapter(private val cards: ArrayList<CardTravel>, private val isLiked: ((CardTravel) -> Boolean)? = null, fragment: Fragment, private val loadSelectedTravel: ((CardTravel) -> Unit)? = null, private val shareTravel: ((CardTravel) -> Unit)? = null, ) : RecyclerView.Adapter<CardAdapter.CardHolder>() {
    private val isProfileFragment = fragment is ProfileFragment
    private val isSharedTravelsFragment = fragment is SharedTravelsFragment
    class CardHolder(val row: View) : RecyclerView.ViewHolder(row) {
        val username: TextView = row.findViewById(R.id.username)
        val userImage: CustomImageView = row.findViewById(R.id.userImage)
        val travelImage: CustomImageView = row.findViewById(R.id.travelImage)
        val travelName: TextView = row.findViewById(R.id.travelName)
        val affinityPerc: TextView? = row.findViewById(R.id.affinityPerc)
        val affinityImage: ImageView? = row.findViewById(R.id.affinityImage)
        val likesNumber: TextView? = row.findViewById(R.id.likesNumber)
        val likesImage: ImageView? = row.findViewById(R.id.likesImage)
        val shareImage: ImageView? = row.findViewById(R.id.shareImage)
        val timestamp: TextView? = row.findViewById(R.id.timestamp)
        val profileBar: View = row.findViewById(R.id.profileBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val layout = LayoutInflater.from(parent.context).inflate(
                R.layout.component_travel_card,
            parent, false
        )
        return CardHolder(layout)
    }

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        val currentCard = cards[position]

        holder.username.text = currentCard.username
        holder.travelImage.setURL(currentCard.travelImage)
        holder.userImage.setURL(currentCard.userImage)
        holder.travelName.text = currentCard.travelName

        if(isProfileFragment){
            holder.profileBar.visibility = View.GONE
            holder.shareImage!!.visibility = View.VISIBLE
            holder.affinityPerc!!.visibility = View.GONE
            holder.affinityImage!!.visibility = View.GONE
            holder.likesNumber!!.visibility = View.GONE
            holder.likesImage!!.visibility = View.GONE
            holder.timestamp!!.visibility = View.VISIBLE

            holder.shareImage.setImageResource(R.drawable.profile_share)
            holder.timestamp.text = currentCard.timestamp

            holder.profileBar.visibility = View.GONE
            holder.travelImage.setOnClickListener{ view ->
                val flag: Int = 0
                val action = ProfileFragmentDirections.actionNavigationProfileToTravelFragment(flag)
                view.findNavController().navigate(action)
                loadSelectedTravel?.invoke(currentCard)
            }
            holder.shareImage.visibility = View.GONE
            if(!currentCard.isShared){
                holder.shareImage.visibility = View.VISIBLE
                holder.shareImage.setImageResource(R.drawable.profile_share)
                holder.shareImage.setOnClickListener(){
                    shareTravel?.invoke(currentCard)
                    holder.shareImage.visibility = View.GONE
                }
            }

        }else{
            if(isSharedTravelsFragment){
                holder.profileBar.visibility = View.GONE
                holder.likesImage!!.visibility = View.VISIBLE
                holder.likesNumber!!.visibility = View.VISIBLE
                holder.affinityPerc!!.visibility = View.GONE
                holder.affinityImage!!.visibility = View.GONE
                holder.timestamp!!.visibility = View.VISIBLE

                holder.travelImage.setOnClickListener { view ->
                    val flag: Int = 1
                    val action = SharedTravelsFragmentDirections.actionNavigationSharedTravelsToTravelFragment(flag)
                    view.findNavController().navigate(action)
                    loadSelectedTravel?.invoke(currentCard)
                }
            }else{
                holder.profileBar.visibility = View.VISIBLE
                holder.affinityPerc!!.visibility = View.VISIBLE
                holder.likesNumber!!.visibility = View.VISIBLE
                holder.likesImage!!.visibility = View.VISIBLE
                holder.timestamp!!.visibility = View.GONE
                holder.affinityImage!!.setImageResource(R.drawable.dashboard_affinity)
                holder.affinityPerc.text = currentCard.affinityPerc

                holder.travelImage.setOnClickListener { view ->
                    val flag: Int = 2
                    val action = DashboardFragmentDirections.actionNavigationDashboardToTravelFragment(flag)
                    view.findNavController().navigate(action)
                    loadSelectedTravel?.invoke(currentCard)
                }
            }

            holder.shareImage!!.visibility = View.GONE
            holder.likesNumber.text = currentCard.travelLikes.toString()
            if (currentCard.isLiked)
                holder.likesImage.setImageResource(R.drawable.dashboard_heart_selected)
            else
                holder.likesImage.setImageResource(R.drawable.dashboard_heart_not_selected)

            holder.likesImage.setOnClickListener {
                if(isLiked!!(currentCard)) {
                    holder.likesImage.setImageResource(R.drawable.dashboard_heart_selected)
                    holder.likesNumber.text = currentCard.travelLikes.toString()
                }
                else {
                    holder.likesImage.setImageResource(R.drawable.dashboard_heart_not_selected)
                    holder.likesNumber.text = currentCard.travelLikes.toString()
                }
            }
        }

        if(position == this.itemCount - 1) {
            val params: LayoutParams =
                holder.row.layoutParams as LayoutParams
            params.bottomMargin = 500
            holder.row.layoutParams = params
        }
    }

    override fun getItemCount(): Int = cards.size

}
