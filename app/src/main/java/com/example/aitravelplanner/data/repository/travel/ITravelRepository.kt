package com.example.aitravelplanner.data.repository.travel

import com.example.aitravelplanner.data.model.Travel

interface ITravelRepository {

    fun getTravels(): List<Travel>

}