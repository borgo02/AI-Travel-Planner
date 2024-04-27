package com.example.aitravelplanner.ui.components

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aitravelplanner.R
import com.squareup.picasso.Picasso

class StageCardAdapter(private val data: MutableList<StageCard>, private val callback: (StageCard) -> Unit = {}) : RecyclerView.Adapter<StageCardAdapter.StageCardHolder>() {
    class StageCardHolder(val row: View) : RecyclerView.ViewHolder(row) {
        val stageName = row.findViewById<TextView>(R.id.stageName)
        val stageImage = row.findViewById<ImageView>(R.id.stageImage)
        val affinityPercentage = row.findViewById<TextView>(R.id.affinityPercentage)
        val stageAffinityImage = row.findViewById<ImageView>(R.id.stageAffinityImage)
        val addStageButton = row.findViewById<ImageButton>(R.id.addStageButton)
        val deleteStageButton = row.findViewById<Button>(R.id.deleteStageButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StageCardHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.stage_card_component, parent, false)
        return StageCardHolder(layout)
    }

    override fun onBindViewHolder(holder: StageCardHolder, position: Int) {
        val stageData = data[position]
        holder.affinityPercentage.visibility = if (stageData.isSearched) View.VISIBLE else View.GONE
        holder.stageAffinityImage.visibility = if (stageData.isSearched) View.VISIBLE else View.GONE
        holder.addStageButton.visibility = if (stageData.isSearched) View.VISIBLE else View.GONE
        holder.deleteStageButton.visibility = if (stageData.isSelected) View.VISIBLE else View.GONE
        holder.affinityPercentage.text = stageData.stageAffinity.toString()
        holder.stageName.text = stageData.stageName
        Picasso
            .get()
            .load(stageData.stageImage)
            .into(holder.stageImage)
        holder.deleteStageButton.setOnClickListener {callback(stageData)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount)}
        holder.addStageButton.setOnClickListener {callback(stageData)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount)}
    }

    override fun getItemCount(): Int {
        return data.size
    }
}