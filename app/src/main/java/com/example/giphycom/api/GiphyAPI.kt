package com.example.giphycom.api

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request

class GiphyAPI {
    companion object {
        private const val BASE_URL = "https://api.giphy.com/v1/gifs/"
        private const val API_KEY = "your_api_key_here"

        private val client = OkHttpClient()

        suspend fun searchGifs(query: String, page: Int): GiphyResponse? {
            val url = (BASE_URL + "search").toHttpUrlOrNull()!!.newBuilder()
                .addQueryParameter("api_key", API_KEY)
                .addQueryParameter("q", query)
                .addQueryParameter("offset", (page * 25).toString())
                .addQueryParameter("limit", "25")
                .build()

            val request = Request.Builder()
                .url(url)
                .build()

            val response = withContext(Dispatchers.IO) {
                client.newCall(request).execute()
            }

            return if (response.isSuccessful) {
                val body = response.body?.string()
                Gson().fromJson(body, GiphyResponse::class.java)
            } else {
                null
            }
        }

        suspend fun getTrendingGifs(page: Int): GiphyResponse? {
            val url = (BASE_URL + "trending").toHttpUrlOrNull()!!.newBuilder()
                .addQueryParameter("api_key", API_KEY)
                .addQueryParameter("offset", (page * 25).toString())
                .addQueryParameter("limit", "25")
                .build()

            val request = Request.Builder()
                .url(url)
                .build()

            val response = withContext(Dispatchers.IO) {
                client.newCall(request).execute()
            }

            return if (response.isSuccessful) {
                val body = response.body?.string()
                Gson().fromJson(body, GiphyResponse::class.java)
            } else {
                null
            }
        }
    }
}