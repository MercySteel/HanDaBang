package com.example.myapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapp.databinding.FragmentAddBinding

import java.text.SimpleDateFormat
import java.util.*

class AddFragment : Fragment() {
    val timestamp: Long = System.currentTimeMillis()
    private lateinit var binding: FragmentAddBinding
    private lateinit var dbHelper: ScheduleDbHelper
    private val startCalendar = Calendar.getInstance()
    private val endCalendar = Calendar.getInstance().apply { add(Calendar.HOUR_OF_DAY, 1) }

    // 定义结果回调接口
    interface OnScheduleAddedListener {
        fun onScheduleAdded()
    }
    private var listener: OnScheduleAddedListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? OnScheduleAddedListener // 绑定宿主Activity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbHelper = ScheduleDbHelper(requireContext())
        updateTimeDisplay()

        binding.btnStartTime.setOnClickListener { showDateTimePicker(true) }
        binding.btnEndTime.setOnClickListener { showDateTimePicker(false) }
        binding.btnCreate.setOnClickListener { createNewSchedule() }
    }

    // 日期选择器（直接使用Fragment的Context）
    private fun showDateTimePicker(isStartTime: Boolean) {
        val calendar = if (isStartTime) startCalendar else endCalendar
        val textView = if (isStartTime) binding.tvStartTime else binding.tvEndTime

        DatePickerDialog(requireContext(), { _, year, month, day ->
            calendar.set(year, month, day)
            TimePickerDialog(requireContext(), { _, hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                textView.text = formatTime(calendar.timeInMillis)
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    // 添加缺失的方法
    private fun updateTimeDisplay() {
        binding.tvStartTime.text = formatTime(startCalendar.timeInMillis)
        binding.tvEndTime.text = formatTime(endCalendar.timeInMillis)
    }

    // 添加缺失的方法
    private fun formatTime(timestamp: Long): String {
        return SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.getDefault()).format(Date(timestamp))
    }

    private fun createNewSchedule() {
        val title = binding.etTitle.text.toString().trim()

            if (startCalendar.timeInMillis < timestamp) {
                Toast.makeText(requireContext(), "开始时间不能早于当前时间", Toast.LENGTH_SHORT).show()
                return
            }
        if (startCalendar.timeInMillis > endCalendar.timeInMillis) {
            Toast.makeText(requireContext(), "开始时间不能晚于结束时间", Toast.LENGTH_SHORT).show()
            return // 阻止保存
        }
        if (title.isEmpty()) {
            Toast.makeText(requireContext(), "标题不能为空", Toast.LENGTH_SHORT).show()
            return
        }

        val newSchedule = Schedule(
            title = title,
            content = binding.etContent.text.toString().trim(),
            startTime = startCalendar.timeInMillis,
            endTime = endCalendar.timeInMillis
        )

        if (dbHelper.insertSchedule(newSchedule) != -1L) {
            Toast.makeText(requireContext(), "日程已添加", Toast.LENGTH_SHORT).show()
            listener?.onScheduleAdded() // 通知宿主刷新数据
            parentFragmentManager.popBackStack() // 关闭当前Fragment
        } else {
            Toast.makeText(requireContext(), "创建失败", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
    }
}