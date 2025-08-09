package com.example.desafiotecniconewsapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.desafiotecniconewsapi.model.Article
import com.example.desafiotecniconewsapi.uistate.NewsUiState
import com.example.desafiotecniconewsapi.ui.theme.DesafioTecnicoNewsAPITheme
import com.example.desafiotecniconewsapi.viewmodel.NewsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DesafioTecnicoNewsAPITheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NewsApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun NewsApp(modifier: Modifier = Modifier, newsViewModel: NewsViewModel = viewModel()) {
    var searchQuery by remember { mutableStateOf("") }
    val newsUiState by newsViewModel.newsUiState.collectAsState()

    Column(modifier = modifier.padding(16.dp)) {
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Buscar notícias por título") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = { newsViewModel.fetchNews(searchQuery) }, modifier = Modifier.fillMaxWidth()) {
            Text("Buscar")
        }

        when (newsUiState) {
            is NewsUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is NewsUiState.Success -> {
                val articles = (newsUiState as NewsUiState.Success).news
                NewsList(articles = articles)
            }
            is NewsUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = (newsUiState as NewsUiState.Error).message)
                }
            }
            is NewsUiState.Empty -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Nenhuma notícia encontrada.")
                }
            }
        }
    }
}

@Composable
fun NewsList(articles: List<Article>) {
    LazyColumn {
        items(articles) { article ->
            ArticleItem(article = article)
        }
    }
}

@Composable
fun ArticleItem(article: Article) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        article.title?.let { Text(text = it, style = MaterialTheme.typography.titleLarge) }
        article.description?.let { Text(text = it, style = MaterialTheme.typography.bodyMedium) }
        // TODO: Adicionar imagem e outros detalhes (Próximo passo)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DesafioTecnicoNewsAPITheme {
        NewsApp()
    }
}