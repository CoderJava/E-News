package com.ysn.enews.view.ui.news

import android.content.Context
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import com.ysn.enews.api.Endpoints
import com.ysn.enews.model.News
import com.ysn.enews.network.NetworkClient
import com.ysn.enews.view.base.mvp.MvpPresenter
import com.ysn.enews.view.ui.news.adapter.HeadlineNewsAdapter
import com.ysn.enews.view.ui.news.adapter.NewsAdapter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

/**
 * Created by yudisetiawan on 2/19/18.
 */
class NewsPresenter : MvpPresenter<NewsView> {

    private val TAG = javaClass.simpleName
    private var newsView: NewsView? = null
    private var newsAdapter: NewsAdapter? = null

    override fun onAttach(mvpView: NewsView) {
        newsView = mvpView
    }

    override fun onDetach() {
        newsView = null
    }

    fun onLoadData() {
        val context = newsView?.getViewContext()
        val endpoints = NetworkClient.RetrofitNews
                .getRetrofitNews()
                ?.create(Endpoints::class.java)
        val observableNews = endpoints!!.getNews("cnn", 10, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

        val observableHeadlineNews = endpoints.getHeadlineNews("bbc-news", 5, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

        Observable
                .zip(
                        observableNews,
                        observableHeadlineNews,
                        BiFunction<News, News, Map<String, Any>> { articleNews, articleHeadlineNews ->
                            val mapData = HashMap<String, Any>()
                            try {
                                val listArticleNews = articleNews.articles
                                val listArticleHeadlineNews = articleHeadlineNews.articles
                                val listViewTypeNews = ArrayList<Int>()
                                val listViewTypeHeadlineNews = ArrayList<Int>()

                                for (a in listArticleNews.indices) {
                                    if (a == 5) {
                                        listViewTypeNews.add(NewsAdapter.VIEW_TYPE_HEADLINE)
                                    } else {
                                        listViewTypeNews.add(NewsAdapter.VIEW_TYPE_NEWS)
                                    }
                                }
                                for (a in listArticleHeadlineNews.indices) {
                                    listViewTypeHeadlineNews.add(HeadlineNewsAdapter.VIEW_TYPE_CONTENT)
                                }
                                newsAdapter = NewsAdapter(
                                        context!!,
                                        listArticleNews,
                                        listArticleHeadlineNews,
                                        listViewTypeNews,
                                        listViewTypeHeadlineNews,
                                        object : NewsAdapter.ListenerNewsAdapter {
                                            override fun onClickNews(url: String) {
                                                launchChromeCustomTabs(context, url)
                                            }
                                        },
                                        object : HeadlineNewsAdapter.ListenerHeadlineNewsAdapter {
                                            override fun onClickHeadlineNews(url: String) {
                                                launchChromeCustomTabs(context, url)
                                            }
                                        }
                                )
                                mapData["error"] = false
                            } catch (e: Exception) {
                                e.printStackTrace()
                                mapData["error"] = true
                                mapData["message"] = e.message!!
                            }
                            mapData
                        }
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { mapData: Map<String, Any> ->
                            val error = mapData["error"] as Boolean
                            if (error) {
                                val message = mapData["message"] as String
                                newsView?.loadDataFailed(message)
                            } else {
                                newsView?.loadData(newsAdapter)
                            }
                        }
                )
    }

    private fun launchChromeCustomTabs(context: Context?, url: String) {
        val customTabsIntent = CustomTabsIntent.Builder().build()
        customTabsIntent.launchUrl(context, Uri.parse(url))
    }

}