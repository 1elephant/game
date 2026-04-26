package com.example.game
import android.R.attr.fontFamily
import android.media.MediaPlayer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun BossMapScreen(
    appViewModel:AppViewModel,
    quizViewModel: QuizViewModel, // Added quizViewModel
    progress: BossProgress,
    navController: NavController
) {
    val isSoundOn = appViewModel.isSoundOn
    val scrollState = rememberScrollState()
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    var drawerVisible by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf("Chapters") }

    MusicPlayer(isSoundOn,R.raw.sapphire)

    val titles = listOf(
        "Introduction",
        "Basic Concepts",
        "Advanced Techniques",
        "Final Review"
    )

    val descriptions = listOf(
        "Get started with the basics begin learning",
        "Learn the fundamental concepts here.",
        "Deep dive into advanced techniques.",
        "Test your knowledge and review everything."
    )

    val img = listOf(
        R.drawable.pink1,
        R.drawable.pink2,
        R.drawable.pink3,
        R.drawable.pink1
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFfde2e4))
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {

            // 🔷 HEADER
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight * 0.1f)
                    .background(Color(0xFFfad2e1)),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "CHAPTERS",
                    fontSize = 18.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF7B9ACC)
                )

                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 16.dp)
                        .clickable { drawerVisible = true },
                    tint = Color(0xFF7B9ACC)
                )
                Icon(
                    imageVector = if (isSoundOn)
                        Icons.Default.VolumeUp
                    else
                        Icons.Default.VolumeOff,
                    contentDescription = "Sound Toggle",
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 16.dp)
                        .clickable {
                            appViewModel.isSoundOn = !appViewModel.isSoundOn
                        },
                    tint = Color(0xFF7B9ACC),

                    )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 📚 CARDS
            Column(modifier = Modifier.padding(horizontal = 8.dp)) {

                val totalItems = minOf(titles.size, descriptions.size, img.size)

                repeat(totalItems) { index ->

                    val level = index + 1

                    ChapterCardCoastal(
                        level = level,
                        title = titles[index],
                        description = descriptions[index],
                        imageRes = img[index],
                        progress = progress,
                        navController = navController
                    )
                }
            }
        }

        // 🌟 DRAWER
        AnimatedVisibility(
            visible = drawerVisible,
            enter = slideInHorizontally(initialOffsetX = { -it }),
            exit = slideOutHorizontally(targetOffsetX = { -it })
        ) {
            Row {

                // Drawer panel
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(280.dp)
                        .background(Color.White)
                        .padding(vertical = 24.dp)
                ) {

                    Column(modifier = Modifier.fillMaxSize()) {

                        Text(
                            text = "MENU",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        DrawerItem(
                            title = "Home",
                            selected = selectedItem == "Home",
                            icon = Icons.Default.Home,
                            onClick = {
                                selectedItem = "Home"
                                drawerVisible = false
                                navController.navigate("home_screen")
                            }
                        )

                        DrawerItem(
                            title = "Chapters",
                            icon = Icons.Default.MenuBook,
                            selected = selectedItem == "Chapters",
                            onClick = {
                                selectedItem = "Chapters"
                                drawerVisible = false
                            }
                        )

                        DrawerItem(
                            title = "Profile",
                            icon= Icons.Default.Person,
                            selected = false,
                            onClick = {
                                drawerVisible = false
                                navController.navigate("profile_setup")
                            }
                        )

                        DrawerItem(
                            title = "LeaderBoard",
                            selected = false,
                            icon = Icons.Default.Leaderboard,
                            onClick = {
                                drawerVisible = false
                                navController.navigate(Screen.Leaderboard.route)
                            }
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        DrawerItem(
                            title = "Logout",
                            selected = false,
                            icon = Icons.Default.Logout,
                            onClick = {
                                FirebaseAuth.getInstance().signOut()
                                // ✅ Clear data on logout
                                appViewModel.clearData()
                                quizViewModel.clearData()
                                drawerVisible = false
                                navController.navigate(Screen.Login.route) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        )
                    }
                }

                // Overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.4f))
                        .clickable { drawerVisible = false }
                )
            }
        }
    }
}
@Composable
fun DrawerItem(
    title: String,
    selected: Boolean,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .background(
                color = if (selected) Color(0xFFfad2e1) else Color.Transparent,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (selected) Color(0xFF7B9ACC) else Color.DarkGray,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
            }
            Text(
                text = title,
                fontSize = 16.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                color = if (selected) Color(0xFF7B9ACC) else Color.DarkGray
            )
        }
    }
}
