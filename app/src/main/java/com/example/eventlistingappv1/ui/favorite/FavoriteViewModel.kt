package com.example.eventlistingappv1.ui.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.eventlistingappv1.data.local.FavoriteEntity
import com.example.eventlistingappv1.repository.FavoriteRepository

class FavoriteViewModel(application: Application) : ViewModel() {
    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    fun getAllFavorites(): LiveData<List<FavoriteEntity>> = mFavoriteRepository.getAllFavorites()
}