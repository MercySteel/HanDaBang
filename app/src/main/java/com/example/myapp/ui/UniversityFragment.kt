package com.example.myapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapp.data.DataProvider
import com.example.myapp.databinding.FragmentUniversityBinding
import com.example.myapp.model.University

class UniversityFragment : Fragment() {

    private var _binding: FragmentUniversityBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUniversityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 获取院校数据（包含985/211标识和分数线）
        val universities = DataProvider.getUniversitiesWithScores()

        // 设置RecyclerView
        binding.universityRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = UniversityAdapter(universities) { university ->
                // 点击事件处理（可跳转到详情页）
                showUniversityDetail(university)
            }
        }
    }

    private fun showUniversityDetail(university: University) {
        // 这里实现跳转到院校详情页的逻辑
        // 可以使用Navigation组件或启动新的Fragment
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = UniversityFragment()
    }
}