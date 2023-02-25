package com.example.currentsnews.data.network

import com.example.currentsnews.privateconstants.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("latest-news?language=en&apiKey=$API_KEY")
    suspend fun gitLatestNews(): NewsApi

    @GET("search?category=myCategory&page_size=200&apiKey=$API_KEY")
    suspend fun getNewsByCategory(@Query(value = "myCategory") myCategory:String): NewsApi

    @GET("search?keywords=keyword&page_size=200&apiKey=$API_KEY")
    suspend fun searchNews(@Query(value = "keyword") keyword: String): NewsApi


}