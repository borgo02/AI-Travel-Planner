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

class StageCardAdapter(val data: List<StageCard>) : RecyclerView.Adapter<StageCardAdapter.StageCardHolder>() {
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
        holder.stageAffinityImage.visibility = if (stageData.isSearched) View.GONE else View.VISIBLE
        holder.addStageButton.visibility = if (stageData.isSearched) View.GONE else View.VISIBLE
        holder.deleteStageButton.visibility = if (stageData.isSelected) View.VISIBLE else View.GONE
        holder.affinityPercentage.text = stageData.stageAffinity.toString()
        holder.stageName.text = stageData.stageName
        holder.stageImage.setImageURI(Uri.parse(stageData.stageImage))
    }

    override fun getItemCount(): Int {
        return data.size
    }
}