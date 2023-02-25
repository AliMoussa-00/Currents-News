package com.example.currentsnews.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.currentsnews.model.News

@Entity(tableName = "news")
class NewsEntity(
    @PrimaryKey val id:String,
    val author: String,
    val category: String,
    val description: String,
    val image: String,
    val language: String,
    val published: String,
    val title: String,
    val url: String,
    val bookMarked: Boolean = false
    )


fun NewsEntity.toNews(): News{
    return News(
        id=id,
        author=author,
        category = category,
        description= description,
        image=image,
        language= language,
        published = published,
        title= title,
        url= url,
        bookMarked = bookMarked
    )
}