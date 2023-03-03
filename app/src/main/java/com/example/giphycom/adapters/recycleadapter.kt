package com.example.giphycom.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.giphycom.R
import com.example.giphycom.api.Gif
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.giphycom.activity.Activitygifinfo

class recycleadapter(private val adapterContext: Context) :
    BaseQuickAdapter<Gif, recycleadapter.GifViewHolder>(R.layout.gif_item), LoadMoreModule {
    private var gifList: MutableList<Gif> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifViewHolder {
        val view = LayoutInflater.from(adapterContext).inflate(R.layout.gif_item, parent, false)
        return GifViewHolder(view)
    }

    override fun onBindViewHolder(holder: GifViewHolder, position: Int) {
        val gif = gifList[position]
        Glide.with(adapterContext)
            .asGif()
            .load(gif.images.fixed_height.url)
            .into(holder.gifImageView)
        holder.init(gif.title, gif.id, gif.images.fixed_height.url)
    }

    override fun convert(holder: GifViewHolder, item: Gif ) {
        Glide.with(adapterContext)
            .asGif()
            .load(item.images.fixed_height.url)
            .into(holder.gifImageView)
    }

    override fun getItemCount(): Int {
        return gifList.size
    }

    fun addGifs(newGifs: List<Gif>) {
        gifList.addAll(newGifs)
        notifyDataSetChanged()
    }

    fun clear() {
        gifList.clear()
        notifyDataSetChanged()
    }

    inner class GifViewHolder(itemView: View) : BaseViewHolder(itemView) {
        val gifImageView: ImageView = itemView.findViewById(R.id.gif_image_view)
        fun init(title: String, id: String, gif: String){
            gifImageView.setOnClickListener {
                val intent = Intent(context, Activitygifinfo::class.java)
                intent.putExtra("id", id)
                intent.putExtra("url", gif)
                intent.putExtra("title", title)
                context.startActivity(intent)
            }
        }
    }
}