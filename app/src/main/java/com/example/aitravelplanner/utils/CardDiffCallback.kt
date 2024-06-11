package com.example.aitravelplanner.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.aitravelplanner.ui.components.travelCard.CardTravel

/** Classe utilizzata per calcolare le differenze tra due liste di oggetti CardTravel
 *
 */
class CardDiffCallback(
    private val oldList: List<CardTravel>, // La vecchia lista di viaggi
    private val newList: List<CardTravel>  // La nuova lista di viaggi
) : DiffUtil.Callback() {

    /** Ritorna la dimensione della vecchia lista
     *
     */
    override fun getOldListSize(): Int = oldList.size

    /** Ritorna la dimensione della nuova lista
     *
     */
    override fun getNewListSize(): Int = newList.size

    /** Verifica se due elementi della vecchia e nuova lista sono gli stessi (stesso ID)
     *
     */
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].travelId == newList[newItemPosition].travelId
    }

    /** Verifica se il contenuto di due elementi della vecchia e nuova lista è lo stesso
     *
     */
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
