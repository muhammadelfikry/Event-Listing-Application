package com.example.eventlistingappv1.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.example.eventlistingappv1.R
import com.example.eventlistingappv1.data.remote.response.Event
import com.example.eventlistingappv1.data.remote.response.EventDetailResponse
import com.example.eventlistingappv1.data.remote.retrofit.ApiConfig
import com.example.eventlistingappv1.databinding.ActivityDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getEventDetail()
    }
    private fun getEventDetail() {
        val eventId = intent.getIntExtra("EVENT_ID", -1)
        val client = ApiConfig.getApiService().getDetailEvent(id = eventId)
        client.enqueue(object : Callback<EventDetailResponse> {
            override fun onResponse(
                call: Call<EventDetailResponse>,
                response: Response<EventDetailResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setEventData(responseBody.event)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.body()}")
                }
            }

            override fun onFailure(call: Call<EventDetailResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setEventData(event: Event) {
        binding.tvItemNameDetail.text = event.name
        binding.tvItemOwnerDetail.text = event.ownerName
        binding.tvItemTimeDetail.text = event.beginTime
        binding.tvItemQuotaDetail.text = getString(R.string.remaining_quota, event.quota.toString())
        binding.tvItemDescDetail.text = HtmlCompat.fromHtml(
            event.description,
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )
        Glide.with(binding.root.context)
            .load(event.mediaCover)
            .into(binding.tvItemPhotoDetail)

        binding.btnItemRegisterDetail.setOnClickListener {
            val data = Uri.parse(event.link)
            val intent = Intent(Intent.ACTION_VIEW, data)
            startActivity(intent)
        }
    }

    companion object {
        private const val TAG = "DetailActivity"
    }
}