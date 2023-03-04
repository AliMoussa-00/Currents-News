package com.example.currentsnews.data

import com.example.currentsnews.data.network.NewsApi
import com.example.currentsnews.data.network.NewsApiService
import com.example.currentsnews.data.room.NewsDao
import com.example.currentsnews.data.room.NewsEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface NewsRepository {
    suspend fun getLatestNewsNet(): NewsApi
    suspend fun getNewsByCategoryNet(category: String): NewsApi
    suspend fun searchNews(keyword:String): NewsApi
    fun getLatestRoom(): Flow<List<NewsEntity>>
    suspend fun insertToRoom(news: List<NewsEntity>)
    suspend fun bookMarkNews(newsEntity: NewsEntity)
    suspend fun deleteNewsInOtherLanguages()
}

class DefaultRepository @Inject constructor(
    private val apiService: NewsApiService,
    private val newsDao : NewsDao
): NewsRepository{
    override suspend fun getLatestNewsNet(): NewsApi = apiService.gitLatestNews()
    override suspend fun getNewsByCategoryNet(category: String): NewsApi = apiService.getNewsByCategory(myCategory = category)

    override suspend fun searchNews(keyword: String) = apiService.searchNews(keyword = keyword)

    override fun getLatestRoom(): Flow<List<NewsEntity>> =  newsDao.getLatestNewsRoom()


    override suspend fun insertToRoom(news: List<NewsEntity>) = newsDao.insertListNews(news)

    override suspend fun bookMarkNews(newsEntity: NewsEntity) = newsDao.bookMarkNews(newsEntity)

    override suspend fun deleteNewsInOtherLanguages() = newsDao.deleteNewsInOtherLanguage()
}