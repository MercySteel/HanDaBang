package com.example.computergraduateexam.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.utils.ImageLoader
import com.example.myapp.R
import model.University

class UniversityAdapter(
    private val universities: List<University>,
    private val onClick: (University) -> Unit
) : RecyclerView.Adapter<UniversityAdapter.UniversityViewHolder>() {

    class UniversityViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.university_name)
        val locationTextView: TextView = view.findViewById(R.id.university_location)
        val rankingTextView: TextView = view.findViewById(R.id.university_ranking)
        val imageView: ImageView = view.findViewById(R.id.university_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UniversityViewHolder {
        // 加载布局文件
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_university, parent, false)
        return UniversityViewHolder(view)
    }

    override fun onBindViewHolder(holder: UniversityViewHolder, position: Int) {
        // 获取当前位置的院校数据
        val university = universities[position]

        // 设置视图内容
        holder.nameTextView.text = university.name
        holder.locationTextView.text = university.location
        holder.rankingTextView.text = "排名: ${university.ranking}"

        // 使用自定义ImageLoader加载图片
        ImageLoader.loadImage(holder.imageView, university.imageUrl)

        // 设置点击事件
        holder.itemView.setOnClickListener { onClick(university) }
    }

    override fun getItemCount() = universities.size
}