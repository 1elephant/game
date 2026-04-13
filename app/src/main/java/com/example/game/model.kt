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

data class Level(
    val levelNumber: Int,
    val questions: List<Question>
)
object QuestionBank {

    val levels = listOf(

        // 🔥 LEVEL 1 — Kotlin Basics in Code
        Level(
            levelNumber = 1,
            questions = listOf(

                Question.FillBlank(
                    "val name: String? = null\nprintln(name.___)",
                    listOf("length")
                ),

                Question.FillBlank(
                    "val name: String? = null\nprintln(name ___ \"Guest\")",
                    listOf("?:")
                ),

                Question.FillBlank(
                    "fun add(a:Int,b:Int):Int {\n    return a ___ b\n}",
                    listOf("+")
                ),

                Question.MCQ(
                    "val x: String? = null\nprintln(x!!.length)",
                    listOf("Crash", "0", "null"),
                    0
                ),

                Question.FillBlank(
                    "data class User(val name:String)\nval u1 = User(\"A\")\nval u2 = u1.___(name=\"B\")",
                    listOf("copy")
                ),

                Question.FillBlank(
                    "val list = listOf(1,2,3)\nprintln(list.___())",
                    listOf("size")
                ),

                Question.FillBlank(
                    "val x = 10\nval y = 20\nprintln(x ___ y)",
                    listOf("+")
                ),

                Question.MCQ(
                    "val a = null\nprintln(a == null)",
                    listOf("true", "false"),
                    0
                ),

                Question.FillBlank(
                    "fun greet(name:String = \"User\") {\n println(\"Hello \$name\")\n}\ngreet() // prints ___",
                    listOf("Hello User")
                ),

                Question.FillBlank(
                    "val str = \"Kotlin\"\nprintln(str.___())",
                    listOf("length")
                )
            )
        ),

        // 🔥 LEVEL 2 — Compose UI Code
        Level(
            levelNumber = 2,
            questions = listOf(

                Question.FillBlank(
                    "@___\nfun MyScreen() {\n Text(\"Hello\")\n}",
                    listOf("Composable")
                ),

                Question.FillBlank(
                    "Column {\n Text(\"A\")\n Text(\"B\")\n}\n// Items are arranged ___",
                    listOf("vertically")
                ),

                Question.FillBlank(
                    "Row {\n Text(\"A\")\n Text(\"B\")\n}\n// Items are arranged ___",
                    listOf("horizontally")
                ),

                Question.FillBlank(
                    "LazyColumn {\n items(10) {\n  Text(\"Item \$it\")\n }\n}\n// This creates ___ list",
                    listOf("scrollable")
                ),

                Question.FillBlank(
                    "Box {\n Text(\"Hello\")\n Text(\"World\")\n}\n// Children are ___",
                    listOf("stacked")
                ),

                Question.FillBlank(
                    "Text(\"Hi\", modifier = Modifier.___())",
                    listOf("fillMaxWidth")
                ),

                Question.FillBlank(
                    "Modifier.padding(16.dp)\n// adds ___ around composable",
                    listOf("space")
                ),

                Question.FillBlank(
                    "LazyVerticalGrid(columns = GridCells.___(2))",
                    listOf("Fixed")
                ),

                Question.FillBlank(
                    "HorizontalPager(pageCount = 5) {\n Text(\"Page \$it\")\n}\n// enables ___",
                    listOf("swipe")
                ),

                Question.MCQ(
                    "LazyColumn vs Column?",
                    listOf("Lazy loads items", "Same thing"),
                    0
                )
            )
        ),

        // 🔥 LEVEL 3 — State & ViewModel (CODE)
        Level(
            levelNumber = 3,
            questions = listOf(

                Question.FillBlank(
                    "var count by ___ { mutableStateOf(0) }",
                    listOf("remember")
                ),

                Question.FillBlank(
                    "val state = mutableStateOf(0)\nstate.value = 5\n// triggers ___",
                    listOf("recomposition")
                ),

                Question.FillBlank(
                    "@Composable\nfun Counter() {\n var count by remember { mutableStateOf(0) }\n Button(onClick = { count++ }) {\n  Text(\"Count: \$count\")\n }\n}\n// count updates UI via ___",
                    listOf("state")
                ),

                Question.FillBlank(
                    "class MyVM: ViewModel() {\n val count = ___(0)\n}",
                    listOf("mutableStateOf")
                ),

                Question.FillBlank(
                    "val vm: MyVM = ___()\n// used in composable",
                    listOf("viewModel")
                ),

                Question.FillBlank(
                    "remember saves value during ___",
                    listOf("recomposition")
                ),

                Question.MCQ(
                    "ViewModel survives rotation?",
                    listOf("Yes", "No"),
                    0
                ),

                Question.FillBlank(
                    "State hoisting moves state ___",
                    listOf("up")
                ),

                Question.FillBlank(
                    "val text by remember { mutableStateOf(\"\") }\nTextField(value = text, onValueChange = { ___ })",
                    listOf("text = it")
                ),

                Question.FillBlank(
                    "State should be ___ to trigger UI update",
                    listOf("observable")
                )
            )
        ),

        // 🔥 LEVEL 4 — Coroutines + Architecture (CODE)
        Level(
            levelNumber = 4,
            questions = listOf(

                Question.FillBlank(
                    "___(Dispatchers.IO) {\n // network call\n}",
                    listOf("launch")
                ),

                Question.FillBlank(
                    "suspend fun fetch() {}\n// call inside ___",
                    listOf("coroutine")
                ),

                Question.FillBlank(
                    "viewModelScope.___ {\n // async work\n}",
                    listOf("launch")
                ),

                Question.FillBlank(
                    "val data = flowOf(1,2,3)\ndata.___ { println(it) }",
                    listOf("collect")
                ),

                Question.FillBlank(
                    "Room is used for ___ database",
                    listOf("local")
                ),

                Question.FillBlank(
                    "Repository handles ___ logic",
                    listOf("data")
                ),

                Question.MCQ(
                    "Dispatchers.Main is for?",
                    listOf("UI", "Network"),
                    0
                ),

                Question.FillBlank(
                    "Flow is ___ stream",
                    listOf("cold")
                ),

                Question.FillBlank(
                    "suspend functions run ___ blocking thread",
                    listOf("without")
                ),

                Question.FillBlank(
                    "MVVM = Model View ___",
                    listOf("ViewModel")
                )
            )
        )
    )

