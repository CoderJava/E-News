/*
 * Created by Yudi Setiawan on 2/21/18 1:57 AM
 * Copyright (c) 2018. All rights reserved.
 *
 * Last modified 2/20/18 11:44 PM
 */

package com.ysn.enews.db.dao

import android.arch.persistence.room.*
import com.ysn.enews.db.entity.FavoriteHeadlineNews

/**
 * Created by yudisetiawan on 2/20/18.
 */

@Dao
interface FavoriteHeadlineNewsDao {

    @Query("select * from favoriteheadlinenews")
    fun getAllFavorites(): List<FavoriteHeadlineNews>

    @Query("select * from favoriteheadlinenews where url = :url")
    fun findFavoriteByUrl(url: String): FavoriteHeadlineNews

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(favoriteHeadlineNews: FavoriteHeadlineNews)

    @Delete
    fun deleteFavorite(favoriteHeadlineNews: FavoriteHeadlineNews)

}