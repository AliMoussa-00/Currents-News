package com.example.currentsnews.di

import android.app.Application
import com.example.currentsnews.data.network.NewsApiService
import com.example.currentsnews.data.room.NewsDao
import com.example.currentsnews.data.room.NewsDataBase
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val baseUrl= "https://api.currentsapi.services/v1/"

    @Provides
    @Singleton
    fun provideRetrofitService(): NewsApiService{
        val moshi= Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(baseUrl)
            .build()
            .create(NewsApiService::class.java)

    }

    @Provides
    @Singleton
    fun provideDatabaseDao(application:Application): NewsDao{
        return NewsDataBase.getDatabase(application).newsDao()
    }
}