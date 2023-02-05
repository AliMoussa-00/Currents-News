package com.example.currentsnews.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news")
class NewsEntity(
    @PrimaryKey val id: String,
    val author: String,
    val category: String,
    val description: String,
    val image: String,
    val language: String,
    val published: String,
    val title: String,
    val url: String
    )