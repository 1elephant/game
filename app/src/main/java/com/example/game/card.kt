package com.example.game
import android.media.MediaPlayer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.PlayArrow
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun ScoreCard1(
    appViewModel: AppViewModel,
    navController: NavController,
    totalQuestions: Int,
    correctAnswers: Int
) {
    val isSoundOn = appViewModel.isSoundOn
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val percentage = if (totalQuestions > 0)
        (correctAnswers * 100) / totalQuestions
    else 0

    val stars = when {
        percentage >= 80 -> 3
        percentage >= 50 -> 2
        percentage >= 30 -> 1
        else -> 0
    }

    MusicPlayer(isSoundOn,R.raw.sapphire)

    Box(modifier = Modifier.fillMaxSize().background(color=Color(0xFFFFCCD5))) {

        // 🔷 HEADER
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight*0.3f)
        ) {

            Image(
                painter = painterResource(id = R.drawable.pink2),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color(0x66FAD2E1))
            )

            // 🔝 Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 40.dp)
                    .align(Alignment.TopCenter),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                // ✅ Go back to the previous screen (Difficulty/Map)
                IconButton(onClick = { 
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF7B9ACC)
                    )
                }

                Text(
                    text = "Score Card",
                    color = Color(0xFF7B9ACC),
                    fontSize = 22.sp,
//                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )

                IconButton(onClick = { appViewModel.isSoundOn = !appViewModel.isSoundOn }) {
                    Icon(
                        imageVector = if (isSoundOn) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                        contentDescription = "Sound Toggle",
                        tint = Color(0xFF7B9ACC),
                    )
                }
            }
        }

        // 🧾 MAIN CONTENT
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = screenHeight * 0.22f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 🎯 Score Circle
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(4.dp, Color(0xFFF48FB1), CircleShape)
            ) {
                Text(
                    text = "$percentage%",
                    fontSize = 32.sp,
//                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF48FB1)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ⭐ Stars
            Row {
                repeat(3) { index ->
                    Icon(
                        imageVector = if (index < stars) Icons.Default.Star else Icons.Default.StarBorder,
                        contentDescription = null,
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 📊 Stats
            Row(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem("Correct", correctAnswers.toString())
                StatItem("Wrong", (totalQuestions - correctAnswers).toString())
                StatItem("Total", totalQuestions.toString())
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 💬 Message
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = when {
                        percentage >= 80 -> "Master of Kotlin! 🏆"
                        percentage >= 50 -> "Well Played! Keep it up! ✨"
                        else -> "Good Effort! Let's try again! 💪"
                    },
                    modifier = Modifier.padding(20.dp),
                    textAlign = TextAlign.Center,
//                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Medium,
                    fontSize = 17.sp,
                    color = Color(0xFF7B9ACC)
                )
            }
        }
    }
}

@Composable
fun StatItem(title: String, value: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFCF8F8)
        ),
        modifier = Modifier
            .padding(horizontal = 6.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 16.dp)
        ) {
            Text(
                text = value,
                fontSize = 18.sp,
//                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = title,
                fontSize = 14.sp,
//                fontFamily = FontFamily.Monospace,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun ChapterCardCoastal(
    level: Int,
    title: String,
    description: String,
    imageRes: Int,
    progress: BossProgress,
    navController: NavController
) {
    val context = LocalContext.current
    var showIcons by remember { mutableStateOf(false) }
    var isNavigating by remember { mutableStateOf(false) }

    val quizUnlocked = progress.isLevelUnlocked(level)
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 8.dp)
            .shadow(6.dp, RoundedCornerShape(20.dp))
            .clickable { showIcons = !showIcons }
            .animateContentSize(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Circular Level Indicator
                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .background(Color(0xFFfad2e1), CircleShape)
                        .border(2.dp, Color(0xFF7B9ACC), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = level.toString().padStart(2, '0'),
                        color = Color(0xFF7B9ACC),
                        fontSize = 18.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Title and Description
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title.uppercase(),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = Color(0xFF7B9ACC)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = description,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontFamily = FontFamily.Monospace,
                        lineHeight = 16.sp,
                        maxLines = 2
                    )
                }
            }

            // Interactive Icons (Visible on tap)
            if (showIcons) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // ▶ Levels Button
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable {
                        if (!isNavigating) {
                            isNavigating = true
                            navController.navigate(Screen.Difficulty.createRoute(level))
                        }
                    }) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color(0xFF7B9ACC), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.PlayArrow, null, tint = Color.White)
                        }
                        Text("LEVELS", fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = Color(0xFF7B9ACC))
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    // 🧠 Quiz Button
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable {
                        if (!isNavigating) {
                            if (quizUnlocked) {
                                isNavigating = true
                                navController.navigate(Screen.QuizMain.createRoute(level))
                            } else {
                                Toast.makeText(context, "Unlock previous chapter first!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(if (quizUnlocked) Color(0xFFF48FB1) else Color.LightGray, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Psychology, null, tint = Color.White)
                        }
                        Text("QUIZ", fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = if (quizUnlocked) Color(0xFFF48FB1) else Color.LightGray)
                    }
                }
            }
        }
    }
}
