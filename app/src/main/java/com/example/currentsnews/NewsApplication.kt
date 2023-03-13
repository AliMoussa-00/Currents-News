package com.example.currentsnews

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.currentsnews.privateconstants.THEME_MODE
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltAndroidApp
class NewsApplication  : Application() {

    @Inject
    lateinit var dataStore: DataStore<Preferences>

    companion object {
        var applicationScope = MainScope()
    }

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            applicationScope.launch {
                val mode= dataStore.data
                    .map { preferences ->
                        Log.e("THEME_MODE", "preferences[THEME_MODE]= ${preferences[THEME_MODE]}")
                        preferences[THEME_MODE] ?: AppCompatDelegate.MODE_NIGHT_UNSPECIFIED
                    }

                mode.collect{
                    Log.e("THEME MODE","mode=$it")
                    AppCompatDelegate.setDefaultNightMode(it)
                }
            }
        }
    }
}