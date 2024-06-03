package com.hyperboot.ziplinekmmtesting

import app.cash.zipline.loader.DefaultFreshnessCheckerNotFresh
import app.cash.zipline.loader.LoadResult
import app.cash.zipline.loader.ZiplineLoader
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

fun startWorldClockZipline(
  scope: CoroutineScope,
  ziplineDispatcher: CoroutineDispatcher,
  ziplineLoader: ZiplineLoader,//lo passa già istanziato
  manifestUrl: String,
  models: MutableStateFlow<WorldClockModel>,
) {
  //Lancia una nuova coroutine senza bloccare il thread corrente e ritorna una reference alla coroutine come Job
  scope.launch(ziplineDispatcher + SupervisorJob()) {
    val loadResultFlow: Flow<LoadResult> = ziplineLoader.load(
      applicationName = "world-clock",//nome qui va bene qualsiasi
      freshnessChecker = DefaultFreshnessCheckerNotFresh,
      manifestUrlFlow = repeatFlow(manifestUrl, 500L)
    )

    var previousJob: Job? = null//questo serve dopo
    //colleziono i risultati dei caricamenti dei flow
    loadResultFlow.collect { result ->
      previousJob?.cancel()//ri-inizializzo il lavoro da fare (perchè altrimenti riceve solo un cambiamento e poi considera il lavoro concluso e interrompe)

      if (result is LoadResult.Success) {
        val zipline = result.zipline
        val presenter = zipline.take<WorldClockPresenter>("WorldClockPresenter")
        //ne lancio un altra, altrimenti riceve un solo cambiamento
        val job = launch {
          models.emitAll(presenter.models())
        }

        job.invokeOnCompletion {
          presenter.close()
          // TODO(jwilson): make this safe.
          // zipline.close()
        }

        previousJob = job//se facessi il cancel direttamente qua farebbe strani glitch
      }
    }

    //così lo carica solo una volta e lo mantiene in memoria anche se si spegne il server
//    val collegamentoAzipline = ziplineLoader.loadOnce("prova", manifestUrl)
//    if (collegamentoAzipline is LoadResult.Success){
//      //provo a prendere l'oggetto "WorldClockPresenter"
//      val zwcp = collegamentoAzipline.zipline.take<WorldClockPresenter>("WorldClockPresenter")
//      //lo restituisco
//      models.emitAll(zwcp.models())
//      zwcp.close()
//    }
  }
}

/** Poll for code updates by emitting the manifest on an interval. */
private fun <T> repeatFlow(content: T, delayMillis: Long): Flow<T> {//gli mando sempre lo stesso url ogni tot perchè non cambia
  return flow {
    while (true) {
      emit(content)
      delay(delayMillis)
    }
  }
}

fun startTriviaZipline(
  scope: CoroutineScope,
  ziplineDispatcher: CoroutineDispatcher,
  ziplineLoader: ZiplineLoader,
  manifestUrl: String,
  trivia: MutableStateFlow<TriviaService?>
) {
  scope.launch(ziplineDispatcher + SupervisorJob()) {

    val connectToZiplineFlow: Flow<LoadResult> = ziplineLoader.load(
      applicationName = "trivia",
      freshnessChecker = DefaultFreshnessCheckerNotFresh,
      manifestUrlFlow = repeatFlow(manifestUrl, 1000L)
    )
    var previousJob: Job? = null
    connectToZiplineFlow.collect{ result ->
      previousJob?.cancel()
      if (result is LoadResult.Success) {
        val oggetto = result.zipline.take<TriviaService>("Trivia")
        val job = launch {
          trivia.emit(oggetto)
        }
        previousJob = job
      }
    }
  }
}
//class prova: FreshnessChecker {
//  override fun isFresh(manifest: ZiplineManifest, freshAtEpochMs: Long): Boolean {
//    return freshAtEpochMs > 5000L
//  }
//}