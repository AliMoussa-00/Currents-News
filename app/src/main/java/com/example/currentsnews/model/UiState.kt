package com.example.currentsnews.model

data class UiState(
    val screenState: ScreenState= ScreenState.List,
    val url: String="",
    val screenType: ScreenType = ScreenType.HOME,
    val category: Filters = Filters.All
)

enum class ScreenState{
    List,WebView
}
enum class ScreenType{
    HOME,SEARCH,SAVED
}