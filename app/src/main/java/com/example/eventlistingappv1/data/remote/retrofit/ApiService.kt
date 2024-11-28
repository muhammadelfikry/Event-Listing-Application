package com.example.eventlistingappv1.data.remote.retrofit

import com.example.eventlistingappv1.data.remote.response.EventDetailResponse
import com.example.eventlistingappv1.data.remote.response.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    fun getEvent(
        @Query("active") active: Int
    ): Call<EventResponse>

    @GET("events/{id}")
    fun getDetailEvent(
        @Path("id") id: Int
    ): Call<EventDetailResponse>
}