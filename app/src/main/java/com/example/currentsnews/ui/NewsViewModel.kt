package com.example.currentsnews.ui

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currentsnews.data.NewsRepository
import com.example.currentsnews.data.network.toNewsEntity
import com.example.currentsnews.data.room.toNews
import com.example.currentsnews.model.News
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository,
) : ViewModel() {

    val latestNews: StateFlow<List<News>> = repository.getLatestRoom()
        .map { newsEntityList ->
            newsEntityList.map {
                it.toNews()
            }
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = emptyList<News>(),
            started = SharingStarted.WhileSubscribed(5_000)
        )

    var isRefreshing = mutableStateOf(false)
        private set

    init {
        refreshDatabase()
    }

    fun refreshDatabase() {
        isRefreshing.value = true

        viewModelScope.launch {
            try {
                val news = repository.getLatestNewsNet()
                    .news
                    .map {
                        it.toNewsEntity()
                    }

                repository.insertToRoom(news)

            } catch (e: IOException) {
                e.localizedMessage?.let { Log.e("TAG", it) }
            }
        }
        isRefreshing.value = false
    }
}