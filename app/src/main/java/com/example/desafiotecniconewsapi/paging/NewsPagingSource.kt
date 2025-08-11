package com.example.desafiotecniconewsapi.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.desafiotecniconewsapi.model.Article
import com.example.desafiotecniconewsapi.network.NewsApiService

class NewsPagingSource(
    private val apiService: NewsApiService,
    private val query: String,
    private val apiKey: String
) : PagingSource<Int, Article>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val page = params.key ?: 1
        return try {
            val response = apiService.getNews(
                query = query,
                apiKey = apiKey,
                page = page,
                pageSize = params.loadSize
            )
            LoadResult.Page(
                data = response.articles,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.articles.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}