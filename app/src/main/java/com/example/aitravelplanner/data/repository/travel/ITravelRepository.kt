package com.example.aitravelplanner.data.repository.travel

import com.example.aitravelplanner.data.model.Stage
import com.example.aitravelplanner.data.model.Travel
import com.example.aitravelplanner.data.model.User
import com.google.firebase.firestore.DocumentSnapshot

interface ITravelRepository {
    /** Inserisce un nuovo viaggio nel database Firestore
     *
     */
    suspend fun setTravel(travel: Travel)

    /** Modifica il valore "shared" di un viaggio da false a true.
     *
     * In questo modo il viaggio sarà pubblicato in dashboard
     */
    suspend fun setStageByTravel(idTravel: String, stage: Stage)

    /** Inserisce una nuova tappa ad un viaggio specifico
     *
     */
    suspend fun setTravelToShared(idTravel: String)

    /** Ritorna tutti i viaggi presenti nel database
     *
     */
    suspend fun getTravels(): ArrayList<Travel>

    /** Ritorna tutti i viaggi presenti nel database che sono stati pubblicati
     *
     */
    suspend fun getSharedTravels(idUser: String): ArrayList<Travel>

    /** Ritorna un viaggio identificato da uno specifico id
     *
     */
    suspend fun getTravelById(idTravel: String, idUser: String): Travel?

    /** Ritorna tutte le tappe filtrate per città e per una "stringa filtro" inserita dall'utente
     *
     */
    suspend fun getFilteredStagesByCity(filter: String, city: String): ArrayList<Stage>

    /** Ritorna tutte le tappe collegate ad uno specifico viaggio
     *
     */
    suspend fun getStagesByTravel(idTravel: String): ArrayList<Stage>

    /** Ritorna tutti i viaggi creati da uno specifico utente
     *
     */
    suspend fun getTravelsCreatedByUser(user: User): ArrayList<Travel>

    /** Presi come input gli id di un viaggio e di utente, ritorna:
     *
     * - false se l'utente non ha messo like al viaggio
     *
     * - true altrimenti.
     *
     */
    suspend fun isTravelLikedByUser(idTravel: String, idUser: String): Boolean

    /**
     * Ritorna i viaggi di un utente specifico
     */
    suspend fun getTravelsByUser(idUser: String): ArrayList<Travel>
    suspend fun mapDocumentToTravel(travel: DocumentSnapshot, idUser: String): Travel?
}