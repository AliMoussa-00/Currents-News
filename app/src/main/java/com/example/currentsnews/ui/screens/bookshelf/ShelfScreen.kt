package com.example.currentsnews.ui.screens.bookshelf

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ShelfScreen(
    modifier: Modifier = Modifier,
    backHandler: ()->Unit
){
    BackHandler {
        backHandler()
    }

    Column(modifier = modifier) {
        Text(text = "Book Shelf")
    }
}