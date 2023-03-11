package com.example.currentsnews.data.room

import androidx.appcompat.app.AppCompatDelegate
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Query("SELECT * FROM news WHERE description <> 'None' AND image <>'None' ORDER BY published DESC")
    fun getLatestNewsRoom(): Flow<List<NewsEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertListNews(listNews: List<NewsEntity>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun bookMarkNews(newsEntity: NewsEntity)

    @Query("DELETE FROM news WHERE language <> :myLanguages AND bookMarked = 0 ")
    suspend fun deleteNewsInOtherLanguage(myLanguages: String= AppCompatDelegate.getApplicationLocales().toLanguageTags())

    @Query("DELETE FROM news WHERE " +
            "julianday(strftime('%Y-%m-%d %H:%M:%S', 'now', 'localtime')) -" +
            " julianday(strftime('%Y-%m-%d %H:%M:%S',substr(published,1,19)))" +
            "> 4 AND bookMarked = 0")
    suspend fun deleteOldNews()
}