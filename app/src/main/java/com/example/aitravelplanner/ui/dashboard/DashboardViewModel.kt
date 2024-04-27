package com.example.aitravelplanner.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aitravelplanner.ui.components.CardTravel

class DashboardViewModel : ViewModel() {
    private val _cardTravel = MutableLiveData<ArrayList<CardTravel>>()
    val cardTravel: LiveData<ArrayList<CardTravel>>
        get() = _cardTravel

    fun loadCardTravel(){
        val cardRoma = CardTravel("Mario Rossi", 1, 2,
                        "Roma", "82", 2, "1000",
                        2, 1, "12-04-2024")
        val cardTeramo = CardTravel("Mario Rossi", 1, 2,
            "Roma", "82", 2, "1000",
            2, 1, "12-04-2024")
        val cardMilano = CardTravel("Mario Rossi", 1, 2,
            "Roma", "82", 2, "1000",
            2, 1, "12-04-2024")
        val cardTorino = CardTravel("Mario Rossi", 1, 2,
            "Roma", "82", 2, "1000",
            2, 1, "12-04-2024")
        val cardAncona = CardTravel("Mario Rossi", 1, 2,
            "Roma", "82", 2, "1000",
            2, 1, "12-04-2024")
        val cardVenezia = CardTravel("Mario Rossi", 1, 2,
            "Roma", "82", 2, "1000",
            2, 1, "12-04-2024")
        val cardGiulianova = CardTravel("Mario Rossi", 1, 2,
            "Roma", "82", 2, "1000",
            2, 1, "12-04-2024")
        val cardLecce = CardTravel("Mario Rossi", 1, 2,
            "Roma", "82", 2, "1000",
            2, 1, "12-04-2024")

        val cards = arrayListOf(cardRoma, cardTeramo, cardMilano, cardTorino,
                                cardAncona, cardVenezia, cardGiulianova, cardLecce)

        _cardTravel.value = cards
    }
}