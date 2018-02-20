package com.ysn.enews.api

import com.ysn.e_news.BuildConfig
import com.ysn.enews.model.News
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * Created by yudisetiawan on 2/19/18.
 */
interface Endpoints {

    @Headers("Authorization: ${BuildConfig.API_KEY}")
    @GET("top-headlines")
    fun getHeadlineNews(
            @Query("sources") sources: String,
            @Query("pageSize") pageSize: Int,
            @Query("page") page: Int
    ): Observable<News>

    @Headers("Authorization: ${BuildConfig.API_KEY}")
    @GET("everything")
    fun getNews(
            @Query("sources") sources: String,
            @Query("pageSize") pageSize: Int,
            @Query("page") page: Int
    ): Observable<News>

}