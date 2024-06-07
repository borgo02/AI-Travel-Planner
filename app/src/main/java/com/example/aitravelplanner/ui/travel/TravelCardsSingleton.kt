package com.example.aitravelplanner.ui.travel

import androidx.lifecycle.MutableLiveData
import com.example.aitravelplanner.data.model.Travel
import com.example.aitravelplanner.data.model.User
import com.example.aitravelplanner.data.repository.travel.TravelRepository
import com.example.aitravelplanner.data.repository.user.UserRepository
import com.example.aitravelplanner.ui.components.stageCard.StageCard
import com.example.aitravelplanner.ui.components.travelCard.CardTravel
import com.example.aitravelplanner.utils.notifyObserver

class TravelCardsSingleton() {
    val travelCardsList = MutableLiveData(arrayListOf<CardTravel>())
    private val travelRepository: TravelRepository = TravelRepository()
    private val userRepository: UserRepository = UserRepository.getInstance()
    suspend fun setTravelCards(userId: String){
        val sharedTravels = travelRepository.getSharedTravels(userId)
        val notSharedTravels = userRepository.getNotSharedTravelsByUser(userId)
        addTravels(sharedTravels)
        addTravels(notSharedTravels)
        notifyChanges()
    }

    private suspend fun addTravels(travels: ArrayList<Travel>){
        for (travel in travels) {
            val userTravel: User = travel.idUser?.path?.let { userRepository.getUserById(it.substringAfterLast("/"))}!!

            val stageCardList = arrayListOf<StageCard>()
            for (stage in travel.stageList!!)
                stageCardList.add(StageCard(stageName = stage.name, stageImage = stage.imageUrl, stageAffinity = 11))

            val cardTravel = CardTravel(
                username = userTravel.fullname,
                userImage = "https://cdn-icons-png.flaticon.com/512/8847/8847419.png",
                travelImage = travel.imageUrl ?: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTnfAxGV-fZxGL9elM_hQ2tp7skLeSwMyUiwo4lMm1zyA&s",
                travelName = travel.name!!,
                affinityPerc = "",
                travelLikes = travel.numberOfLikes,
                timestamp = travel.timestamp.toString(),
                isLiked = travel.isLiked!!,
                info = travel.info!!,
                stageCardList = stageCardList,
                userId = userTravel.idUser,
                travelId = travel.idTravel!!,
                isShared = travel.isShared!!
            )
            travelCardsList.value!!.add(cardTravel)
        }
    }

    fun addTravel(travel: Travel, userTravel: User){
        val stageCardList = arrayListOf<StageCard>()
        for (stage in travel.stageList!!)
            stageCardList.add(StageCard(stageName = stage.name, stageImage = stage.imageUrl, stageAffinity = 11))

        val cardTravel = CardTravel(
            username = userTravel.fullname,
            userImage = "https://cdn-icons-png.flaticon.com/512/8847/8847419.png",
            travelImage = travel.imageUrl ?: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTnfAxGV-fZxGL9elM_hQ2tp7skLeSwMyUiwo4lMm1zyA&s",
            travelName = travel.name!!,
            affinityPerc = "",
            travelLikes = travel.numberOfLikes,
            timestamp = travel.timestamp.toString(),
            isLiked = travel.isLiked!!,
            info = travel.info!!,
            stageCardList = stageCardList,
            userId = userTravel.idUser,
            travelId = travel.idTravel!!,
            isShared = travel.isShared!!
        )
        travelCardsList.value!!.add(cardTravel)
        travelCardsList.notifyObserver()
    }

    fun notifyChanges(){
        travelCardsList.notifyObserver()
    }
    companion object {
        @Volatile
        private var instance: TravelCardsSingleton? = null

        fun getInstance(): TravelCardsSingleton {
            return instance ?: synchronized(this) {
                instance ?: TravelCardsSingleton().also { instance = it }
            }
        }
    }
}