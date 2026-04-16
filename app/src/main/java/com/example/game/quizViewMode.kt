package com.example.game


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
class QuizViewModel : ViewModel() {

    var progress by mutableStateOf(BossProgress())
        private set
    fun submitScore(level: Int, score: Int) {
        val updated = progress.scores.toMutableList()

        if (level <= updated.size) {
            updated[level - 1] = maxOf(updated[level - 1], score)
        } else {
            updated.add(score) // ✅ ONLY add when actually completed
        }

        progress = progress.copy(scores = updated)
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
    }
}
class AppViewModel : ViewModel() {
    var isSoundOn by mutableStateOf(true)
}