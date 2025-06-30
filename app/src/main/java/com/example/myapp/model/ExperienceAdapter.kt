
package model

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.R
import java.io.File

class ExperienceAdapter(
    private var experiences: List<Experience>,
    private val onDeleteClick: (Long) -> Unit
) : RecyclerView.Adapter<ExperienceAdapter.ExperienceViewHolder>() {

    inner class ExperienceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val contentText: TextView = itemView.findViewById(R.id.experienceContent)
        val dateText: TextView = itemView.findViewById(R.id.experienceDate)
        val imageView: ImageView = itemView.findViewById(R.id.experienceImage)
        val deleteBtn: ImageView = itemView.findViewById(R.id.deleteBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExperienceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_experience, parent, false)
        return ExperienceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExperienceViewHolder, position: Int) {
        val experience = experiences[position]

        // 绑定文本内容
        holder.contentText.text = experience.content
        holder.dateText.text = experience.createdAt ?: ""

        // 处理图片显示
        experience.imagePath?.let { path ->
            try {
                val imageFile = File(path)
                if (imageFile.exists()) {
                    holder.imageView.visibility = View.VISIBLE
                    val bitmap = BitmapFactory.decodeFile(path)
                    holder.imageView.setImageBitmap(bitmap)
                } else {
                    holder.imageView.visibility = View.GONE
                }
            } catch (e: Exception) {
                holder.imageView.visibility = View.GONE
            }
        } ?: run {
            holder.imageView.visibility = View.GONE
        }

        // 设置删除按钮点击事件
        holder.deleteBtn.setOnClickListener {
            onDeleteClick(experience.id) // 直接传递id
        }
    }

    override fun getItemCount(): Int = experiences.size

    fun updateExperiences(newExperiences: List<Experience>) {
        experiences = newExperiences
        notifyDataSetChanged()
    }
}

