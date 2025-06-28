package com.example.myapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.R
import com.example.myapp.model.University

class UniversityAdapter(
    private val universities: List<University>,
    private val onItemClick: (University) -> Unit
) : RecyclerView.Adapter<UniversityAdapter.UniversityViewHolder>() {

    inner class UniversityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // 修正ID引用（必须与布局文件完全一致）
        val name: TextView = itemView.findViewById(R.id.university_name)
        val tag: TextView = itemView.findViewById(R.id.university_tag)
        val scoreContainer: ViewGroup = itemView.findViewById(R.id.score_lines_container)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UniversityViewHolder {
        // 确保使用正确的布局文件
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_university, parent, false)
        return UniversityViewHolder(view)
    }

    override fun onBindViewHolder(holder: UniversityViewHolder, position: Int) {
        val university = universities[position]

        holder.name.text = university.name
        holder.tag.text = when {
            university.is985 -> "985高校"
            university.is211 -> "211高校"
            else -> "双非院校"
        }

        // 清空分数容器
        holder.scoreContainer.removeAllViews()

        // 动态添加分数行
        university.scoreLines.forEach { scoreLine ->
            val scoreView = LayoutInflater.from(holder.itemView.context)
                .inflate(R.layout.item_score_line, holder.scoreContainer, false)

            // 修正ID拼写（注意下划线和大小写）
            scoreView.findViewById<TextView>(R.id.major_name).text = scoreLine.major
            scoreView.findViewById<TextView>(R.id.score_year).text = "${scoreLine.year}年"
            scoreView.findViewById<TextView>(R.id.score_value).text = "${scoreLine.score}分"

            holder.scoreContainer.addView(scoreView)
        }

        holder.itemView.setOnClickListener { onItemClick(university) }
    }

    override fun getItemCount() = universities.size
}