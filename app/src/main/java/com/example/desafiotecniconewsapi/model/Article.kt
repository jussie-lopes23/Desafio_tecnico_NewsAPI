package com.example.desafiotecniconewsapi.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Article(
    val title: String?,
    val description: String?,
    val urlToImage: String?,
    val content: String?,
    val url: String?
) : Parcelable

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)