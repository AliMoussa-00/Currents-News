package com.example.currentsnews.data

import com.example.currentsnews.data.network.NewsApiService
import com.example.currentsnews.data.network.NewsNetwork
import com.example.currentsnews.data.room.NewsDao
import com.example.currentsnews.data.room.NewsEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface NewsRepository {
    suspend fun getLatestNewsNet(): List<NewsNetwork>
    fun getLatestRoom(): Flow<List<NewsEntity>>
    suspend fun insertToRoom(news: List<NewsEntity>)
}

class DefaultRepository @Inject constructor(
    private val apiService: NewsApiService,
    private val newsDao : NewsDao
): NewsRepository{
    override suspend fun getLatestNewsNet(): List<NewsNetwork> = apiService.gitLatestNews()

    override fun getLatestRoom(): Flow<List<NewsEntity>> = newsDao.getLatestNewsRoom()

    override suspend fun insertToRoom(news: List<NewsEntity>) = newsDao.insertListNews(news)

}