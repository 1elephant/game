package com.example.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MCQView(
    mcq: Question.MCQ,
    currentQuestion: Int,
    totalQuestions: Int,
    onAnswerSelected: (Boolean) -> Unit,
) {
    var selectedIndex by remember(currentQuestion) { mutableStateOf<Int?>(null) }
    var answered by remember(currentQuestion) { mutableStateOf(false) }

    var showFeedback by remember(currentQuestion) { mutableStateOf(false) }
    var isCorrectAnswer by remember(currentQuestion) { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

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
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color(0xFF7B9ACC))
                Text("MCQ Question", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF7B9ACC))
                Icon(Icons.Default.Settings, contentDescription = null, tint = Color(0xFF7B9ACC))
            }

            // 🔹 Progress
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(50)),
                color = Color(0xFFFF758F),
                trackColor = Color.White
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 🔹 Question Card
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFfad2e1)),
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Box(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = mcq.question,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF4A4E69),
                        textAlign = TextAlign.Start,
                        lineHeight = 26.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 🔹 Options Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(mcq.options) { index, option ->
                    val isSelected = selectedIndex == index
                    val isCorrectOption = index == mcq.correctAnswerIndex

                    val backgroundColor = when {
                        answered && isCorrectOption -> Color(0xFF81C784)
                        answered && isSelected -> Color(0xFFFF8A80)
                        isSelected -> Color(0xFFFF758F)
                        else -> Color(0xFFFFB3C1)
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp) // ✅ Fixed height for equal size
                            .clickable(enabled = !answered) {
                                selectedIndex = index
                            },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = backgroundColor),
                        elevation = CardDefaults.cardElevation(if (isSelected) 8.dp else 2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize() // ✅ Fill size for centering
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = option,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isSelected || answered) Color.White else Color(0xFF4A4E69),
                                textAlign = TextAlign.Center // ✅ Centered text
                            )
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = {
                if (!answered && selectedIndex != null) {
                    answered = true
                    val isCorrect = mcq.isCorrect(selectedIndex!!)
                    isCorrectAnswer = isCorrect
                    showFeedback = true

                    scope.launch {
                        delay(1000)
                        showFeedback = false
                        onAnswerSelected(isCorrect)
                    }
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            containerColor = if (selectedIndex != null && !answered)
                Color(0xFFFF758F)
            else
                Color.LightGray
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next", tint = Color.White)
        }

        if (showFeedback) {
            FeedbackDialog(isCorrectAnswer, screenWidth)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FillBlankView(
    question: Question.FillBlank,
    currentQuestion: Int,
    totalQuestions: Int,
    onAnswerSubmitted: (Boolean) -> Unit
) {
    val blankCount = remember(question.question) { question.question.split("___").size - 1 }
    var answersState by remember(currentQuestion) { mutableStateOf(List(blankCount) { "" }) }
    var answered by remember(currentQuestion) { mutableStateOf(false) }
    var showFeedback by remember(currentQuestion) { mutableStateOf(false) }
    var isCorrectAnswer by remember(currentQuestion) { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val progress = if (totalQuestions > 0) (currentQuestion + 1).toFloat() / totalQuestions else 0f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFfde2e4))
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            // 🔹 Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color(0xFF7B9ACC))
                Text("Fill in the Blank", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF7B9ACC))
                Icon(Icons.Default.Settings, contentDescription = null, tint = Color(0xFF7B9ACC))
            }

            // 🔹 Progress
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(50)),
                color = Color(0xFFFF758F),
                trackColor = Color.White
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 🔹 Question Content Card (Pink themed)
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFfad2e1)),
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalArrangement = Arrangement.Center
                    ) {
                        val parts = question.question.split("___")
                        parts.forEachIndexed { index, part ->
                            Text(
                                text = part,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF4A4E69),
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                            
                            if (index < parts.lastIndex) {
                                val text = answersState.getOrElse(index) { "" }
                                var isFocused by remember(index) { mutableStateOf(false) }

                                Column(
                                    modifier = Modifier.padding(horizontal = 6.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    BasicTextField(
                                        value = text,
                                        onValueChange = { newValue ->
                                            if (!answered) {
                                                val newList = answersState.toMutableList()
                                                newList[index] = newValue
                                                answersState = newList
                                            }
                                        },
                                        singleLine = true,
                                        textStyle = TextStyle(
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color(0xFFFF758F),
                                            textAlign = TextAlign.Center
                                        ),
                                        cursorBrush = SolidColor(Color(0xFFFF758F)),
                                        modifier = Modifier
                                            .width(IntrinsicSize.Min)
                                            .widthIn(min = 70.dp, max = 150.dp)
                                            .onFocusChanged { isFocused = it.isFocused }
                                    )
                                    Box(
                                        modifier = Modifier
                                            .width(70.dp)
                                            .height(3.dp)
                                            .background(if (isFocused) Color(0xFFFF758F) else Color(0xFF4A4E69).copy(alpha = 0.5f))
                                    )
                                }
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
        }

        FloatingActionButton(
            onClick = {
                if (!answered && answersState.all { it.isNotBlank() }) {
                    answered = true
                    val isCorrect = question.isCorrect(answersState)
                    isCorrectAnswer = isCorrect
                    showFeedback = true
                    scope.launch {
                        delay(1000)
                        showFeedback = false
                        onAnswerSubmitted(isCorrect)
                    }
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            containerColor = if (answersState.all { it.isNotBlank() } && !answered)
                Color(0xFFFF758F)
            else
                Color.LightGray
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next", tint = Color.White)
        }

        if (showFeedback) {
            FeedbackDialog(isCorrectAnswer, screenWidth)
        }
    }
}

@Composable
fun FeedbackDialog(isCorrect: Boolean, screenWidth: androidx.compose.ui.unit.Dp) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.size(screenWidth * 0.7f),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(
                        if (isCorrect) R.drawable.happy
                        else R.drawable.wrong
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(screenWidth * 0.5f)
                )
            }
        }
    }
}
