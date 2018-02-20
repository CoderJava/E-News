/*
 * Created by Yudi Setiawan on 2/21/18 1:57 AM
 * Copyright (c) 2018. All rights reserved.
 *
 * Last modified 2/20/18 10:36 PM
 */

package com.ysn.enews.db.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by yudisetiawan on 2/20/18.
 */

@Entity(tableName = "favoriteheadlinenews")
data class FavoriteHeadlineNews(
        @ColumnInfo(name = "id")
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0,
        @ColumnInfo(name = "title")
        var title: String = "",
        @ColumnInfo(name = "description")
        var description: String = "",
        @ColumnInfo(name = "url")
        var url: String = ""
)
