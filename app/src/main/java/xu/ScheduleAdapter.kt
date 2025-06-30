package com.example.myapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

import android.widget.ImageButton
import com.example.myapp.R

// 新增接口定义
interface OnItemClickListener {
    fun onItemClick(schedule: Schedule)
}

class ScheduleAdapter(
    private val schedules: MutableList<Schedule>,
    private val clickListener: OnItemClickListener, // 接口实现的点击监听
    private val onStatusChange: (Long, Boolean) -> Unit,
    private val onDeleteClick: (Long) -> Unit// 状态变更回调
) : RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        val tvTimeRange: TextView = itemView.findViewById(R.id.tv_time_range)
        val cbCompleted: CheckBox = itemView.findViewById(R.id.cb_completed)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btn_delete)

        init {
            itemView.setOnClickListener {
                // 通过接口调用点击事件
                clickListener.onItemClick(schedules[adapterPosition])
            }
            btnDelete.setOnClickListener {
                val schedule = schedules[adapterPosition]
                onDeleteClick(schedule.id)
            }
            cbCompleted.setOnCheckedChangeListener { _, isChecked ->
                val schedule = schedules[adapterPosition]
                onStatusChange(schedule.id, isChecked) // 触发状态更新
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_schedule, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val schedule = schedules[position]
        holder.tvTitle.text = schedule.title
        holder.tvTimeRange.text = formatTimeRange(schedule.startTime, schedule.endTime)
        holder.cbCompleted.isChecked = schedule.isCompleted
    }

    override fun getItemCount() = schedules.size

    // 时间格式化工具
    private fun formatTimeRange(start: Long, end: Long): String {
        val dateFormat = SimpleDateFormat("MM月dd日 HH:mm", Locale.getDefault())
        return "${dateFormat.format(Date(start))} - ${dateFormat.format(Date(end))}"
    }

    // 数据更新方法
    fun updateData(newSchedules: List<Schedule>) {
        schedules.clear()
        schedules.addAll(newSchedules)
        notifyDataSetChanged()
    }
}