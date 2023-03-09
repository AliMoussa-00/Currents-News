package com.example.currentsnews.ui

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currentsnews.data.NewsRepository
import com.example.currentsnews.data.network.toNews
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

    private var _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private fun refreshDatabase() {
        viewModelScope.launch {
            try {
                val news = repository.getLatestNewsNet()
                    .news
                    .map {
                        it.toNewsEntity()
                    }

                if (news.isEmpty()) {
                    _isLoading.value = false
                }

                repository.insertToRoom(news)

            } catch (e: IOException) {
                e.localizedMessage?.let { Log.e("TAG", "Refreshing $it") }
                _isLoading.value = false
            }
        }
    }

    private val allNews = repository.getLatestRoom()
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

    // ----------------------------------
    // getting latest News
    // ----------------------------------

    val latestNews: StateFlow<List<News>> = allNews
        .map { newsList ->
            newsList.filter {
                it.language == AppCompatDelegate.getApplicationLocales().toLanguageTags()
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

    private var searchNewsNet = mutableStateOf(emptyList<News>())

    private var _searchList = MutableStateFlow(emptyList<News>())
    val searchedList = _searchList.asStateFlow()

    private var _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()
    private var _isError = MutableStateFlow(false)
    val isError = _isError.asStateFlow()

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun submitSearch() {
        _searchList.value = emptyList()
        _isSearching.value = true
        _isError.value = false
        viewModelScope.launch {
            try {
                searchNewsNet.value =
                    repository.searchNews(_searchText.value).news.map { it.toNews() }


                repository.insertToRoom(searchNewsNet.value.map { it.toNewsEntity() })

                _searchList.value = getSearchedList(searchNewsNet.value)

            } catch (e: IOException) {
                Log.e("TAG", "Submitting Search ${e.localizedMessage}")
                _isSearching.value = false
                _isError.value = true
            }
        }
    }

    private fun getSearchedList(searchNewsNet: List<News>): List<News> {

        return allNews
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = allNews.value
            )
            .value
            .filter { news ->
                news.id in searchNewsNet.map { it.id }
            }
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
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val news: List<NewsEntity> = repository
                    .getNewsByCategoryNet(_uiState.value.category.toFilterLowerCase())
                    .news
                    .map {
                        it.toNewsEntity()
                    }

                if (news.isEmpty()) {
                    _isLoading.value = false
                }

                repository.insertToRoom(news = news)

            } catch (e: IOException) {
                Log.e("TAG", "Getting News by category: ${e.localizedMessage}")
                _isLoading.value = false
            }
        }
    }

    fun getNewsByCategory(category: Filters): StateFlow<List<News>> {
        return allNews.map { newsList ->
            newsList.filter {
                it.category.contains(category.toFilterLowerCase()) &&
                        it.language == AppCompatDelegate.getApplicationLocales().toLanguageTags()
            }
        }
            .stateIn(
                scope = viewModelScope,
                initialValue = emptyList<News>(),
                started = SharingStarted.WhileSubscribed(5_000)
            )
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
        return allNews.map { listNews ->
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

    //----------------------------------------
    // storing theme in preferences dataStore
    //----------------------------------------
    fun storeTheme(mode: Int) {
        viewModelScope.launch {
            repository.storeTheme(mode)
        }
    }
}
