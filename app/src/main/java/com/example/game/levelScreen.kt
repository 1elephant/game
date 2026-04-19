package com.example.game


import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DifficultyScreen(
    appViewModel:AppViewModel,
    chapter: Int,
    navController: NavController,
    quizViewModel: QuizViewModel // ✅ PASS from MainActivity (NO viewModel())
) {
    val isSoundOn = appViewModel.isSoundOn
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
//    var isSoundOn by remember { mutableStateOf(true) }
    val difficulties = listOf(
        Triple("Easy", "Beginner", Difficulty.EASY),
        Triple("Medium", "Intermediate", Difficulty.MEDIUM),
        Triple("Hard", "Advanced", Difficulty.HARD)
    )

    val pagerState = rememberPagerState(pageCount = { difficulties.size })
    MusicPlayer(isSoundOn,R.raw.sapphire)

    // ✅ prevent double navigation
    var isNavigating by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier

            .fillMaxSize()
            .background(Color(0xFFfde2e4))
    ) {

        Column(modifier = Modifier.fillMaxSize()) {

            // TOP BAR
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFfad2e1))
                    .height(screenHeight * 0.1f)
                    .padding(horizontal=9.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = null,tint = Color(0xFF7B9ACC),
                    modifier = Modifier.clickable {
                        navController.popBackStack()
                    }
                )
                Text("Select Difficulty", fontWeight = FontWeight.SemiBold,color=Color(0xFF7B9ACC),fontSize = 18.sp)
//                Icon(Icons.Default.Settings, contentDescription = null,tint = Color(0xFF7B9ACC))
                Icon(
                    imageVector = if (isSoundOn)
                        Icons.Default.VolumeUp
                    else
                        Icons.Default.VolumeOff,
                    contentDescription = "Sound Toggle",
                    modifier = Modifier
                        .clickable {
                            appViewModel.isSoundOn = !appViewModel.isSoundOn
                        },
                    tint = Color(0xFF7B9ACC),

                    )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // PAGER
            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 50.dp),
                pageSpacing = 0.dp
            ) { page ->

                val (title, subtitle, difficulty) = difficulties[page]

                val pageOffset =
                    (pagerState.currentPage - page).toFloat() +
                            pagerState.currentPageOffsetFraction

                val scale =
                    0.85f + (1f - pageOffset.absoluteValue).coerceIn(0f, 1f) * 0.15f

                val questions = remember(chapter, difficulty) {
                    LearningQuestionBank.getQuestions(chapter, difficulty)
                }

                val scoreMap = quizViewModel.learningScores
                val savedScore = scoreMap[chapter to difficulty] ?: 0

                Card(
                    modifier = Modifier
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        }
                        .fillMaxWidth(0.85f)
                        .height(480.dp),
                    shape = RoundedCornerShape(28.dp),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(28.dp))
                    ) {

                        Image(
                            painter = painterResource(
                                when (difficulty) {
                                    Difficulty.EASY -> R.drawable.pink2
                                    Difficulty.MEDIUM -> R.drawable.pink3
                                    Difficulty.HARD -> R.drawable.pink1
                                }
                            ),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

//                        Box(
//                            modifier = Modifier
//                                .fillMaxSize()
//                                .background(Color.Black.copy(alpha = 0.25f))
//                        )

                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Spacer(modifier = Modifier.height(16.dp))

                            Box(
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(CircleShape)
                                    .background(Color.White),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = when (difficulty) {
                                        Difficulty.EASY -> Icons.Default.Spa
                                        Difficulty.MEDIUM -> Icons.Default.LightMode
                                        Difficulty.HARD -> Icons.Default.Bolt
                                    },
                                    contentDescription = null,
                                    tint = Color(0xFF7B9ACC),
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                title,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Text(
                                subtitle,
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.8f)
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            Text(
                                text = "$savedScore / ${questions.size}",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // DOTS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(3) { index ->
                    val isSelected = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(if (isSelected) 10.dp else 6.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) Color.DarkGray else Color.LightGray
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // START BUTTON
            Button(
                onClick = {
                    if (!isNavigating) {
                        isNavigating = true

                        val selectedDifficulty =
                            difficulties[pagerState.currentPage].third

                        val questions = LearningQuestionBank.getQuestions(
                            chapter,
                            selectedDifficulty
                        )

                        if (questions.isNotEmpty()) {
                            navController.navigate(
                                Screen.LearningQuiz.createRoute(
                                    chapter,
                                    selectedDifficulty.name
                                )
                            )
                        } else {
                            Toast.makeText(
                                navController.context,
                                "No questions for this level",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 60.dp),
                shape = RoundedCornerShape(9.dp)
            ) {
                Text("Start")
            }
        }
    }
}