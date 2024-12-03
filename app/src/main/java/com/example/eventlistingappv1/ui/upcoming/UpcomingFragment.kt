package com.example.eventlistingappv1.ui.upcoming

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventlistingappv1.data.remote.response.EventResponse
import com.example.eventlistingappv1.data.remote.response.ListEventsItem
import com.example.eventlistingappv1.data.remote.retrofit.ApiConfig
import com.example.eventlistingappv1.databinding.FragmentUpcomingBinding
import com.example.eventlistingappv1.ui.DetailActivity
import com.example.eventlistingappv1.ui.EventAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpcomingFragment : Fragment() {

    private var _binding : FragmentUpcomingBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvEvent.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvEvent.addItemDecoration(itemDecoration)

        getEventList()
    }

    private fun getEventList() {
        showLoading(true)
        val client = ApiConfig.getApiService().getEvent(active = ACTIVE)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setEventData(responseBody.listEvents)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.body()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setEventData(event: List<ListEventsItem>) {
        val adapter = EventAdapter { selectedEvent ->
            val intent = Intent(requireActivity(), DetailActivity::class.java).apply {
                putExtra("EVENT_ID", selectedEvent.id)
            }
            startActivity(intent)
        }
        adapter.submitList(event)
        binding.rvEvent.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        private const val TAG = "UpcomingFragment"
        private const val ACTIVE = 1
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}