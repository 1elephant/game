//package com.igdtuw.kotlifymadapp
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.grid.GridCells
//import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
//import androidx.compose.foundation.lazy.grid.items
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material.icons.filled.ArrowForward
//import androidx.compose.material.icons.filled.Settings
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.igdtuw.kotlifymadapp.ui.theme.KotlifyMADAppTheme
//
//// Colors from the image
//val BackgroundPink = Color(0xFFFFE1E9)
//val CardPink = Color(0xFFF9C5D5)
//val DarkText = Color(0xFF3E4E88)
//val AccentBlue = Color(0xFF81D4FA)
//val FABPink = Color(0xFFF06292)
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            KotlifyMADAppTheme {
//                Surface(modifier = Modifier.fillMaxSize(), color = BackgroundPink) {
//                    var currentScreen by remember { mutableStateOf("quiz") }
//                    var finalScore by remember { mutableStateOf(0) }
//
//                    if (currentScreen == "quiz") {
//                        QuizScreen(
//                            questions = sampleQuestions,
//                            onBack = { /* Handle actual exit logic here */ },
//                            onFinish = { score ->
//                                finalScore = score
//                                currentScreen = "score"
//                            }
//                        )
//                    } else {
//                        ScoreScreen(finalScore, sampleQuestions.size) {
//                            currentScreen = "quiz"
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//enum class QuestionType { MCQ, FILL_BLANK }
//
//data class Question(
//    val questionText: String,
//    val options: List<String> = emptyList(),
//    val correctAnswer: String,
//    val type: QuestionType
//)
//
//val sampleQuestions = listOf(
//    Question(
//        "In Kotlin which keyword is used for define functions",
//        listOf("fun", "def", "no keyword", "compose"),
//        "fun",
//        QuestionType.MCQ
//    ),
//    Question(
//        "Kotlin is developed by ____",
//        correctAnswer = "JetBrains",
//        type = QuestionType.FILL_BLANK
//    )
//)
//
//@Composable
//fun QuizScreen(
//    questions: List<Question>,
//    onBack: () -> Unit,
//    onFinish: (Int) -> Unit
//) {
//    var index by remember { mutableStateOf(0) }
//    var score by remember { mutableStateOf(0) }
//    var selectedOption by remember { mutableStateOf("") }
//    var fillAnswer by remember { mutableStateOf("") }
//
//    val q = questions[index]
//
//    Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
//        // --- Header ---
//        Row(
//            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
//            horizontalArrangement = Arrangement.SpaceBetween,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            IconButton(onClick = { if (index > 0) index-- else onBack() }) {
//                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = DarkText)
//            }
//            Text("MCQ Question", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = DarkText)
//            IconButton(onClick = { }) {
//                Icon(Icons.Default.Settings, contentDescription = "Settings", tint = DarkText)
//            }
//        }
//
//        // --- Progress Bar ---
//        LinearProgressIndicator(
//            progress = { (index + 1).toFloat() / questions.size },
//            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).height(8.dp),
//            color = AccentBlue,
//            trackColor = Color.White.copy(alpha = 0.5f),
//            strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
//        )
//
//        Spacer(modifier = Modifier.height(30.dp))
//
//        // --- Question Card ---
//        Card(
//            modifier = Modifier.fillMaxWidth().height(250.dp).padding(horizontal = 24.dp),
//            shape = RoundedCornerShape(30.dp),
//            colors = CardDefaults.cardColors(containerColor = CardPink)
//        ) {
//            Box(Modifier.fillMaxSize().padding(24.dp)) {
//                Text(
//                    text = q.questionText,
//                    fontSize = 22.sp,
//                    fontWeight = FontWeight.Medium,
//                    color = DarkText
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.height(30.dp))
//
//        // --- Question Type Logic ---
//        when (q.type) {
//            QuestionType.MCQ -> {
//                LazyVerticalGrid(
//                    columns = GridCells.Fixed(2),
//                    modifier = Modifier.padding(horizontal = 16.dp),
//                    contentPadding = PaddingValues(8.dp),
//                    verticalArrangement = Arrangement.spacedBy(16.dp),
//                    horizontalArrangement = Arrangement.spacedBy(16.dp)
//                ) {
//                    items(q.options) { option ->
//                        val isSelected = selectedOption == option
//                        Card(
//                            modifier = Modifier.fillMaxWidth().height(80.dp)
//                                .clickable { selectedOption = option },
//                            shape = RoundedCornerShape(20.dp),
//                            colors = CardDefaults.cardColors(
//                                containerColor = if (isSelected) AccentBlue else CardPink
//                            ),
//                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//                        ) {
//                            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
//                                Text(option, color = DarkText, textAlign = TextAlign.Center)
//                            }
//                        }
//                    }
//                }
//            }
//
//            QuestionType.FILL_BLANK -> {
//                Column(modifier = Modifier.padding(horizontal = 32.dp)) {
//                    OutlinedTextField(
//                        value = fillAnswer,
//                        onValueChange = { fillAnswer = it },
//                        modifier = Modifier.fillMaxWidth(),
//                        label = { Text("Your Answer") },
//                        shape = RoundedCornerShape(15.dp)
//                    )
//                }
//            }
//        }
//
//        Spacer(modifier = Modifier.weight(1f))
//
//        // --- Bottom FAB (Next Button) ---
//        Row(
//            modifier = Modifier.fillMaxWidth().padding(30.dp),
//            horizontalArrangement = Arrangement.End
//        ) {
//            FloatingActionButton(
//                onClick = {
//                    // Check Answer
//                    val currentAns = if (q.type == QuestionType.MCQ) selectedOption else fillAnswer
//                    if (currentAns.trim().equals(q.correctAnswer, ignoreCase = true)) {
//                        score++
//                    }
//
//                    // Navigate
//                    if (index < questions.size - 1) {
//                        index++
//                        selectedOption = ""
//                        fillAnswer = ""
//                    } else {
//                        onFinish(score)
//                    }
//                },
//                containerColor = FABPink,
//                shape = RoundedCornerShape(16.dp)
//            ) {
//                Icon(Icons.Default.ArrowForward, contentDescription = "Next")
//            }
//        }
//    }
//}
//
//@Composable
//fun ScoreScreen(score: Int, total: Int, onRestart: () -> Unit) {
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text("Quiz Completed!", fontSize = 28.sp, color = DarkText, fontWeight = FontWeight.Bold)
//        Spacer(modifier = Modifier.height(20.dp))
//        Text("Your Score: $score / $total", fontSize = 24.sp, color = DarkText)
//        Spacer(modifier = Modifier.height(40.dp))
//        Button(onClick = onRestart, colors = ButtonDefaults.buttonColors(containerColor = FABPink)) {
//            Text("Try Again")
//        }
//    }
//}

