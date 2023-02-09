package com.example.currentsnews.data.network

import com.example.currentsnews.privateconstants.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("latest-news?apiKey=$API_KEY")
    suspend fun gitLatestNews(): NewsApi

    @GET("search?keywords=keyword&apiKey=$API_KEY")
    suspend fun searchNews(@Query(value = "keyword") keyword: String): NewsApi
}