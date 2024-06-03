package com.example.aitravelplanner.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.aitravelplanner.ui.components.travelCard.CardTravel

class CardDiffCallback(
    private val oldList: List<CardTravel>,
    private val newList: List<CardTravel>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].travelId == newList[newItemPosition].travelId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
