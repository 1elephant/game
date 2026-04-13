package com.example.game

sealed class Question {

    data class MCQ(
        val question: String,
        val options: List<String>,
        val correctAnswerIndex: Int
    ) : Question() {
        init {
            require(options.size >= 2) { "MCQ must have at least 2 options" }
            require(correctAnswerIndex in options.indices) {
                "correctAnswerIndex must be within options range"
            }
        }

        fun isCorrect(selectedIndex: Int): Boolean =
            selectedIndex == correctAnswerIndex
    }

    data class FillBlank(
        val question: String,
        val correctAnswer: String
    ) : Question() {

        fun isCorrect(answer: String): Boolean =
            answer.trim().equals(correctAnswer, ignoreCase = true)
    }
}

data class Level(
    val levelNumber: Int,
    val questions: List<Question>
)
object QuestionBank {

    val levels = listOf(

        Level(
            levelNumber = 1,
            questions = listOf(
                Question.MCQ("In Kotlin which keyword is used for define functions", listOf("fun", "def", "no keyword", "compose"), 0),
                Question.FillBlank(
                    """@___
fun greet(text: String) {
    println("hello $ text")
}""",
                    "Composable"
                )
            )
        ),

        Level(
            levelNumber = 2,
            questions = listOf(
                Question.MCQ("Capital of India?", listOf("Delhi", "Mumbai", "Chennai"), 0),
                Question.FillBlank("5 + 5 = ___", "10")
            )
        ),

        Level(
            levelNumber = 3,
            questions = listOf(
                Question.MCQ("Sun rises from?", listOf("West", "East"), 1)
            )
        ),

        Level(
            levelNumber = 4,
            questions = listOf(
                Question.MCQ("10 / 2?", listOf("2", "5", "6", "10"), 1),
                Question.FillBlank("Water freezes at ___ °C", "0")
            )
        )
    )

    fun getQuestions(level: Int): List<Question> =
        levels.find { it.levelNumber == level }?.questions ?: emptyList()
}
data class BossProgress(
    val scores: List<Int> = emptyList()
) {
    val completedLevels: Int get() = scores.size

    fun isLevelUnlocked(level: Int): Boolean =
        level == 1 || level <= completedLevels + 1

    fun getScore(level: Int): Int? = scores.getOrNull(level - 1)
}
enum class Difficulty {
    EASY,
    MEDIUM,
    HARD
}
data class LearningLevel(
    val chapter: Int,
    val difficulty: Difficulty,
    val questions: List<Question>
)
object LearningQuestionBank {

    val levels = listOf(

        LearningLevel(
            chapter = 1,
            difficulty = Difficulty.EASY,
            questions = listOf(
                Question.MCQ("What is Kotlin?", listOf("Language", "OS", "IDE"), 0),
                Question.FillBlank("Kotlin is developed by ___", "JetBrains")
            )
        ),

        LearningLevel(
            chapter = 1,
            difficulty = Difficulty.MEDIUM,
            questions = listOf(
                Question.MCQ("val x=7 x=78 what will be output if we run code?", listOf(" syntax error", "symmentic  error"), 1)
            )
        ),

        LearningLevel(
            chapter = 1,
            difficulty = Difficulty.HARD,
            questions = listOf(
                Question.MCQ("Coroutine scope is?", listOf("Thread", "Async tool"), 1)
            )
        ),
        LearningLevel(
            chapter = 2,
            difficulty = Difficulty.EASY,
            questions = listOf(
                Question.MCQ("Coroutine scope is?", listOf("Thread", "Async tool"), 1)
            )
        ),
        LearningLevel(
            chapter = 2,
            difficulty = Difficulty.MEDIUM,
            questions = listOf(
                Question.MCQ("Coroutine scope is?", listOf("Thread", "Async tool"), 1)
            )
        ),
        LearningLevel(
            chapter = 2,
            difficulty = Difficulty.HARD,
            questions = listOf(
                Question.MCQ("Coroutine scope is?", listOf("Thread", "Async tool"), 1)
            )
        ),
        LearningLevel(
            chapter = 3,
            difficulty = Difficulty.EASY,
            questions = listOf(
                Question.MCQ("Coroutine scope is?", listOf("Thread", "Async tool"), 1)
            )
        ),
        LearningLevel(
            chapter = 3,
            difficulty = Difficulty.MEDIUM,
            questions = listOf(
                Question.MCQ("Coroutine scope is?", listOf("Thread", "Async tool"), 1)
            )
        ),
        LearningLevel(
            chapter = 3,
            difficulty = Difficulty.HARD,
            questions = listOf(
                Question.MCQ("Coroutine scope is?", listOf("Thread", "Async tool"), 1)
            )
        ),
        LearningLevel(
            chapter = 4,
            difficulty = Difficulty.EASY,
            questions = listOf(
                Question.MCQ("Coroutine scope is?", listOf("Thread", "Async tool"), 1)
            )
        ),
        LearningLevel(
            chapter = 4,
            difficulty = Difficulty.MEDIUM,
            questions = listOf(
                Question.MCQ("Coroutine scope is?", listOf("Thread", "Async tool"), 1)
            )
        ),
        LearningLevel(
            chapter = 4,
            difficulty = Difficulty.HARD,
            questions = listOf(
                Question.MCQ("Coroutine scope is?", listOf("Thread", "Async tool"), 1)
            )
        ),


    )

    fun getQuestions(chapter: Int, difficulty: Difficulty): List<Question> {
        return levels.find {
            it.chapter == chapter && it.difficulty == difficulty
        }?.questions ?: emptyList()
    }
}