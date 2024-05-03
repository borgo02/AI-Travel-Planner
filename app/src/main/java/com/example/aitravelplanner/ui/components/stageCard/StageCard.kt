package com.example.aitravelplanner.ui.components.stageCard

import android.net.Uri

data class StageCard (val stageName:String, val stageImage: String, val stageAffinity: Int, var isSearched: Boolean = false, var isSelected: Boolean = false)