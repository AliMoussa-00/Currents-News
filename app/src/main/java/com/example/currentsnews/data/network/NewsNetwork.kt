package com.example.currentsnews.data.network

import com.example.currentsnews.data.room.NewsEntity
import com.example.currentsnews.model.News

data class NewsApi(
    val status:String,
    val news: List<NewsNetwork>,
)

data class NewsNetwork(
    val id: String,
    val category: List<String>,
    val description: String?,
    val image: String?,
    val language: String,
    val published: String,
    val title: String,
    val url: String,
    val author: String
)

fun NewsNetwork.toNewsEntity(): NewsEntity {
    return NewsEntity(
        id=id,
        author = author,
        category = category.joinToString(),
        description = description ?: "null",
        image = image ?: "null",
        language = language,
        published = published,
        title = title,
        url = url
    )
}

fun NewsNetwork.toNews(): News {
    return News(
        id=id,
        author = author,
        category = category.joinToString(),
        description = description ?: "null",
        image = image ?: "null",
        language = language,
        published = published,
        title = title,
        url = url
    )
}
