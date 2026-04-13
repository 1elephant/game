package com.example.game
import androidx.compose.material.icons.Icons
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
@Composable
fun ScoreCard1(
    totalQuestions: Int,
    correctAnswers: Int
) {
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

    Box(modifier = Modifier.fillMaxSize()) {

        // 🔷 HEADER (Image + Top Bar together)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight*0.3f)
        ) {

            // 🖼️ Background Image
            Image(
                painter = painterResource(id = R.drawable.pink2),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )

            // 🌫️ Light pink overlay
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color(0x66FAD2E1))
            )

            // 🔝 Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 20.dp)
                    .align(Alignment.TopCenter),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = null,
                        tint = Color(0xFF7B9ACC)
                    )
                }

                Text(
                    text = "Score Card",
                    color = Color(0xFF7B9ACC),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )

                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = null,
                        tint = Color(0xFF7B9ACC)
                    )
                }
            }
        }

        // 🧾 MAIN CONTENT
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 180.dp),
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
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF48FB1)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ⭐ Stars
            Row {
                repeat(3) { index ->
                    Icon(
                        imageVector = if (index < stars)
                            Icons.Default.Star
                        else
                            Icons.Default.StarBorder,
                        contentDescription = null,
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 📊 Stats
            Row(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem("Correct", correctAnswers.toString())
                StatItem("Wrong", (totalQuestions - correctAnswers).toString())
                StatItem("Total", totalQuestions.toString())
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 💬 Message
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFCF8F8)),
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = if (percentage >= 80) "Excellent work!"
                    else if (percentage >= 50) "Good job!"
                    else "Keep practicing!",
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp
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
                fontWeight = FontWeight.Bold
            )
            Text(
                text = title,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}
@Composable
fun ChapterCardCoastal0(
    level: Int,
    title: String,
    description: String,
    imageRes: Int,
    progress: BossProgress,
    navController: NavController
) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }

    // ✅ Prevent double navigation crash
    var isNavigating by remember { mutableStateOf(false) }

    val quizUnlocked = progress.isLevelUnlocked(level)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp)
            .shadow(10.dp, RoundedCornerShape(20.dp))
            .clickable { expanded = !expanded }
            .animateContentSize(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF5F7FB)
        )
    ) {

        Row {
            Box {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = null,
                    modifier = Modifier
                        .size(width = 120.dp, height = 145.dp)
                        .clip(RoundedCornerShape(5.dp)),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .background(Color(0xFF7B9ACC), CircleShape)
                        .size(45.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = level.toString().padStart(2, '0'),
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 5.dp)
            ) {

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2D3A4A)
                )

                Text(
                    text = description,
                    fontSize = 9.sp,
                    color = Color.Gray,
                    lineHeight = 12.sp,
                    maxLines = 2
                )

                if (expanded) {

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        Spacer(modifier = Modifier.width(75.dp))

                        // ▶ Levels Button
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {

                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(Color(0xFF7B9ACC), CircleShape)
                                    .clickable {
                                        if (!isNavigating) {
                                            isNavigating = true
                                            navController.navigate(
                                                Screen.Difficulty.createRoute(level)
                                            ) {
                                                launchSingleTop = true
                                            }
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }

                            Text(
                                "Levels",
                                fontSize = 10.sp,
                                color = Color(0xFF7B9ACC)
                            )
                        }

                        // 🧠 Quiz Button
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {

                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(
                                        if (quizUnlocked) Color(0xFFBFA2DB)
                                        else Color.LightGray,
                                        CircleShape
                                    )
                                    .clickable {
                                        if (!isNavigating) {
                                            if (quizUnlocked) {
                                                isNavigating = true
                                                navController.navigate(
                                                    Screen.QuizMain.createRoute(level)
                                                ) {
                                                    launchSingleTop = true
                                                }
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    "Complete previous chapter first",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Psychology,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }

                            Text(
                                "Quiz",
                                fontSize = 10.sp,
                                color = Color(0xFFBFA2DB)
                            )
                        }
                    }
                }
            }
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
            .height(screenHeight * 0.22f) // ✅ responsive height
            .padding(vertical = 15.dp)
            .shadow(10.dp, RoundedCornerShape(20.dp))
            .clickable { showIcons = !showIcons }, // ✅ only toggle visibility
        shape = RoundedCornerShape(5.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF5F7FB)
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
//                .padding(10.dp)
        ) {

            // 📷 IMAGE + LEVEL
            Box {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = null,
                    modifier = Modifier
                        .width(120.dp)
                        .fillMaxHeight() // ✅ match card height
                        .clip(RoundedCornerShape(5.dp)),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
//                        .padding(6.dp)
                        .size(45.dp)
                        .background(Color(0xFF7B9ACC), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = level.toString().padStart(2, '0'),
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            // 📄 TEXT + ICONS
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(vertical=6.dp,horizontal=9.dp),
                verticalArrangement = Arrangement.SpaceBetween // ✅ key fix
            ) {

                // 🔹 Title + Description
                Column {
                    Text(
                        text = title,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF2D3A4A)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = description,
                        fontSize = 10.sp,
                        color = Color.Gray,
                        lineHeight = 12.sp,
                        maxLines = 2
                    )
                }

                // 🔻 ICONS (fixed bottom, no shifting)
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal=9.dp),
                    horizontalArrangement = Arrangement.End
                ) {

                    // ▶ Levels
                    AnimatedVisibility(visible = showIcons) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {

                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(Color(0xFF7B9ACC), CircleShape)
                                    .clickable {
                                        if (!isNavigating) {
                                            isNavigating = true
                                            navController.navigate(
                                                Screen.Difficulty.createRoute(level)
                                            ) {
                                                launchSingleTop = true
                                            }
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }

                            Text(
                                "Levels",
                                fontSize = 10.sp,
                                color = Color(0xFF7B9ACC)
                            )
                        }
                    }
                    Spacer(modifier=Modifier.width(9.dp))

                    // 🧠 Quiz
                    AnimatedVisibility(visible = showIcons) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {

                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(
                                        if (quizUnlocked) Color(0xFFBFA2DB)
                                        else Color.LightGray,
                                        CircleShape
                                    )
                                    .clickable {
                                        if (!isNavigating) {
                                            if (quizUnlocked) {
                                                isNavigating = true
                                                navController.navigate(
                                                    Screen.QuizMain.createRoute(level)
                                                ) {
                                                    launchSingleTop = true
                                                }
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    "Complete previous chapter first",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Psychology,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }

                            Text(
                                "Quiz",
                                fontSize = 10.sp,
                                color = Color(0xFFBFA2DB)
                            )
                        }
                    }
                }
            }
        }
    }
}