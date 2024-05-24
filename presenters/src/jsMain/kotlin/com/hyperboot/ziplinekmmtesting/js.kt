package com.hyperboot.ziplinekmmtesting

import app.cash.zipline.Zipline
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

//Skeleton (implementa le interfaccie)

private val zipline by lazy { Zipline.get() }

@OptIn(ExperimentalJsExport::class)
@JsExport
fun main() {
  zipline.bind<WorldClockPresenter>(
    name = "WorldClockPresenter",
    instance = RealWorldClockPresenter(),//le virgole servono perchè continua nella riga sotto
  )
}

class RealWorldClockPresenter : WorldClockPresenter {
  override fun models(): Flow<WorldClockModel> {
    return flow {//infatti restituisce un flusso
      while (true) {//come da documentazione (flusso infinito)
        emit(//emette tutta la classe istanziata qua sotto
          WorldClockModel(
            label = TimeFormatter().formatWorldTime(millis = true)
            //label = TimeFormatter().formatLocalTime()//solo 1 orario
            //label = SayHello().getMessage()//alternativa d'esempio fatta da loro
          ),
        )
        delay(16)//ogni 16 milli secondi
      }
    }
  }
}
