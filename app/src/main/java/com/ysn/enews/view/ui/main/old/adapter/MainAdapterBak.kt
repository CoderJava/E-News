package com.ysn.enews.view.ui.main.adapter

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.ysn.e_news.R
import kotlinx.android.synthetic.main.headline_news_bak.view.*

/**
 * Created by yudisetiawan on 2/19/18.
 */
class MainAdapterBak(private val context: Context, private val listData: List<String>, private val listViewType: List<Int>) : RecyclerView.Adapter<MainAdapterBak.ViewHolder>() {

    private val TAG = javaClass.simpleName
    companion object {
        val VIEW_TYPE_NEWS = 1
        val VIEW_TYPE_HEADLINE = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_NEWS -> {
                val view = layoutInflater.inflate(R.layout.item_news_bak, null)
                ViewHolderNews(view)
            }
            else -> {
                val view = layoutInflater.inflate(R.layout.headline_news_bak, null)
                ViewHolderHeadlineNews(view)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val viewType = listViewType[position]
        when (viewType) {
            VIEW_TYPE_NEWS -> {
            }
            else -> {
                val holderHeadlineNews = holder as ViewHolderHeadlineNews
                val headlineAdapter = HeadlineAdapterBak(listData)
                holderHeadlineNews.itemView
                        .recycler_view_item_headline_news.layoutManager = LinearLayoutManager(context, LinearLayout.HORIZONTAL, false)
                holderHeadlineNews.itemView
                        .recycler_view_item_headline_news.adapter = headlineAdapter

            }
        }
    }

    override fun getItemCount(): Int = listData.size

    override fun getItemViewType(position: Int): Int = listViewType[position]

    open inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class ViewHolderNews(itemView: View) : ViewHolder(itemView)

    inner class ViewHolderHeadlineNews(itemView: View) : ViewHolder(itemView)

}