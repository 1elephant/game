package com.example.game

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun DifficultyScreen(
    appViewModel: AppViewModel,
    chapter: Int,
    navController: NavController,
    quizViewModel: QuizViewModel
) {
    val isSoundOn = appViewModel.isSoundOn
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    
    MusicPlayer(isSoundOn, R.raw.sapphire)

    // Current screen state to toggle between Levels and Theory
    var showTheory by remember { mutableStateOf(false) }

    if (showTheory) {
        TheoryScreenContent(chapter.toString()) { showTheory = false }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFFFDECEF), Color(0xFFF8BBD0))
                    )
                )
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // 🔹 TOP BAR
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color(0xFF7B9ACC))
                }
                
                Text(
                    text = "Chapter $chapter",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF7B9ACC)
                )

                IconButton(onClick = { appViewModel.isSoundOn = !appViewModel.isSoundOn }) {
                    Icon(
                        imageVector = if (isSoundOn) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                        contentDescription = "Sound Toggle",
                        tint = Color(0xFF7B9ACC)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 🔹 LEVEL BUTTONS
            val difficulties = listOf(
                Triple("Level 1", "Easy", Difficulty.EASY),
                Triple("Level 2", "Intermediate", Difficulty.MEDIUM),
                Triple("Level 3", "Advanced", Difficulty.HARD)
            )

            difficulties.forEach { (title, subtitle, diff) ->
                StylishLevelCard(title, subtitle) {
                    val questions = LearningQuestionBank.getQuestions(chapter, diff)
                    if (questions.isNotEmpty()) {
                        navController.navigate(Screen.LearningQuiz.createRoute(chapter, diff.name))
                    } else {
                        Toast.makeText(navController.context, "No questions available", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // 🔹 VIEW THEORY BUTTON
            Button(
                onClick = { showTheory = true },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp),
                elevation = ButtonDefaults.buttonElevation(4.dp)
            ) {
                Text(text = "View Theory", color = Color(0xFF7B9ACC), fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun StylishLevelCard(title: String, subtitle: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            // 🌫️ TRANSLUCENT STRIP
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(Color.White.copy(alpha = 0.25f), RoundedCornerShape(20.dp))
                    .border(1.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
            )

            // 💖 CIRCULAR BUTTON
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(110.dp)
                    .shadow(10.dp, shape = RoundedCornerShape(100.dp))
                    .background(
                        brush = Brush.radialGradient(colors = listOf(Color(0xFFFDECEF), Color(0xFFF8BBD0))),
                        shape = RoundedCornerShape(100.dp)
                    )
                    .border(3.dp, Color(0xFFF177A0), RoundedCornerShape(100.dp))
                    .clickable { onClick() }
            ) {
                Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E3A59))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = subtitle, fontSize = 14.sp, color = Color.DarkGray, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun TheoryScreenContent(chapter: String, onBack: () -> Unit) {
    val (title, content) = when (chapter) {
        "1" -> "Kotlin Basics" to """
Kotlin is a modern, concise, and safe programming language used mainly for Android development. It reduces boilerplate code compared to Java and provides built-in safety features like null handling. Kotlin programs start from a main() function, and variables can be declared using val (immutable) and var (mutable). Type inference allows Kotlin to automatically detect variable types.

One of Kotlin’s most important features is null safety, which helps prevent crashes caused by null values. A variable cannot hold null unless explicitly declared nullable using ?. Kotlin also provides safe operators like ?., ?:, and !!.

Functions in Kotlin are first-class citizens and can be written in a concise way. You can define default parameters and even write single-line functions.

Data classes are used to store data efficiently. They automatically generate useful methods like toString(), equals(), and copy().

Example:

fun main() {
    val name: String = "Drishti"
    var age: Int = 18

    // Nullable variable
    var city: String? = null

    println(city?.length)   // Safe call
    println(city ?: "No City") // Elvis operator

    greet(name)
}

fun greet(name: String) {
    println("Hello ${'$'}name")
}

// Data class
data class User(val name: String, val age: Int)

val user1 = User("Drishti", 18)
        """.trimIndent()

        "2" -> "UI & Layouts" to """
In modern Android development, Jetpack Compose is used to build UI declaratively. Instead of XML layouts, UI is written using Kotlin functions called @Composable. This makes UI more readable and easier to manage.

Compose provides layout components like Column, Row, and Box to arrange UI elements. You can also make scrollable layouts using Modifier.verticalScroll() or LazyColumn for large lists.

Drawables and styling are handled using modifiers such as background(), padding(), and size().

Example:

@Composable
fun MyUI() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Hello World")

        Row {
            Text("Item 1")
            Text("Item 2")
        }
    }
}

Scrollable List:

@Composable
fun ListUI() {
    LazyColumn {
        items(10) {
            Text("Item ${'$'}it", modifier = Modifier.padding(8.dp))
        }
    }
}
        """.trimIndent()

        "3" -> "State & Logic" to """
In Compose, UI is driven by state. Whenever state changes, the UI automatically recomposes (updates). This is a key concept called declarative UI.

The remember function is used to store state within a composable so it survives recomposition. For larger apps, we use a ViewModel, which holds UI-related data and survives configuration changes like screen rotation.

State can be managed using mutableStateOf() and observed directly in UI. Kotlin objects can be used to create singletons for shared logic.

Example:

@Composable
fun Counter() {
    var count by remember { mutableStateOf(0) }

    Column {
        Text("Count: ${'$'}count")
        Button(onClick = { count++ }) {
            Text("Increase")
        }
    }
}

ViewModel Example:

class MyViewModel : ViewModel() {
    var count = mutableStateOf(0)

    fun increment() {
        count.value++
    }
}
        """.trimIndent()

        "4" -> "App Architecture" to """
Real Android apps require handling background tasks, APIs, data storage, and structured architecture.

Coroutines are used for asynchronous programming. They allow tasks like network calls to run in the background without blocking the UI. launch and suspend functions are commonly used.

For API calls, libraries like Retrofit are used to fetch data from the internet. Data storage can be handled using SharedPreferences, Room Database, or DataStore.

Modern apps follow architecture patterns like MVVM (Model-View-ViewModel), where:
Model → Data layer
View → UI (Compose)
ViewModel → Logic & state

Coroutine Example:

fun fetchData() {
    CoroutineScope(Dispatchers.IO).launch {
        val data = getDataFromAPI()
        println(data)
    }
}

suspend fun getDataFromAPI(): String {
    return "Data loaded"
}

Simple MVVM Flow:

class MyViewModel : ViewModel() {
    var data = mutableStateOf("Loading...")

    fun loadData() {
        viewModelScope.launch {
            data.value = getDataFromAPI()
        }
    }
}
        """.trimIndent()

        else -> "Chapter Content" to "Ready to learn more?"
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFFDECEF))
    ) {
        // BACK BUTTON
        Box(
            modifier = Modifier
                .padding(16.dp)
                .shadow(4.dp, RoundedCornerShape(50.dp))
                .background(Color.White.copy(alpha = 0.3f), RoundedCornerShape(50.dp))
                .clickable { onBack() }
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(text = "← Back", fontSize = 16.sp, color = Color(0xFF7B9ACC))
        }

        Column(modifier = Modifier.padding(20.dp)) {
            Text(text = "Chapter $chapter", fontSize = 14.sp, color = Color(0.5f, 0.5f, 0.5f, 1f))
            Text(text = title, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E3A59))

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(6.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                Column(modifier = Modifier.padding(24.dp).verticalScroll(rememberScrollState())) {
                    Text(text = content, fontSize = 16.sp, lineHeight = 24.sp, color = Color(0xFF4A4E69))
                }
            }
        }
    }
}
