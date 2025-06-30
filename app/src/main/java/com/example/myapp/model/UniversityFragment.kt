package com.example.myapp.model


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapp.R
import com.example.myapp.data.DataProvider
import com.example.myapp.databinding.FragmentUniversityBinding

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

        // 获取院校数据并设置RecyclerView
        val universities = DataProvider.getUniversitiesWithScores()
        binding.universityRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = UniversityAdapter(universities) { university ->
                openUniversityWebsite(university)
            }
        }
    }

    private fun openUniversityWebsite(university: University) {
        // 确保URL格式正确
        val url = if (university.officialWebsite.startsWith("http://") ||
            university.officialWebsite.startsWith("https://")) {
            university.officialWebsite
        } else {
            "https://${university.officialWebsite}"
        }

        // 尝试使用外部浏览器打开
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            // 如果外部浏览器不可用，使用WebView作为备选方案
            openWebView(url)
        }
    }

    private fun openWebView(url: String) {
        // 创建并显示WebViewFragment
        val webViewFragment = WebViewFragment.newInstance(url)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, webViewFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = UniversityFragment()
    }
}