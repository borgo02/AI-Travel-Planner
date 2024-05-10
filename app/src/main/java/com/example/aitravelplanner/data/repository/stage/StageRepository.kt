package com.example.aitravelplanner.data.repository.stage

import com.example.aitravelplanner.data.model.Stage
import com.example.aitravelplanner.data.model.Travel
import com.example.aitravelplanner.data.repository.BaseRepository
import kotlinx.coroutines.tasks.await

class StageRepository: IStageRepository, BaseRepository() {
    private var stage: Stage? = null
    override suspend fun setStage(stage: Stage) {
        db.collection("stages").document(stage.idStage).set(stage).await()
    }

    override suspend fun getStage(idStage: String): Stage? {
        val stageRef = db.collection("stages").document(idStage).get().await()

        if (stageRef.exists())
            stage = stageRef.toObject(Stage::class.java)
        else
            return null

        return stage
    }

    override suspend fun getStages(): ArrayList<Stage> {
        val stageRef = db.collection("stages").get().await()
        val stageList: ArrayList<Stage> = arrayListOf()

        for(stage in stageRef.documents){
            val stageData = stage.toObject(Stage::class.java)
            if(stageData != null)
                stageList.add(stageData)
        }

        return stageList
    }

    override suspend fun getStagesByTravel(travel: Travel): ArrayList<Stage> {
        val travelRef = db.collection("travels").document(travel.idTravel).get().await()
        var stageList: ArrayList<Stage> = arrayListOf()

        if(travelRef.exists()){
            val travelData = travelRef.toObject(Travel::class.java)
            if(travelData != null)
                stageList = travelData.stageList

        }

        return stageList
    }

}