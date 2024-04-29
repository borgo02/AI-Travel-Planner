package com.example.aitravelplanner.ui.components

data class CardTravel(val username: String, val userImage: String,
                      val travelImage: String, val travelName: String,
                      val affinityPerc: String?, val travelLikes: Int?,
                      val timestamp: String, var isLiked: Boolean){

}