    fun getQuestions(level: Int): List<Question> =
        levels.find { it.levelNumber == level }?.questions ?: emptyList()
}
//object QuestionBank {
//
//    val levels = listOf(
//
//        Level(
//            levelNumber = 1,
//            questions = listOf(
//                Question.MCQ(
//                    "In Kotlin which keyword is used for define functions",
//                    listOf("fun", "def", "no keyword", "compose"),
//                    0
//                ),
//
//                Question.FillBlank(
//                    "@___\nfun greet(text: String) {\n    println(\"hello \$text\")\n}",
//                    listOf("Composable")
//                )
//            )
//        ),
//
//        Level(
//            levelNumber = 2,
//            questions = listOf(
//                Question.MCQ(
//                    "Capital of India?",
//                    listOf("Delhi", "Mumbai", "Chennai"),
//                    0
//                ),
//
//                // 🔥 Single blank
//                Question.FillBlank(
//                    "5 + 5 = ___",
//                    listOf("10")
//                ),
//
//                // 🔥 Multiple blanks example
//                Question.FillBlank(
//                    "___ is the capital of ___",
//                    listOf("Delhi", "India")
//                )
//            )
//        ),
//
//        Level(
//            levelNumber = 3,
//            questions = listOf(
//                Question.MCQ(
//                    "Sun rises from?",
//                    listOf("West", "East"),
//                    1
//                ),
//
//                // 🔥 Multiple blanks
//                Question.FillBlank(
//                    "Sun rises from ___ and sets in ___",
//                    listOf("East", "West")
//                )
//            )
//        ),
//
//        Level(
//            levelNumber = 4,
//            questions = listOf(
//                Question.MCQ(
//                    "10 / 2?",
//                    listOf("2", "5", "6", "10"),
//                    1
//                ),
//
//                Question.FillBlank(
//                    "Water freezes at ___ °C",
//                    listOf("0")
//                ),
//
//                // 🔥 Multiple blanks
//                Question.FillBlank(
//                    "___ + ___ = 10",
//                    listOf("5", "5")
//                )
//            )
//        )
//    )
//
//    fun getQuestions(level: Int): List<Question> =
//        levels.find { it.levelNumber == level }?.questions ?: emptyList()
//}
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