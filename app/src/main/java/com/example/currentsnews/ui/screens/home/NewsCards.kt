package com.example.currentsnews.ui.screens.home

import android.content.Context
import android.content.Intent
import androidx.compose.animation.*
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.currentsnews.R
import com.example.currentsnews.model.News
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun NewsCard(
    modifier: Modifier = Modifier,
    news: News,
    onClickSeeMore: (String) -> Unit,
    onClickMarked: (News) -> Unit,
    onExpandCard: (Boolean) -> Unit,
) {

    var isRow by remember { mutableStateOf(true) }

    Card(
        modifier = modifier
            .wrapContentHeight()
            .padding(8.dp)
            .clickable {
                isRow = !isRow
                onExpandCard(isRow)
            },
        elevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
        ) {
            Row {

                ItemImage(imgUrl = news.image, imgTitle = news.title, isRow)

                if (isRow) {
                    AnimatedVisibility(
                        visible = isRow,
                        enter = expandHorizontally(
                            expandFrom = Alignment.End,
                            animationSpec = tween(
                                durationMillis = 500,
                                easing = LinearOutSlowInEasing
                            )
                        ),
                        exit = shrinkHorizontally(
                            shrinkTowards = Alignment.End,
                            animationSpec = tween(
                                durationMillis = 250,
                                easing = LinearOutSlowInEasing
                            )
                        )
                    ) {
                        ItemContents(
                            news = news,
                            onClickMarked = onClickMarked,
                            onClickSeeMore = onClickSeeMore,
                            isExpanded = false
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = !isRow,
                enter = expandVertically(
                    animationSpec = tween(durationMillis = 350, easing = LinearOutSlowInEasing)
                ),
                exit = shrinkVertically(
                    animationSpec = tween(durationMillis = 350, easing = LinearOutSlowInEasing)
                )
            ) {

                ItemContents(
                    news = news,
                    showDetail = true,
                    onClickMarked = onClickMarked,
                    onClickSeeMore = onClickSeeMore,
                    isExpanded = true,
                )


            }
        }
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ItemImage(
    imgUrl: String,
    imgTitle: String,
    isRow: Boolean = false,
) {
    val sizeModifier: Modifier =
        if (isRow) Modifier
            .width(140.dp)
            .height(160.dp) else Modifier
            .fillMaxWidth()
            .height(180.dp)

    val imageModifier: Modifier = sizeModifier
        .animateContentSize(
            animationSpec = tween(durationMillis = 450, easing = LinearOutSlowInEasing)
        )

    AnimatedVisibility(
        visible = true,
        enter = expandHorizontally(
            expandFrom = Alignment.Start
        ),
        exit = shrinkHorizontally(
            shrinkTowards = Alignment.Start
        )
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imgUrl)
                .build(),
            contentDescription = imgTitle,
            modifier = imageModifier,
            contentScale = ContentScale.Crop
        )
    }

}

@Composable
fun ItemContents(
    modifier: Modifier = Modifier,
    news: News,
    showDetail: Boolean = false,
    onClickMarked: (News) -> Unit,
    onClickSeeMore: (String) -> Unit,
    isExpanded:Boolean
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(160.dp)
            .padding(8.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = news.title, style = MaterialTheme.typography.h2
        )
        if (!showDetail) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_watch_later_24),
                    contentDescription = null,
                    tint = Color.DarkGray
                )
                Text(
                    text = calculateDate(news.published, LocalContext.current),
                    style = MaterialTheme.typography.body2
                )
            }

        } else {

            Text(text = news.description, style = MaterialTheme.typography.body1)

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = news.category, style = MaterialTheme.typography.h3, color = Color.Blue)
                Text(text = news.author, style = MaterialTheme.typography.h3)
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_watch_later_24),
                    contentDescription = null,
                    tint = Color.DarkGray
                )
                Text(
                    text = calculateDate(news.published, LocalContext.current),
                    style = MaterialTheme.typography.body2
                )

                Spacer(modifier = Modifier.weight(1f))

                TrailingButtons(
                    news = news,
                    onClickMarked = onClickMarked,
                    onClickSeeMore = onClickSeeMore
                )
            }
        }
    }
}

@Composable
fun TrailingButtons(
    news: News,
    onClickMarked: (News) -> Unit,
    onClickSeeMore: (String) -> Unit
) {
    val context= LocalContext.current
    Row {

        IconButton(
            onClick = { onClickMarked(news.copy(bookMarked = !news.bookMarked)) }
        ) {
            Icon(
                painter = painterResource(
                    id = if (news.bookMarked) R.drawable.baseline_bookmark_24
                    else R.drawable.bookmark_border_24
                ),
                contentDescription = stringResource(id = R.string.bookMarked)
            )
        }
        IconButton(
            onClick = { shareNews(news.url,context) }
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = null
            )
        }
        TextButton(onClick = { onClickSeeMore(news.url) }) {

            Text(text = stringResource(id = R.string.see_more))
        }
    }
}


fun calculateDate(dateString: String, context: Context): String {
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.getDefault())
    val publishedDate = dateFormatter.parse(dateString)
    val currentTime = Calendar.getInstance().timeInMillis
    val timePassed = currentTime - publishedDate.time
//2023-03-04 10:54:09 +0000
    val sec = (timePassed / 1000).toInt()
    val min = (sec / 60)
    val h = (min / 60)
    val d = (h / 24)

    return when {
        d > 0 -> context.resources.getQuantityString(R.plurals.days_passed, d, d)
        h > 0 -> context.resources.getQuantityString(R.plurals.hours_passed, h, h)
        min > 0 -> context.resources.getQuantityString(R.plurals.min_passed, min, min)
        else -> context.resources.getQuantityString(R.plurals.sec_passed, sec, sec)
    }
}

private fun shareNews(urlNews:String,context: Context){

    val sendIntent: Intent = Intent.createChooser(Intent().apply{
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT,urlNews)
        type= "text/plain"
    },null)

    context.startActivity(sendIntent)
}


