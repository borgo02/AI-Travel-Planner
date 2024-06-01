package com.example.aitravelplanner.ui.components.travelCard

import com.example.aitravelplanner.ui.components.stageCard.StageCard

data class CardTravel(val username: String, val userImage: String,
                      val travelImage: String, var travelName: String,
                      val affinityPerc: String?, var travelLikes: Int?,
                      val timestamp: String, var isLiked: Boolean, val info: String, val stageCardList: ArrayList<StageCard>, val userId: String, val travelId: String, var isShared: Boolean = true)
