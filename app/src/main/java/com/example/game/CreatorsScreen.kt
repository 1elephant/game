package com.example.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

// Using your signature colors
val PinkBG = Color(0xFFFFF5F7)
val PinkDark = Color(0xFFD81B60)
val PinkMedium = Color(0xFFFFC1CC)

@Composable
fun CreatorsScreen(navController: NavController) {
    val creators = listOf(
        CreatorData("Charvee Chhatwani", R.drawable.charveee), 
        CreatorData("D Rasi Shree", R.drawable.rasi),
        CreatorData("Dishita Gupta", R.drawable.dishita),
        CreatorData("Drishti Yadav", R.drawable.drishtii)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PinkBG)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Text(
                text = "THE CREATORS",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = PinkDark,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Creator List
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(creators) { creator ->
                    CreatorCard(creator)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Back Button in Pixel Style
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .width(200.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(4.dp), 
                colors = ButtonDefaults.buttonColors(containerColor = PinkDark),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    text = "GO BACK",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

@Composable
fun CreatorCard(creator: CreatorData) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        // Shadow Effect
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(85.dp)
                .offset(y = 4.dp, x = 4.dp)
                .background(PinkDark.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
        )
        // Card Content
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(85.dp)
                .background(Color.White, RoundedCornerShape(8.dp))
                .border(2.dp, PinkMedium, RoundedCornerShape(8.dp))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image with Pixel Border
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(PinkDark)
                    .padding(2.dp)
                    .background(Color.White)
            ) {
                Image(
                    painter = painterResource(id = creator.imageRes),
                    contentDescription = creator.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            Text(
                text = creator.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = PinkDark
            )
        }
    }
}

data class CreatorData(val name: String, val imageRes: Int)

@Preview(showBackground = true)
@Composable
fun CreatorsScreenPreview() {
    val navController = rememberNavController()
    CreatorsScreen(navController = navController)
}
