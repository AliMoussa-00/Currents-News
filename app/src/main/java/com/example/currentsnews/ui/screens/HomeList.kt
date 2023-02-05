package com.example.currentsnews.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.currentsnews.model.News
import com.example.currentsnews.ui.NewsViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeList(
    newsViewModel: NewsViewModel = hiltViewModel(),
) {
    val newsList by newsViewModel.latestNews.collectAsState()

    val isRefreshing by newsViewModel.isRefreshing
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { newsViewModel.refreshDatabase() })

    NewsList(latestNews = newsList, isRefreshing = isRefreshing, pullRefreshState = pullRefreshState)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NewsList(
    modifier: Modifier = Modifier,
    latestNews: List<News>,
    isRefreshing: Boolean,
    pullRefreshState: PullRefreshState,
) {
    Box {
        if(latestNews.isEmpty()){
            Text(text = "Loading...")
        }
        else{
            LazyColumn(
                modifier = modifier.pullRefresh(pullRefreshState)
            ) {
                items(latestNews) {
                    ListItems(news = it)
                }
            }
            PullRefreshIndicator(refreshing = isRefreshing, state = pullRefreshState)
        }

    }
}

@Composable
fun ListItems(
    modifier: Modifier = Modifier,
    news: News,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 2.dp
    ) {
        Row {
            ItemImage(imgUrl = news.image, imgTitle = news.title)
            ItemContents(news = news)
        }
    }
}

@Composable
fun ItemImage(modifier: Modifier = Modifier, imgUrl: String, imgTitle: String) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imgUrl)
            .build(),
        contentDescription = imgTitle,
        modifier = modifier.size(88.dp),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun ItemContents(modifier: Modifier = Modifier, news: News) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(text = news.title)
        Text(text = news.description)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = news.author)
            Text(text = calculateDate(news.published))
        }
    }
}

fun calculateDate(dateString: String): String {
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val publishedDate = dateFormatter.parse(dateString)
    val currentTime = Calendar.getInstance().timeInMillis
    val timePassed = currentTime - publishedDate.time

    val sec = timePassed / 1000
    val min = sec / 60
    val h = min / 60
    val d = h / 24

    return when {
        d > 0 -> "$d days"
        h > 0 -> "$h h"
        min > 0 -> "$min min"
        else -> "$sec sec"
    }
}