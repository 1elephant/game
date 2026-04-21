package com.example.game

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.game.ui.theme.GameTheme
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val quizViewModel: QuizViewModel by viewModels()
    private val appViewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            GameTheme {
                val navController = rememberNavController()
                val progress = quizViewModel.progress
                val scope = rememberCoroutineScope()
                val context = LocalContext.current

                NavHost(
                    navController = navController,
                    startDestination = Screen.SplashScreen.route
                ) {

                    // 🔹 SPLASH
                    composable(Screen.SplashScreen.route) {
                        SplashScreen {
                            navController.navigate(Screen.Login.route) {
                                popUpTo(Screen.SplashScreen.route) { inclusive = true }
                            }
                        }
                    }

                    // 🔹 LOGIN
                    composable(Screen.Login.route) {
                        var navTriggered by remember { mutableStateOf(false) }

                        LoginScreen { email, _ ->
                            if (navTriggered) return@LoginScreen
                            appViewModel.userEmail = email
                            
                            scope.launch {
                                val profileJob = launch {
                                    appViewModel.loadProfile { hasProfile, error ->
                                        if (!navTriggered) {
                                            navTriggered = true
                                            if (error != null) {
                                                Toast.makeText(context, "Database Error: $error", Toast.LENGTH_LONG).show()
                                                navController.navigate("profile_setup") { 
                                                    popUpTo(Screen.Login.route) { inclusive = true } 
                                                }
                                            } else {
                                                quizViewModel.loadUserData()
                                                if (hasProfile) {
                                                    navController.navigate(Screen.BossMapScreen.route) {
                                                        popUpTo(Screen.Login.route) { inclusive = true }
                                                    }
                                                } else {
                                                    navController.navigate("profile_setup") {
                                                        popUpTo(Screen.Login.route) { inclusive = true }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                
                                delay(3500) // Safety timeout
                                if (!navTriggered) {
                                    navTriggered = true
                                    profileJob.cancel()
                                    navController.navigate("profile_setup") {
                                        popUpTo(Screen.Login.route) { inclusive = true }
                                    }
                                }
                            }
                        }
                    }

                    // 🔹 BOSS MAP
                    composable(Screen.BossMapScreen.route) {
                        BossMapScreen(
                            appViewModel = appViewModel,
                            quizViewModel = quizViewModel,
                            progress = progress,
                            navController = navController
                        )
                    }

                    // 🔹 LEADERBOARD
                    composable(Screen.Leaderboard.route) {
                        LeaderboardScreen(
                            onBack = { navController.popBackStack() }
                        )
                    }

                    // 🔹 PROFILE SETUP
                    composable("profile_setup") {
                        ProfileSetupScreen(
                            appViewModel = appViewModel,
                            onSave = {
                                quizViewModel.loadUserData()
                                navController.navigate(Screen.BossMapScreen.route) {
                                    popUpTo("profile_setup") { inclusive = true }
                                }
                            }
                        )
                    }

                    // 🔹 DIFFICULTY
                    composable(Screen.Difficulty.route) { backStackEntry ->
                        val chapter = backStackEntry.arguments?.getString("chapter")?.toIntOrNull() ?: 1
                        DifficultyScreen(
                            appViewModel = appViewModel,
                            chapter = chapter,
                            navController = navController,
                            quizViewModel = quizViewModel
                        )
                    }

                    // 🔹 SCORE SCREEN
                    composable("score_screen/{score}/{total}") { backStackEntry ->
                        val score = backStackEntry.arguments?.getString("score")?.toIntOrNull() ?: 0
                        val total = backStackEntry.arguments?.getString("total")?.toIntOrNull() ?: 0

                        ScoreCard1(
                            appViewModel = appViewModel,
                            navController = navController, // ✅ FIXED: Passed missing parameter
                            totalQuestions = total,
                            correctAnswers = score
                        )
                    }

                    // 🔹 LEARNING QUIZ
                    composable(Screen.LearningQuiz.route) { backStackEntry ->
                        val chapter = backStackEntry.arguments?.getString("chapter")?.toIntOrNull() ?: 1
                        val difficultyString = backStackEntry.arguments?.getString("difficulty") ?: "EASY"
                        val difficulty = try { Difficulty.valueOf(difficultyString) } catch (e: Exception) { Difficulty.EASY }

                        val questions = LearningQuestionBank.getQuestions(chapter, difficulty)

                        if (questions.isEmpty()) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text("No questions available")
                            }
                        } else {
                            QuizScreen(
                                appViewModel = appViewModel,
                                questions = questions,
                                level = chapter,
                                onQuizFinished = { _, score ->
                                    quizViewModel.submitLearningScore(chapter, difficulty, score)
                                    // ✅ FIX: Remove quiz from backstack when moving to scorecard
                                    navController.navigate("score_screen/$score/${questions.size}") {
                                        popUpTo(Screen.LearningQuiz.route) { inclusive = true }
                                    }
                                }
                            )
                        }
                    }

                    // 🔹 MAIN QUIZ
                    composable(Screen.QuizMain.route) { backStackEntry ->
                        val level = backStackEntry.arguments?.getString("level")?.toIntOrNull() ?: 1
                        val questions = QuestionBank.getQuestions(level)

                        if (questions.isEmpty()) {
                            Text("No questions available")
                        } else {
                            QuizScreen(
                                appViewModel = appViewModel,
                                questions = questions,
                                level = level,
                                onQuizFinished = { lvl, score ->
                                    quizViewModel.submitScore(lvl, score)
                                    // ✅ FIX: Remove quiz from backstack when moving to scorecard
                                    navController.navigate("score_screen/$score/${questions.size}") {
                                        popUpTo(Screen.QuizMain.route) { inclusive = true }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
