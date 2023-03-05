package com.example.currentsnews.di

import android.app.Application
import com.example.currentsnews.data.network.NewsApiService
import com.example.currentsnews.data.room.NewsDao
import com.example.currentsnews.data.room.NewsDataBase
import com.example.currentsnews.privateconstants.BASE_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    val okHttp = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS) // set connection timeout
        .readTimeout(30, TimeUnit.SECONDS) // set read timeout
        .writeTimeout(30, TimeUnit.SECONDS) // set write timeout
        .build()

    @Provides
    @Singleton
    fun provideRetrofitService(): NewsApiService{
        val moshi= Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BASE_URL)
            .client(okHttp)
            .build()
            .create(NewsApiService::class.java)

    }

    @Provides
    @Singleton
    fun provideDatabaseDao(application:Application): NewsDao{
        return NewsDataBase.getDatabase(application).newsDao()
    }
}