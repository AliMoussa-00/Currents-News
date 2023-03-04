package com.example.currentsnews

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.currentsnews.model.ScreenState
import com.example.currentsnews.model.ScreenType
import com.example.currentsnews.ui.NewsViewModel
import com.example.currentsnews.ui.screens.NewsBottomNavigationBar
import com.example.currentsnews.ui.screens.NewsWebViewContainer
import com.example.currentsnews.ui.screens.bookshelf.ShelfScreen
import com.example.currentsnews.ui.screens.home.HomeList
import com.example.currentsnews.ui.screens.home.NewsTopBar
import com.example.currentsnews.ui.screens.search.SearchScreen
import com.example.currentsnews.ui.screens.settings.NewsTheme
import com.example.currentsnews.ui.screens.settings.SettingsDialog
import kotlinx.coroutines.launch


@Composable
fun NewsScreen(
    newsViewModel: NewsViewModel = hiltViewModel(),
) {
    val uiState by newsViewModel.uiState.collectAsState()

    val openDialog = remember { mutableStateOf(false) }

    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        NewsTopBar(
            onClickSetting = { openDialog.value = !openDialog.value }
        )

        if (uiState.screenState == ScreenState.List) {
            when (uiState.screenType) {
                ScreenType.HOME -> {
                    HomeList(
                        newsViewModel = newsViewModel,
                        modifier = Modifier.weight(1f),
                        uiState = uiState,
                        lazyListState = lazyListState
                    )
                }
                ScreenType.SEARCH -> {
                    SearchScreen(
                        modifier = Modifier.weight(1f),
                        newsViewModel = newsViewModel,
                        lazyListState = lazyListState,
                        backHandler = {
                            newsViewModel.setScreenState("")
                            newsViewModel.setScreenType(ScreenType.HOME)
                        }
                    )
                }
                ScreenType.SAVED -> {
                    ShelfScreen(
                        modifier = Modifier.weight(1f),
                        newsViewModel = newsViewModel,
                        lazyListState = lazyListState,
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
            onTabPressed = {
                newsViewModel.setScreenType(it)
                coroutineScope.launch {
                    lazyListState.animateScrollToItem(0)
                }
            },
            currentTab = uiState.screenType
        )

        SettingsDialog(
            openDialog = openDialog.value,
            closeDialog = { openDialog.value = !openDialog.value },
            selectedTheme = NewsTheme.Light,
            onSelectTheme = {},
            resetLanguage = { newsViewModel.resetListNews() }
        )
    }

}
