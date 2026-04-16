package com.example.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

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
        val correctAnswers: List<String>
    ) : Question() {

        fun isCorrect(answers: List<String>): Boolean {
            if (answers.size != correctAnswers.size) return false
            return answers.zip(correctAnswers).all { (user, correct) ->
                user.trim().equals(correct, ignoreCase = true)
            }
        }
    }
}
object QuestionBank {

    val levels = listOf(

        // 🔥 LEVEL 1 — Kotlin Logic & Output
        Level(
            levelNumber = 1,
            questions = listOf(

                Question.MCQ(
                    "val name: String? = null\nval result = name?.length ?: 5\nprintln(result)",
                    listOf("5", "0", "Crash", "null"),
                    0
                ),

                Question.MCQ(
                    "val list = listOf(1, 2, 3, 4)\nval result = list.filter { it % 2 == 0 }.map { it * 2 }\nprintln(result)",
                    listOf("[4, 8]", "[2, 4]", "[1, 3]", "[2, 8]"),
                    0
                ),

                Question.FillBlank(
                    "val str = \"Kotlin\"\nval res = str.substring(0, 3)\nprintln(res) // ___",
                    listOf("Kot")
                ),

                Question.MCQ(
                    "fun update(x: Int): Int {\n    var y = x\n    y += 5\n    return y\n}\n\nval a = 10\nval b = update(a)\nprintln(a)\nprintln(b)",
                    listOf("10 and 15", "15 and 15", "10 and 10", "Crash"),
                    0
                ),

                Question.FillBlank(
                    "val list = listOf(\"a\", \"bb\", \"ccc\")\nval result = list.map { it.length }\nprintln(result) // ___",
                    listOf("[1, 2, 3]")
                )
            )
        ),

        // 🔥 LEVEL 2 — Compose (Real Thinking + Multi-step)
        Level(
            levelNumber = 2,
            questions = listOf(

                Question.FillBlank(
                    "@Composable\nfun Test() {\n    var count by ___ { mutableStateOf(0) }\n\n    Button(onClick = { ___ }) {\n        Text(\"count\")\n    }\n}",
                    listOf("remember", "count++")
                ),

                Question.FillBlank(
                    "@Composable\nfun Test() {\n    val list = (1..1000).toList()\n\n    ___ {\n        items(list.size) {\n            Text(\"Item it\")\n        }\n    }\n}",
                    listOf("LazyColumn")
                ),

                Question.MCQ(
                    "@Composable\nfun Test() {\n    val list = (1..1000).toList()\n\n    Column {\n        list.forEach {\n            Text(\"Item it\")\n        }\n    }\n}",
                    listOf(
                        "Performance issue for large lists",
                        "Nothing will show",
                        "Crash always",
                        "Same as LazyColumn"
                    ),
                    0
                ),

                Question.FillBlank(
                    "@Composable\nfun Test() {\n    var text by remember { mutableStateOf(\"\") }\n\n    TextField(\n        value = text,\n        onValueChange = { ___ }\n    )\n}",
                    listOf("text = it")
                ),

                Question.FillBlank(
                    "@Composable\nfun Test() {\n    var count by remember { mutableStateOf(0) }\n\n    println(\"Recompose\")\n\n    Button(onClick = { count++ }) {\n        Text(\"count\")\n    }\n}\n// Button click triggers ___",
                    listOf("recomposition")
                ),

                Question.MCQ(
                    "@Composable\nfun Test() {\n    val count = mutableStateOf(0)\n\n    Button(onClick = { count.value++ }) {\n        Text(\"{count.value}\")\n    }\n}\n\n// What is missing here for correct Compose usage?",
                    listOf(
                        "remember",
                        "launch",
                        "collect",
                        "padding"
                    ),
                    0
                ),

                Question.FillBlank(
                    "@Composable\nfun Test() {\n    val count = remember { mutableStateOf(0) }\n\n    Button(onClick = { count.value++ }) {\n        Text(\"{count.value}\")\n    }\n}\n// count is updated because state is ___",
                    listOf("observable")
                )
            )
        ),

        // 🔥 LEVEL 3 — State & ViewModel
        Level(
            levelNumber = 3,
            questions = listOf(

                Question.MCQ(
                    "@Composable\nfun Counter() {\n    var count = 0\n    Button(onClick = { count++ }) {\n        Text(\"count\")\n    }\n}",
                    listOf(
                        "UI does not update",
                        "UI updates",
                        "Crash",
                        "Infinite loop"
                    ),
                    0
                ),

                Question.MCQ(
                    "@Composable\nfun Counter() {\n    var count by remember { mutableStateOf(0) }\n    Button(onClick = { count++ }) {\n        Text(\"count\")\n    }\n}",
                    listOf(
                        "UI updates correctly",
                        "UI does not update",
                        "Crash",
                        "No change"
                    ),
                    0
                ),

                Question.FillBlank(
                    "TextField(\n value = text,\n onValueChange = { ___ }\n)",
                    listOf("text = it")
                ),

                Question.MCQ(
                    "class MyVM : ViewModel() {\n    var count = mutableStateOf(0)\n}",
                    listOf(
                        "State survives rotation",
                        "State resets",
                        "Crash",
                        "No effect"
                    ),
                    0
                )
            )
        ),

        // 🔥 LEVEL 4 — Coroutines
        Level(
            levelNumber = 4,
            questions = listOf(

                Question.MCQ(
                    "viewModelScope.launch {\n    val data = fetchData()\n}",
                    listOf(
                        "Runs asynchronously",
                        "Blocks UI",
                        "Crash",
                        "Runs forever"
                    ),
                    0
                ),

                Question.MCQ(
                    "suspend fun fetch(): String {\n    delay(1000)\n    return \"Done\"\n}",
                    listOf(
                        "Must be called in coroutine",
                        "Can be called anywhere",
                        "Runs instantly",
                        "Crash"
                    ),
                    0
                ),

                Question.MCQ(
                    "withContext(Dispatchers.IO) {\n    // network\n}",
                    listOf(
                        "Runs on background thread",
                        "Runs on UI thread",
                        "Crash",
                        "No difference"
                    ),
                    0
                ),

                Question.MCQ(
                    "val flow = flowOf(1,2,3)\nflow.collect { println(it) }",
                    listOf(
                        "Prints 1 2 3",
                        "Prints only 1",
                        "Crash",
                        "Prints nothing"
                    ),
                    0
                )
            )
        )
    )

    fun getQuestions(level: Int): List<Question> =
        levels.find { it.levelNumber == level }?.questions ?: emptyList()
}


data class Level(
    val levelNumber: Int,
    val questions: List<Question>
)
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
                Question.FillBlank("Kotlin is developed by ___", listOf("JetBrains"))
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