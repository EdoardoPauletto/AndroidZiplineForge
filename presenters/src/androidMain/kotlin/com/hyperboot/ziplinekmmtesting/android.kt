package com.hyperboot.ziplinekmmtesting

import app.cash.zipline.loader.ManifestVerifier.Companion.NO_SIGNATURE_CHECKS
import app.cash.zipline.loader.ZiplineLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.OkHttpClient
import java.util.concurrent.Executors

class WorldClockAndroid(private val scope: CoroutineScope) {
    private val ziplineExecutorService = Executors.newSingleThreadExecutor { Thread(it, "Zipline") }
    private val ziplineDispatcher = ziplineExecutorService.asCoroutineDispatcher()
    //private val okHttpClient = OkHttpClient()

    val models = MutableStateFlow(WorldClockModel(label = "..."))//inizializza la lable così in caso di js spento
    //val triviaGame: TriviaGame? = null
    //val answerResult: AnswerResult? = null
    private val interfaccia: TriviaService? = null
    val trivia = MutableStateFlow(interfaccia)
    //val triviaA = MutableStateFlow(answerResult)

    fun start() {
        startWorldClockZipline(
            scope = scope,
            ziplineDispatcher = ziplineDispatcher,
            ziplineLoader = ZiplineLoader(
                dispatcher = ziplineDispatcher,
                manifestVerifier = NO_SIGNATURE_CHECKS,
                httpClient = OkHttpClient(),
            ),
            manifestUrl = "http://10.0.2.2:8080/manifest.zipline.json",
            models = models,
        )
    }
    fun startTrivia() {
        startTriviaZipline(
            scope = scope,
            ziplineDispatcher = ziplineDispatcher,
            ziplineLoader = ZiplineLoader(
                dispatcher = ziplineDispatcher,
                manifestVerifier = NO_SIGNATURE_CHECKS,
                httpClient = OkHttpClient(),
            ),
            manifestUrl = "https://raw.githubusercontent.com/EdoardoPauletto/forge/main/productionExecutable/kotlinZipline/manifest.zipline.json",
            trivia = trivia
        )
    }

    fun close() {
        ziplineExecutorService.shutdown()
    }
}
