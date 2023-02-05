package com.example.currentsnews.di

import com.example.currentsnews.data.DefaultRepository
import com.example.currentsnews.data.NewsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRepository(
        defaultRepository: DefaultRepository
    ): NewsRepository
}