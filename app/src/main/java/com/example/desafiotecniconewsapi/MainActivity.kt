package com.example.desafiotecniconewsapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.desafiotecniconewsapi.model.Article
import com.example.desafiotecniconewsapi.model.SearchQuery
import com.example.desafiotecniconewsapi.ui.screens.DetailScreen
import com.example.desafiotecniconewsapi.ui.screens.WebViewScreen
import com.example.desafiotecniconewsapi.ui.state.NewsUiState
import com.example.desafiotecniconewsapi.ui.theme.DesafioTecnicoNewsAPITheme
import com.example.desafiotecniconewsapi.viewmodel.NewsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DesafioTecnicoNewsAPITheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "newsList") {
                    composable("newsList") {
                        NewsApp(navController = navController)
                    }
                    composable("detail") {
                        val article = navController.previousBackStackEntry?.savedStateHandle?.get<Article>("article")
                        article?.let {
                            DetailScreen(navController = navController, article = it)
                        }
                    }
                    composable("webview/{url}") { backStackEntry ->
                        val url = backStackEntry.arguments?.getString("url")
                        url?.let {
                            WebViewScreen(url = it)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsApp(
    navController: NavController,
    modifier: Modifier = Modifier,
    newsViewModel: NewsViewModel = viewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    val newsUiState by newsViewModel.newsUiState.collectAsState()
    val searchHistory by newsViewModel.searchHistory.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    Scaffold(modifier = modifier) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it }, // <--- A linha foi alterada aqui
                    label = { Text("Buscar notícias por título") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    colors = ExposedDropdownMenuDefaults.textFieldColors()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    searchHistory.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item.query) },
                            onClick = {
                                searchQuery = item.query
                                expanded = false
                                newsViewModel.fetchNews(item.query)
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { newsViewModel.fetchNews(searchQuery) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Buscar")
            }
            Spacer(modifier = Modifier.height(16.dp))

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
                    NewsList(articles = articles, navController = navController)
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
}

@Composable
fun NewsList(articles: List<Article>, navController: NavController) {
    LazyColumn {
        items(articles) { article ->
            ArticleItem(article = article, navController = navController)
        }
    }
}

@Composable
fun ArticleItem(article: Article, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                navController.currentBackStackEntry?.savedStateHandle?.set("article", article)
                navController.navigate("detail")
            },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = article.urlToImage,
                contentDescription = article.title,
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop,
            )
            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .fillMaxWidth()
            ) {
                article.title?.let { Text(text = it, style = MaterialTheme.typography.titleLarge) }
                article.description?.let { Text(text = it, style = MaterialTheme.typography.bodyMedium) }
            }
        }
    }
}