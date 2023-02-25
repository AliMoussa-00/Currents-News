package com.example.currentsnews.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.example.currentsnews.R
import com.example.currentsnews.model.ScreenType


data class NavigationItemsContent(
    val icon : ImageVector,
    val title: Int,
    val screenType:ScreenType
)

val navigationItemsContentList= listOf(
    NavigationItemsContent(
        icon = Icons.Default.Home,
        title= R.string.home,
        screenType = ScreenType.HOME
    ),
    NavigationItemsContent(
        icon = Icons.Default.Search,
        title = R.string.search,
        screenType = ScreenType.SEARCH
    ),
    NavigationItemsContent(
        icon = Icons.Default.FavoriteBorder,
        title = R.string.bookShelf,
        ScreenType.SAVED
    )
)

@Composable
fun NewsBottomNavigationBar(
    modifier: Modifier = Modifier,
    onTabPressed: (ScreenType)->Unit,
    navList: List<NavigationItemsContent> = navigationItemsContentList,
    currentTab: ScreenType
){
    BottomNavigation(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = MaterialTheme.colors.background,
    ) {
        for (item in navList){
            BottomNavigationItem(
                selected =(currentTab == item.screenType),
                onClick = { onTabPressed(item.screenType) },
                icon ={
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = item.icon, contentDescription = stringResource(id = item.title))
                        AnimatedVisibility(
                            visible = (currentTab == item.screenType),
                            enter = expandHorizontally(
                                animationSpec = tween(durationMillis = 150, easing = LinearOutSlowInEasing)
                            ),
                            exit = shrinkHorizontally(
                                animationSpec = tween(durationMillis = 150, easing = FastOutLinearInEasing)
                            )
                        ) {
                            Text(text = stringResource(id = item.title))
                        }
                    }
                }
            )
        }
    }
}





