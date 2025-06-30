package com.example.myapp.ui.Info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapp.R
import com.example.myapp.databinding.FragmentGalleryBinding
import com.example.myapp.model.ExamSubjectsFragment
import model.ExperienceFragment
import com.example.myapp.model.MajorsFragment
import com.example.myapp.model.UniversityFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class InfoFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!
    private var currentSelectedItemId = R.id.navigation_universities // 默认选中院校

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

        // 恢复保存的状态
        savedInstanceState?.let {
            currentSelectedItemId = it.getInt("SELECTED_ITEM", R.id.navigation_universities)
        }

        setupBottomNavigation()

        // 加载初始Fragment（如果当前没有添加的Fragment）
        if (savedInstanceState == null) {
            loadInitialFragment()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("SELECTED_ITEM", currentSelectedItemId)
    }

    private fun setupBottomNavigation() {
        val bottomNav = binding.root.findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // 设置当前选中项
        bottomNav.selectedItemId = currentSelectedItemId

        bottomNav.setOnNavigationItemSelectedListener { item ->
            currentSelectedItemId = item.itemId
            when (item.itemId) {
                R.id.navigation_universities -> {
                    replaceFragment(UniversityFragment.newInstance())
                    true
                }
                R.id.navigation_majors -> {
                    replaceFragment(MajorsFragment.newInstance())
                    true
                }
                R.id.navigation_exam_subjects -> {
                    replaceFragment(ExamSubjectsFragment.newInstance())
                    true
                }
                R.id.navigation_experience -> {
                    replaceFragment(ExperienceFragment.newInstance())
                    true
                }
                else -> false
            }
        }
    }

    private fun loadInitialFragment() {
        val fragment = when (currentSelectedItemId) {
            R.id.navigation_majors -> MajorsFragment.newInstance()
            R.id.navigation_exam_subjects -> ExamSubjectsFragment.newInstance()
            R.id.navigation_experience -> ExperienceFragment.newInstance()
            else -> UniversityFragment.newInstance()
        }
        replaceFragment(fragment)
    }

    private fun replaceFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .setReorderingAllowed(true)
            .commitNowAllowingStateLoss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}