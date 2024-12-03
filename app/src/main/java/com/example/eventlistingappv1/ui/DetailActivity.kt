package com.example.eventlistingappv1.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.eventlistingappv1.R
import com.example.eventlistingappv1.data.local.FavoriteEntity
import com.example.eventlistingappv1.data.remote.response.Event
import com.example.eventlistingappv1.data.remote.response.EventDetailResponse
import com.example.eventlistingappv1.data.remote.retrofit.ApiConfig
import com.example.eventlistingappv1.databinding.ActivityDetailBinding
import com.example.eventlistingappv1.helper.ViewModelFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        detailViewModel = obtainViewModel(this@DetailActivity)

        getEventDetail()
    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailViewModel {
        val factory = ViewModelFactory.getInstace(activity.application)
        return ViewModelProvider(activity, factory).get(DetailViewModel::class.java)
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

        detailViewModel.getFavoriteById(event.id).observe(this) { favorite ->
            if (favorite != null) {
                isFavorite = true
                binding.fabFavorite.setImageResource(R.drawable.ic_favorite_black_24dp)
            } else {
                isFavorite = false
                binding.fabFavorite.setImageResource(R.drawable.ic_favorite_border_black_24dp)
            }
        }

        binding.fabFavorite.setOnClickListener {
            if (isFavorite) {
                val favorite = FavoriteEntity(
                    id = event.id,
                    name = event.name,
                    imageLogo = event.imageLogo
                )
                detailViewModel.delete(favorite)
                Toast.makeText(this, "Event dihapus dari favorite!", Toast.LENGTH_SHORT).show()
                isFavorite = false
                binding.fabFavorite.setImageResource(R.drawable.ic_favorite_border_black_24dp)
            } else {
                val favorite = FavoriteEntity(
                    id = event.id,
                    name = event.name,
                    imageLogo = event.imageLogo
                )
                detailViewModel.insert(favorite)
                Toast.makeText(this, "Event disimpan ke favorite!", Toast.LENGTH_SHORT).show()
                isFavorite = true
                binding.fabFavorite.setImageResource(R.drawable.ic_favorite_black_24dp)
            }
        }
    }

    companion object {
        private const val TAG = "DetailActivity"
    }
}