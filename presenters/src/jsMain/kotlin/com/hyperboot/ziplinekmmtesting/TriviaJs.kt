package com.hyperboot.ziplinekmmtesting

class RealTriviaService: TriviaService{
    private val gameWithAnswersList = listOf(
        GameWithAnswers(
            id = 0,
            name = "IDEs of March",
            questionList = listOf(
                object : QuestionAndAnswer {
                    override val question = "This Java IDE was IBM's attempt at blocking out the SUN"
                    override fun result(answer: String) = when {
                        answer.trim().equals("Eclipse", ignoreCase = true) -> AnswerResult(
                            correct = true,
                            message = "Yep! Next they'll need to block out an Oracle."
                        )
                        else -> AnswerResult(
                            correct = false,
                            message = "Nope! The stars aren't in alignment for you."
                        )
                    }
                },
                object : QuestionAndAnswer {
                    override val question =
                        "IntelliJ ships with a mode to emulate this editor in case you can't quit it"

                    override fun result(answer: String) = when {
                        answer.matches(Regex("vim?", option = RegexOption.IGNORE_CASE)) -> AnswerResult(
                            correct = true,
                            message = "You got it! :wq while you're ahead!",
                        )
                        else -> AnswerResult(
                            correct = false,
                            message = "Not that! Are you taking your VItamins?",
                        )
                    }
                },
//                object : QuestionAndAnswer {
//                    override val question =
//                        "Android Studio ships with a mode to emulate this editor in case you can't quit it"
//
//                    override fun result(answer: String) = when {
//                        answer.matches(Regex("vim?", option = RegexOption.IGNORE_CASE)) -> AnswerResult(
//                            correct = true,
//                            message = "You got it! :wq while you're ahead!",
//                        )
//                        else -> AnswerResult(
//                            correct = false,
//                            message = "Not that! Are you taking your VItamins?",
//                        )
//                    }
//                },
            )
        )
    )

    override fun games() = gameWithAnswersList.map { it.game }
    override fun answer(gameId: Int, questionId: Int, answer: String) = gameWithAnswersList[gameId].questionList[questionId].result(answer)
}

interface QuestionAndAnswer {
    val question: String
    fun result(answer: String): AnswerResult
}

class GameWithAnswers(
    private val id: Int,
    private val name: String,
    val questionList: List<QuestionAndAnswer>
) {
    val game: TriviaGame
        get() = TriviaGame(
            id = id,
            name = name,
            question = questionList.withIndex().map { (index, value) ->
                Question(index, value.question)
            }
        )
}