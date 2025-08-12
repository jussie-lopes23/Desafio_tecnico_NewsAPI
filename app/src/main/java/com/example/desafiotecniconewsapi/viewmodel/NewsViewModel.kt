package com.example.desafiotecniconewsapi.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.desafiotecniconewsapi.data.AppDatabase
import com.example.desafiotecniconewsapi.model.SearchQuery
import com.example.desafiotecniconewsapi.network.RetrofitInstance
import com.example.desafiotecniconewsapi.ui.state.NewsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.IOException

class NewsViewModel(application: Application) : AndroidViewModel(application) {

    private val searchQueryDao = AppDatabase.getDatabase(application).searchQueryDao()

    private val _newsUiState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)
    val newsUiState: StateFlow<NewsUiState> = _newsUiState.asStateFlow()

    private val _searchHistory = MutableStateFlow<List<SearchQuery>>(emptyList())
    val searchHistory: StateFlow<List<SearchQuery>> = _searchHistory.asStateFlow()

    private val apiKey = "1a23f717273c48daba0a8d04d39609d0"

    init {
        viewModelScope.launch {
            searchQueryDao.getSearchHistory().collectLatest { history ->
                _searchHistory.value = history
            }
        }
    }

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
                    // Salva a pesquisa no banco de dados após sucesso
                    saveSearchQuery(query)
                }
            } catch (e: IOException) {
                _newsUiState.value = NewsUiState.Error("Falha na conexão de rede. Verifique sua internet.")
            } catch (e: Exception) {
                _newsUiState.value = NewsUiState.Error("Ocorreu um erro inesperado: ${e.localizedMessage}")
            }
        }
    }

    fun saveSearchQuery(query: String) {
        viewModelScope.launch {
            searchQueryDao.insert(SearchQuery(query = query))
        }
    }
}