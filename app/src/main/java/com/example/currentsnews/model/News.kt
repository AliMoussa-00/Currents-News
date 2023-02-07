package com.example.currentsnews.model

data class News(
    val author: String = "",
    val category: List<String> = emptyList(),
    val description: String = "",
    val id: String="",
    val image: String="",
    val language: String="",
    val published: String="",
    val title: String="",
    val url: String=""
)

data class UiState(
    val screenState: ScreenState= ScreenState.List,
    val url: String=""
)

enum class ScreenState{
    List,WebView
}

