package com.example.eventlistingappv1.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventlistingappv1.data.remote.response.ListEventsItem
import com.example.eventlistingappv1.databinding.FragmentFavoriteBinding
import com.example.eventlistingappv1.helper.ViewModelFactory
import com.example.eventlistingappv1.ui.DetailActivity
import com.example.eventlistingappv1.ui.EventAdapter

class FavoriteFragment : Fragment() {

    private var _binding : FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvEvent.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvEvent.addItemDecoration(itemDecoration)

        favoriteViewModel = obtainViewModel()

        setFavoriteData()

        observeFavoriteEvents()
    }

    private fun observeFavoriteEvents() {
        favoriteViewModel.getAllFavorites().observe(viewLifecycleOwner) {favoriteEntities ->
            if (favoriteEntities.isNotEmpty()) {
                val favoriteEvents = favoriteEntities.map { favorite ->
                    ListEventsItem(
                        summary = "",
                        mediaCover = "",
                        registrants = 0,
                        imageLogo = favorite.imageLogo ?: "",
                        link = "",
                        ownerName = "",
                        description = "",
                        cityName = "",
                        quota = 0,
                        name = favorite.name ?: "",
                        id = favorite.id,
                        beginTime = "",
                        endTime = "",
                        category = ""
                    )
                }
                eventAdapter.submitList(favoriteEvents)
                binding.tvEmptyFavorites.visibility = View.GONE
            } else {
                eventAdapter.submitList(emptyList())
                binding.tvEmptyFavorites.visibility = View.VISIBLE
            }
        }
    }
    private fun setFavoriteData() {
        eventAdapter = EventAdapter { event ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("EVENT_ID", event.id)
            startActivity(intent)
        }

        binding.rvEvent.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = eventAdapter
        }
    }

    private fun obtainViewModel(): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(requireActivity().application)
        return ViewModelProvider(this, factory)[FavoriteViewModel::class.java]
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}