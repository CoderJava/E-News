package com.ysn.enews.view.ui.main.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ysn.e_news.R

/**
 * Created by yudisetiawan on 2/19/18.
 */
class HeadlineAdapterBak(private val listData: List<String>) : RecyclerView.Adapter<HeadlineAdapterBak.ViewHolderHeadline>() {

    private val TAG = javaClass.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderHeadline {
        return ViewHolderHeadline(LayoutInflater.from(parent.context).inflate(R.layout.item_headline_news_bak, null))
    }

    override fun onBindViewHolder(holder: ViewHolderHeadline, position: Int) {

    }

    override fun getItemCount(): Int = listData.size

    class ViewHolderHeadline(itemView: View) : RecyclerView.ViewHolder(itemView)

}