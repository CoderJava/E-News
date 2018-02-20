package com.ysn.enews.view.ui.news

import android.content.Context
import com.ysn.enews.view.base.mvp.MvpView
import com.ysn.enews.view.ui.news.adapter.NewsAdapter

/**
 * Created by yudisetiawan on 2/19/18.
 */
interface NewsView : MvpView {

    fun getViewContext(): Context

    fun loadData(newsAdapter: NewsAdapter?)

    fun loadDataFailed(message: String)

}