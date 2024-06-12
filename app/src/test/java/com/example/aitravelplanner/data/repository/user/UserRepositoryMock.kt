package com.example.aitravelplanner.data.repository.user

import com.example.aitravelplanner.data.model.Likes
import com.example.aitravelplanner.data.model.Travel
import com.example.aitravelplanner.data.model.User
import com.google.firebase.firestore.DocumentReference
import io.mockk.mockk
import java.util.Calendar
import java.util.TimeZone

class UserRepositoryMock: IUserRepository {
    private var currentUser: User = User("idUserTest1", "usertest1@mail", "User Test 1", true, mapOf("interest1" to 0.5f, "interest2" to 0.3f, ))
    private val users = mutableListOf<User>(currentUser)
    private val travels = mutableListOf<Travel>()
    private val likes = mutableListOf<Likes>()

    init{users.add(currentUser)}
    override fun getUser(): User?{
        return currentUser
    }

    override suspend fun getUserReference(idUser: String): DocumentReference {
        return mockk(idUser)
    }

    override fun updateUser(newUser: User) {
        currentUser = newUser
    }

    override suspend fun setUser(user: User) {
        users.add(user)
    }

    override suspend fun updateLikedTravelByUser(idUser: String, idTravel: String, isLiked: Boolean) {
        val like = likes.find { it.idTravel == mockk(idTravel)}
        if (isLiked) {
            if (like == null) {
                likes.add(Likes(idUser, mockk(idTravel), Calendar.getInstance(TimeZone.getTimeZone("UTC")).time))
            }
        } else {
            likes.remove(like)
        }
    }

    override suspend fun getInterestsByUser(idUser: String): Map<String, Float>? {
        return users.find { it.idUser == idUser }?.interests
    }

    override suspend fun getTravelsByUser(idUser: String): ArrayList<Travel> {
        return ArrayList(travels.filter { it.idUser == mockk(idUser) })
    }

    override suspend fun getSharedTravelsByUser(idUser: String): ArrayList<Travel> {
        return ArrayList(travels.filter { it.isShared == true })
    }

    override suspend fun getNotSharedTravelsByUser(idUser: String): ArrayList<Travel> {
        return ArrayList(travels.filter { it.idUser == mockk(idUser) && it.isShared == false })
    }

    override suspend fun getUsers(): ArrayList<User> {
        return ArrayList(users)
    }

    override suspend fun getUserById(idUser: String, isCurrentUser: Boolean): User? {
        return users.find { it.idUser == idUser }
    }

    override suspend fun getUserByTravel(idTravel: String): User? {
        val travel = travels.find { it.idTravel == idTravel }
        return travel?.let { users.find { user -> travel.idUser == mockk( user.idUser) } }
    }

    override suspend fun getLikesByUser(idUser: String): ArrayList<Likes> {
        return ArrayList(likes)
    }

    companion object {
        @Volatile
        private var instance: UserRepositoryMock? = null

        fun getInstance(): UserRepositoryMock {
            if(instance == null){
                instance = UserRepositoryMock()
                return instance as UserRepositoryMock
            }
            else
                return instance as UserRepositoryMock
        }
    }
}