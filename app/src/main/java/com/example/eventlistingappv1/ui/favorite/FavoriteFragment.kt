package com.example.eventlistingappv1.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.eventlistingappv1.databinding.FragmentFavoriteBinding
import com.example.eventlistingappv1.helper.ViewModelFactory

class FavoriteFragment : Fragment() {

    private var _binding : FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var favoriteViewModel: FavoriteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteViewModel = obtainViewModel()

        favoriteViewModel.getAllFavorites().observe(viewLifecycleOwner) {favoriteEvents ->
            if (favoriteEvents.isNotEmpty()) {
                binding.tvTextFavorite.text = favoriteEvents.joinToString { it.name ?: ""}
            } else {
                binding.tvTextFavorite.text = "tidak ada event Favorite"
            }
        }
    }

    private fun obtainViewModel(): FavoriteViewModel {
        val factory = ViewModelFactory.getInstace(requireActivity().application)
        return ViewModelProvider(this, factory)[FavoriteViewModel::class.java]
    }
}