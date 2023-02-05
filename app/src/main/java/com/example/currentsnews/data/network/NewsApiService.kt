package com.example.currentsnews.data.network

import com.example.currentsnews.privateconstants.API_KEY
import retrofit2.http.GET

interface NewsApiService {
    @GET("latest-news?apiKey=$API_KEY")
    suspend fun gitLatestNews(): NewsApi
}