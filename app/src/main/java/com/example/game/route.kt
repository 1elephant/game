package com.example.game


sealed class Screen(val route: String) {

    object BossMapScreen : Screen("boss_map")
    object SplashScreen : Screen("splash")
    object Login:Screen("login")
    object Leaderboard:Screen("leaderboard")

    object QuizMain : Screen("quiz_main/{level}") {
        fun createRoute(level: Int) = "quiz_main/$level"
    }

    object LearningQuiz : Screen("quiz/{chapter}/{difficulty}") {
        fun createRoute(chapter: Int, difficulty: String) =
            "quiz/$chapter/$difficulty"
    }

    object Difficulty : Screen("difficulty/{chapter}") {
        fun createRoute(chapter: Int) = "difficulty/$chapter"
    }
}