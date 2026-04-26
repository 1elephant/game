package com.example.game

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// Pixel Art Palette
val PixelPinkLight = Color(0xFFFFF5F7)
val PixelPinkMedium = Color(0xFFFFB6C1)
val PixelPinkDark = Color(0xFFFF69B4)
val PixelBorder = Color(0xFFD81B60)
val PixelShadow = Color(0xFF880E4F)

@Composable
fun PixelButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    Box(
        modifier = modifier
            .padding(4.dp)
            .background(PixelShadow)
            .padding(bottom = 4.dp, end = 4.dp)
            .background(PixelBorder)
            .padding(2.dp)
            .background(if (isLoading) Color.Gray else PixelPinkMedium)
            .clickable(enabled = !isLoading, onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
        } else {
            Text(
                text = text,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}

@Composable
fun LoginScreen(
    onContinue: (email: String, password: String) -> Unit
) {
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PixelPinkLight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Pixel Art Logo
            Image(
                painter = painterResource(id = R.drawable.ic_app_logo),
                contentDescription = "Logo",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "KOTIFY",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = PixelPinkDark
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Pixel-style Window/Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PixelShadow)
                    .padding(bottom = 4.dp, end = 4.dp)
                    .background(PixelBorder)
                    .padding(2.dp)
                    .background(Color.White)
                    .padding(20.dp)
            ) {
                Column {
                    Text(
                        text = "LOGIN",
                        fontSize = 20.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = PixelBorder
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Retro Input Fields
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("email", fontFamily = FontFamily.Monospace) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PixelPinkDark,
                            unfocusedBorderColor = PixelBorder
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("password", fontFamily = FontFamily.Monospace) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PixelPinkDark,
                            unfocusedBorderColor = PixelBorder
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    PixelButton(
                        text = "CONTINUE",
                        isLoading = isLoading,
                        onClick = {
                            if (email.isNotBlank() && password.isNotBlank()) {
                                isLoading = true
                                val cleanEmail = email.trim()
                                val cleanPassword = password.trim()

                                // 🚀 Updated Logic: Always try to Log In, if fails, Always try to Sign Up.
                                auth.signInWithEmailAndPassword(cleanEmail, cleanPassword)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            isLoading = false
                                            onContinue(cleanEmail, cleanPassword)
                                        } else {
                                            // Account might not exist, so just create it!
                                            auth.createUserWithEmailAndPassword(cleanEmail, cleanPassword)
                                                .addOnCompleteListener { createTask ->
                                                    isLoading = false
                                                    if (createTask.isSuccessful) {
                                                        onContinue(cleanEmail, cleanPassword)
                                                    } else {
                                                        val error = createTask.exception?.message ?: "Auth Error"
                                                        if (error.contains("already in use", ignoreCase = true)) {
                                                            Toast.makeText(context, "Wrong password for this email!", Toast.LENGTH_SHORT).show()
                                                        } else {
                                                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                                                        }
                                                    }
                                                }
                                        }
                                    }
                            } else {
                                Toast.makeText(context, "Fill all fields!", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
