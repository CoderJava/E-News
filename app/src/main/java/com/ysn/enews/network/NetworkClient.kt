package com.ysn.enews.network

import com.ysn.e_news.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by yudisetiawan on 2/19/18.
 */
object NetworkClient {

    object RetrofitNews {
        private var retrofitNews: Retrofit? = null

        fun getRetrofitNews(): Retrofit? {
            if (retrofitNews == null) {
                val builder = OkHttpClient.Builder()
                val okHttpClient = builder.build()
                retrofitNews = Retrofit.Builder()
                        .baseUrl(BuildConfig.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(okHttpClient)
                        .build()
            }
            return retrofitNews
        }
    }

}