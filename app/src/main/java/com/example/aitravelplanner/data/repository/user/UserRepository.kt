package com.example.aitravelplanner.data.repository.user

import android.util.Log
import com.example.aitravelplanner.data.model.Likes
import com.example.aitravelplanner.data.model.Travel
import com.example.aitravelplanner.data.model.User
import com.example.aitravelplanner.data.repository.BaseRepository
import com.example.aitravelplanner.data.repository.travel.TravelRepository
import kotlinx.coroutines.tasks.await
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject private constructor(): IUserRepository, BaseRepository() {
    private var currentUser: User? = null
    private val travelRepository: TravelRepository = TravelRepository()
    private val usersCollectionRef: CollectionReference = db.collection("users")
    private val travelsCollectionReference: CollectionReference = db.collection("travels")
    private val repositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun getUser(): User? {
        return currentUser
    }

    override suspend fun getUserReference(idUser: String): DocumentReference{
        return usersCollectionRef.document(idUser)
    }

    override fun updateUser(newUser: User) {
        currentUser = newUser
        repositoryScope.launch {
            setUser(newUser)
        }
    }

    override suspend fun setUser(user: User) {
        db.collection("users").document(user.idUser).set(user).await()
    }

    override suspend fun updateLikedTravelByUser(idUser: String, idTravel: String, isLiked: Boolean) {
        val travelRef = travelsCollectionReference.document(idTravel)
        val likedTravelsRef = usersCollectionRef.document(idUser).collection("likedTravels")
        if(isLiked) {
            val like = Likes(null, travelRef, Timestamp.now().toDate())
            db.runTransaction{transaction ->
                val snapshot = transaction.get(travelRef)
                val newLikesValue = snapshot.getLong("numberOfLikes")!! + 1
                transaction.update(travelRef, "numberOfLikes", newLikesValue)
                likedTravelsRef.add(like)
            }.await()
        }else{
            val likes = likedTravelsRef.get().await()
            for(like in likes.documents) {
                val idLike = like.id
                val idTravelReferencePath = like.getDocumentReference("idTravel")?.path
                val idTravelDoc = idTravelReferencePath?.substringAfterLast("/")
                if (idTravelDoc == idTravel) {
                    db.runTransaction{transaction ->
                        val snapshot = transaction.get(travelRef)
                        val newLikesValue = snapshot.getLong("numberOfLikes")!! - 1
                        transaction.update(travelRef, "numberOfLikes", newLikesValue)
                        likedTravelsRef.document(idLike).delete()
                    }.await()
                }
            }
        }
    }

    override suspend fun getTravelsByUser(idUser: String): ArrayList<Travel> {
        val userRef = usersCollectionRef.document(idUser)
        val travelRef = travelsCollectionReference.whereEqualTo("idUser", userRef).get().await()
        val sharedTravelList: ArrayList<Travel> = arrayListOf()
        for(travel in travelRef.documents){
            val travelData = travelRepository.getTravelById(travel.id, idUser)
            if (travelData != null)
                sharedTravelList.add(travelData)
        }

        return sharedTravelList
    }

    override suspend fun getInterestsByUser(idUser: String): Map<String, Float>? {
        val userRef = db.collection("users").document(idUser).get().await()

        return if(userRef.exists()){
            val interests = userRef.get("interests") as Map<String, Float>
            interests
        }else
            null
    }

    override suspend fun getSharedTravelsByUser(idUser: String): ArrayList<Travel> {
        val userRef = usersCollectionRef.document(idUser)
        val travelRef = travelsCollectionReference.whereEqualTo("idUser", userRef).get().await()
        val sharedTravelList: ArrayList<Travel> = arrayListOf()
        for(travel in travelRef.documents){
            val travelData = travelRepository.getTravelById(travel.id, idUser)
            if(travelData != null && travelData.isShared!!)
                sharedTravelList.add(travelData)
        }

        return sharedTravelList
    }

    override suspend fun getNotSharedTravelsByUser(idUser: String): ArrayList<Travel> {
        val userRef = usersCollectionRef.document(idUser)
        val travelRef = travelsCollectionReference.whereEqualTo("idUser", userRef).get().await()
        val notSharedTravelList: ArrayList<Travel> = arrayListOf()
        for(travel in travelRef.documents){
            val travelData = travelRepository.getTravelById(travel.id, idUser)
            if(travelData != null && !travelData.isShared!!)
                notSharedTravelList.add(travelData)
        }

        return notSharedTravelList
    }

    override suspend fun getUsers(): ArrayList<User> {
        val users = usersCollectionRef.get().await()
        val userList: ArrayList<User> = arrayListOf()
        for (doc in users.documents) {
            val idUser = doc.id
            val userData = this.getUserById(idUser)
            if(userData != null)
                userList.add(userData)
        }

        return userList
    }

    override suspend fun getUserById(idUser: String, isCurrentUser: Boolean): User? {
        val userDoc = usersCollectionRef.document(idUser).get().await()
        val likedTravelList: ArrayList<Likes>
        return if(userDoc.exists()){
            val email = userDoc.getString("email")
            val isInit = userDoc.getBoolean("initialized")
            val fullname = userDoc.getString("fullname")
            val interests = userDoc.get("interests") as Map<String, Float>
            likedTravelList = this.getLikesByUser(idUser)
            val user = User(idUser, email!!, fullname!!, isInit!!, interests, likedTravelList)
            if (isCurrentUser)
                currentUser = user
            user
        }else
            null
    }

    override suspend fun getUserByTravel(idTravel: String): User? {
        val travelRef = travelsCollectionReference.document(idTravel).get().await()
        return if(travelRef.exists()){
            val idUserReferencePath = travelRef.getDocumentReference("idUser")?.path
            val idUser = idUserReferencePath?.substringAfterLast("/")!!
            val userRef = db.collection("users").document(idUser).get().await()
            if(userRef.exists())
                getUserById(idUser)
            else
                null
        } else
            null
    }

    override suspend fun getLikesByUser(idUser: String): ArrayList<Likes> {
        val likesList: ArrayList<Likes> = arrayListOf()
        val likesRef = usersCollectionRef.document(idUser).collection("likedTravels").get().await()
        for(like in likesRef.documents){
            val idLike = like.id
            val idTravelReferencePath = like.getDocumentReference("idTravel")!!.path
            val idTravelDoc = idTravelReferencePath.substringAfterLast("/")
            val timestamp = like.getTimestamp("timestamp")!!.toDate()
            val travelRef = db.collection("travels").document(idTravelDoc)
            val likeItem = Likes(idLike, travelRef, timestamp)
            likesList.add(likeItem)
        }

        return likesList
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(): UserRepository {
            return instance ?: synchronized(this) {
                instance ?: UserRepository().also { instance = it }
            }
        }
    }
}