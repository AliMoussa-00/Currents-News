package com.example.currentsnews.model

import com.example.currentsnews.R

enum class Filters(val category: Int) {
    All(R.string.all),
    Politics(R.string.politics),
    Finance(R.string.finance),
    Business(R.string.business),
    Academia(R.string.academia),
    Technology(R.string.technology),
    Health(R.string.health),
    Programming(R.string.programming),
    Science(R.string.science),
    Entertainment(R.string.entertainment),
    Sports(R.string.sports)
}

fun Filters.toFilterLowerCase(): String{
    return name.lowercase()
}

val categoryFilters: List<Filters> = listOf(
    Filters.All,
    Filters.Politics,
    Filters.Finance,
    Filters.Business,
    Filters.Academia,
    Filters.Technology,
    Filters.Health,
    Filters.Programming,
    Filters.Science,
    Filters.Entertainment,
    Filters.Sports
)
