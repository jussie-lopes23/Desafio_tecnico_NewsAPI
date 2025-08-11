package com.example.desafiotecniconewsapi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.desafiotecniconewsapi.network.RetrofitInstance
import com.example.desafiotecniconewsapi.ui.state.NewsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class NewsViewModel : ViewModel() {

    private val _newsUiState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)
    val newsUiState: StateFlow<NewsUiState> = _newsUiState

    // Substitua "apiKey" pela sua chave da NewsAPI
    private val apiKey = "1a23f717273c48daba0a8d04d39609d0"

    fun fetchNews(query: String) {
        viewModelScope.launch {
            _newsUiState.value = NewsUiState.Loading
            try {
                val response = RetrofitInstance.api.getNews(
                    query = query,
                    apiKey = apiKey,
                    page = 1,
                    pageSize = 20
                )
                if (response.articles.isEmpty()) {
                    _newsUiState.value = NewsUiState.Empty
                } else {
                    _newsUiState.value = NewsUiState.Success(response.articles)
                }
            } catch (e: IOException) {
                _newsUiState.value = NewsUiState.Error("Falha na conexão de rede. Verifique sua internet.")
            } catch (e: Exception) {
                _newsUiState.value = NewsUiState.Error("Ocorreu um erro inesperado: ${e.localizedMessage}")
            }
        }
    }
}