package com.ysn.enews.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import com.ysn.enews.db.entity.FavoriteNews

/**
 * Created by yudisetiawan on 2/20/18.
 */

@Dao
interface FavoriteNewsDao {

    @Query("select * from favoritenews")
    fun getAllFavorites(): List<FavoriteNews>

    @Query("select * from favoritenews where url = :url")
    fun findFavoriteByUrl(url: String): FavoriteNews

    @Insert(onConflict = REPLACE)
    fun insertFavorite(favoriteNews: FavoriteNews)

    @Delete
    fun deleteFavorite(favoriteNews: FavoriteNews)
}