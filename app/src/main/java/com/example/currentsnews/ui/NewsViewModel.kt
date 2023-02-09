package com.example.currentsnews.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currentsnews.data.NewsRepository
import com.example.currentsnews.data.network.toNews
import com.example.currentsnews.data.network.toNewsEntity
import com.example.currentsnews.data.room.toNews
import com.example.currentsnews.model.News
import com.example.currentsnews.model.ScreenState
import com.example.currentsnews.model.ScreenType
import com.example.currentsnews.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository,
) : ViewModel() {

    init {
        refreshDatabase()
    }

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

    private var _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> get() = _uiState

    private fun refreshDatabase() {
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
    }

    fun setScreenState(url: String, isWeb: Boolean = false) {
        _uiState.update {
            it.copy(
                screenState = if (isWeb) ScreenState.WebView else ScreenState.List,
                url = url
            )
        }
    }

    fun setScreenType(screenType: ScreenType) {
        _uiState.update {
            it.copy(
                screenType = screenType
            )
        }
    }

    private var _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    private var _searchedNews = MutableStateFlow(listOf<News>())
    val searchedNews = _searchedNews.asStateFlow()

    fun submitSearch() {
        _searchedNews.value = emptyList()
        viewModelScope.launch {
            try{
                _searchedNews.value = repository.searchNews(_searchText.value).news.map { it.toNews() }
            }
            catch (e: SocketTimeoutException){
                Log.e("TAG","${e.localizedMessage}")
            }
        }
    }

}