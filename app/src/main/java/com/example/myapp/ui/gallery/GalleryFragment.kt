package com.example.myapp.ui.gallery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapp.R
import com.example.myapp.databinding.FragmentGalleryBinding
import com.example.myapp.model.ExamSubjectsFragment
import com.example.myapp.model.ExperienceFragment
import com.example.myapp.model.MajorsFragment
import com.example.myapp.model.UniversityFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class GalleryFragment : Fragment() {

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
        val bottomNav = binding.root.findViewById<BottomNavigationView>(R.id.bottom_navigation).apply {
            selectedItemId = currentSelectedItemId

            setOnNavigationItemSelectedListener { item ->
                currentSelectedItemId = item.itemId
                when (item.itemId) {
                    R.id.navigation_universities ->
                        safeReplaceFragment(UniversityFragment.newInstance())
                    R.id.navigation_majors ->
                        safeReplaceFragment(MajorsFragment.newInstance())
                    R.id.navigation_exam_subjects ->
                        safeReplaceFragment(ExamSubjectsFragment.newInstance())
                    R.id.navigation_experience ->
                        safeReplaceFragment(ExperienceFragment.newInstance())
                    else -> false
                }.also { result ->
                    if (!result) {
                        Log.e("GalleryFragment", "Fragment替换失败: ${item.title}")
                    }
                }
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
        if (!isAdded || isDetached) return

        childFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .setReorderingAllowed(true)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun safeReplaceFragment(fragment: Fragment): Boolean {
        return if (isAdded && !isDetached) {
            childFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commitAllowingStateLoss()
            true
        } else {
            false
        }
    }
}