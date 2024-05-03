package com.example.aitravelplanner.ui.components

data class CardTravel(val username: String, val userImage: String,
                      val travelImage: String, var travelName: String,
                      val affinityPerc: String?, var travelLikes: Int?,
                      val timestamp: String, var isLiked: Boolean){

}