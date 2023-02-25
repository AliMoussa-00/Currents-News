package com.example.currentsnews.ui.screens.home

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.currentsnews.model.Filters
import com.example.currentsnews.model.News
import com.example.currentsnews.model.UiState
import com.example.currentsnews.model.toFilterLowerCase
import com.example.currentsnews.ui.NewsViewModel
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeList(
    newsViewModel: NewsViewModel,
    modifier: Modifier,
    uiState: UiState,
) {

    val listNews = if (uiState.category == Filters.All) {
        newsViewModel.latestNews.collectAsState()
    } else {
        newsViewModel.getNewsByCategory(uiState.category).collectAsState()
    }


    Log.e(
        "TAG",
        "category= ${uiState.category.toFilterLowerCase()} & newsByCategory= ${listNews.value.size}"
    )

    Column(modifier = modifier) {
        FilterNews(
            onClickFilter = { newsViewModel.setCategory(it) },
            selectedFilters = uiState.category
        )
        NewsList(
            modifier = modifier,
            latestNews = listNews.value,
            onNewsClicked = { newsViewModel.setScreenState(url = it, isWeb = true) },
            onBookMarked = { newsViewModel.bookMarkNews(news = it) }
        )
    }


}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NewsList(
    modifier: Modifier,
    latestNews: List<News>,
    onNewsClicked: (String) -> Unit,
    onBookMarked: (News) -> Unit,
) {
    Column(modifier = modifier) {

        if (latestNews.isEmpty()) {
            Text(text = "Loading...")
        } else {
            LazyColumn {

                items(latestNews) { news ->
                    NewsCard(
                        news = news,
                        onClickSeeMore = onNewsClicked,
                        onClickMarked =  onBookMarked
                        )
                }
            }
        }
    }
}





