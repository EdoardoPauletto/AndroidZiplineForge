/*
 * Copyright (C) 2022 Block, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hyperboot.ziplinekmmtesting

import kotlin.js.Date

//classe che semplicemente resistuisce l'orario (singolo o multiplo)
class TimeFormatter {
  fun formatLocalTime(
    now: dynamic = Date(),
    millis: Boolean = false,//default è false, quindi se valore omesso usa quello
  ): String {
    val originalHours = now.getHours()
    now.setHours(originalHours - 4) // This sample doesn't implement DST.
    val nyc = formatDate(now, millis)//chiama la funz sotto

    return """
      |Time in NYC
      |$nyc
      """.trimMargin()//elimina la barra iniziare ad ogni riga (tra paremtesi si può definire anche un altro carattere)
  }

  fun formatWorldTime(now: dynamic = Date(),millis: Boolean = false): String {//indentata così perchè mi viene più facile leggerla (ma uguale all'altra sorpa)
    val originalHours = now.getHours()

    now.setHours(originalHours + 2)
    val barcelona = formatDate(now, millis)

    now.setHours(originalHours - 4)
    val nyc = formatDate(now, millis)

    now.setHours(originalHours - 7)
    val sf = formatDate(now, millis)

    now.setHours(originalHours + 2)     //aggiunta io
    val rome = formatDate(now, millis)

    return """
      |Barcelona
      |$barcelona
      |
      |New York City
      |$nyc
      |
      |San Francisco
      |$sf
      |
      |Roma
      |$rome
      """.trimMargin()
  }

  private fun formatDate(
    date: dynamic,
    millis: Boolean = false,
  ): String {
    val limit = when {
      millis -> 23//se dovrà stampare anche milliSec allora taglierà la stringa la char di posto 23
      else -> 19//altrimenti al 19 (quindi non visualizzando ":123")
    }

    val string = date.toISOString() as String
    return string.slice(11 until limit)//ritorna la stringa tagliata da quando iniziano gli orari (char 11) fino a (until) limit (che sarà 23 o 19)
  }
}
