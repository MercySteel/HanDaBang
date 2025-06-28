package com.example.myapp.model


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapp.R

class ExamSubjectsFragment : Fragment() {

    companion object {
        // 添加newInstance工厂方法
        fun newInstance() = ExamSubjectsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_exam_subjects, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 在这里实现考试科目页面逻辑
    }
}