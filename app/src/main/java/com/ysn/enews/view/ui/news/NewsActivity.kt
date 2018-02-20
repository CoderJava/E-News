package com.ysn.enews.view.ui.news

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import com.ysn.e_news.R
import com.ysn.enews.view.ui.news.adapter.NewsAdapter
import kotlinx.android.synthetic.main.activity_news.*

class NewsActivity : AppCompatActivity(), NewsView {

    private val TAG = javaClass.simpleName
    private var newsPresenter: NewsPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        initPresenter()
        onAttachView()
        doLoadData()
    }

    private fun doLoadData() {
        showLoading()
        newsPresenter?.onLoadData()
    }

    override fun getViewContext(): Context {
        return this
    }

    private fun showLoading() {
        progress_bar_activity_news.visibility = VISIBLE
        recycler_view_data_news_activity_news.visibility = GONE
    }

    private fun hideLoading() {
        progress_bar_activity_news.visibility = GONE
        recycler_view_data_news_activity_news.visibility = VISIBLE
    }

    private fun initPresenter() {
        newsPresenter = NewsPresenter()
    }

    override fun onDestroy() {
        super.onDestroy()
        onDetachView()
    }

    override fun onAttachView() {
        newsPresenter?.onAttach(this)
    }

    override fun onDetachView() {
        newsPresenter?.onDetach()
    }

    override fun loadData(newsAdapter: NewsAdapter?) {
        hideLoading()
        recycler_view_data_news_activity_news.layoutManager = LinearLayoutManager(this)
        recycler_view_data_news_activity_news.adapter = newsAdapter
    }

    override fun loadDataFailed(message: String) {
        hideLoading()
        showToast(message, Toast.LENGTH_LONG)
    }

    fun Activity.showToast(message: String, duration: Int) {
        Toast.makeText(this, message, duration)
                .show()
    }
}
