package com.example.eventlistingappv1.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.eventlistingappv1.data.local.FavoriteDao
import com.example.eventlistingappv1.data.local.FavoriteEntity
import com.example.eventlistingappv1.data.local.FavoriteRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {
    private val mFavoriteDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteRoomDatabase.getDatabase(application)
        mFavoriteDao = db.favoriteDao()
    }

    fun getAllFavorites(): LiveData<List<FavoriteEntity>> = mFavoriteDao.getAllFavorites()

    fun insert(favorite: FavoriteEntity) {
        executorService.execute { mFavoriteDao.insert(favorite) }
    }

    fun delete(favorite: FavoriteEntity) {
        executorService.execute { mFavoriteDao.delete(favorite) }
    }

    fun update(favorite: FavoriteEntity) {
        executorService.execute { mFavoriteDao.update(favorite) }
    }

    fun getFavoriteById(id: Int): LiveData<FavoriteEntity?> {
        return mFavoriteDao.getFavoritesById(id)
    }
}