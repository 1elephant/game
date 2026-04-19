package com.example.game

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillNode
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalAutofill
import androidx.compose.ui.platform.LocalAutofillTree
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    onContinue: (email: String, password: String) -> Unit
){
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val primaryBlue = Color(0xFF7B9ACC)
    val accentPink = Color(0xFFF48FB1)

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    val autofill = LocalAutofill.current
    val autofillTree = LocalAutofillTree.current

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFFFCCD5))) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight * 0.3f)
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
                text = "Welcome",
                color = primaryBlue,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = screenHeight * 0.45f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            val emailAutofillNode = AutofillNode(
                autofillTypes = listOf(AutofillType.EmailAddress),
                onFill = { email = it }
            )
            autofillTree += emailAutofillNode

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .onGloballyPositioned { emailAutofillNode.boundingBox = it.localBoundingBoxOf(it) }
                    .onFocusChanged { autofill?.run { if (it.isFocused) requestAutofillForNode(emailAutofillNode) else cancelAutofillForNode(emailAutofillNode) } },
                colors = fieldColors(primaryBlue, accentPink),
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(12.dp))

            val passwordAutofillNode = AutofillNode(
                autofillTypes = listOf(AutofillType.Password),
                onFill = { password = it }
            )
            autofillTree += passwordAutofillNode

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .onGloballyPositioned { passwordAutofillNode.boundingBox = it.localBoundingBoxOf(it) }
                    .onFocusChanged { autofill?.run { if (it.isFocused) requestAutofillForNode(passwordAutofillNode) else cancelAutofillForNode(passwordAutofillNode) } },
                colors = fieldColors(primaryBlue, accentPink),
                shape = RoundedCornerShape(16.dp),
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (email.isNotBlank() && password.isNotBlank()) {
                        isLoading = true
                        
                        val cleanEmail = email.trim()
                        val cleanPassword = password.trim()

                        auth.signInWithEmailAndPassword(cleanEmail, cleanPassword)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    isLoading = false
                                    onContinue(cleanEmail, cleanPassword)
                                } else {
                                    // 🚀 Create account AND placeholder doc immediately
                                    auth.createUserWithEmailAndPassword(cleanEmail, cleanPassword)
                                        .addOnCompleteListener { createTask ->
                                            if (createTask.isSuccessful) {
                                                val uid = createTask.result?.user?.uid
                                                if (uid != null) {
                                                    val placeholder = hashMapOf(
                                                        "email" to cleanEmail,
                                                        "username" to "Explorer",
                                                        "emoji" to "😀",
                                                        "bossScores" to listOf<Int>(),
                                                        "learningScores" to mapOf<String, Int>()
                                                    )
                                                    db.collection("users").document(uid).set(placeholder)
                                                        .addOnSuccessListener {
                                                            isLoading = false
                                                            onContinue(cleanEmail, cleanPassword)
                                                        }
                                                        .addOnFailureListener {
                                                            isLoading = false
                                                            // If DB fails, we still let them in (will sync later)
                                                            onContinue(cleanEmail, cleanPassword)
                                                        }
                                                } else {
                                                    isLoading = false
                                                    onContinue(cleanEmail, cleanPassword)
                                                }
                                            } else {
                                                isLoading = false
                                                val errorMsg = createTask.exception?.message ?: "Auth Failed"
                                                Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                                            }
                                        }
                                }
                            }
                    } else {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                shape = RoundedCornerShape(16.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Continue", color = Color.White)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen { _, _ -> }
}
