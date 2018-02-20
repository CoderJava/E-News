package com.ysn.enews.view.ui.news

import android.content.Context
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.ysn.enews.api.Endpoints
import com.ysn.enews.db.AppDatabase
import com.ysn.enews.db.entity.FavoriteHeadlineNews
import com.ysn.enews.db.entity.FavoriteNews
import com.ysn.enews.model.Article
import com.ysn.enews.model.News
import com.ysn.enews.network.NetworkClient
import com.ysn.enews.view.base.mvp.MvpPresenter
import com.ysn.enews.view.ui.news.adapter.HeadlineNewsAdapter
import com.ysn.enews.view.ui.news.adapter.NewsAdapter
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function4
import io.reactivex.schedulers.Schedulers

/**
 * Created by yudisetiawan on 2/19/18.
 */
class NewsPresenter : MvpPresenter<NewsView> {

    private val TAG = javaClass.simpleName
    private var newsView: NewsView? = null
    private var listArticleNews = ArrayList<Article>()
    private var listArticleHeadlineNews = ArrayList<Article>()
    private var listViewTypeNews = ArrayList<Int>()
    private var listViewTypeHeadlineNews = ArrayList<Int>()
    private var newsAdapter: NewsAdapter? = null
    private var currentPageNews: Int = 1
    private var currentPageHeadlineNews: Int = 1
    private var lastPageResultNews: Int = 1
    private var lastPageResultHeadlineNews: Int = 1
    private var listFavoriteNewsResult = ArrayList<Boolean>()
    private var loading: Boolean = false
    private var linearLayoutManager: LinearLayoutManager? = null

    override fun onAttach(mvpView: NewsView) {
        newsView = mvpView
    }

    override fun onDetach() {
        newsView = null
    }

