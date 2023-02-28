package com.example.currentsnews.data.network

import androidx.appcompat.app.AppCompatDelegate
import com.example.currentsnews.privateconstants.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("latest-news?")
    suspend fun gitLatestNews(
        @Query(value = "apiKey") apiKey:String= API_KEY,
        @Query(value= "language")language:String= AppCompatDelegate.getApplicationLocales().toLanguageTags()
    ): NewsApi

    @GET("search?category=myCategory&apiKey=$API_KEY")
    suspend fun getNewsByCategory(
        @Query(value = "myCategory") myCategory:String
    ): NewsApi

    @GET("search?keywords=keyword&apiKey=$API_KEY")
    suspend fun searchNews(@Query(value = "keyword") keyword: String): NewsApi


}