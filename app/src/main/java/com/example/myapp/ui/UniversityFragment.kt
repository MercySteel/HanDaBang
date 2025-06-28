package com.example.myapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.computergraduateexam.ui.UniversityAdapter
import com.example.myapp.R
import com.example.myapp.data.DataProvider
import model.University

class UniversityFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 加载布局文件
        return inflater.inflate(R.layout.fragment_university, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 获取RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.university_recycler_view)

        // 设置布局管理器
        recyclerView.layoutManager = LinearLayoutManager(context)

        // 获取院校数据
        val universities = DataProvider.getUniversities()

        // 创建适配器
        val adapter = UniversityAdapter(universities) { university ->
            // 处理点击事件，跳转到院校详情页
            // 这里简化处理，实际应用中应该启动新的Activity或Fragment
            // val intent = Intent(activity, UniversityDetailActivity::class.java).apply {
            //     putExtra("university_id", university.id)
            // }
            // startActivity(intent)
        }

        // 设置适配器
        recyclerView.adapter = adapter
    }

    companion object {
        fun newInstance() = UniversityFragment()
    }

    // 定义点击监听器接口
    interface OnItemClickListener {
        fun onItemClick(university: University)
    }
}