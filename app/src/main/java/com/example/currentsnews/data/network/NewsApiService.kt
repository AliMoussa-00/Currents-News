package com.example.currentsnews.data.network

import androidx.appcompat.app.AppCompatDelegate
import com.example.currentsnews.privateconstants.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("latest-news?")
    suspend fun gitLatestNews(
        @Query(value = "apiKey") apiKey:String= API_KEY,
        @Query(value= "language")language:String= AppCompatDelegate.getApplicationLocales().toLanguageTags(),
        @Query(value= "page_size") pageSize: Int= 100
    ): NewsApi

    @GET("search?")
    suspend fun getNewsByCategory(
        @Query(value = "apiKey") apiKey:String= API_KEY,
        @Query(value = "myCategory") myCategory:String,
        @Query(value= "language")language:String= AppCompatDelegate.getApplicationLocales().toLanguageTags(),
        @Query(value= "page_size") pageSize: Int= 20
    ): NewsApi

    @GET("search?")
    suspend fun searchNews(
        @Query(value = "apiKey") apiKey:String= API_KEY,
        @Query(value = "keyword") keyword: String,
        @Query(value= "language")language:String= AppCompatDelegate.getApplicationLocales().toLanguageTags(),
        @Query(value= "page_size") pageSize: Int= 20
    ): NewsApi


}