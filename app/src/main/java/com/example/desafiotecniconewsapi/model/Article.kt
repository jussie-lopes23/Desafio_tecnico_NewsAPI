package com.example.desafiotecniconewsapi.model

data class Article(
    val title: String?,
    val description: String?,
    val urlToImage: String?,
    val content: String?
)

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)