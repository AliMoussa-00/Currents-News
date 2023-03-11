package com.example.currentsnews.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.currentsnews.data.network.NewsApi
import com.example.currentsnews.data.network.NewsApiService
import com.example.currentsnews.data.room.NewsDao
import com.example.currentsnews.data.room.NewsEntity
import com.example.currentsnews.privateconstants.THEME_MODE
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface NewsRepository {
    suspend fun getLatestNewsNet(): NewsApi
    suspend fun getNewsByCategoryNet(category: String): NewsApi
    suspend fun searchNews(keyword: String): NewsApi
    fun getLatestRoom(): Flow<List<NewsEntity>>
    suspend fun insertToRoom(news: List<NewsEntity>)
    suspend fun bookMarkNews(newsEntity: NewsEntity)
    suspend fun deleteNewsInOtherLanguages()
    suspend fun storeTheme(themeMode: Int)
    suspend fun deleteOldNews()
}

class DefaultRepository @Inject constructor(
    private val apiService: NewsApiService,
    private val newsDao: NewsDao,
    private val dataStore: DataStore<Preferences>,
) : NewsRepository {
    override suspend fun getLatestNewsNet(): NewsApi = apiService.gitLatestNews()
    override suspend fun getNewsByCategoryNet(category: String): NewsApi =
        apiService.getNewsByCategory(myCategory = category)

    override suspend fun searchNews(keyword: String) = apiService.searchNews(keyword = keyword)

    override fun getLatestRoom(): Flow<List<NewsEntity>> = newsDao.getLatestNewsRoom()

    override suspend fun insertToRoom(news: List<NewsEntity>) = newsDao.insertListNews(news)

    override suspend fun bookMarkNews(newsEntity: NewsEntity) = newsDao.bookMarkNews(newsEntity)

    override suspend fun deleteNewsInOtherLanguages() = newsDao.deleteNewsInOtherLanguage()

    override suspend fun storeTheme(themeMode: Int) {
        dataStore
            .edit { preferences ->
                preferences[THEME_MODE] = themeMode
            }
    }

    override suspend fun deleteOldNews() = newsDao.deleteOldNews()
}