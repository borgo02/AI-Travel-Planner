package com.example.aitravelplanner.data.repository.user

import com.example.aitravelplanner.data.model.Likes
import com.example.aitravelplanner.data.model.Travel
import com.example.aitravelplanner.data.model.User
import com.google.firebase.firestore.DocumentReference

interface IUserRepository {
    /** Ritorna l'utente corrente
     *
     */
    fun getUser(): User?

    /** Ritorna il riferimento di uno specifico utente nel database Firestore
     *
     */
    suspend fun getUserReference(idUser: String): DocumentReference

    /** Set o update dell'utente corrente
     *
     */
    fun updateUser(newUser: User)

    /** Inserimento di un nuovo utente nel database
     *
     */
    suspend fun setUser(user: User)

    /** Ritornare i viaggi generati da un utente specifico
     *
     */
    suspend fun getTravelsByUser(idUser: String): ArrayList<Travel>

    /** Ritorna i viaggi pubblicati da un utente specifico
     *
     */
    suspend fun getSharedTravelsByUser(idUser: String): ArrayList<Travel>

    /** Ritorna i viaggi non pubblicati da un utente specifico
     *
     */
    suspend fun getNotSharedTravelsByUser(idUser: String): ArrayList<Travel>

    /** Ritorna tutti gli utenti presenti nel database
     *
     */
    suspend fun getUsers(): ArrayList<User>

    /** Ritorna uno utente identificato da uno specifico id
     *
     */
    suspend fun getUserById(idUser: String, isCurrentUser: Boolean = false): User?

    /** Ritorna il proprietario (utente) di uno specifico viaggio passato in input
     *
     */
    suspend fun getUserByTravel(idTravel: String): User?

    /** Ritorna la lista dei like inseriti da un utente a degli specifici viaggi
     *
     */
    suspend fun getLikesByUser(idUser: String): ArrayList<Likes>

    /** Ritorna gli interessi che sono stati inseriti dopo il primo login da un utente specifico.
     *
     */
    suspend fun getInterestsByUser(idUser: String): Map<String, Float>?

    /** Aggiornamento del numero di like e della lista dei viaggi che sono piaciuti ad un utente
     *
     */
    suspend fun updateLikedTravelByUser(idUser: String, idTravel: String, isLiked: Boolean)
}