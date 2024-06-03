package com.example.aitravelplanner.ui.components.travelCard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.aitravelplanner.R
import com.example.aitravelplanner.ui.components.imageview.CustomImageView
import com.example.aitravelplanner.ui.dashboard.DashboardFragment
import com.example.aitravelplanner.ui.dashboard.DashboardFragmentDirections
import com.example.aitravelplanner.ui.profile.ProfileFragment
import com.example.aitravelplanner.ui.profile.ProfileFragmentDirections
import com.example.aitravelplanner.ui.profile.SharedTravelsFragment
import com.example.aitravelplanner.ui.profile.SharedTravelsFragmentDirections
import com.example.aitravelplanner.utils.CardDiffCallback

class CardAdapter(
    private val cards: MutableList<CardTravel>,
    private val isLiked: ((CardTravel, String) -> Boolean)? = null,
    fragment: Fragment,
    private val loadSelectedTravel: ((CardTravel) -> Unit)? = null,
    private val shareTravel: ((CardTravel) -> Unit)? = null
) : RecyclerView.Adapter<CardAdapter.CardHolder>() {

    private val fragmentType: FragmentType = when (fragment) {
        is DashboardFragment -> FragmentType.DASHBOARD
        is ProfileFragment -> FragmentType.PROFILE
        is SharedTravelsFragment -> FragmentType.SHARED_TRAVELS
        else -> FragmentType.UNKNOWN
    }

    enum class FragmentType {
        DASHBOARD, PROFILE, SHARED_TRAVELS, UNKNOWN
    }

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
        if (currentCard.travelImage.isEmpty())
            holder.travelImage.setImageResource(R.mipmap.ic_image_not_found)
        else
            holder.travelImage.setURL(currentCard.travelImage)
        holder.userImage.setURL(currentCard.userImage)
        holder.travelName.text = currentCard.travelName

        when (fragmentType) {
            FragmentType.PROFILE -> {
                holder.profileBar.visibility = View.GONE
                holder.shareImage?.visibility = if (currentCard.isShared) View.GONE else View.VISIBLE
                holder.affinityPerc?.visibility = View.GONE
                holder.affinityImage?.visibility = View.GONE
                holder.likesNumber?.visibility = View.GONE
                holder.likesImage?.visibility = View.GONE
                holder.timestamp?.visibility = View.VISIBLE

                holder.timestamp?.text = currentCard.timestamp
                holder.shareImage?.setImageResource(R.drawable.profile_share)
                holder.shareImage?.setOnClickListener {
                    shareTravel?.invoke(currentCard)
                    holder.shareImage.visibility = View.GONE
                }
                holder.travelImage.setOnClickListener { view ->
                    val action = ProfileFragmentDirections.actionNavigationProfileToTravelFragment(0)
                    view.findNavController().navigate(action)
                    loadSelectedTravel?.invoke(currentCard)
                }
            }
            FragmentType.SHARED_TRAVELS -> {
                holder.profileBar.visibility = View.GONE
                holder.likesImage?.visibility = View.VISIBLE
                holder.likesNumber?.visibility = View.VISIBLE
                holder.affinityPerc?.visibility = View.GONE
                holder.affinityImage?.visibility = View.GONE
                holder.timestamp?.visibility = View.VISIBLE
                holder.shareImage?.visibility = View.GONE

                holder.travelImage.setOnClickListener { view ->
                    val action = SharedTravelsFragmentDirections.actionNavigationSharedTravelsToTravelFragment(1)
                    view.findNavController().navigate(action)
                    loadSelectedTravel?.invoke(currentCard)
                }

                holder.likesNumber?.text = currentCard.travelLikes.toString()
                holder.likesImage?.setImageResource(
                    if (currentCard.isLiked) R.drawable.dashboard_heart_selected
                    else R.drawable.dashboard_heart_not_selected
                )

                holder.likesImage?.setOnClickListener {
                    val liked = isLiked?.invoke(currentCard, fragmentType.name.lowercase()) ?: false
                    holder.likesImage.setImageResource(
                        if (liked) R.drawable.dashboard_heart_selected
                        else R.drawable.dashboard_heart_not_selected
                    )
                    holder.likesNumber?.text = currentCard.travelLikes.toString()
                }
            }
            FragmentType.DASHBOARD -> {
                holder.profileBar.visibility = View.VISIBLE
                holder.affinityPerc?.visibility = View.VISIBLE
                holder.likesNumber?.visibility = View.VISIBLE
                holder.likesImage?.visibility = View.VISIBLE
                holder.timestamp?.visibility = View.GONE
                holder.shareImage?.visibility = View.GONE
                holder.affinityImage?.setImageResource(R.drawable.dashboard_affinity)
                holder.affinityPerc?.text = currentCard.affinityPerc

                holder.travelImage.setOnClickListener { view ->
                    val action = DashboardFragmentDirections.actionNavigationDashboardToTravelFragment(2)
                    view.findNavController().navigate(action)
                    loadSelectedTravel?.invoke(currentCard)
                }

                holder.likesNumber?.text = currentCard.travelLikes.toString()
                holder.likesImage?.setImageResource(
                    if (currentCard.isLiked) R.drawable.dashboard_heart_selected
                    else R.drawable.dashboard_heart_not_selected
                )

                holder.likesImage?.setOnClickListener {
                    val liked = isLiked?.invoke(currentCard, fragmentType.name.lowercase()) ?: false
                    holder.likesImage.setImageResource(
                        if (liked) R.drawable.dashboard_heart_selected
                        else R.drawable.dashboard_heart_not_selected
                    )
                    holder.likesNumber?.text = currentCard.travelLikes.toString()
                }
            }

            FragmentType.UNKNOWN -> TODO()
        }

        val layoutParams = holder.itemView.layoutParams as RecyclerView.LayoutParams
        layoutParams.bottomMargin = if (position == itemCount - 1) 300 else 0
        holder.itemView.layoutParams = layoutParams
    }

    override fun getItemCount(): Int = cards.size

    fun updateData(newCardList: List<CardTravel>) {
        val oldCards = ArrayList(cards)
        cards.clear()
        cards.addAll(newCardList)

        val diffResult = DiffUtil.calculateDiff(CardDiffCallback(oldCards, newCardList))
        diffResult.dispatchUpdatesTo(this)
    }

    fun addCard(card: CardTravel) {
        cards.add(card)
        notifyItemInserted(cards.size - 1)
    }

    fun removeCard(card: CardTravel) {
        val index = cards.indexOf(card)
        if (index != -1) {
            cards.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    fun updateCard(card: CardTravel) {
        val index = cards.indexOf(card)
        if (index != -1) {
            cards[index] = card
            notifyItemChanged(index)
        }
    }
}
