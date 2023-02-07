package com.example.currentsnews.ui.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.currentsnews.model.News
import com.example.currentsnews.ui.NewsViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeList(
    newsViewModel: NewsViewModel,
) {
    val newsList by newsViewModel.latestNews.collectAsState()

    val isRefreshing by newsViewModel.isRefreshing
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { newsViewModel.refreshDatabase() })

    NewsList(
        latestNews = newsList,
        isRefreshing = isRefreshing,
        pullRefreshState = pullRefreshState,
        onNewsClicked = {newsViewModel.setScreenState(it)}
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NewsList(
    modifier: Modifier = Modifier,
    latestNews: List<News>,
    isRefreshing: Boolean,
    pullRefreshState: PullRefreshState,
    onNewsClicked: (String)->Unit
) {
    Box(contentAlignment = Alignment.TopCenter) {
        if (latestNews.isEmpty()) {
            Text(text = "Loading...")
        } else {
            LazyColumn(
                modifier = modifier.pullRefresh(pullRefreshState)
            ) {
                items(latestNews) {
                    ListItems(news = it, onCardClick = onNewsClicked)
                }
            }
            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState
            )
        }

    }
}

@Composable
fun ListItems(
    modifier: Modifier = Modifier,
    news: News,
    onCardClick: (String)->Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp)
            .padding(8.dp)
            .clickable { onCardClick(news.url) },
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
        modifier = modifier
            .width(120.dp)
            .height(140.dp),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun ItemContents(
    modifier: Modifier = Modifier,
    news: News,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(

            modifier = Modifier
                .weight(1f)
                .wrapContentHeight(),
            text = news.title,
            overflow = TextOverflow.Ellipsis
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = modifier.weight(1f),
                text = news.author,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(text = calculateDate(news.published), fontSize = 14.sp)
        }
    }
}

fun calculateDate(dateString: String): String {
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.getDefault())
    val publishedDate = dateFormatter.parse(dateString)
    val currentTime = Calendar.getInstance().timeInMillis
    val timePassed = currentTime - publishedDate.time

    val sec = timePassed / 1000
    val min = sec / 60
    val h = min / 60
    val d = h / 24

    return when {
        d > 0 -> "$d days ago"
        h > 0 -> "$h h ago"
        min > 0 -> "$min min ago"
        else -> "$sec sec ago"
    }
}