//

package com.igdtuw.kotlifymadapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.igdtuw.kotlifymadapp.ui.theme.KotlifyMADAppTheme

// 🎨 COLORS (UNCHANGED)
val BackgroundPink = Color(0xFFFFE1E9)
val CardPink = Color(0xFFF9C5D5)
val DarkText = Color(0xFF3E4E88)
val AccentBlue = Color(0xFF81D4FA)
val FABPink = Color(0xFFF06292)

// 🔥 TYPES
enum class QuestionType { MCQ, FILL_BLANK }
enum class Level { EASY, MEDIUM, HARD }

data class Question(
    val questionText: String,
    val options: List<String> = emptyList(),
    val correctAnswer: String,
    val type: QuestionType
)

data class Chapter(
    val name: String,
    val levels: Map<Level, List<Question>>
)

// 🔥 ALL CHAPTERS DATA
val allChapters = listOf(

    Chapter("Kotlin Basics", mapOf(
        Level.EASY to listOf(
            Question("What keyword is used to declare a variable in Kotlin?",
                listOf("val","varr","let","define"), "val", QuestionType.MCQ),
            Question("Which keyword defines a function?",
                listOf("fun","def","function","lambda"), "fun", QuestionType.MCQ),
            Question("What is Kotlin primarily used for?",
                listOf("Mobile App Development","Gaming Engine","Hardware Design","Networking"),
                "Mobile App Development", QuestionType.MCQ)
        ),
        Level.MEDIUM to listOf(
            Question("Kotlin is a ______ typed language", correctAnswer = "statically", type = QuestionType.FILL_BLANK),
            Question("To make a variable mutable, we use ______", correctAnswer = "var", type = QuestionType.FILL_BLANK),
            Question("Safe call operator in Kotlin is ______", correctAnswer = "?.", type = QuestionType.FILL_BLANK)
        ),
        Level.HARD to listOf(
            Question("_____ add(a: Int, b: Int): Int { return a + b }", correctAnswer = "fun", type = QuestionType.FILL_BLANK),
            Question("println(name_____length)", correctAnswer = "?.", type = QuestionType.FILL_BLANK),
            Question("data class User(val name: String, val _____: Int)", correctAnswer = "age", type = QuestionType.FILL_BLANK)
        )
    )),

    Chapter("UI & Layouts", mapOf(
        Level.EASY to listOf(
            Question("Which function is used to create UI in Compose?",
                listOf("@Composable","@UI","@Layout","@View"), "@Composable", QuestionType.MCQ),
            Question("Which layout arranges items vertically?",
                listOf("Column","Row","Box","Stack"), "Column", QuestionType.MCQ),
            Question("Which layout arranges items horizontally?",
                listOf("Row","Column","Box","Grid"), "Row", QuestionType.MCQ)
        ),
        Level.MEDIUM to listOf(
            Question("To add spacing, we use ______", correctAnswer = "Spacer", type = QuestionType.FILL_BLANK),
            Question("Modifier.______ fills screen", correctAnswer = "fillMaxSize", type = QuestionType.FILL_BLANK),
            Question("Modifier.______ adds spacing", correctAnswer = "padding", type = QuestionType.FILL_BLANK)
        ),
        Level.HARD to listOf(
            Question("Column(modifier = Modifier._____())", correctAnswer = "fillMaxSize", type = QuestionType.FILL_BLANK),
            Question("Text(\"Hello\", modifier = Modifier._____(16.dp))", correctAnswer = "padding", type = QuestionType.FILL_BLANK),
            Question("Arrangement._____", correctAnswer = "SpaceBetween", type = QuestionType.FILL_BLANK)
        )
    )),

    Chapter("State & Logic", mapOf(
        Level.EASY to listOf(
            Question("Which function stores state in Compose?",
                listOf("remember","save","store","keep"), "remember", QuestionType.MCQ),
            Question("Which keyword defines a class?",
                listOf("class","object","define","struct"), "class", QuestionType.MCQ),
            Question("Which keyword creates singleton?",
                listOf("object","class","static","final"), "object", QuestionType.MCQ)
        ),
        Level.MEDIUM to listOf(
            Question("State is stored using ______", correctAnswer = "remember", type = QuestionType.FILL_BLANK),
            Question("Mutable state is created using ______", correctAnswer = "mutableStateOf", type = QuestionType.FILL_BLANK),
            Question("ViewModel manages ______", correctAnswer = "UI state", type = QuestionType.FILL_BLANK)
        ),
        Level.HARD to listOf(
            Question("var count by remember { _____(0) }", correctAnswer = "mutableStateOf", type = QuestionType.FILL_BLANK),
            Question("val name = remember { _____(\"Kotlin\") }", correctAnswer = "mutableStateOf", type = QuestionType.FILL_BLANK),
            Question("count._____()", correctAnswer = "toString", type = QuestionType.FILL_BLANK)
        )
    )),

    Chapter("Real App Dev", mapOf(
        Level.EASY to listOf(
            Question("Coroutines are used for:",
                listOf("Async tasks","UI design","Storage","Layout"), "Async tasks", QuestionType.MCQ),
            Question("Which is used for API calls?",
                listOf("Retrofit","Room","Glide","Compose"), "Retrofit", QuestionType.MCQ),
            Question("Which is used for local storage?",
                listOf("Room","Retrofit","Firebase","UI"), "Room", QuestionType.MCQ)
        ),
        Level.MEDIUM to listOf(
            Question("Coroutine scope is ______", correctAnswer = "CoroutineScope", type = QuestionType.FILL_BLANK),
            Question("API calls run on ______ thread", correctAnswer = "background", type = QuestionType.FILL_BLANK),
            Question("Room is used for ______ storage", correctAnswer = "local", type = QuestionType.FILL_BLANK)
        ),
        Level.HARD to listOf(
            Question("delay(_____)", correctAnswer = "1000", type = QuestionType.FILL_BLANK),
            Question("api._____()", correctAnswer = "getData", type = QuestionType.FILL_BLANK),
            Question("@Database(... version = ___)", correctAnswer = "1", type = QuestionType.FILL_BLANK)
        )
    ))
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KotlifyMADAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = BackgroundPink
                ) {
                    var screen by remember { mutableStateOf("quiz") }
                    var finalScore by remember { mutableStateOf(0) }

                    // 🔥 TEMP TEST (Chapter 1 EASY)
                    val questions = allChapters[0].levels[Level.EASY]!!

                    if (screen == "quiz") {
                        QuizScreen(
                            questions = questions,
                            onBack = {},
                            onFinish = {
                                finalScore = it
                                screen = "score"
                            }
                        )
                    } else {
                        ScoreScreen(finalScore, questions.size) {
                            screen = "quiz"
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun QuizScreen(
    questions: List<Question>,
    onBack: () -> Unit,
    onFinish: (Int) -> Unit
) {
    var index by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var selectedOption by remember { mutableStateOf("") }
    var fillAnswer by remember { mutableStateOf("") }
    var showResult by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf(false) }

    val q = questions[index]

    Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {

        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { if (index > 0) index-- else onBack() }) {
                Icon(Icons.Default.ArrowBack, null, tint = DarkText)
            }

            Text(
                "Question ${index + 1}/${questions.size}",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = DarkText
            )

            IconButton(onClick = {}) {
                Icon(Icons.Default.Settings, null, tint = DarkText)
            }
        }

        LinearProgressIndicator(
            progress = { (index + 1).toFloat() / questions.size },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(8.dp),
            color = AccentBlue
        )

        Spacer(modifier = Modifier.height(25.dp))

        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
            shape = RoundedCornerShape(30.dp),
            colors = CardDefaults.cardColors(containerColor = CardPink)
        ) {
            Text(
                q.questionText,
                fontSize = 20.sp,
                color = DarkText,
                modifier = Modifier.padding(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(25.dp))

        when (q.type) {

            QuestionType.MCQ -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(q.options) { option ->
                        val isSelected = selectedOption == option

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .clickable {
                                    if (!showResult) selectedOption = option
                                },
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = when {
                                    showResult && option == q.correctAnswer -> Color(0xFFB2F2BB)
                                    showResult && option == selectedOption -> Color(0xFFFFB3B3)
                                    isSelected -> AccentBlue
                                    else -> CardPink
                                }
                            )
                        ) {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(option, color = DarkText, textAlign = TextAlign.Center)
                            }
                        }
                    }
                }
            }

            QuestionType.FILL_BLANK -> {
                Column(modifier = Modifier.padding(horizontal = 32.dp)) {
                    OutlinedTextField(
                        value = fillAnswer,
                        onValueChange = { if (!showResult) fillAnswer = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Your Answer") }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (showResult) {
            Text(
                if (isCorrect) "✅ Correct!" else "❌ Wrong!",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = DarkText,
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            horizontalArrangement = Arrangement.End
        ) {
            FloatingActionButton(
                onClick = {

                    if (!showResult) {
                        val ans =
                            if (q.type == QuestionType.MCQ) selectedOption else fillAnswer

                        if (ans.isBlank()) return@FloatingActionButton

                        isCorrect = ans.trim().equals(q.correctAnswer, true)

                        if (isCorrect) score++

                        showResult = true
                    } else {
                        if (index < questions.size - 1) {
                            index++
                            selectedOption = ""
                            fillAnswer = ""
                            showResult = false
                        } else {
                            onFinish(score)
                        }
                    }
                },
                containerColor = FABPink
            ) {
                Icon(Icons.Default.ArrowForward, null)
            }
        }
    }
}


@Composable
fun ScoreScreen(score: Int, total: Int, onRestart: () -> Unit) {

    val percentage = (score * 100) / total

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("🎉 Great Job!", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = DarkText)

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = CardPink)
        ) {
            Column(
                modifier = Modifier.padding(30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("$percentage%", fontSize = 36.sp, fontWeight = FontWeight.Bold)
                Text("Score: $score / $total")
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = onRestart,
            colors = ButtonDefaults.buttonColors(containerColor = FABPink)
        ) {
            Text("Play Again")
        }
    }
}