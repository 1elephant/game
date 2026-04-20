package com.example.game

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class QuizViewModel : ViewModel() {

    var progress by mutableStateOf(BossProgress())
        private set

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun submitScore(level: Int, score: Int) {
        val updated = progress.scores.toMutableList()

        if (level <= updated.size) {
            updated[level - 1] = maxOf(updated[level - 1], score)
        } else {
            while (updated.size < level - 1) {
                updated.add(0)
            }
            updated.add(score)
        }

        progress = progress.copy(scores = updated)
        saveAllToFirestore()
    }

    var learningScores by mutableStateOf(
        mutableMapOf<Pair<Int, Difficulty>, Int>()
    )
        private set

    fun submitLearningScore(chapter: Int, difficulty: Difficulty, score: Int) {
        val key = chapter to difficulty
        val current = learningScores[key] ?: 0

        learningScores = learningScores.toMutableMap().apply {
            this[key] = maxOf(current, score)
        }
        saveAllToFirestore()
    }

    private fun calculateTotalPoints(): Int {
        // 🎯 Boss levels: 3 points per mark
        val bossTotal = progress.scores.sum() * 3
        
        // 🎯 Learning levels: 1 point per mark
        val learningTotal = learningScores.values.sum()
        
        return bossTotal + learningTotal
    }

    private fun saveAllToFirestore() {
        val uid = auth.currentUser?.uid ?: return
        val totalPoints = calculateTotalPoints()
        
        val learningData = learningScores.mapKeys { "${it.key.first}_${it.key.second}" }
        
        val data = hashMapOf(
            "bossScores" to progress.scores,
            "learningScores" to learningData,
            "totalPoints" to totalPoints
        )

        db.collection("users").document(uid)
            .set(data, SetOptions.merge())
            .addOnFailureListener { e ->
                Log.e("QUIZ_VM", "Error saving data", e)
            }
    }

    fun loadUserData() {
        val uid = auth.currentUser?.uid ?: return
        db.collection("users").document(uid).get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    // 1. Load Boss Scores
                    val bossScores = doc.get("bossScores") as? List<*>
                    if (bossScores != null) {
                        progress = BossProgress(bossScores.filterIsInstance<Number>().map { it.toInt() })
                    }

                    // 2. Load Learning Scores
                    val lScores = doc.get("learningScores") as? Map<*, *>
                    if (lScores != null) {
                        val converted = mutableMapOf<Pair<Int, Difficulty>, Int>()
                        lScores.forEach { (key, value) ->
                            val keyStr = key.toString()
                            val valNum = (value as? Number)?.toInt() ?: 0
                            val parts = keyStr.split("_")
                            if (parts.size == 2) {
                                val chapter = parts[0].toIntOrNull()
                                val difficulty = try { Difficulty.valueOf(parts[1]) } catch (e: Exception) { null }
                                if (chapter != null && difficulty != null) {
                                    converted[chapter to difficulty] = valNum
                                }
                            }
                        }
                        learningScores = converted
                    }
                    
                    // 3. 🚀 CRITICAL FIX: Recalculate total points locally if it's 0 or missing in Firestore
                    val firestorePoints = (doc.get("totalPoints") as? Number)?.toInt() ?: 0
                    val currentCalculated = calculateTotalPoints()
                    
                    if (firestorePoints != currentCalculated && currentCalculated > 0) {
                        saveAllToFirestore() // Sync it back to Firestore correctly
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("QUIZ_VM", "Error loading user data", e)
            }
    }
    
    fun clearData() {
        progress = BossProgress()
        learningScores = mutableMapOf()
    }
}

class AppViewModel : ViewModel() {
    var isSoundOn by mutableStateOf(true)
    var userEmail by mutableStateOf("")
    var userName by mutableStateOf("")
    var userUsername by mutableStateOf("")
    var userEmoji by mutableStateOf("😀")

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun loadProfile(onComplete: (Boolean, String?) -> Unit = { _, _ -> }) {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            onComplete(false, null)
            return
        }
        
        db.collection("users").document(uid).get()
            .addOnSuccessListener { doc ->
                if (doc.exists() && doc.contains("name")) {
                    userName = doc.getString("name") ?: ""
                    userUsername = doc.getString("username") ?: ""
                    userEmail = doc.getString("email") ?: ""
                    userEmoji = doc.getString("emoji") ?: "😀"
                    onComplete(true, null)
                } else {
                    onComplete(false, null)
                }
            }
            .addOnFailureListener { e ->
                Log.e("PROFILE_LOAD", "Failed to load", e)
                onComplete(false, e.message)
            }
    }
    
    fun clearData() {
        userEmail = ""
        userName = ""
        userUsername = ""
        userEmoji = "😀"
    }
}
