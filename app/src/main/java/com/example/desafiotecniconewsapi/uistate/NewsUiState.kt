package com.example.desafiotecniconewsapi.uistate

import com.example.desafiotecniconewsapi.model.Article

sealed class NewsUiState {
    object Loading : NewsUiState()
    data class Success(val news: List<Article>) : NewsUiState()
    data class Error(val message: String) : NewsUiState()
    object Empty : NewsUiState()
}