package com.example.myapp.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.databinding.ItemExamSubjectBinding
import com.example.myapp.model.ExamSubject

class ExamSubjectAdapter(
    private val examSubjects: List<ExamSubject>
) : RecyclerView.Adapter<ExamSubjectAdapter.ExamSubjectViewHolder>() {

    inner class ExamSubjectViewHolder(val binding: ItemExamSubjectBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExamSubjectViewHolder {
        val binding = ItemExamSubjectBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ExamSubjectViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExamSubjectViewHolder, position: Int) {
        val examSubject = examSubjects[position]
        holder.binding.apply {
            // 设置院校和专业信息
            tvUniversity.text = examSubject.universityName
            tvMajor.text = examSubject.majorName

            // 清空原有科目显示
            subjectsContainer.removeAllViews()

            // 动态添加考试科目
            examSubject.subjects.forEach { subject ->
                val subjectView = TextView(root.context).apply {
                    text = buildSubjectText(subject)
                    textSize = 14f
                    setPadding(0, 8.dpToPx(context.resources.displayMetrics.density), 0, 8.dpToPx(context.resources.displayMetrics.density))
                }
                subjectsContainer.addView(subjectView)
            }
        }
    }

    private fun buildSubjectText(subject: ExamSubject.SubjectDetail): String {
        return StringBuilder().apply {
            append("(${subject.code}) ${subject.name}")
            append(" • ${subject.examType}")
            if (subject.isRequired) append(" • 必考")
        }.toString()
    }

    private fun Int.dpToPx(density: Float): Int {
        return (this * density).toInt()
    }

    override fun getItemCount() = examSubjects.size
}