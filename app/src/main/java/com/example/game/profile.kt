package com.example.game

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ProfileSetupScreen(
    appViewModel: AppViewModel,
    onSave: () -> Unit
){
    val db = FirebaseFirestore.getInstance()
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    var name = appViewModel.userName
    var username = appViewModel.userUsername
    var email = appViewModel.userEmail
    var emoji = appViewModel.userEmoji

    val primaryBlue = Color(0xFF7B9ACC)
    val accentPink = Color(0xFFF48FB1)

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    Box(modifier = Modifier.fillMaxSize().background(color=Color(0xFFFFCCD5))) {

        // 🔷 HEADER (ScoreCard style)
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

            Text(
                text = "Profile",
                color = primaryBlue,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // 🧾 CONTENT
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 180.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 🔵 Avatar
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(4.dp, accentPink, CircleShape)
            ) {
                Text(
                    text = emoji,
                    fontSize = 42.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                "Choose your avatar",
                color = primaryBlue,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // ✨ Emoji Input
            OutlinedTextField(
                value = emoji,
                onValueChange = {
                    appViewModel.userEmoji = it.takeLast(2)
                },
                singleLine = true,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.width(120.dp),
                textStyle = TextStyle(
                    fontSize = 26.sp,
                    textAlign = TextAlign.Center
                ),
                placeholder = { Text("😀") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedContainerColor = Color(0xFFFCF8F8),
                    unfocusedContainerColor = Color(0xFFFCF8F8),
                    focusedBorderColor = primaryBlue,
                    unfocusedBorderColor = accentPink,
                    cursorColor = primaryBlue
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 👤 Name
            OutlinedTextField(
                value = name,
                onValueChange = { appViewModel.userName = it },
                label = { Text("Full Name") },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                colors = fieldColors(primaryBlue, accentPink)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 🧑 Username
            OutlinedTextField(
                value = username,
                onValueChange = { appViewModel.userUsername= it },
                label = { Text("Username") },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                colors = fieldColors(primaryBlue, accentPink)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 📧 Email
            OutlinedTextField(
                value = email,
                onValueChange = { appViewModel.userEmail = it },
                label = { Text("Email") },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                colors = fieldColors(primaryBlue, accentPink)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 💾 Save Button
            Button(
                onClick = {

                    if (
                        appViewModel.userName.isNotBlank() &&
                        appViewModel.userUsername.isNotBlank() &&
                        appViewModel.userEmail.isNotBlank()
                    ) {

                        val db = FirebaseFirestore.getInstance()
                        val userId = FirebaseAuth.getInstance().currentUser?.uid

                        if (userId != null) {

                            val userMap = hashMapOf(
                                "name" to appViewModel.userName,
                                "username" to appViewModel.userUsername,
                                "email" to appViewModel.userEmail,
                                "emoji" to appViewModel.userEmoji
                            )

                            db.collection("users")
                                .document(userId)
                                .set(userMap)
                                .addOnSuccessListener {
                                    onSave()
                                }
                                .addOnFailureListener {
                                    Log.e("FIRESTORE", "Error saving", it)
                                }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Save & Continue", color = Color.White)
            }
        }
    }
}

// 🔥 Reusable field colors
@Composable
fun fieldColors(primaryBlue: Color, accentPink: Color) =
    OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.DarkGray,
        focusedContainerColor = Color(0xFFFCF8F8),
        unfocusedContainerColor = Color(0xFFFCF8F8),
        focusedBorderColor = primaryBlue,
        unfocusedBorderColor = accentPink,
        cursorColor = primaryBlue
    )

//@Preview(showBackground = true)
//@Composable
//fun PreviewProfileSetup() {
//    ProfileSetupScreen { _, _, _, _ -> }
//}