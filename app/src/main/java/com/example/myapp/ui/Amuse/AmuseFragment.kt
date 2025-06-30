package com.example.myapp.ui.Amuse

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.myapp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AlertDialog
import com.example.myapp.HostActivity
import com.example.myapp.OnItemClickListener
import com.example.myapp.Schedule
import com.example.myapp.ScheduleAdapter
import com.example.myapp.ScheduleDbHelper


class AmuseFragment : Fragment(), OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ScheduleAdapter
    private val schedules = mutableListOf<Schedule>()
    private lateinit var dbHelper: ScheduleDbHelper

    // 使用registerForActivityResult处理Activity返回结果
    private val hostActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            refreshDataFromDatabase()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 使用适配后的布局（移除标题栏）
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbHelper = ScheduleDbHelper(requireContext())

        // 初始化RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = ScheduleAdapter(
            schedules,
            this,
            ::onScheduleStatusChanged,
            ::onScheduleDelete
        )
        recyclerView.adapter = adapter

        // 加载初始数据
        refreshDataFromDatabase()

        // 设置FAB点击事件
        view.findViewById<FloatingActionButton>(R.id.fab_add).setOnClickListener {
            val intent = Intent(requireActivity(), HostActivity::class.java).apply {
                putExtra("fragment_name", "AddFragment")
            }
            hostActivityLauncher.launch(intent)
        }
    }

    private fun refreshDataFromDatabase() {
        schedules.clear()
        schedules.addAll(dbHelper.getAllSchedules())
        adapter.notifyDataSetChanged()
        if (schedules.isEmpty()) {
            Toast.makeText(requireContext(), "点击+号添加第一条日程", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onScheduleStatusChanged(scheduleId: Long, newStatus: Boolean) {
        dbHelper.setCompletion(scheduleId, newStatus)
    }

    private fun onScheduleDelete(scheduleId: Long) {
        AlertDialog.Builder(requireContext())
            .setTitle("确认删除")
            .setMessage("确定要删除这个日程吗？")
            .setPositiveButton("删除") { _, _ ->
                dbHelper.deleteSchedule(scheduleId)
                refreshDataFromDatabase()
            }
            .setNegativeButton("取消", null)
            .show()
    }

    override fun onItemClick(schedule: Schedule) {
        val intent = Intent(requireActivity(), HostActivity::class.java).apply {
            putExtra("fragment_name", "EditFragment")
            putExtra("schedule", schedule)
        }
        hostActivityLauncher.launch(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
    }
}