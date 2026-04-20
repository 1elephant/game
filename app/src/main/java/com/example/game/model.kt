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
        init {
            val blanks = question.split("___").size - 1
            require(blanks == correctAnswers.size) {
                "Mismatch: blanks=$blanks answers=${correctAnswers.size}"
            }
        }

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
        Level(
            levelNumber = 1,
            questions = listOf(
                Question.MCQ(
                    "val name: String? = null\n\nval result = name?.length ?: 5\n\nprintln(result)",
                    listOf("5", "0", "Crash", "null"),
                    0
                ),
                Question.MCQ(
                    "val list = listOf(1, 2, 3, 4)\n\nval result = list\n    .filter { it % 2 == 0 }\n    .map { it * 2 }\n\nprintln(result)",
                    listOf("[4, 8]", "[2, 4]", "[1, 3]", "[2, 8]"),
                    0
                ),
                Question.FillBlank(
                    "val str = \"Kotlin\"\nval res = str.substring(0, 3)\n\nprintln(res) // ___",
                    listOf("Kot")
                ),
                Question.MCQ(
                    "fun update(x: Int): Int {\n    var y = x\n    y += 5\n    return y\n}\n\nval a = 10\nval b = update(a)\n\nprintln(a)\nprintln(b)",
                    listOf("10 and 15", "15 and 15", "10 and 10", "Crash"),
                    0
                ),
                Question.FillBlank(
                    "val list = listOf(\"a\", \"bb\", \"ccc\")\nval result = list.map { it.length }\n\nprintln(result) // ___",
                    listOf("[1, 2, 3]")
                )
            )
        ),
        Level(
            levelNumber = 2,
            questions = listOf(
                Question.FillBlank(
                    "@Composable\nfun Test() {\n    var count by ___ { \n        mutableStateOf(0) \n    }\n\n    Button(onClick = { ___ }) {\n        Text(\"count\")\n    }\n}",
                    listOf("remember", "count++")
                ),
                Question.FillBlank(
                    "@Composable\nfun Test() {\n    val list = (1..1000).toList()\n\n    ___ {\n        items(list.size) {\n            Text(\"Item \$it\")\n        }\n    }\n}",
                    listOf("LazyColumn")
                ),
                Question.MCQ(
                    "@Composable\nfun Test() {\n    val list = (1..1000).toList()\n\n    Column {\n        list.forEach {\n            Text(\"Item \$it\")\n        }\n    }\n}",
                    listOf(
                        "Performance issue for large lists",
                        "Nothing will show",
                        "Crash always",
                        "Same as LazyColumn"
                    ),
                    0
                ),
                Question.FillBlank(
                    "@Composable\nfun Test() {\n    var text by remember { \n        mutableStateOf(\"\") \n    }\n\n    TextField(\n        value = text,\n        onValueChange = { ___ }\n    )\n}",
                    listOf("text = it")
                ),
                Question.FillBlank(
                    "@Composable\nfun Test() {\n    var count by remember { \n        mutableStateOf(0) \n    }\n\n    println(\"Recompose\")\n\n    Button(onClick = { count++ }) {\n        Text(\"count\")\n    }\n}\n\n// Button click triggers ___",
                    listOf("recomposition")
                ),
                Question.MCQ(
                    "@Composable\nfun Test() {\n    val count = mutableStateOf(0)\n\n    Button(onClick = { count.value++ }) {\n        Text(\"\${count.value}\")\n    }\n}\n\n// What is missing here for correct Compose usage?",
                    listOf("remember", "launch", "collect", "padding"),
                    0
                ),
                Question.FillBlank(
                    "@Composable\nfun Test() {\n    val count = remember { \n        mutableStateOf(0) \n    }\n\n    Button(onClick = { count.value++ }) {\n        Text(\"\${count.value}\")\n    }\n}\n\n// count is updated because state is ___",
                    listOf("observable")
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

enum class Difficulty { EASY, MEDIUM, HARD }

data class LearningLevel(
    val chapter: Int,
    val difficulty: Difficulty,
    val questions: List<Question>
)

object LearningQuestionBank {
    val levels = listOf(
<<<<<<< HEAD
=======

        // --- CHAPTER 1: KOTLIN BASICS ---
>>>>>>> 1319c9c45022bc04ecf85a9a2d63e2d21ea7ad59
        LearningLevel(
            chapter = 1,
            difficulty = Difficulty.EASY,
            questions = listOf(
<<<<<<< HEAD
                Question.MCQ("What is Kotlin?", listOf("Language", "OS", "IDE"), 0),
                Question.FillBlank("Kotlin is developed by ___", listOf("JetBrains"))
            )
=======
                Question.MCQ("What keyword is used to declare a variable in Kotlin?", listOf("val","varr","let","define"), 0),
                Question.MCQ("Which keyword defines a function?", listOf("fun","def","function","lambda"), 0),
                Question.MCQ("What is Kotlin primarily used for?", listOf("Mobile App Development","Gaming Engine","Hardware Design","Networking"), 0)
            )
        ),
        LearningLevel(
            chapter = 1,
            difficulty = Difficulty.MEDIUM,
            questions = listOf(
                Question.FillBlank("Kotlin is a ___ typed language", listOf("statically")),
                Question.FillBlank("To make a variable mutable, we use ___", listOf("var")),
                Question.FillBlank("Safe call operator in Kotlin is ___", listOf("?."))
            )
        ),
        LearningLevel(
            chapter = 1,
            difficulty = Difficulty.HARD,
            questions = listOf(
                Question.FillBlank("___ add(a: Int, b: Int): Int { return a + b }", listOf("fun")),
                Question.FillBlank("println(name___length)", listOf("?.")),
                Question.FillBlank("data class User(val name: String, val ___: Int)", listOf("age"))
            )
        ),

        // --- CHAPTER 2: UI & LAYOUTS ---
        LearningLevel(
            chapter = 2,
            difficulty = Difficulty.EASY,
            questions = listOf(
                Question.MCQ("Which function is used to create UI in Compose?", listOf("@Composable","@UI","@Layout","@View"), 0),
                Question.MCQ("Which layout arranges items vertically?", listOf("Column","Row","Box","Stack"), 0),
                Question.MCQ("Which layout arranges items horizontally?", listOf("Row","Column","Box","Grid"), 0)
            )
        ),
        LearningLevel(
            chapter = 2,
            difficulty = Difficulty.MEDIUM,
            questions = listOf(
                Question.FillBlank("To add spacing, we use ___", listOf("Spacer")),
                Question.FillBlank("Modifier.___ fills screen", listOf("fillMaxSize")),
                Question.FillBlank("Modifier.___ adds spacing", listOf("padding"))
            )
        ),
        LearningLevel(
            chapter = 2,
            difficulty = Difficulty.HARD,
            questions = listOf(
                Question.FillBlank("Column(modifier = Modifier.___())", listOf("fillMaxSize")),
                Question.FillBlank("Text(\"Hello\", modifier = Modifier.___(16.dp))", listOf("padding")),
                Question.FillBlank("Arrangement.___ fills space between", listOf("SpaceBetween"))
            )
        ),

        // --- CHAPTER 3: STATE & LOGIC ---
        LearningLevel(
            chapter = 3,
            difficulty = Difficulty.EASY,
            questions = listOf(
                Question.MCQ("Which function stores state in Compose?", listOf("remember","save","store","keep"), 0),
                Question.MCQ("Which keyword defines a class?", listOf("class","object","define","struct"), 0),
                Question.MCQ("Which keyword creates singleton?", listOf("object","class","static","final"), 0)
            )
        ),
        LearningLevel(
            chapter = 3,
            difficulty = Difficulty.MEDIUM,
            questions = listOf(
                Question.FillBlank("State is stored using ___", listOf("remember")),
                Question.FillBlank("Mutable state is created using ___", listOf("mutableStateOf")),
                Question.FillBlank("ViewModel manages ___", listOf("UI state"))
            )
        ),
        LearningLevel(
            chapter = 3,
            difficulty = Difficulty.HARD,
            questions = listOf(
                Question.FillBlank("var count by remember { ___(0) }", listOf("mutableStateOf")),
                Question.FillBlank("val name = remember { ___(\"Kotlin\") }", listOf("mutableStateOf")),
                // Adjusted to match her model logic
                Question.FillBlank("count.___()", listOf("toString"))
            )
        ),

        // --- CHAPTER 4: REAL APP DEV ---
        LearningLevel(
            chapter = 4,
            difficulty = Difficulty.EASY,
            questions = listOf(
                Question.MCQ("Coroutines are used for:", listOf("Async tasks","UI design","Storage","Layout"), 0),
                Question.MCQ("Which is used for API calls?", listOf("Retrofit","Room","Glide","Compose"), 0),
                Question.MCQ("Which is used for local storage?", listOf("Room","Retrofit","Firebase","UI"), 0)
            )
        ),
        LearningLevel(
            chapter = 4,
            difficulty = Difficulty.MEDIUM,
            questions = listOf(
                Question.FillBlank("Coroutine scope is ___", listOf("CoroutineScope")),
                Question.FillBlank("API calls run on ___ thread", listOf("background")),
                Question.FillBlank("Room is used for ___ storage", listOf("local"))
            )
        ),
        LearningLevel(
            chapter = 4,
            difficulty = Difficulty.HARD,
            questions = listOf(
                Question.FillBlank("delay(___)", listOf("1000")),
                // Updated to match her syntax
                Question.FillBlank("api.___()", listOf("getData")),
                Question.FillBlank("@Database(... version = ___)", listOf("1"))
            )
>>>>>>> 1319c9c45022bc04ecf85a9a2d63e2d21ea7ad59
        )
    )

    fun getQuestions(chapter: Int, difficulty: Difficulty): List<Question> {
        return levels.find {
            it.chapter == chapter && it.difficulty == difficulty
        }?.questions ?: emptyList()
    }
}
