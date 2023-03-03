package com.example.giphycom.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.giphycom.R
import com.example.giphycom.adapters.recycleadapter
import com.example.giphycom.api.Gif
import com.example.giphycom.api.GiphyResponse
import com.github.rahatarmanahmed.cpv.CircularProgressView
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class MainActivity : AppCompatActivity() {
    private lateinit var GifRecycler: RecyclerView
    private lateinit var TextSearch: EditText
    private lateinit var listnumber: TextView
    private lateinit var progressbar: CircularProgressView
             private var numberlist = 1
             private val RecAvaAdapter = recycleadapter(this)
             private var loadedItemCount = 25

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listnumber = findViewById(R.id.textnumber)
        GifRecycler = findViewById(R.id.GifRecycler)
        TextSearch = findViewById(R.id.hintsearch)
        progressbar = findViewById(R.id.progressbar)

        GifRecycler.adapter = RecAvaAdapter
        GifRecycler.layoutManager = GridLayoutManager(this, 2)
        GifRecycler.setHasFixedSize(true)

        GifRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                listnumber.text = "Страница №$numberlist"
                if (!recyclerView.canScrollVertically(1) && dy > 0) {
                    loadMoreData()
                }
            }
        })

        val handler = Handler()
        TextSearch.addTextChangedListener(object: TextWatcher {
            private var searchRunnable: Runnable? = null
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {numberlist = 1}
            override fun afterTextChanged(s: Editable?) {
              if (searchRunnable != null)  handler.removeCallbacks(searchRunnable!!)
                searchRunnable = Runnable {
                    request(s.toString())
                }
                handler.postDelayed(searchRunnable!!, 500)
            } })
    }

    private fun loadMoreData() {
        RecAvaAdapter.loadMoreModule.isEnableLoadMore = false
        CoroutineScope(Dispatchers.Main).launch {
            progressbar.visibility = View.VISIBLE
            val newItems = searchGifs("${R.string.apikey}", TextSearch.text.toString(), loadedItemCount)
            if (newItems.isNotEmpty()) {
                loadedItemCount += 25
                RecAvaAdapter.clear()
                numberlist++
                listnumber.text = "${R.string.page} $numberlist"
                RecAvaAdapter.addGifs(newItems)
            } else {
                Toast.makeText(this@MainActivity, R.string.end.toString(), Toast.LENGTH_SHORT).show()
            }
            progressbar.visibility = View.GONE
            RecAvaAdapter.loadMoreModule.isEnableLoadMore = true
        }
    }

    fun request(s: String){
        RecAvaAdapter.clear()
        CoroutineScope(Dispatchers.Main).launch {
            RecAvaAdapter.addGifs(searchGifs("${R.string.apikey}", s, 0))
        }
    }

    suspend fun searchGifs(apiKey: String, query: String, offset: Int): List<Gif> {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.giphy.com/v1/gifs/search?api_key=$apiKey&q=$query&limit=25&offset=$offset&rating=g&lang=en")
            .build()
        val response = withContext(Dispatchers.IO) { client.newCall(request).execute() }
        val responseBody = response.body?.string()
        response.close()
        val gson = Gson()
        val searchResponse = gson.fromJson(responseBody, GiphyResponse::class.java)
        return searchResponse.data
    }
}