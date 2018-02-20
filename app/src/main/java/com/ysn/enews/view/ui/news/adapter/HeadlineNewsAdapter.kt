package com.ysn.enews.view.ui.news.adapter

import android.content.Context
import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.ysn.e_news.R
import com.ysn.enews.model.Article
import kotlinx.android.synthetic.main.item_news.view.*

/**
 * Created by yudisetiawan on 2/19/18.
 */
class HeadlineNewsAdapter(private val context: Context, private val articleHeadlineNews: List<Article>,
                          private val listViewTypes: List<Int>,
                          private val listenerHeadlineNewsAdapter: ListenerHeadlineNewsAdapter) : RecyclerView.Adapter<HeadlineNewsAdapter.ViewHolder>() {

    private val TAG = javaClass.simpleName

    companion object {
        val VIEW_TYPE_CONTENT = 1
        val VIEW_TYPE_LOADING = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_CONTENT -> {
                val view = layoutInflater.inflate(R.layout.item_news, null)
                ViewHolderContentHeadlineNews(view)
            }
            else -> {
                val view = layoutInflater.inflate(R.layout.item_loading, null)
                ViewHolderLoadingHeadlineNews(view)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val viewType = listViewTypes[position]
        when (viewType) {
            VIEW_TYPE_CONTENT -> {
                val holderContentHeadlineNews = holder as ViewHolderContentHeadlineNews
                val articleHeadlineNews = articleHeadlineNews[position]
                Glide.with(context)
                        .load(articleHeadlineNews.urlToImage)
                        .asBitmap()
                        .placeholder(R.drawable.ic_image_black_24dp)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(object : SimpleTarget<Bitmap>() {
                            override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                                holderContentHeadlineNews.itemView.image_view_cover_item_news.scaleType = ImageView.ScaleType.CENTER_CROP
                                holderContentHeadlineNews.itemView.image_view_cover_item_news.setImageBitmap(resource)
                            }
                        })
                holderContentHeadlineNews.itemView.text_view_title_item_news.text = articleHeadlineNews.title
            }
            else -> {
                /** nothing to do in here */
            }
        }
    }

    override fun getItemCount(): Int = listViewTypes.size

    override fun getItemViewType(position: Int): Int = listViewTypes[position]

    open inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class ViewHolderContentHeadlineNews(itemView: View) : ViewHolder(itemView) {

        init {
            itemView.relative_layout_content_item_news.setOnClickListener {
                listenerHeadlineNewsAdapter.onClickHeadlineNews(articleHeadlineNews[adapterPosition].url)
            }
        }
    }

    inner class ViewHolderLoadingHeadlineNews(itemView: View) : ViewHolder(itemView)

    interface ListenerHeadlineNewsAdapter {

        fun onClickHeadlineNews(url: String)

    }

}