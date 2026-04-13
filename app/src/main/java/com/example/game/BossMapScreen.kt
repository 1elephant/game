package com.example.game
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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.text.font.FontWeight

@Composable
fun BossMapScreen0(
    progress: BossProgress,
    navController: NavController
) {

    val scrollState = rememberScrollState()
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

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
                    text = "Chapters",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF7B9ACC)
                )

                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 16.dp),
                    tint = Color(0xFF7B9ACC)
                )

                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 16.dp),
                    tint = Color(0xFF7B9ACC)
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
    }
}
@Composable
fun BossMapScreen(
    progress: BossProgress,
    navController: NavController
) {

    val scrollState = rememberScrollState()
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    var drawerVisible by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf("Chapters") }

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
                    text = "Chapters",
                    fontSize = 18.sp,
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
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 16.dp),
                    tint = Color(0xFF7B9ACC)
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

                    Column {

                        Text(
                            text = "Menu",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        DrawerItem(
                            title = "Home",
                            selected = selectedItem == "Home",
                            onClick = {
                                selectedItem = "Home"
                                drawerVisible = false
                            }
                        )

                        DrawerItem(
                            title = "Chapters",
                            selected = selectedItem == "Chapters",
                            onClick = {
                                selectedItem = "Chapters"
                                drawerVisible = false
                            }
                        )

                        DrawerItem(
                            title = "Settings",
                            selected = selectedItem == "Settings",
                            onClick = {
                                selectedItem = "Settings"
                                drawerVisible = false
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
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = if (selected) Color(0xFF7B9ACC) else Color.DarkGray
        )
    }
}