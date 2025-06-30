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

import java.text.SimpleDateFormat
import java.util.*
import androidx.appcompat.app.AlertDialog
import com.example.myapp.databinding.FragmentEditBinding

class EditFragment : Fragment() {
    private lateinit var binding: FragmentEditBinding // 修正绑定类
    private lateinit var dbHelper: ScheduleDbHelper
    private lateinit var originalSchedule: Schedule
    private val startCalendar = Calendar.getInstance()
    private val endCalendar = Calendar.getInstance()

    // 定义更新回调接口
    interface OnScheduleUpdatedListener {
        fun onScheduleUpdated(schedule: Schedule)
    }
    private var listener: OnScheduleUpdatedListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? OnScheduleUpdatedListener
    }

    // 静态工厂方法传递参数
    companion object {
        private const val ARG_SCHEDULE = "schedule"

        fun newInstance(schedule: Schedule): EditFragment {
            val args = Bundle().apply {
                putSerializable(ARG_SCHEDULE, schedule)
            }
            return EditFragment().apply { arguments = args }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        originalSchedule = arguments?.getSerializable(ARG_SCHEDULE) as? Schedule
            ?: throw IllegalStateException("Schedule data missing")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditBinding.inflate(inflater, container, false) // 修正绑定初始化
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbHelper = ScheduleDbHelper(requireContext())
        initTimeData()
        binding.btnSave.setOnClickListener { saveUpdatedSchedule() }
    }

    private fun initTimeData() {
        startCalendar.timeInMillis = originalSchedule.startTime
        endCalendar.timeInMillis = originalSchedule.endTime
        binding.etTitle.setText(originalSchedule.title)
        binding.etContent.setText(originalSchedule.content)
        updateTimeDisplay()

        binding.btnStartTime.setOnClickListener { showDateTimePicker(true) }
        binding.btnEndTime.setOnClickListener { showDateTimePicker(false) }
        binding.btnDelete.setOnClickListener { deleteSchedule() }
    }

    // 添加缺失的方法
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

    private fun deleteSchedule() {
        AlertDialog.Builder(requireContext())
            .setTitle("确认删除")
            .setMessage("确定要删除这个日程吗？")
            .setPositiveButton("删除") { _, _ ->
                if (dbHelper.deleteSchedule(originalSchedule.id) > 0) {
                    Toast.makeText(requireContext(), "日程已删除", Toast.LENGTH_SHORT).show()
                    listener?.onScheduleUpdated(originalSchedule) // 通知主界面刷新
                    parentFragmentManager.popBackStack()
                }
            }
            .setNegativeButton("取消", null)
            .show()
    }
    // 添加缺失的方法
    private fun formatTime(timestamp: Long): String {
        return SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.getDefault()).format(Date(timestamp))
    }

    private fun saveUpdatedSchedule() {
        val newTitle = binding.etTitle.text.toString().trim()

        if (startCalendar.timeInMillis > endCalendar.timeInMillis) {
            Toast.makeText(requireContext(), "开始时间不能晚于结束时间", Toast.LENGTH_SHORT).show()
            return // 阻止保存
        }
        if (newTitle.isEmpty()) {
            Toast.makeText(requireContext(), "标题不能为空", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedSchedule = originalSchedule.copy(
            title = newTitle,
            content = binding.etContent.text.toString().trim(),
            startTime = startCalendar.timeInMillis,
            endTime = endCalendar.timeInMillis
        )

        if (dbHelper.updateSchedule(updatedSchedule) > 0) {
            Toast.makeText(requireContext(), "日程已更新", Toast.LENGTH_SHORT).show()
            listener?.onScheduleUpdated(updatedSchedule) // 回调更新
            parentFragmentManager.popBackStack()
        } else {
            Toast.makeText(requireContext(), "更新失败", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
    }
}