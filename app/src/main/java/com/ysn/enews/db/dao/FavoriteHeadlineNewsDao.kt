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