package com.example.currentsnews.ui.screens.bookshelf

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.currentsnews.ui.NewsViewModel
import com.example.currentsnews.ui.screens.home.NewsList

@Composable
fun ShelfScreen(
    modifier: Modifier = Modifier,
    newsViewModel: NewsViewModel,
    backHandler: ()->Unit
){
    BackHandler {
        backHandler()
    }

    val bookMarkedNews by newsViewModel.getBookMarkedNEws().collectAsState()

    NewsList(
        modifier = modifier,
        latestNews = bookMarkedNews,
        onNewsClicked = { newsViewModel.setScreenState(url = it, isWeb = true) },
        onBookMarked = {newsViewModel.bookMarkNews(it)}
    )
}