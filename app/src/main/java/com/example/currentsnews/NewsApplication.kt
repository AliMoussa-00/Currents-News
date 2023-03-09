package com.example.currentsnews

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.MainScope
import javax.inject.Inject


@HiltAndroidApp
class NewsApplication : Application() {

    @Inject
    lateinit var dataStore: DataStore<Preferences>

    companion object {
        var applicationScope = MainScope()
    }

    override fun onCreate() {
        super.onCreate()

       /* if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
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
        }*/
    }
}