package com.example.game


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun QuizScreen(
    appViewModel: AppViewModel,
    questions: List<Question>,   // ✅ changed
    level: Int,
    onQuizFinished: (level: Int, score: Int) -> Unit
) {

    var currentIndex by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var isFinished by remember { mutableStateOf(false) }
    val currentQuestion = questions.getOrNull(currentIndex)
    if (currentQuestion == null) {
        isFinished = true
        return
    }
    val colors = listOf(
        Color(0xFFF9D0CD),
        Color(0xFFFFCCD5),
        Color(0xFFFFB3C1),
    )
    if (questions.isEmpty()) {
        Text("No questions for this level")
        return
    }

    if (isFinished) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = colors
                )
            )
        ) {
            ScoreCard1(
                appViewModel = appViewModel,
                totalQuestions = questions.size,
                correctAnswers = score
            )

            FloatingActionButton(
                onClick = { onQuizFinished(level, score) },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp),
                containerColor = Color(0xFFFF758F)

            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",

                )
            }
        }
    } else {

        val currentQuestion = questions[currentIndex]

        when (val q = currentQuestion) {

            is Question.MCQ -> {
                MCQView(
                    mcq = q,
                    currentQuestion = currentIndex,
                    totalQuestions = questions.lastIndex,
                    onAnswerSelected = { isCorrect ->
                        if (isCorrect) score++

                        if (currentIndex < questions.lastIndex) {
                            currentIndex++
                        } else {
                            isFinished = true
                        }
                    }
                )
            }

            is Question.FillBlank -> {
                FillBlankView(
                    question = q,
                    currentQuestion = currentIndex,
                    totalQuestions = questions.size,
                    onAnswerSubmitted = { isCorrect ->
                        if (isCorrect) score++

                        if (currentIndex < questions.lastIndex) {
                            currentIndex++
                        } else {
                            isFinished = true
                        }
                    }
                )
            }
        }
    }
}