package com.example.myapp.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapp.R
import com.example.myapp.databinding.FragmentGalleryBinding
import model.ExperienceFragment
import model.MajorsFragment
import com.example.myapp.ui.UniversityFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 设置底部导航
        setupBottomNavigation()

        // 默认显示院校Fragment
        if (savedInstanceState == null) {
            loadFragment(UniversityFragment.newInstance())
        }
    }

    private fun setupBottomNavigation() {
        val bottomNav = binding.root.findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_universities -> {
                    loadFragment(UniversityFragment.newInstance())
                    true
                }
                R.id.navigation_majors -> {
                    loadFragment(MajorsFragment.newInstance())
                    true
                }
                R.id.navigation_experience -> {
                    loadFragment(ExperienceFragment.newInstance())
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}