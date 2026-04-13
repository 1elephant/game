package com.example.game


import androidx.benchmark.traceprocessor.Row
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@Composable
fun MCQView(
    mcq: Question.MCQ,
    currentQuestion: Int,
    totalQuestions: Int,
//    navController: NavController,
    onAnswerSelected: (Boolean) -> Unit,
) {
    // 🔥 reset when question changes
    var selectedIndex by remember(currentQuestion) { mutableStateOf<Int?>(null) }

    // 🔥 prevent double click
    var answered by remember(currentQuestion) { mutableStateOf(false) }

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    val progress = if (totalQuestions > 0)
        (currentQuestion + 1).toFloat() / totalQuestions
    else 0f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFfde2e4))
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color(0xFF6B7A99),
//                    modifier = Modifier.clickable {
//                        navController.popBackStack()
//                    }
                    )
                Text("MCQ Question", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                Icon(Icons.Default.Settings, contentDescription = null, tint = Color(0xFF6B7A99))
            }

            // Progress Bar
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(50)),
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Question Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFfad2e1)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight * 0.3f)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = mcq.question,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF2E3A59)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(mcq.options) { index, option ->
                    val isSelected = selectedIndex == index

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(enabled = !answered) {
                                selectedIndex = index
                            },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) Color(0xFFFFCCD5) else Color(0xFFFFB3C1)
                        ),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = option,
                                fontSize = 14.sp,
                                color = Color(0xFF2E3A59)
                            )
                        }
                    }
                }
            }
        }
        // ✅ FAB (safe)
        FloatingActionButton(
            onClick = {
                if (!answered && selectedIndex != null) {
                    answered = true
                    onAnswerSelected(mcq.isCorrect(selectedIndex!!))
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = if (selectedIndex != null && !answered)
                Color(0xFFFF758F)
            else
                Color.LightGray
        ) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Next"
            )
        }
    }
}
@Composable
fun FillBlankView(
    question: Question.FillBlank,
    currentQuestion: Int,
    totalQuestions: Int,
    onAnswerSubmitted: (Boolean) -> Unit
) {
    // 🔥 reset per question
    var text by remember(currentQuestion) { mutableStateOf("") }

    // 🔥 prevent double submit
    var answered by remember(currentQuestion) { mutableStateOf(false) }

    val progress = if (totalQuestions > 0)
        (currentQuestion + 1).toFloat() / totalQuestions
    else 0f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFfde2e4))
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = null)
                Text("Fill in the Blank", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                Icon(Icons.Default.Settings, contentDescription = null)
            }

            // Progress Bar
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(50))
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Question Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFfad2e1)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = question.question,
                    modifier = Modifier.padding(16.dp),
                    fontSize = 16.sp,
                    color = Color(0xFF2E3A59)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Input Field
            OutlinedTextField(
                value = text,
                onValueChange = { if (!answered) text = it }, // 🔥 lock after submit
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Type your answer...") },
                enabled = !answered, // 🔥 disable after submit
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    cursorColor = Color(0xFFFF758F),
                    focusedBorderColor = Color(0xFFFF758F),
                    unfocusedBorderColor = Color.Gray,
                    focusedPlaceholderColor = Color.Gray,
                    unfocusedPlaceholderColor = Color.LightGray
                )
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        // FAB (safe)
        FloatingActionButton(
            onClick = {
                if (!answered && text.isNotBlank()) {
                    answered = true
                    val isCorrect = question.isCorrect(text)
                    onAnswerSubmitted(isCorrect)
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = if (text.isNotBlank() && !answered)
                Color(0xFFFF758F)
            else
                Color.LightGray
        ) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Next"
            )
        }
    }
}