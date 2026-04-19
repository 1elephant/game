package com.example.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// We do NOT define colors here again because they are already in QuizScreen.kt

@Composable
fun ScoreScreen(score: Int, total: Int, onRestart: () -> Unit) {
    val percentage = if (total > 0) (score * 100) / total else 0

    Column(
        modifier = Modifier.fillMaxSize().background(BackgroundPink), // Uses color from QuizScreen.kt
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
                Text("$percentage%", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = DarkText)
                Text("Score: $score / $total", color = DarkText)
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