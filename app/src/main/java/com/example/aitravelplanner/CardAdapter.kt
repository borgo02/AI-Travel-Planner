package com.example.aitravelplanner
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aitravelplanner.ui.components.CardTravel

class CardAdapter(val cards: List<CardTravel>) : RecyclerView.Adapter<CardAdapter.CardHolder>() {
    class CardHolder(val row: View) : RecyclerView.ViewHolder(row) {
        val username: TextView = row.findViewById(R.id.username)
        val userImage: ImageView = row.findViewById(R.id.userImage)
        val travelImage: ImageView = row.findViewById(R.id.travelImage)
        val travelName: TextView = row.findViewById(R.id.travelName)
        val affinityPerc: TextView = row.findViewById(R.id.affinityPerc)
        val likesNumber: TextView = row.findViewById(R.id.likesNumber)
        val likesImage: TextView = row.findViewById(R.id.likesImage)
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

        holder.username.text = currentCard.username
        holder.userImage.setImageResource(currentCard.userImage)
        holder.travelImage.setImageResource(currentCard.travelImage)
        holder.travelName.text = currentCard.travelName
        holder.affinityPerc.text = currentCard.affinityPerc
        holder.likesNumber.text = currentCard.likesNumber
    }

    override fun getItemCount(): Int = cards.size
}