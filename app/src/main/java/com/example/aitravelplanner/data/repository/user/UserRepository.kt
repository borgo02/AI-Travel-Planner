package com.example.aitravelplanner.data.repository.user

import com.example.aitravelplanner.data.model.Likes
import com.example.aitravelplanner.data.model.Travel
import com.example.aitravelplanner.data.model.User
import com.example.aitravelplanner.data.repository.BaseRepository
import com.example.aitravelplanner.data.repository.travel.TravelRepository
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/** Repository della classe User che estende la superclasse BaseRepository
 *
 */
@Singleton
class UserRepository @Inject private constructor(): IUserRepository, BaseRepository(){
    private val pageOffset: Long = 10
    private var lastSnapshot: DocumentSnapshot? = null
    private var currentUser: User? = null
    var travelRepository: TravelRepository = TravelRepository()
    var usersCollectionRef: CollectionReference = db.collection("users")
    var travelsCollectionReference: CollectionReference = db.collection("travels")
    private val repositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val mutex = Mutex()

    /** Ritorna l'utente corrente
     *
     */
    override fun getUser(): User? {
        return currentUser
    }

    /** Ritorna il riferimento di uno specifico utente nel database Firestore
     *
     */
    override suspend fun getUserReference(idUser: String): DocumentReference{
        return usersCollectionRef.document(idUser)
    }

    /** Set o update dell'utente corrente
     *
     */
    override fun updateUser(newUser: User) {
        currentUser = newUser
        repositoryScope.launch {
            setUser(newUser)
        }
    }

    /** Inserimento di un nuovo utente nel database
     *
     */
    override suspend fun setUser(user: User) {
        db.collection("users").document(user.idUser).set(user).await()
    }

    /** Aggiornamento del numero di like e della lista dei viaggi che sono piaciuti ad un utente
     *
     */
    override suspend fun updateLikedTravelByUser(idUser: String, idTravel: String, isLiked: Boolean) {
        mutex.withLock {
            val travelRef = travelsCollectionReference.document(idTravel)
            val likedTravelsRef = usersCollectionRef.document(idUser).collection("likedTravels")
            val likedTravelQuery = likedTravelsRef.whereEqualTo("idTravel", travelRef).get().await()

            db.runTransaction { transaction ->
                val snapshot = transaction.get(travelRef)
                val currentLikesValue = snapshot.getLong("numberOfLikes") ?: 0

                if (isLiked)
                    if (likedTravelQuery.isEmpty) {
                        val like = Likes(null, travelRef, Timestamp.now().toDate())
                        transaction.update(travelRef, "numberOfLikes", currentLikesValue + 1)
                        likedTravelsRef.document().set(like)
                    }
                    else
                        for (like in likedTravelQuery.documents) {
                            val idLike = like.id
                            transaction.update(travelRef, "numberOfLikes", currentLikesValue - 1)
                            likedTravelsRef.document(idLike).delete()
                            break
                        }
            }.await()
        }
    }

    /** Ritornare i viaggi generati da un utente specifico
     *
     */
    override suspend fun getTravelsByUser(idUser: String): ArrayList<Travel> {
        val travelRef = travelsCollectionReference.whereEqualTo("idUser", idUser).get().await()
        val sharedTravelList: ArrayList<Travel> = arrayListOf()
        for(travel in travelRef.documents){
            val travelData = travelRepository.getTravelById(travel.id, idUser)
            if (travelData != null)
                sharedTravelList.add(travelData)
        }

        return sharedTravelList
    }

    /** Ritorna gli interessi che sono stati inseriti dopo il primo login da un utente specifico.
     *
     */
    override suspend fun getInterestsByUser(idUser: String): Map<String, Float>? {
        val userRef = db.collection("users").document(idUser).get().await()

        return if(userRef.exists()){
            val interests = userRef.get("interests") as Map<String, Float>
            interests
        }else
            null
    }


    /** Ritorna tutti gli utenti presenti nel database
     *
     */
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

    /** Ritorna uno utente identificato da uno specifico id
     *
     */
    override suspend fun getUserById(idUser: String, isCurrentUser: Boolean): User? {
        val userDoc = usersCollectionRef.document(idUser).get().await()
        val likedTravelList: ArrayList<Likes>
        return if(userDoc.exists()){
            val email = userDoc.getString("email")
            val isInit = userDoc.getBoolean("initialized")
            val fullname = userDoc.getString("fullname")
            var interests: Map<String, Float>? = null
            if (userDoc.get("interests") != null)
            {
                interests = userDoc.get("interests") as Map<String, Float>
            }
            likedTravelList = this.getLikesByUser(idUser)
            val user = User(idUser, email!!, fullname!!, isInit!!, interests, likedTravelList)
            if (isCurrentUser)
                currentUser = user
            user
        }else
            null
    }

    /** Ritorna il proprietario (utente) di uno specifico viaggio passato in input
     *
     */
    override suspend fun getUserByTravel(idTravel: String): User? {
        val travelRef = travelsCollectionReference.document(idTravel).get().await()
        return if(travelRef.exists()){
            val idUser = travelRef.getString("idUser")
            val userRef = db.collection("users").document(idUser!!).get().await()
            if(userRef.exists())
                getUserById(idUser)
            else
                null
        } else
            null
    }

    /** Ritorna la lista dei like inseriti da un utente a degli specifici viaggi
     *
     */
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

    /** Oggetto per l'implementazione del pattern Singleton.
     *
     * Permette di utilizzare una sola istanza di UserRepository all'interno dell'intera applicazione.
     */
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