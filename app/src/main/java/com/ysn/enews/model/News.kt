/*
 * Created by Yudi Setiawan on 2/21/18 1:57 AM
 * Copyright (c) 2018. All rights reserved.
 *
 * Last modified 2/19/18 10:16 PM
 */

package com.ysn.enews.model
import com.google.gson.annotations.SerializedName


/**
 * Created by yudisetiawan on 2/19/18.
 */

data class News(
		@SerializedName("status") val status: String,
		@SerializedName("totalResults") val totalResults: Int,
		@SerializedName("articles") val articles: List<Article>
)

data class Article(
		@SerializedName("source") val source: Source,
		@SerializedName("author") val author: String,
		@SerializedName("title") val title: String,
		@SerializedName("description") val description: String,
		@SerializedName("url") val url: String,
		@SerializedName("urlToImage") val urlToImage: String,
		@SerializedName("publishedAt") val publishedAt: String
)

data class Source(
		@SerializedName("id") val id: String,
		@SerializedName("name") val name: String
)