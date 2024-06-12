package com.example.aitravelplanner.ui.components.stageCard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aitravelplanner.R
import com.example.aitravelplanner.ui.components.imageview.CustomImageView

/**
 * Adapter per gestire la visualizzazione delle card delle tappe in una RecyclerView.
 *
 * @param data Lista mutabile delle card delle tappe.
 * @param callback Funzione opzionale per gestire le azioni sui pulsanti delle card.
 */
class StageCardAdapter(
    private val data: MutableList<StageCard>,
    private val callback: (StageCard) -> Unit = {}
) : RecyclerView.Adapter<StageCardAdapter.StageCardHolder>() {

    /**
     * ViewHolder per la visualizzazione della card della tappa.
     *
     */
    class StageCardHolder(val row: View) : RecyclerView.ViewHolder(row) {
        val stageName: TextView = row.findViewById<TextView>(R.id.stageName)
        val stageImage: CustomImageView = row.findViewById<CustomImageView>(R.id.stageImage)
        val affinityPercentage: TextView = row.findViewById<TextView>(R.id.affinityPercentage)
        val stageAffinityImage: ImageView = row.findViewById<ImageView>(R.id.stageAffinityImage)
        val addStageButton: ImageButton = row.findViewById<ImageButton>(R.id.addStageButton)
        val deleteStageButton: Button = row.findViewById<Button>(R.id.deleteStageButton)
    }

    /**
     * Crea il ViewHolder per la visualizzazione della card della tappa.
     *
     * @return StageCardHolder per la visualizzazione della card.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StageCardHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.component_stage_card, parent, false)
        return StageCardHolder(layout)
    }

    /**
     * Collega i dati della tappa al ViewHolder.
     */
    override fun onBindViewHolder(holder: StageCardHolder, position: Int) {
        val stageData = data[position]
        holder.affinityPercentage.visibility = if (stageData.isSearched) View.VISIBLE else View.GONE
        holder.stageAffinityImage.visibility = if (stageData.isSearched) View.VISIBLE else View.GONE
        holder.addStageButton.visibility = if (stageData.isSearched) View.VISIBLE else View.GONE
        holder.deleteStageButton.visibility = if (stageData.isSelected) View.VISIBLE else View.GONE
        holder.affinityPercentage.text = stageData.stageAffinity.toString()
        holder.stageName.text = stageData.stageName

        // Carica l'immagine della tappa o una immagine di default se non è disponibile
        if (stageData.stageImage != "")
            holder.stageImage.setURL(stageData.stageImage)
        else
            holder.stageImage.setImageResource(R.mipmap.ic_image_not_found)

        // Gestisce il click sul pulsante di eliminazione della tappa
        holder.deleteStageButton.setOnClickListener {
            callback(stageData)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount)
        }

        // Gestisce il click sul pulsante di aggiunta della tappa
        holder.addStageButton.setOnClickListener {
            callback(stageData)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount)
        }
    }

    /**
     * Restituisce il numero totale di card delle tappe.
     *
     */
    override fun getItemCount(): Int {
        return data.size
    }
}
