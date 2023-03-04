package com.example.currentsnews.ui.screens.home

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.currentsnews.model.Filters
import com.example.currentsnews.model.News
import com.example.currentsnews.model.UiState
import com.example.currentsnews.ui.NewsViewModel
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.LazyListSnapperLayoutInfo
import dev.chrisbanes.snapper.rememberLazyListSnapperLayoutInfo
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior
import kotlinx.coroutines.launch
import java.util.*

val LazyListState.isScroll: Boolean
    get() =
        firstVisibleItemIndex > 0 || firstVisibleItemScrollOffset > 0

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeList(
    newsViewModel: NewsViewModel,
    modifier: Modifier,
    uiState: UiState,
    lazyListState: LazyListState,
) {

    val listNews = if (uiState.category == Filters.All) {
        newsViewModel.latestNews.collectAsState()
    } else {
        newsViewModel.getNewsByCategory(uiState.category).collectAsState()
    }


    Column(modifier = modifier) {
        FilterNews(
            onClickFilter = { newsViewModel.setCategory(it) },
            selectedFilters = uiState.category,
            modifier = Modifier
                .animateContentSize(tween(300))
                .height(if (lazyListState.isScroll) 0.dp else 52.dp)
        )
        NewsList(
            modifier = modifier,
            latestNews = listNews.value,
            onNewsClicked = { newsViewModel.setScreenState(url = it, isWeb = true) },
            onBookMarked = { newsViewModel.bookMarkNews(news = it) },
            lazyListState = lazyListState
        )
    }


}

@OptIn(
    ExperimentalMaterialApi::class, ExperimentalSnapperApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun NewsList(
    modifier: Modifier,
    latestNews: List<News>,
    onNewsClicked: (String) -> Unit,
    onBookMarked: (News) -> Unit,
    lazyListState: LazyListState,
) {
    Column(modifier = modifier) {

        if (latestNews.isEmpty()) {
            Text(text = "Loading...")
        } else {

            val layoutInfo: LazyListSnapperLayoutInfo =
                rememberLazyListSnapperLayoutInfo(lazyListState)

            LazyColumn(
                state = lazyListState,
                flingBehavior = rememberSnapperFlingBehavior(lazyListState = lazyListState)
            ) {

                items(latestNews) { news ->

                    val coroutineScope = rememberCoroutineScope()

                    NewsCard(
                        modifier = Modifier.animateItemPlacement(),
                        news = news,
                        onClickSeeMore = onNewsClicked,
                        onClickMarked = onBookMarked,
                        onExpandCard = {
                            if (!it) {
                                coroutineScope.launch {
                                    if (
                                        latestNews.indexOf(news) != layoutInfo.visibleItems.first().index &&
                                        latestNews.indexOf(news) != layoutInfo.visibleItems.first().index + 1
                                    ) {
                                        lazyListState.animateScrollToItem(index = layoutInfo.currentItem?.index!!)
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}





