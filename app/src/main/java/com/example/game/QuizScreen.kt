package com.example.game

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


val BackgroundPink = Color(0xFFFFE1E9)
val CardPink = Color(0xFFF9C5D5)
val DarkText = Color(0xFF3E4E88)
val AccentBlue = Color(0xFF81D4FA)
val FABPink = Color(0xFFF06292)

@Composable
fun QuizScreen(
    appViewModel: AppViewModel,
    questions: List<Question>,
    level: Int,
    onQuizFinished: (Int, Int) -> Unit
) {
    var index by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var selectedOption by remember { mutableStateOf("") }
    var fillAnswer by remember { mutableStateOf("") }
    var showResult by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf(false) }

    if (questions.isEmpty()) return
    val q = questions[index]

    Column(modifier = Modifier.fillMaxSize().background(BackgroundPink).statusBarsPadding()) {
        // TOP BAR
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { if (index > 0) index-- else onQuizFinished(level, score) }) {
                Icon(Icons.Default.ArrowBack, null, tint = DarkText)
            }
            Text(
                "Question ${index + 1}/${questions.size}",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = DarkText
            )
            IconButton(onClick = {}) { Icon(Icons.Default.Settings, null, tint = DarkText) }
        }

        
        LinearProgressIndicator(
            progress = { (index + 1).toFloat() / questions.size },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).height(8.dp),
            color = FABPink, // ✅ Changed to Pink
            trackColor = Color.White.copy(alpha = 0.5f)
        )

        Spacer(modifier = Modifier.height(25.dp))

        
        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
            shape = RoundedCornerShape(30.dp),
            colors = CardDefaults.cardColors(containerColor = CardPink)
        ) {
            val questionText = when(q) {
                is Question.MCQ -> q.question
                is Question.FillBlank -> q.question
            }
            Text(questionText, fontSize = 20.sp, color = DarkText, modifier = Modifier.padding(24.dp))
        }

        Spacer(modifier = Modifier.height(25.dp))

        Box(modifier = Modifier.weight(1f)) {
            when (q) {
                is Question.MCQ -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(q.options) { option ->
                            val isSelected = selectedOption == option
                            val isCorrectOption = q.options.indexOf(option) == q.correctAnswerIndex

                            Card(
                                modifier = Modifier.fillMaxWidth().height(80.dp).clickable {
                                    if (!showResult) selectedOption = option
                                },
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = when {
                                        showResult && isCorrectOption -> Color(0xFFB2F2BB)
                                        showResult && isSelected -> Color(0xFFFFB3B3)
                                        isSelected -> FABPink // ✅ Changed to Pink
                                        else -> CardPink
                                    }
                                )
                            ) {
                                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Text(option, color = if(isSelected) Color.White else DarkText, textAlign = TextAlign.Center)
                                }
                            }
                        }
                    }
                }
                is Question.FillBlank -> {
                    Column(modifier = Modifier.padding(horizontal = 32.dp)) {
                        OutlinedTextField(
                            value = fillAnswer,
                            onValueChange = { if (!showResult) fillAnswer = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Your Answer") },
                            textStyle = TextStyle(color = FABPink, fontWeight = FontWeight.Bold, fontSize = 18.sp), // ✅ Pink Text
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = FABPink,
                                focusedLabelColor = FABPink,
                                cursorColor = FABPink,
                                unfocusedBorderColor = CardPink
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                    }
                }
            }
        }

        if (showResult) {
            Text(
                if (isCorrect) "✅ Correct!" else "❌ Wrong!",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = if(isCorrect) Color(0xFF4CAF50) else Color.Red,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Row(modifier = Modifier.fillMaxWidth().padding(24.dp), horizontalArrangement = Arrangement.End) {
            FloatingActionButton(
                onClick = {
                    if (!showResult) {
                        val ans = if (q is Question.MCQ) selectedOption else fillAnswer
                        if (ans.isBlank()) return@FloatingActionButton
                        isCorrect = when (q) {
                            is Question.MCQ -> q.options.indexOf(selectedOption) == q.correctAnswerIndex
                            is Question.FillBlank -> q.isCorrect(listOf(fillAnswer))
                        }
                        if (isCorrect) score++
                        showResult = true
                    } else {
                        if (index < questions.size - 1) {
                            index++; selectedOption = ""; fillAnswer = ""; showResult = false
                        } else {
                            onQuizFinished(level, score)
                        }
                    }
                },
                containerColor = FABPink
            ) {
                Icon(Icons.Default.ArrowForward, null, tint = Color.White)
            }
        }
    }
}
