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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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

    LaunchedEffect(Unit) {
        db.collection("users").get()
            .addOnSuccessListener { result ->
                val fetchedUsers = result.documents.map { doc ->
                    
                    // 🚀 AGGRESSIVE DATA PARSING
                    
                    // 1. Parse Boss Scores (Array of any numeric type)
                    val rawBoss = doc.get("bossScores") as? List<*>
                    val bossTotal = rawBoss?.sumOf { 
                        (it as? Number)?.toInt()?.times(3) ?: 0 
                    } ?: 0
                    
                    // 2. Parse Learning Scores (Map of String to any numeric type)
                    val rawLearning = doc.get("learningScores") as? Map<*, *>
                    val learningTotal = rawLearning?.values?.sumOf { 
                        (it as? Number)?.toInt() ?: 0 
                    } ?: 0
                    
                    // 3. Get totalPoints (as final fallback)
                    val storedPoints = (doc.get("totalPoints") as? Number)?.toInt() ?: 0
                    
                    // Combine - use calculation if available, else use stored
                    val total = if (bossTotal + learningTotal > 0) {
                        bossTotal + learningTotal
                    } else {
                        storedPoints
                    }

                    LeaderboardUser(
                        id = doc.id,
                        username = doc.getString("username") ?: doc.getString("name") ?: "Explorer",
                        emoji = doc.getString("emoji") ?: "😀",
                        totalScore = total
                    )
                }.sortedByDescending { it.totalScore }
                
                users = fetchedUsers
                
                currentUserId?.let { uid ->
                    val index = fetchedUsers.indexOfFirst { it.id == uid }
                    if (index != -1) {
                        currentUserRank = index + 1
                        currentUserData = fetchedUsers[index]
                    }
                }
                isLoading = false
            }
            .addOnFailureListener {
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
                Text(
                    text = "LEADER_BOARD",
                    fontFamily = FontFamily.Monospace,
                    color = primaryBlue,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = primaryBlue)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = screenHeight * 0.20f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                
                // Top 5 Row
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

                // Current User Rank Card
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
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "#${currentUserRank}",
                                    color = Color.White,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 20.sp
                                )
                            }
                            
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "Current Points",
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "${user.totalScore} pts",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )
                            }
                        }
                    }
                }

                // Top 5 Rank List
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().weight(1f).padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 30.dp)
                ) {
                    itemsIndexed(users.take(5)) { index, user ->
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
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(Color.White)
                .border(2.5.dp, if (rank == 1) pink else blue, CircleShape)
        ) {
            Text(text = user.emoji, fontSize = (size.value * 0.5).sp)
        }
        Text(text = "#$rank", fontSize = 12.sp, color = blue, fontWeight = FontWeight.ExtraBold)
        Text(text = "${user.totalScore} pts", fontSize = 10.sp, color = Color.Gray)
    }
}

@Composable
fun RankListItem(user: LeaderboardUser, rank: Int, blue: Color, isMe: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth().height(65.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = if (isMe) Color(0xFFFFF0F3) else Color.White),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp).fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "#$rank", 
                fontWeight = FontWeight.Bold, 
                color = if (rank <= 3) Color(0xFFFFC107) else blue,
                modifier = Modifier.width(35.dp)
            )
            
            Box(
                modifier = Modifier.size(36.dp).background(Color(0xFFFDE2E4), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = user.emoji, fontSize = 20.sp)
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Text(
                text = if (isMe) "${user.username} (You)" else user.username, 
                modifier = Modifier.weight(1f), 
                fontWeight = FontWeight.Medium,
                color = if (isMe) Color.Black else Color.DarkGray
            )
            
            Text(
                text = "${user.totalScore} pts", 
                fontWeight = FontWeight.ExtraBold, 
                color = blue
            )
        }
    }
}
