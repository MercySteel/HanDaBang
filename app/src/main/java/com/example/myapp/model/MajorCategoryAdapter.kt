package com.example.myapp.model


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.databinding.ItemMajorCategoryBinding
import com.example.myapp.databinding.ItemMajorBinding
import com.example.myapp.model.Major

class MajorCategoryAdapter(
    private val categories: Map<String, List<Major>>
) : RecyclerView.Adapter<MajorCategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(val binding: ItemMajorCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemMajorCategoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val categoryName = categories.keys.elementAt(position)
        val majors = categories[categoryName]!!

        holder.binding.categoryName.text = categoryName
        setupMajorsRecycler(holder.binding.majorsRecycler, majors)
    }

    private fun setupMajorsRecycler(recyclerView: RecyclerView, majors: List<Major>) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(recyclerView.context)
            adapter = MajorAdapter(majors)
        }
    }

    override fun getItemCount() = categories.size
}

class MajorAdapter(private val majors: List<Major>) :
    RecyclerView.Adapter<MajorAdapter.MajorViewHolder>() {

    inner class MajorViewHolder(val binding: ItemMajorBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MajorViewHolder {
        val binding = ItemMajorBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MajorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MajorViewHolder, position: Int) {
        val major = majors[position]
        holder.binding.apply {
            majorName.text = major.getFullName()
            majorDescription.text = major.description
            typicalScore.text = "平均分数: ${major.typicalScore ?: "暂无"}分"
        }
    }

    override fun getItemCount() = majors.size
}