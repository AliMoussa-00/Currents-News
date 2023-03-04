package com.example.currentsnews.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currentsnews.data.NewsRepository
import com.example.currentsnews.data.network.toNewsEntity
import com.example.currentsnews.data.room.NewsEntity
import com.example.currentsnews.data.room.toNews
import com.example.currentsnews.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository,
) : ViewModel() {

    init {
        refreshDatabase()
    }

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

    // ----------------------------------
    // getting latest News
    // ----------------------------------

    val latestNews: StateFlow<List<News>> = repository.getLatestRoom()
        .map { newsEntities ->
            newsEntities.map {
                it.toNews()
            }
        }
        .stateIn(
            scope = viewModelScope,
            initialValue = emptyList(),
            started = SharingStarted.WhileSubscribed(5_000)
        )

fun resetListNews() {
    refreshDatabase()
    deleteNewsInOtherLanguages()
}
private fun deleteNewsInOtherLanguages() {
    viewModelScope.launch {
        try {
            repository.deleteNewsInOtherLanguages()
        } catch (e: IOException) {
            Log.e("TAG", "Deleting news in different languages: ${e.localizedMessage}")
        }
    }
}

// ----------------------------------
// setting screen state: listScreen or webScreen
// ----------------------------------
fun setScreenState(url: String, isWeb: Boolean = false) {
    _uiState.update {
        it.copy(
            screenState = if (isWeb) ScreenState.WebView else ScreenState.List,
            url = url
        )
    }
}

// ----------------------------------
// setting screen type : Home, Search, Saved
// ----------------------------------
fun setScreenType(screenType: ScreenType) {
    _uiState.update {
        it.copy(
            screenType = screenType
        )
    }
}

// ----------------------------------
// setting the search Field
// ----------------------------------
private var _searchText = MutableStateFlow("")
val searchText = _searchText.asStateFlow()

fun onSearchTextChange(text: String) {
    _searchText.value = text
}

fun submitSearch() {
    viewModelScope.launch {
        try {
            val searchNewsNet =
                repository.searchNews(_searchText.value).news.map { it.toNewsEntity() }

            Log.e("TAG","searchNewsNet=${searchNewsNet.size}")
            repository.insertToRoom(searchNewsNet)
        } catch (e: IOException) {
            Log.e("TAG", "Submitting Search ${e.localizedMessage}")
        }
    }
}

fun getSearchedList(): StateFlow<List<News>> {

    return latestNews.map { newsList ->
        newsList.filter {
            it.title.contains(_searchText.value) || it.description.contains(_searchText.value)
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(1_000),
            initialValue = latestNews.value
        )
}

// ----------------------------------
// Setting the category
// ----------------------------------
fun setCategory(filters: Filters) {
    _uiState.update {
        it.copy(
            category = filters
        )
    }
    setNewsByCategory()
}

// ----------------------------------
// Setting & Getting News by Category
// ----------------------------------
private fun setNewsByCategory() {
    viewModelScope.launch {
        try {
            val news: List<NewsEntity> = repository
                .getNewsByCategoryNet(_uiState.value.category.toFilterLowerCase())
                .news
                .map {
                    it.toNewsEntity()
                }
            repository.insertToRoom(news = news)
        } catch (e: IOException) {
            Log.e("TAG", "Getting News by category: ${e.localizedMessage}")
        }
    }
}

fun getNewsByCategory(category: Filters): StateFlow<List<News>> {

    val newsByCategory: StateFlow<List<News>> = latestNews.map { newsList ->
        newsList.filter {
            it.category.contains(category.toFilterLowerCase())
        }
    }
        .stateIn(
            scope = viewModelScope,
            initialValue = emptyList<News>(),
            started = SharingStarted.WhileSubscribed(5_000)
        )


    return newsByCategory
}

// ----------------------------------
// Setting & Getting bookMarkedNews
// ----------------------------------

fun bookMarkNews(news: News) {
    viewModelScope.launch {
        try {
            repository.bookMarkNews(news.toNewsEntity())
        } catch (e: IOException) {
            Log.e("CATCH", "bookMarkingNews: ${e.localizedMessage}")
        }
    }
}

fun getBookMarkedNEws(): StateFlow<List<News>> {
    return latestNews.map { listNews ->
        listNews.filter {
            it.bookMarked
        }
    }
        .stateIn(
            scope = viewModelScope,
            initialValue = emptyList(),
            started = SharingStarted.WhileSubscribed(3_000)
        )
}
}