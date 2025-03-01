package com.example.projectapp

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.projectapp.navigation.DeepLinkHandler
import com.example.projectapp.ui.MainScreenWithNavHost
import com.example.projectapp.ui.theme.ProjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Process deep link data
        val deepLinkData = DeepLinkHandler.handleIntent(intent)

        setContent {
            var isDarkTheme by remember { mutableStateOf(false) }
            var isEnglish by remember { mutableStateOf(true) }
            ProjectTheme(darkTheme = isDarkTheme) {
                MainScreenWithNavHost(
                    isDarkTheme = isDarkTheme,
                    isEnglish = isEnglish,
                    onThemeToggle = {
                        playButtonSound(this)
                        isDarkTheme = !isDarkTheme
                    },
                    onLanguageToggle = {
                        playButtonSound(this)
                        isEnglish = !isEnglish
                    },
                    deepLinkData = deepLinkData
                )
            }
        }
    }
}

fun playButtonSound(context: Context) {
    val mediaPlayer = MediaPlayer.create(context, R.raw.button)
    mediaPlayer?.start()
    mediaPlayer?.setOnCompletionListener {
        it.release() // Release resources when playback is complete
    }
}