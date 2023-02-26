package com.example.currentsnews

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.os.LocaleListCompat
import com.example.currentsnews.ui.screens.settings.NewsLanguage
import com.example.currentsnews.ui.theme.CurrentsNewsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CurrentsNewsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NewsScreen(
                        selectedLanguage = getLanguage(),
                        setLanguage = { setLanguage(it) }
                    )
                }
            }
        }
    }

    private fun setLanguage(language: NewsLanguage){

        val localOptions = mapOf(
            NewsLanguage.En to "en",
            NewsLanguage.Arab to "ar",
            NewsLanguage.Fr to "fr"
        )
        Log.e("TAG","localOptions[language] = ${localOptions[language]}")
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(
                localOptions[language]
            )
        )
    }

    private fun getLanguage(): NewsLanguage {
        val selectedLanguage = AppCompatDelegate.getApplicationLocales()
Log.e("TAG","selectedLanguage=$selectedLanguage")
        return  when (selectedLanguage.toLanguageTags()){
            "ar"-> NewsLanguage.Arab
            "fr" -> NewsLanguage.Fr
            else -> NewsLanguage.En
        }
    }
}

