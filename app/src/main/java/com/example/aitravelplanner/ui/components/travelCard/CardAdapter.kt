package com.example.aitravelplanner.ui.components.travelCard
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.ui.unit.dp
import androidx.core.view.marginBottom
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.aitravelplanner.R
import com.example.aitravelplanner.ui.profile.ProfileFragment
import com.example.aitravelplanner.ui.profile.SharedTravelsFragment
import com.squareup.picasso.Picasso

class CardAdapter(private val cards: ArrayList<CardTravel>, private val isLiked: ((CardTravel) -> Boolean)? = null, private val fragment: Fragment) : RecyclerView.Adapter<CardAdapter.CardHolder>() {
    class CardHolder(val row: View) : RecyclerView.ViewHolder(row) {
        val username: TextView = row.findViewById(R.id.username)
        val userImage: ImageView = row.findViewById(R.id.userImage)
        val travelImage: ImageView = row.findViewById(R.id.travelImage)
        val travelName: TextView = row.findViewById(R.id.travelName)
        val affinityPerc: TextView? = row.findViewById(R.id.affinityPerc)
        val affinityImage: ImageView? = row.findViewById(R.id.affinityImage)
        val likesNumber: TextView? = row.findViewById(R.id.likesNumber)
        val likesImage: ImageView? = row.findViewById(R.id.likesImage)
        val shareImage: ImageView? = row.findViewById(R.id.shareImage)
        val timestamp: TextView? = row.findViewById(R.id.timestamp)
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

        val isProfileFragment = fragment is ProfileFragment
        val isSharedTravelsFragment = fragment is SharedTravelsFragment

        holder.username.text = currentCard.username
        Picasso
            .get()
            .load(currentCard.userImage)
            .into(holder.userImage)
        Picasso
            .get()
            .load(currentCard.travelImage)
            .into(holder.travelImage)
        holder.travelName.text = currentCard.travelName

        if(isProfileFragment){
            holder.shareImage!!.visibility = View.VISIBLE
            holder.affinityPerc!!.visibility = View.GONE
            holder.affinityImage!!.visibility = View.GONE
            holder.likesNumber!!.visibility = View.GONE
            holder.likesImage!!.visibility = View.GONE
            holder.timestamp!!.visibility = View.VISIBLE

            holder.shareImage.setImageResource(R.drawable.profile_share)
            holder.timestamp.text = currentCard.timestamp
        }else{
            if(isSharedTravelsFragment){
                holder.likesImage!!.visibility = View.VISIBLE
                holder.likesNumber!!.visibility = View.VISIBLE
                holder.affinityPerc!!.visibility = View.GONE
                holder.affinityImage!!.visibility = View.GONE
                holder.timestamp!!.visibility = View.VISIBLE
            }else{
                holder.affinityPerc!!.visibility = View.VISIBLE
                holder.likesNumber!!.visibility = View.VISIBLE
                holder.likesImage!!.visibility = View.VISIBLE
                holder.timestamp!!.visibility = View.GONE
                holder.affinityImage!!.setImageResource(R.drawable.dashboard_affinity)
                holder.affinityPerc.text = currentCard.affinityPerc
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
            val params: FrameLayout.LayoutParams =
                holder.row.layoutParams as FrameLayout.LayoutParams
            params.bottomMargin = 100
            holder.row.layoutParams = params
        }
    }

    override fun getItemCount(): Int = cards.size

}
