package com.example.currentsnews.data.datastore

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.currentsnews.privateconstants.DATA_STORE_NAME
import com.example.currentsnews.privateconstants.IS_DARK_THEME
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import okio.IOException
import javax.inject.Inject
import javax.inject.Singleton

// creating dataStore
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)

@Singleton
class DataStoreManager @Inject constructor (@ApplicationContext appContext: Context) {

    private companion object{
        val DAY_NiGHT_THEME = intPreferencesKey(IS_DARK_THEME)
    }

    private val settingsDataStore = appContext.dataStore

    suspend fun saveTheme(mode:Int){
        settingsDataStore.edit {
            it[DAY_NiGHT_THEME] = mode
        }
    }

    val themeMode : Flow<Int> = settingsDataStore.data
        .catch {
            if(it is IOException) {
                Log.e("PREFERENCES DATA STORE", "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[DAY_NiGHT_THEME] ?:  AppCompatDelegate.MODE_NIGHT_UNSPECIFIED
        }
}

