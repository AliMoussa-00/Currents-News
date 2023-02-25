package com.example.currentsnews.model

import com.example.currentsnews.data.room.NewsEntity

data class News(
    val id:String= "",
    val author: String = "",
    val category:String="",
    val description: String = "",
    val image: String="",
    val language: String="",
    val published: String="",
    val title: String="",
    val url: String="",
    val bookMarked: Boolean = false
)

fun News.toNewsEntity(): NewsEntity{
    return NewsEntity(
        id=id,
        author = author,
        category = category,
        description = description,
        image = image,
        language = language,
        published = published,
        title = title,
        url = url,
        bookMarked = bookMarked
    )
}



