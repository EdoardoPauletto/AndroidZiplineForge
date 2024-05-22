package com.hyperboot.ziplinekmmtesting


import app.cash.zipline.ZiplineService
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable

//Come già notato ha la stessa struttura di Java RMI
//Qui ci sono le interfaccie vuote (da implementare)
//Js dovrebbe implementarle (cioè è lo Skeleton)
//Jvm dovrebbe usarle (cioè è lo Stub) (Jvm in questo caso è host)
@Serializable
data class WorldClockEvent(val event: String)//classe con solo una stringa ("data" se non ricordo male imposta automaticamente setter e getter)

@Serializable
data class WorldClockModel(val label: String)//idem

interface WorldClockPresenter : ZiplineService {//implementa Zipline come da spiegazione git
    fun models(events: Flow<WorldClockEvent>): Flow<WorldClockModel>//flow è un flusso di valori (esempio: dati in tempo reale ogni 5sec)
    //in questo caso prende un flusso di classi stringhe (event) e restituirà un flusso di classi stringhe(label)
}

interface WorldClockHost : ZiplineService {//idem
    fun timeZones(): List<String>
}