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
package app.cash.zipline.samples.worldclock

import it.uninsubria.prototype.AnswerResult
import it.uninsubria.prototype.Question
import it.uninsubria.prototype.TriviaGame
import it.uninsubria.prototype.TriviaService
import it.uninsubria.prototype.WorldClockIos
import it.uninsubria.prototype.WorldClockModel
import it.uninsubria.prototype.WorldClockPresenter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import okio.ByteString
import okio.ByteString.Companion.toByteString
import platform.Foundation.NSData

@Suppress("unused", "UNUSED_PARAMETER") // Used to export types to Objective-C / Swift.
fun exposedTypes(
    worldClockIos: WorldClockIos,
  //trivia
    triviaService: TriviaService,
    triviaGame: TriviaGame,
    question: Question,
    answerResult: AnswerResult,
  //orologio
    worldClockModel: WorldClockModel,
    worldClockPresenter: WorldClockPresenter,
) {
  throw AssertionError()
}

fun byteStringOf(data: NSData): ByteString = data.toByteString()

fun mainScope(): CoroutineScope = MainScope()
