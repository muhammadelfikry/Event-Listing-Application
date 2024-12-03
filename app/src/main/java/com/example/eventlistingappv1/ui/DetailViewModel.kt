package com.example.eventlistingappv1.ui

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.eventlistingappv1.data.local.FavoriteEntity
import com.example.eventlistingappv1.repository.FavoriteRepository

class DetailViewModel(application: Application) : ViewModel() {
    private val mFavoriteRepository : FavoriteRepository = FavoriteRepository(application)

    fun insert(favorite: FavoriteEntity) {
        mFavoriteRepository.insert(favorite)
    }

    fun delete(favorite: FavoriteEntity) {
        mFavoriteRepository.delete(favorite)
    }

    fun getFavoriteById(id: Int): LiveData<FavoriteEntity?> {
        return mFavoriteRepository.getFavoriteById(id)
    }
}