package com.example.game

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth

// --- THEME PALETTE ---
//private val PinkBG = Color(0xFFFFF5F7)
private val PinkLight = Color(0xFFFFB6C1)
//private val PinkMedium = Color(0xFFFF69B4)
//private val PinkDark = Color(0xFFD81B60)
private val PixelBlue = Color(0xFF7B9ACC)

@Composable
fun HomeScreen(navController: NavController) {
    val scrollState = rememberScrollState()
    var drawerVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PinkBG)
    ) {
        // --- SCROLLING WORLD ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(900.dp) 
            ) {
                // 1. BACKGROUND DECORATIONS
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 20f), 0f)
                    
                    drawLine(
                        color = PinkLight,
                        start = Offset(size.width / 2, 80f),
                        end = Offset(size.width / 2, size.height - 80f),
                        strokeWidth = 4f,
                        pathEffect = pathEffect
                    )

                    val mountainColor = PinkLight.copy(alpha = 0.4f)
                    val mPath = androidx.compose.ui.graphics.Path().apply {
                        moveTo(0f, size.height)
                        lineTo(size.width * 0.15f, size.height - 120f)
                        lineTo(size.width * 0.35f, size.height - 40f)
                        lineTo(size.width * 0.6f, size.height - 160f)
                        lineTo(size.width * 0.85f, size.height - 80f)
                        lineTo(size.width, size.height)
                        close()
                    }
                    drawPath(mPath, color = mountainColor)
                }

                AnimatedBackgroundElements()

                // 2. MAIN CONTENT
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_app_logo),
                        contentDescription = "Logo",
                        modifier = Modifier.size(150.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "KOTIFY",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = PinkDark,
                        letterSpacing = 4.sp
                    )

                    Spacer(modifier = Modifier.height(60.dp))

                    AnimatedHomeButton(
                        text = "LET'S START",
                        onClick = { 
                            navController.navigate(Screen.BossMapScreen.route)
                        }
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))

                    AnimatedHomeButton(
                        text = "CREATORS",
                        onClick = { 
                            navController.navigate(Screen.Creators.route)
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    AnimatedHomeButton(
                        text = "OUR VISION",
                        onClick = { 
                            navController.navigate(Screen.Vision.route)
                        }
                    )
                }
            }
        }

        // --- FIXED HEADER ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(16.dp)
        ) {
            IconButton(
                onClick = { drawerVisible = true },
                modifier = Modifier
                    .background(Color.White.copy(alpha = 0.8f), RoundedCornerShape(8.dp))
                    .size(40.dp)
            ) {
                Icon(Icons.Default.Menu, contentDescription = "Menu", tint = PixelBlue)
            }
        }

        // --- DRAWER ---
        AnimatedVisibility(
            visible = drawerVisible,
            enter = slideInHorizontally(initialOffsetX = { -it }),
            exit = slideOutHorizontally(targetOffsetX = { -it })
        ) {
            Row(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(280.dp)
                        .background(Color.White)
                        .padding(24.dp)
                ) {
                    Column {
                        Text("MENU", fontSize = 24.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = PixelBlue)
                        Spacer(modifier = Modifier.height(32.dp))
                        
                        DrawerItemPixel("Home", true) { 
                            drawerVisible = false 
                        }
                        
                        DrawerItemPixel("Creators", false) {
                            drawerVisible = false
                            navController.navigate(Screen.Creators.route)
                        }
                        
                        DrawerItemPixel("Our Vision", false) {
                            drawerVisible = false
                            navController.navigate(Screen.Vision.route)
                        }
                    }
                }
                Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(0.4f)).clickable { drawerVisible = false })
            }
        }
    }
}

@Composable
fun AnimatedHomeButton(text: String, onClick: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "btn_anim")
    val floatY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -4f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "btn_float"
    )

    Box(
        modifier = Modifier
            .padding(8.dp)
            .width(240.dp)
            .offset(y = floatY.dp)
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .offset(y = 4.dp, x = 4.dp)
                .background(PinkDark.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(PinkLight, RoundedCornerShape(8.dp))
                .border(2.dp, PinkMedium, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
    }
}

@Composable
fun AnimatedBackgroundElements() {
    val infiniteTransition = rememberInfiniteTransition(label = "bg_anim")
    val floatingOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 15f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floating"
    )
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )
    val drift by infiniteTransition.animateFloat(
        initialValue = -20f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "drift"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Text("👻", fontSize = 32.sp, modifier = Modifier.offset(x = 40.dp, y = (250 + floatingOffset).dp).alpha(0.6f))
        Text("✨", fontSize = 24.sp, modifier = Modifier.offset(x = 300.dp, y = 150.dp).scale(scale).alpha(0.7f))
        Text("✨", fontSize = 18.sp, modifier = Modifier.offset(x = 60.dp, y = 550.dp).scale(scale).alpha(0.7f))
        Text("✨", fontSize = 20.sp, modifier = Modifier.offset(x = 320.dp, y = 800.dp).scale(scale).alpha(0.6f))
        Text("☁️", fontSize = 40.sp, modifier = Modifier.offset(x = (280 + drift).dp, y = 750.dp).alpha(0.5f))
        Text("☁️", fontSize = 30.sp, modifier = Modifier.offset(x = (20 + drift).dp, y = 120.dp).alpha(0.5f))
        Text("🌸", fontSize = 20.sp, modifier = Modifier.offset(x = 330.dp, y = 450.dp).alpha(0.6f))
        Text("⭐", fontSize = 16.sp, modifier = Modifier.offset(x = 50.dp, y = 880.dp).alpha(0.6f))
        Text("🍬", fontSize = 18.sp, modifier = Modifier.offset(x = 310.dp, y = 600.dp).alpha(0.5f))
        Text("🍄", fontSize = 22.sp, modifier = Modifier.offset(x = 80.dp, y = 380.dp).alpha(0.6f))
        Text("🧁", fontSize = 20.sp, modifier = Modifier.offset(x = 40.dp, y = 700.dp).alpha(0.5f))
        Text("🎀", fontSize = 24.sp, modifier = Modifier.offset(x = 300.dp, y = 950.dp).alpha(0.6f))
    }
}

@Composable
fun DrawerItemPixel(title: String, selected: Boolean, icon: androidx.compose.ui.graphics.vector.ImageVector? = null, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() }
            .background(if (selected) PinkBG else Color.Transparent, RoundedCornerShape(4.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(icon, contentDescription = null, tint = if (selected) PinkMedium else Color.Gray, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
        }
        Text(title, fontFamily = FontFamily.Monospace, color = if (selected) PinkMedium else Color.Gray)
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    HomeScreen(navController = navController)
}
