package com.example.aitravelplanner
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.aitravelplanner.ui.profile.ProfileFragment

class CardAdapter(private val cards: List<CardTravel>, private val fragment: Fragment) : RecyclerView.Adapter<CardAdapter.CardHolder>() {
    class CardHolder(private val row: View) : RecyclerView.ViewHolder(row) {
        val username: TextView = row.findViewById(R.id.username)
        val userImage: ImageView = row.findViewById(R.id.userImage)
        val travelImage: ImageView = row.findViewById(R.id.travelImage)
        val travelName: TextView = row.findViewById(R.id.travelName)
        val affinityPerc: TextView = row.findViewById(R.id.affinityPerc)
        val likesNumber: TextView = row.findViewById(R.id.likesNumber)
        val likesImage: ImageView = row.findViewById(R.id.likesImage)
        val shareImage: ImageView = row.findViewById(R.id.shareImage)
        val timestamp: TextView = row.findViewById(R.id.timestamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardHolder {
        val layout = LayoutInflater.from(parent.context).inflate(
            R.layout.travel_card_component,
            parent, false
        )
        return CardHolder(layout)
    }

    override fun onBindViewHolder(holder: CardHolder, position: Int) {
        val currentCard = cards[position]

        val childFragmentManager = fragment.childFragmentManager
        val isProfileFragment = childFragmentManager.findFragmentById(R.id.fragment_profile) is ProfileFragment

        if(isProfileFragment){
            holder.affinityPerc.visibility = View.GONE
            holder.likesNumber.visibility = View.GONE
            holder.likesImage.visibility = View.GONE
            holder.shareImage.visibility = View.VISIBLE
            holder.timestamp.visibility = View.VISIBLE

            holder.shareImage.setImageResource(currentCard.shareImage)
            holder.timestamp.text = currentCard.timestamp
        }else{
            holder.affinityPerc.visibility = View.VISIBLE
            holder.likesNumber.visibility = View.VISIBLE
            holder.likesImage.visibility = View.VISIBLE
            holder.shareImage.visibility = View.GONE
            holder.timestamp.visibility = View.GONE

            holder.affinityPerc.text = currentCard.affinityPerc
            holder.likesNumber.text = currentCard.likesNumber
            holder.likesImage.setImageResource(currentCard.likesImage)
        }

        holder.username.text = currentCard.username
        holder.userImage.setImageResource(currentCard.userImage)
        holder.travelImage.setImageResource(currentCard.travelImage)
        holder.travelName.text = currentCard.travelName
    }

    override fun getItemCount(): Int = cards.size
}