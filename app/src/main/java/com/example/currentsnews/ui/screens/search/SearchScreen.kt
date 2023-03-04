package com.example.currentsnews.ui.screens.search

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.currentsnews.R
import com.example.currentsnews.ui.NewsViewModel
import com.example.currentsnews.ui.screens.home.NewsList


@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    newsViewModel: NewsViewModel,
    backHandler : ()->Unit,
    lazyListState: LazyListState
) {
    BackHandler {
        backHandler()
    }

    val searchText by newsViewModel.searchText.collectAsState()

    val searchedNews by newsViewModel.getSearchedList().collectAsState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {

        SearchField(
            searchText = searchText,
            onSearchTextChange = { newsViewModel.onSearchTextChange(it) },
            submitSearch = {
                newsViewModel.submitSearch()
            }
        )

        NewsList(
            modifier = modifier,
            latestNews = searchedNews ,
            onNewsClicked = { newsViewModel.setScreenState(url=it,isWeb = true)},
            onBookMarked = {
                newsViewModel.bookMarkNews(news = it)
            },
            lazyListState = lazyListState
        )
    }
}

@Composable
fun SearchField(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    submitSearch: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = searchText,
        onValueChange = { onSearchTextChange(it) },
        placeholder = { Text(text = stringResource(id = R.string.search)) },
        singleLine = true,
        trailingIcon = {
            if (searchText.isEmpty()) {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            } else {
                IconButton(onClick = { onSearchTextChange("") }) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                }
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),

        keyboardActions = KeyboardActions(onSearch = {
            submitSearch()
            focusManager.clearFocus()
        }),

        )
}