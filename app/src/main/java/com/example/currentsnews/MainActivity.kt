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
import com.example.currentsnews.ui.theme.CurrentsNewsTheme
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val ui = AppCompatDelegate.getDefaultNightMode()

       Log.e("TAG","AppCompatDelegate.getDefaultNightMode() = $ui")

        super.onCreate(savedInstanceState)

        setContent {
            CurrentsNewsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NewsScreen()
                }
            }
        }
    }
}

