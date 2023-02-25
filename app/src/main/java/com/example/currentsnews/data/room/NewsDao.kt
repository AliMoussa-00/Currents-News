package com.example.currentsnews.data.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Query("SELECT * FROM news WHERE description <> 'null' AND image <>'null' ORDER BY published DESC")
    fun getLatestNewsRoom(): Flow<List<NewsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListNews(listNews: List<NewsEntity>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun bookMarkNews(newsEntity: NewsEntity)
}