package com.example.game


import androidx.benchmark.traceprocessor.Row
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
    // 🔥 count total blanks in entire question
    val totalBlanks = question.question.split("___").size - 1

    // 🔥 answers state
    var answers by remember(currentQuestion) {
        mutableStateOf(List(totalBlanks) { "" })
    }

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

            // 🔹 Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = null)
                Text(
                    "Fill in the Blank",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Icon(Icons.Default.Settings, contentDescription = null)
            }

            // 🔹 Progress Bar
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(50))
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 🔥 QUESTION CARD (MULTI-LINE + INLINE BLANKS)
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFfad2e1)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    val lines = question.question.split("\n")

                    // 🔥 Create stable mapping of blanks
                    val partsPerLine = remember(question.question) {
                        lines.map { it.split("___") }
                    }

                    // 🔥 Flatten to know total blanks
                    val totalBlanks = partsPerLine.sumOf { it.size - 1 }

                    // 🔥 Ensure answers size is correct
                    if (answers.size != totalBlanks) {
                        answers = List(totalBlanks) { "" }
                    }

                    var currentIndex = 0

                    partsPerLine.forEach { parts ->

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            parts.forEachIndexed { index, part ->

                                Text(
                                    text = part,
                                    fontSize = 16.sp,
                                    color = Color(0xFF2E3A59)
                                )

                                if (index < parts.size - 1) {

                                    val safeIndex = currentIndex // 🔥 FIX

                                    val text = answers.getOrElse(safeIndex) { "" }

                                    val dynamicWidth = (text.length * 12).coerceIn(60, 220)
                                    var isFocused by remember { mutableStateOf(false) }

                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {

                                        BasicTextField(
                                            value = text,
                                            onValueChange = { newValue ->
                                                if (!answered && safeIndex < answers.size) {
                                                    val updated = answers.toMutableList()
                                                    updated[safeIndex] = newValue
                                                    answers = updated
                                                }
                                            },
                                            singleLine = true,
                                            textStyle = TextStyle(
                                                fontSize = 16.sp,
                                                color = Color(0xFFD81B60), // 🔥 darker pink text
                                                textAlign = TextAlign.Center
                                            ),
                                            cursorBrush = SolidColor(Color(0xFFFF758F)), // pink cursor
                                            modifier = Modifier
                                                .width(dynamicWidth.dp)
                                                .onFocusChanged { isFocused = it.isFocused }
                                        )

                                        // 🔥 underline
                                        Box(
                                            modifier = Modifier
                                                .width(dynamicWidth.dp)
                                                .height(2.dp)
                                                .background(
                                                    if (isFocused)
                                                        Color(0xFFFF758F) // pink when typing
                                                    else
                                                        Color.Gray
                                                )
                                        )
                                    }

                                    currentIndex++
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // 🔥 SUBMIT BUTTON
        FloatingActionButton(
            onClick = {
                if (!answered && answers.all { it.isNotBlank() }) {
                    answered = true
                    val isCorrect = question.isCorrect(answers)
                    onAnswerSubmitted(isCorrect)
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = if (answers.all { it.isNotBlank() } && !answered)
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