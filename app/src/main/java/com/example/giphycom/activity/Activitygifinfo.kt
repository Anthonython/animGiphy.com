package com.example.giphycom.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.giphycom.R

class Activitygifinfo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activitygifinfo)

        val image: ImageView = findViewById(R.id.gifview)
        val id: TextView = findViewById(R.id.idtext)
        val title: TextView = findViewById(R.id.nametext)

        Glide.with(this).asGif().load(intent.getStringExtra("url").toString()).into(image)
        id.text = "${R.string.id} ${intent.getStringExtra("id").toString()}"
        title.text = "${R.string.namegif} ${intent.getStringExtra("title").toString()}"
    }
}