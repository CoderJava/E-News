package com.ysn.enews.view.ui.news.adapter

import android.content.Context
import android.graphics.Bitmap
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.ysn.e_news.R
import com.ysn.enews.model.Article
import kotlinx.android.synthetic.main.item_headline_news.view.*
import kotlinx.android.synthetic.main.item_news.view.*

/**
 * Created by yudisetiawan on 2/19/18.
 */
class NewsAdapter(private val context: Context, private val articlesNews: List<Article>,
                  private val articleHeadlineNews: List<Article>,
                  private val listViewTypeNews: List<Int>,
                  private val listViewTypeHeadlineNews: List<Int>,
                  private val listenerNewsAdapter: ListenerNewsAdapter,
                  private val listenerHeadlineNewsAdapter: HeadlineNewsAdapter.ListenerHeadlineNewsAdapter) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {


    private val TAG = javaClass.simpleName

    companion object {
        val VIEW_TYPE_NEWS = 1
        val VIEW_TYPE_HEADLINE = 2
        val VIEW_TYPE_LOADING = 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_NEWS -> {
                val view = layoutInflater.inflate(R.layout.item_news, null)
                ViewHolderNews(view)
            }
            VIEW_TYPE_HEADLINE -> {
                val view = layoutInflater.inflate(R.layout.item_headline_news, null)
                ViewHolderHeadlineNews(view)
            }
            else -> {
                val view = layoutInflater.inflate(R.layout.item_loading, null)
                ViewHolderLoadingNews(view)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val viewType = listViewTypeNews[position]
        when (viewType) {
            VIEW_TYPE_NEWS -> {
                val holderNews = holder as ViewHolderNews
                val articleNews = articlesNews[position]
                Glide.with(context)
                        .load(articleNews.urlToImage)
                        .asBitmap()
                        .placeholder(R.drawable.ic_image_black_24dp)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(object : SimpleTarget<Bitmap>() {
                            override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                                holderNews.itemView.image_view_cover_item_news.scaleType = ImageView.ScaleType.CENTER_CROP
                                holderNews.itemView.image_view_cover_item_news.setImageBitmap(resource)
                            }
                        })
                holderNews.itemView.text_view_title_item_news.text = articleNews.title
            }
            VIEW_TYPE_HEADLINE -> {
                val holderHeadlineNews = holder as ViewHolderHeadlineNews
                holderHeadlineNews.itemView.recycler_view_data_headline_news_item_headline_news
                        .layoutManager = LinearLayoutManager(context, LinearLayout.HORIZONTAL, false)
                holderHeadlineNews.itemView.recycler_view_data_headline_news_item_headline_news
                        .adapter = HeadlineNewsAdapter(context, articleHeadlineNews, listViewTypeHeadlineNews, listenerHeadlineNewsAdapter)
            }
            else -> {
                /** nothing to do in here */
            }
        }
    }

    override fun getItemCount(): Int = listViewTypeNews.size

    override fun getItemViewType(position: Int): Int = listViewTypeNews[position]

    open inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class ViewHolderNews(itemView: View) : ViewHolder(itemView) {

        init {
            itemView.relative_layout_content_item_news.setOnClickListener {
                listenerNewsAdapter.onClickNews(articlesNews[adapterPosition].url)
            }
        }

    }

    inner class ViewHolderHeadlineNews(itemView: View) : ViewHolder(itemView)

    inner class ViewHolderLoadingNews(itemView: View) : ViewHolder(itemView)

    interface ListenerNewsAdapter {

        fun onClickNews(url: String)

    }

}