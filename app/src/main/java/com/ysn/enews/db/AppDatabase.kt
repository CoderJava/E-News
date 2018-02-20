/*
 * Created by Yudi Setiawan on 2/21/18 1:57 AM
 * Copyright (c) 2018. All rights reserved.
 *
 * Last modified 2/20/18 11:08 PM
 */

package com.ysn.enews.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.ysn.enews.db.dao.FavoriteHeadlineNewsDao
import com.ysn.enews.db.dao.FavoriteNewsDao
import com.ysn.enews.db.entity.FavoriteHeadlineNews
import com.ysn.enews.db.entity.FavoriteNews

/**
 * Created by yudisetiawan on 2/20/18.
 */

@Database(entities = [(FavoriteNews::class), (FavoriteHeadlineNews::class)], version = 1)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        private val DATABASE_NAME = "enews"
        var instance: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                        .build()
            }
            return instance as AppDatabase
        }
    }

    abstract fun favoriteNewsDao(): FavoriteNewsDao

    abstract fun favoriteHeadlineNewsDao(): FavoriteHeadlineNewsDao

}