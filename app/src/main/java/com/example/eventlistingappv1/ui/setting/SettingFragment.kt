package com.example.eventlistingappv1.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.eventlistingappv1.databinding.FragmentSettingBinding
import com.example.eventlistingappv1.helper.ViewModelFactory

class SettingFragment : Fragment() {

    private var _binding : FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private val settingViewModel: SettingViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val switchTheme = binding.switchTheme

        settingViewModel.getThemeSettings().observe(requireActivity()) {isDarkModeActive ->
            switchTheme.isChecked = isDarkModeActive
        }

        switchTheme.setOnCheckedChangeListener { _, isChecked: Boolean ->
            settingViewModel.saveThemeSetting(isChecked)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}