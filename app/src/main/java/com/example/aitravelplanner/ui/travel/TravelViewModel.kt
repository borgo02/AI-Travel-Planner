package com.example.aitravelplanner.ui.travel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aitravelplanner.ui.components.StageCard

class TravelViewModel : ViewModel() {
    private val _travelName = MutableLiveData<String>("")
    private val _userName = MutableLiveData<String>("")
    private val _affinityPercentage = MutableLiveData<String>("")
    private val _travelImage = MutableLiveData<String>("")
    private val _likesNumber = MutableLiveData<Int>(0)
    private val _description = MutableLiveData<String>("")
    private val _stageCardList = MutableLiveData<ArrayList<StageCard>>(arrayListOf<StageCard>())
    private val _likedTravel = MutableLiveData<Boolean>(false)

    val travelName: LiveData<String>
            get() = _travelName
    val userName: LiveData<String>
            get() = _userName
    val affinityPercentage: LiveData<String>
            get() = _affinityPercentage
    val travelImage: LiveData<String>
            get() = _travelImage
    val likesNumber: LiveData<Int>
            get() = _likesNumber
    val description: LiveData<String>
            get() = _description

    val stageCardList: LiveData<ArrayList<StageCard>>
        get() = _stageCardList

    val likedTravel: LiveData<Boolean>
        get() = _likedTravel

    private var stageNameList: ArrayList<String> = arrayListOf()
    private var stageImageList: ArrayList<String> = arrayListOf()
    private var stageAffinityList: ArrayList<Int> = arrayListOf()

    init{
        _likedTravel.value = false
        _travelName.value = "Roma"
        _userName.value = "Daniele Spalazzi"
        _affinityPercentage.value = 10.toString()
        _travelImage.value = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/de/Colosseo_2020.jpg/1200px-Colosseo_2020.jpg"
        _likesNumber.value = 100
        _description.value = "Roma  è la capitale d'Italia. È il capoluogo dell'omonima città metropolitana e della regione Lazio. Il comune di Roma è dotato di un ordinamento amministrativo speciale, denominato Roma Capitale e disciplinato dalla legge dello Stato.[7]\n" +
                "\n" +
                "Con 2 754 228 abitanti, è il comune più popoloso d'Italia e il terzo dell'Unione europea dopo Berlino e Madrid, mentre con una superficie di 1287,36 km², è il comune più esteso dell'Unione Europea e il quinto del continente europeo, preceduto da Istanbul, Mosca, Londra e San Pietroburgo. È inoltre il comune europeo con la maggiore superficie di aree verdi.[8]\n" +
                "\n" +
                "Fondata secondo la tradizione il 21 aprile 753 a.C. da Romolo (sebbene scavi recenti presso il Lapis niger indichino la presenza di insediamenti già due secoli prima),[9][10] nel corso dei suoi tre millenni di storia, è stata la prima metropoli dell'Occidente,[11] cuore pulsante di una delle più importanti civiltà antiche, che influenzò la società, la cultura, la lingua, la letteratura, l'arte, l'architettura, l'urbanistica, l'ingegneria civile, la filosofia, la religione, il diritto e i costumi dei secoli successivi. Luogo di origine della lingua latina, fu capitale dell'antico Stato romano, che estendeva il suo dominio su tutto il bacino del Mediterraneo e gran parte dell'Europa; poi dello Stato Pontificio, sottoposto al potere temporale dei papi; quindi del Regno d'Italia dal 1871 al 1946, poi della Repubblica Italiana. Per antonomasia è definita l'Urbe, Caput mundi e Città eterna.\n" +
                "\n" +
                "Cuore della cristianità cattolica, è l'unica città al mondo a ospitare al proprio interno un intero Stato, l'enclave della Città del Vaticano,[12] cui si aggiunge la sede del Sovrano Militare Ordine di Malta, persona giuridica di diritto internazionale. Il suo centro storico, delimitato dal perimetro delle Mura aureliane, sovrapposizione di testimonianze di quasi tre millenni, è espressione del patrimonio storico, artistico e culturale del mondo occidentale europeo,[13] tanto che, nel 1980, insieme alle proprietà extraterritoriali della Santa Sede nella città, è stato inserito nella lista dei patrimoni dell'umanità dell'UNESCO,[14][15] provvedimento esteso nel 1990 ai territori compresi all'interno delle Mura gianicolensi.[16] Nel 2007 il Colosseo, simbolo della città, è stato inserito tra le nuove sette meraviglie del mondo.[17]\n" +
                "\n" +
                "Luogo di fondazione della Comunità economica europea e dell'Euratom,[18] ospita anche le sedi di tre organizzazioni delle Nazioni Unite: la FAO, il Fondo internazionale per lo sviluppo agricolo (IFAD) e il Programma alimentare mondiale (PAM). Nel 1960 ha ospitato i Giochi olimpici.\n" +
                "\n" +
                "Per tutte queste caratteristiche, Roma rientra nel novero delle città globali, precisamente nella categoria Beta+"
        stageNameList.add("Colosseo")
        stageImageList.add("https://colosseo.it/sito/wp-content/uploads/2023/05/Colosseo_restauro_30-maggio_veduta-dallalto-scaled.jpg")
        stageAffinityList.add(100)

        stageNameList.add("San Pietro")
        stageImageList.add("https://upload.wikimedia.org/wikipedia/commons/thumb/f/f5/Basilica_di_San_Pietro_in_Vaticano_September_2015-1a.jpg/1200px-Basilica_di_San_Pietro_in_Vaticano_September_2015-1a.jpg")
        stageAffinityList.add(100)

        getStageCards()
    }
    private fun getStageCards(){
        for( i in (stageNameList.indices)){
            val stageCard = StageCard(stageName = stageNameList[i], stageImage = stageImageList[i], stageAffinity = stageAffinityList[i])
            _stageCardList.value!!.add(stageCard)
        }
    }

    fun likeClicked(){
        _likedTravel.value = !_likedTravel.value!!
        if (_likedTravel.value!!) _likesNumber.value = _likesNumber.value?.plus(1) else _likesNumber.value = _likesNumber.value?.minus(1)
    }
}