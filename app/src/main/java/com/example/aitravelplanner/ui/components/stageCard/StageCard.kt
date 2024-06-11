package com.example.aitravelplanner.ui.components.stageCard

/** Data class utile per la visualizzazione delle card delle tappe relative ad un viaggio.
 *
 * Utile anche dopo la generazione di un viaggio per aggiungere o eliminare tappe.
 *
 */
data class StageCard (val stageName:String, val stageImage: String, val stageAffinity: Int, var isSearched: Boolean = false, var isSelected: Boolean = false, var description: String = "")