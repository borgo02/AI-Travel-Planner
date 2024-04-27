package com.example.aitravelplanner.ui.components

import android.net.Uri

data class StageCard (val stageName:String, val stageImage: String, val stageAffinity: Int, val isSearched: Boolean = false, val isSelected: Boolean = false)