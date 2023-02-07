package com.example.currentsnews

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.currentsnews.model.ScreenState
import com.example.currentsnews.ui.NewsViewModel
import com.example.currentsnews.ui.screens.HomeList
import com.example.currentsnews.ui.screens.NewsWebViewContainer


@Composable
fun NewsScreen(
    newsViewModel: NewsViewModel = hiltViewModel(),
){
    val uiState by newsViewModel.uiState.collectAsState()

    if(uiState.screenState == ScreenState.List){
        HomeList(newsViewModel = newsViewModel)
    }
    else{
        NewsWebViewContainer(uiState.url, backHandler = { newsViewModel.setScreenState("") })
    }
}