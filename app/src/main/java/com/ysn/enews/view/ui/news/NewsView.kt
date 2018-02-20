/*
 * Created by Yudi Setiawan on 2/21/18 1:57 AM
 * Copyright (c) 2018. All rights reserved.
 *
 * Last modified 2/21/18 12:36 AM
 */

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

    fun clickUnfavorite()

    fun clickUnfavoriteFailed(message: String)

    fun clickFavorite()

    fun clickFavoriteFailed(message: String)

    fun scrollRecyclerViewProcess()

    fun scrollRecyclerView()

    fun scrollRecyclerViewFailed()

}