package com.example.currentsnews.data.network

import com.example.currentsnews.data.room.NewsEntity

data class NewsApi(
    val news : List<NewsNetwork>
)
data class NewsNetwork(
    val author: String,
    val category: List<String>,
    val description: String,
    val id: String,
    val image: String,
    val language: String,
    val published: String,
    val title: String,
    val url: String
)

fun NewsNetwork.toNewsEntity(): NewsEntity{
    return NewsEntity(
        id = id,
        author=author,
        category = category.joinToString(),
        description= description,
        image=image,
        language= language,
        published = published,
        title= title,
        url= url
    )
}