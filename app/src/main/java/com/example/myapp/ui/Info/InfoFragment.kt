package com.example.myapp.ui.Info

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapp.databinding.FragmentInfoBinding

class InfoFragment : Fragment() {

    private var _binding: FragmentInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val infoViewModel = ViewModelProvider(this).get(InfoViewModel::class.java)

        _binding = FragmentInfoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        infoViewModel.text.observe(viewLifecycleOwner) {
            binding.textGallery.text = it
        }

        // 解决浏览器打不开问题的改进方案
        binding.btnOpenYzw.setOnClickListener {
            val url = "https://yz.chsi.com.cn/sch/"

            // 方案1：使用选择器（更可靠）
            openUrlWithChooser(url)

            // 或者方案2：直接尝试打开Chrome，然后回退通用方式
            // openUrlWithFallback(url)
        }

        return root
    }

    // 方法1：使用选择器确保兼容性
    private fun openUrlWithChooser(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            val chooser = Intent.createChooser(intent, "打开研招网")
            startActivity(chooser)
        } catch (e: Exception) {
            binding.textGallery.text = "无法打开链接: ${e.localizedMessage}"
            Log.e("Browser", "打开URL失败", e)
        }
    }

    // 方法2：先尝试特定浏览器再回退
    private fun openUrlWithFallback(url: String) {
        try {
            // 优先尝试Chrome
            val chromeIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            chromeIntent.setPackage("com.android.chrome")
            startActivity(chromeIntent)
        } catch (e: Exception) {
            Log.w("Browser", "无法通过Chrome打开链接，尝试通用方式")

            try {
                // 回退到通用方案
                val fallbackIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(fallbackIntent)
            } catch (e2: Exception) {
                binding.textGallery.text = "请安装浏览器应用"
                Log.e("Browser", "通用方式打开失败", e2)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}