    fun onLoadData() {
        loading = true
        val context = newsView?.getViewContext()
        val endpoints = NetworkClient.RetrofitNews
                .getRetrofitNews()
                ?.create(Endpoints::class.java)
        val observableNews = endpoints!!.getNews("cnn", 10, currentPageNews)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

        val observableHeadlineNews = endpoints.getHeadlineNews("bbc-news", 5, currentPageHeadlineNews)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

        val observableFavoriteNews = Observable
                .create<List<FavoriteNews>> { emitter: ObservableEmitter<List<FavoriteNews>> ->
                    val listFavoriteNews = AppDatabase.getInstance(context!!)
                            .favoriteNewsDao()
                            .getAllFavorites()
                    emitter.onNext(listFavoriteNews)
                    emitter.onComplete()
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

        val observableFavoriteHeadlineNews = Observable
                .create<List<FavoriteHeadlineNews>> { emitter: ObservableEmitter<List<FavoriteHeadlineNews>> ->
                    val listFavoriteHeadlineNews = AppDatabase.getInstance(context!!)
                            .favoriteHeadlineNewsDao()
                            .getAllFavorites()
                    emitter.onNext(listFavoriteHeadlineNews)
                    emitter.onComplete()
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

        Observable
                .zip(
                        observableNews,
                        observableHeadlineNews,
                        observableFavoriteNews,
                        observableFavoriteHeadlineNews,
                        Function4<News, News, List<FavoriteNews>, List<FavoriteHeadlineNews>, Map<String, Any>> { articleNews, articleHeadlineNews, listFavoriteNews, listFavoriteHeadlineNews ->
                            val mapData = HashMap<String, Any>()
                            try {
                                listArticleNews = articleNews.articles as ArrayList<Article>
                                listArticleHeadlineNews = articleHeadlineNews.articles as ArrayList<Article>
                                listViewTypeNews = ArrayList()
                                listViewTypeHeadlineNews = ArrayList()

                                val totalResultNews = articleNews.totalResults
                                val totalResultHeadlineNews = articleHeadlineNews.totalResults

                                val modulusTotalResultNews = totalResultNews % 10
                                lastPageResultNews = if (modulusTotalResultNews == 0) {
                                    totalResultNews.div(10)
                                } else {
                                    totalResultNews.div(10).plus(1)
                                }

                                val modulusTotalResultHeadlineNews = totalResultHeadlineNews % 10
                                lastPageResultHeadlineNews = if (modulusTotalResultHeadlineNews == 0) {
                                    totalResultHeadlineNews.div(5)
                                } else {
                                    totalResultHeadlineNews.div(10).plus(1)
                                }


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

                                listFavoriteNewsResult = ArrayList<Boolean>()
                                listArticleNews
                                        .map { datumFavoriteNews -> listFavoriteNews.any { it.url == datumFavoriteNews.url } }
                                        .forEach {
                                            if (it) {
                                                listFavoriteNewsResult.add(true)
                                            } else {
                                                listFavoriteNewsResult.add(false)
                                            }
                                        }

                                val listFavoriteHeadlineNewsResult = ArrayList<Boolean>()
                                listArticleHeadlineNews
                                        .map { datumFavoriteHeadlineNews -> listFavoriteHeadlineNews.any { it.url == datumFavoriteHeadlineNews.url } }
                                        .forEach {
                                            if (it) {
                                                listFavoriteHeadlineNewsResult.add(true)
                                            } else {
                                                listFavoriteHeadlineNewsResult.add(false)
                                            }
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

                                            override fun onClickFavorite(article: Article) {
                                                Observable
                                                        .create<Map<String, Any>> { emitter: ObservableEmitter<Map<String, Any>> ->
                                                            val mapData = HashMap<String, Any>()
                                                            val dataLocalFavoriteNews = AppDatabase.getInstance(context)
                                                                    .favoriteNewsDao()
                                                                    .findFavoriteByUrl(article.url)
                                                            if (dataLocalFavoriteNews != null) {
                                                                // Unfavorite news
                                                                AppDatabase.getInstance(context)
                                                                        .favoriteNewsDao()
                                                                        .deleteFavorite(dataLocalFavoriteNews)
                                                                mapData["type"] = "unfavorite"
                                                            } else {
                                                                // Favorite news
                                                                val favoriteNews = FavoriteNews(title = article.title, description = article.description, url = article.url)
                                                                AppDatabase.getInstance(context)
                                                                        .favoriteNewsDao()
                                                                        .insertFavorite(favoriteNews)
                                                                mapData["type"] = "favorite"
                                                            }
                                                            emitter.onNext(mapData)
                                                            emitter.onComplete()
                                                        }
                                                        .subscribeOn(Schedulers.io())
                                                        .observeOn(AndroidSchedulers.mainThread())
                                                        .subscribe(
                                                                { mapData: Map<String, Any> ->
                                                                    if ((mapData["type"] as String).equals("unfavorite", true)) {
                                                                        newsView?.clickUnfavorite()
                                                                        newsAdapter?.refreshItemUnfavorite(article)
                                                                    } else {
                                                                        newsView?.clickFavorite()
                                                                        newsAdapter?.refreshItemFavorite(article)
                                                                    }
                                                                },
                                                                { throwable: Throwable ->
                                                                    throwable.printStackTrace()
                                                                }
                                                        )
                                            }

                                            override fun onLoadMoreData() {
                                                if (linearLayoutManager != null) {
                                                    onLoadMoreDataFromServer()
                                                }
                                            }
                                        },
                                        object : HeadlineNewsAdapter.ListenerHeadlineNewsAdapter {
                                            override fun onClickHeadlineNews(url: String) {
                                                launchChromeCustomTabs(context, url)
                                            }

                                            override fun onClickFavorite(article: Article) {
                                                Observable
                                                        .create<Map<String, Any>> { emitter: ObservableEmitter<Map<String, Any>> ->
                                                            val mapData = HashMap<String, Any>()
                                                            val dataLocalFavoriteHeadlineNews = AppDatabase.getInstance(context)
                                                                    .favoriteHeadlineNewsDao()
                                                                    .findFavoriteByUrl(article.url)
                                                            if (dataLocalFavoriteHeadlineNews != null) {
                                                                // Unfavorite headline news
                                                                AppDatabase.getInstance(context)
                                                                        .favoriteHeadlineNewsDao()
                                                                        .deleteFavorite(dataLocalFavoriteHeadlineNews)
                                                                mapData["type"] = "unfavorite"
                                                            } else {
                                                                // favorite headline news
                                                                val favoriteHeadlineNews = FavoriteHeadlineNews(title = article.title, description = article.description, url = article.url)
                                                                AppDatabase.getInstance(context)
                                                                        .favoriteHeadlineNewsDao()
                                                                        .insertFavorite(favoriteHeadlineNews)
                                                                mapData["type"] = "favorite"
                                                            }
                                                            emitter.onNext(mapData)
                                                            emitter.onComplete()
                                                        }
                                                        .subscribeOn(Schedulers.io())
                                                        .observeOn(AndroidSchedulers.mainThread())
                                                        .subscribe(
                                                                { mapData: Map<String, Any> ->
                                                                    if ((mapData["type"] as String).equals("unfavorite", true)) {
                                                                        newsView?.clickUnfavorite()
                                                                        newsAdapter?.refreshItemUnfavoriteHeadlineNews(article)
                                                                    } else {
                                                                        newsView?.clickFavorite()
                                                                        newsAdapter?.refreshItemFavoriteHeadlineNews(article)
                                                                    }
                                                                }
                                                        )
                                            }
                                        },
                                        listFavoriteNewsResult,
                                        listFavoriteHeadlineNewsResult
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
                            loading = false
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

    fun onScrollRecyclerView(linearLayoutManager: LinearLayoutManager, dy: Int) {
        this.linearLayoutManager = linearLayoutManager
        if (dy > 0) {
            val totalItemCount = linearLayoutManager.itemCount
            val isEndOfList = linearLayoutManager.findLastCompletelyVisibleItemPosition() == totalItemCount - 1
            if (isEndOfList && !loading && currentPageNews < lastPageResultNews) {
                loading = true
                newsView?.scrollRecyclerViewProcess()
                newsAdapter?.onLoadMoreData()
                Log.d(TAG, "lastPageResultNews: $lastPageResultNews")
            }
        }
    }

    private fun onLoadMoreDataFromServer() {
        Log.d(TAG, "onLoadMoreDataFromServer")
        val context = newsView?.getViewContext()
        val endpoints = NetworkClient.RetrofitNews
                .getRetrofitNews()
                ?.create(Endpoints::class.java)
        val observableNews = endpoints!!.getNews("cnn", 10, currentPageNews + 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

        val observableFavoriteNews = Observable
                .create<List<FavoriteNews>> { emitter: ObservableEmitter<List<FavoriteNews>> ->
                    val listFavoriteNews = AppDatabase.getInstance(context!!)
                            .favoriteNewsDao()
                            .getAllFavorites()
                    emitter.onNext(listFavoriteNews)
                    emitter.onComplete()
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

        Observable
                .zip(
                        observableNews,
                        observableFavoriteNews,
                        io.reactivex.functions.BiFunction<News, List<FavoriteNews>, Map<String, Any>> { articleNews, listFavoriteNews ->
                            val mapData = HashMap<String, Any>()

                            val totalResultNews = articleNews.totalResults
                            val modulusTotalResultNews = totalResultNews % 10
                            lastPageResultNews = if (modulusTotalResultNews == 0) {
                                totalResultNews.div(10)
                            } else {
                                totalResultNews.div(10).plus(1)
                            }

                            val listArticleNewsRefresh = articleNews.articles
                            val listViewTypeNewsRefresh = ArrayList<Int>()
                            for (a in listArticleNewsRefresh.indices) {
                                listViewTypeNewsRefresh.add(NewsAdapter.VIEW_TYPE_NEWS)
                            }

                            val listFavoriteNewsResultRefresh = ArrayList<Boolean>()
                            listArticleNewsRefresh
                                    .map { datumFavoriteNews -> listFavoriteNews.any { it.url == datumFavoriteNews.url } }
                                    .forEach {
                                        if (it) {
                                            listFavoriteNewsResultRefresh.add(true)
                                        } else {
                                            listFavoriteNewsResultRefresh.add(false)
                                        }
                                    }

                            listArticleNews.addAll(listArticleNewsRefresh)
                            listViewTypeNews.addAll(listViewTypeNewsRefresh)
                            listFavoriteNewsResult.addAll(listFavoriteNewsResultRefresh)
                            mapData["error"] = false
                            mapData
                        }
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { mapData: Map<String, Any> ->
                            loading = false
                            val error = mapData["error"] as Boolean
                            if (error) {
                                newsView?.scrollRecyclerViewFailed()
                            } else {
                                newsAdapter?.refreshNews(listArticleNews, listViewTypeNews, listFavoriteNewsResult)
                                currentPageNews += 1
                                newsView?.scrollRecyclerView()
                            }
                        }
                )
    }

}
