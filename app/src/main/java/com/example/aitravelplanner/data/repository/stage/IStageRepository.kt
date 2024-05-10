package com.example.aitravelplanner.data.repository.stage

import com.example.aitravelplanner.data.model.Stage
import com.example.aitravelplanner.data.model.Travel

interface IStageRepository {
    suspend fun setStage(stage: Stage)
    suspend fun getStage(idStage: String): Stage?
    suspend fun getStages(): ArrayList<Stage>
    suspend fun getStagesByTravel(travel: Travel): ArrayList<Stage>
}