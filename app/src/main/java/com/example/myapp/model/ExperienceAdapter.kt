package com.example.myapp.ui.experience

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.databinding.ItemExperienceBinding
import com.example.myapp.model.Experience

class ExperienceAdapter(
    private val onItemClick: (Experience) -> Unit  // 修正：接收Experience对象
) : ListAdapter<Experience, ExperienceAdapter.ExperienceViewHolder>(DiffCallback()) {

    inner class ExperienceViewHolder(
        private val binding: ItemExperienceBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(experience: Experience) {
            binding.apply {
                experienceContent.text = experience.content
                experienceDate.text = experience.createdAt

                // 图片处理
                experience.imagePath?.let { path ->
                    try {
                        experienceImage.visibility = View.VISIBLE
                        experienceImage.setImageBitmap(BitmapFactory.decodeFile(path))
                    } catch (e: Exception) {
                        experienceImage.visibility = View.GONE
                    }
                } ?: run {
                    experienceImage.visibility = View.GONE
                }

                deleteBtn.setOnClickListener {
                    onItemClick(experience)  // 传递整个对象
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExperienceViewHolder {
        val binding = ItemExperienceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ExperienceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExperienceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<Experience>() {
        override fun areItemsTheSame(oldItem: Experience, newItem: Experience): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Experience, newItem: Experience): Boolean {
            return oldItem == newItem
        }
    }
}