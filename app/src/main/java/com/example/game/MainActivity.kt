package com.example.game


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.game.ui.theme.GameTheme

class MainActivity : ComponentActivity() {

    private val quizViewModel: QuizViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            GameTheme {

                val navController = rememberNavController()

                // ✅ observe properly (prevents stale UI issues)
                val progress = quizViewModel.progress
                val learningScores = quizViewModel.learningScores

                NavHost(
                    navController = navController,
                    startDestination = Screen.SplashScreen.route
                ) {

                    // 🔹 SPLASH
                    composable(Screen.SplashScreen.route) {
                        SplashScreen {
                            navController.navigate(Screen.BossMapScreen.route) {
                                popUpTo(Screen.SplashScreen.route) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    }

                    // 🔹 BOSS MAP
                    composable(Screen.BossMapScreen.route) {
                        BossMapScreen(
                            progress = progress,
                            navController = navController
                        )
                    }

                    // 🔹 DIFFICULTY
                    composable(Screen.Difficulty.route) { backStackEntry ->

                        val chapter =
                            backStackEntry.arguments?.getString("chapter")?.toIntOrNull() ?: 1

                        DifficultyScreen(
                            chapter = chapter,
                            navController = navController,
                            quizViewModel = quizViewModel
                        )
                    }

                    // 🔹 LEARNING QUIZ
                    composable(Screen.LearningQuiz.route) { backStackEntry ->

                        val chapter =
                            backStackEntry.arguments?.getString("chapter")?.toIntOrNull() ?: 1

                        val difficultyString =
                            backStackEntry.arguments?.getString("difficulty") ?: "EASY"

                        val difficulty = try {
                            Difficulty.valueOf(difficultyString)
                        } catch (e: Exception) {
                            Difficulty.EASY
                        }

                        val questions =
                            LearningQuestionBank.getQuestions(chapter, difficulty)

                        if (questions.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("No questions available")
                            }
                        } else {
                            QuizScreen(
                                questions = questions,
                                level = chapter,
                                onQuizFinished = { _, score ->
                                    quizViewModel.submitLearningScore(chapter, difficulty, score)
                                    navController.popBackStack()
                                }
                            )
                        }
                    }

                    // 🔹 MAIN QUIZ
                    composable(Screen.QuizMain.route) { backStackEntry ->

                        val level =
                            backStackEntry.arguments?.getString("level")?.toIntOrNull() ?: 1

                        val questions = QuestionBank.getQuestions(level)

                        if (questions.isEmpty()) {
                            Text("No questions available")
                            return@composable
                        }

                        QuizScreen(
                            questions = questions,
                            level = level,
                            onQuizFinished = { lvl, score ->
                                quizViewModel.submitScore(lvl, score)
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}
