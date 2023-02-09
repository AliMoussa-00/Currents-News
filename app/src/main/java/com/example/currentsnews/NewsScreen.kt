package com.example.currentsnews

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.currentsnews.model.ScreenState
import com.example.currentsnews.model.ScreenType
import com.example.currentsnews.ui.NewsViewModel
import com.example.currentsnews.ui.screens.HomeList
import com.example.currentsnews.ui.screens.NewsBottomNavigationBar
import com.example.currentsnews.ui.screens.NewsWebViewContainer
import com.example.currentsnews.ui.screens.bookshelf.ShelfScreen
import com.example.currentsnews.ui.screens.search.SearchScreen


@Composable
fun NewsScreen(
    newsViewModel: NewsViewModel = hiltViewModel(),
) {
    val uiState by newsViewModel.uiState.collectAsState()


    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        if (uiState.screenState == ScreenState.List) {
            when (uiState.screenType) {
                ScreenType.HOME -> {
                    HomeList(newsViewModel = newsViewModel, modifier = Modifier.weight(1f))
                }
                ScreenType.SEARCH -> {
                    SearchScreen(
                        modifier = Modifier.weight(1f),
                        newsViewModel = newsViewModel,
                        backHandler = {
                            newsViewModel.setScreenState("")
                            newsViewModel.setScreenType(ScreenType.HOME)
                        }
                    )
                }
                ScreenType.SAVED -> {
                    ShelfScreen(
                        modifier = Modifier.weight(1f),
                        backHandler = {
                            newsViewModel.setScreenType(ScreenType.HOME)
                        }
                    )
                }
            }
        } else {
            NewsWebViewContainer(
                uiState.url,
                backHandler = { newsViewModel.setScreenState("") })
        }

        NewsBottomNavigationBar(
            onTabPressed = { newsViewModel.setScreenType(it) },
            currentTab = uiState.screenType
        )
    }

}