package com.abid.githubuserapp.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.abid.githubuserapp.database.Favorite
import com.abid.githubuserapp.database.FavoriteDao
import com.abid.githubuserapp.database.FavoriteRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {
    private val mFavoriteDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteRoomDatabase.getDatabase(application)
        mFavoriteDao = db.favDao()
    }

    fun getAllFav(): LiveData<List<Favorite>> = mFavoriteDao.getFavorites()

    fun insert(favorite: Favorite){
        executorService.execute{mFavoriteDao.insert(favorite)}
    }

    fun delete(favorite: Favorite){
        executorService.execute{mFavoriteDao.delete(favorite)}
    }

    fun check(username: String): LiveData<Favorite> {
        return mFavoriteDao.checkFav(username)
    }

}