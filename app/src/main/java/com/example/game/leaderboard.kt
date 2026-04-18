package com.example.game

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

data class LeaderboardUser(
    val id: String = "",
    val username: String = "",
    val emoji: String = "😀",
    val totalScore: Int = 0
)

@Composable
fun LeaderboardScreen(
    onBack: () -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
    var users by remember { mutableStateOf<List<LeaderboardUser>>(emptyList()) }
    var currentUserRank by remember { mutableStateOf<Int?>(null) }
    var currentUserData by remember { mutableStateOf<LeaderboardUser?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    val primaryBlue = Color(0xFF7B9ACC)
    val accentPink = Color(0xFFF48FB1)
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    // Fetch and calculate scores
    LaunchedEffect(Unit) {
        db.collection("users").get()
            .addOnSuccessListener { result ->
                val fetchedUsers = result.documents.map { doc ->
                    // 1. Get Boss Scores (Array of numbers)
                    val bossScores = doc.get("bossScores") as? List<*>
                    val bossTotal = bossScores?.filterIsInstance<Number>()?.sumOf { it.toInt() } ?: 0
                    
                    // 2. Get Learning Scores (Map of String to number)
                    val learningScoresMap = doc.get("learningScores") as? Map<*, *>
                    val learningTotal = learningScoresMap?.values?.filterIsInstance<Number>()?.sumOf { it.toInt() } ?: 0
                    
                    val total = bossTotal + learningTotal
                    
                    LeaderboardUser(
                        id = doc.id,
                        username = doc.getString("username") ?: doc.getString("name") ?: "Explorer",
                        emoji = doc.getString("emoji") ?: "😀",
                        totalScore = total
                    )
                }
                // Filter out users with 0 score if you want, or just sort them
                val sortedUsers = fetchedUsers.sortedByDescending { it.totalScore }
                
                users = sortedUsers
                
                // Find current user's rank
                currentUserId?.let { uid ->
                    val index = sortedUsers.indexOfFirst { it.id == uid }
                    if (index != -1) {
                        currentUserRank = index + 1
                        currentUserData = sortedUsers[index]
                    }
                }
                
                isLoading = false
            }
            .addOnFailureListener { e ->
                Log.e("Leaderboard", "Error fetching users", e)
                isLoading = false
            }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFFFCCD5))) {

        // 🔷 HEADER
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight * 0.28f)
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 40.dp)
                    .align(Alignment.TopCenter),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = primaryBlue
                    )
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = "Top Achievers",
                    color = primaryBlue,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // 🧾 CONTENT
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = primaryBlue)
            }
        } else if (users.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No scores yet!", color = primaryBlue, fontWeight = FontWeight.Medium)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = screenHeight * 0.20f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                
                // Top 5 Mini-Profile Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {
                    users.take(5).forEachIndexed { index, user ->
                        LeaderboardCircle(user, index + 1, primaryBlue, accentPink)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Current User Focus Card
                currentUserData?.let { user ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 8.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = primaryBlue),
                        elevation = CardDefaults.cardElevation(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Your Position",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "#${currentUserRank}",
                                    color = Color.White,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 22.sp
                                )
                            }
                            
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(Color.White, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = user.emoji, fontSize = 28.sp)
                            }
                            
                            Spacer(modifier = Modifier.width(12.dp))
                            
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "Total Points",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "${user.totalScore}",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            }
                        }
                    }
                }

                // Full List with Rankings
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(top = 10.dp, bottom = 30.dp)
                ) {
                    itemsIndexed(users) { index, user ->
                        RankListItem(user, index + 1, primaryBlue, user.id == currentUserId)
                    }
                }
            }
        }
    }
}

@Composable
fun LeaderboardCircle(user: LeaderboardUser, rank: Int, blue: Color, pink: Color) {
    val size = if (rank == 1) 70.dp else 55.dp
    val color = if (rank == 1) pink else blue
    
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(Color.White)
                .border(2.5.dp, color, CircleShape)
        ) {
            Text(text = user.emoji, fontSize = (size.value * 0.5).sp)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = user.username.take(7),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray,
            maxLines = 1
        )
        Text(
            text = "#$rank",
            fontSize = 12.sp,
            color = color,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
fun RankListItem(user: LeaderboardUser, rank: Int, blue: Color, isMe: Boolean) {
    val rankColor = when(rank) {
        1 -> Color(0xFFFFD700) // Gold
        2 -> Color(0xFFC0C0C0) // Silver
        3 -> Color(0xFFCD7F32) // Bronze
        else -> blue.copy(alpha = 0.6f)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isMe) Color(0xFFFFF0F3) else Color.White
        ),
        elevation = CardDefaults.cardElevation(if (isMe) 4.dp else 1.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rank Circle
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(rankColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$rank",
                    color = if (rank <= 3) Color.White else Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Emoji
            Text(text = user.emoji, fontSize = 24.sp)
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Name
            Text(
                text = if (isMe) "${user.username} (You)" else user.username,
                fontWeight = if (isMe) FontWeight.Bold else FontWeight.Medium,
                fontSize = 16.sp,
                color = if (isMe) Color.Black else Color.DarkGray,
                modifier = Modifier.weight(1f)
            )
            
            // Score
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${user.totalScore}",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = blue
                )
                Text(
                    text = "pts",
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }
        }
    }
}
