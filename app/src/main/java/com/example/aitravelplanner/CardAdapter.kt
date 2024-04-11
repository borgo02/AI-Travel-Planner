package com.example.aitravelplanner
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CardAdapter(val cards: List<CardTravel>) : RecyclerView.Adapter<CardAdapter.CardHolder>() {
    class CardHolder(val row: View) : RecyclerView.ViewHolder(row) {
        val username = row.findViewById<TextView>(R.id.username)
        val userImage = row.findViewById<ImageView>(R.id.userImage)
        val travelImage = row.findViewById<ImageView>(R.id.travelImage)
        val travelName = row.findViewById<TextView>(R.id.travelName)
        val affinityPerc = row.findViewById<TextView>(R.id.affinityPerc)
        val likesNumber = row.findViewById<TextView>(R.id.likesNumber)
        val likesImage = row.findViewById<TextView>(R.id.likesImage)
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