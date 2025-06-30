package com.example.myapp

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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AlertDialog
import com.example.myapp.R

class MainFragment : Fragment(), OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ScheduleAdapter
    private val schedules = mutableListOf<Schedule>()
    private lateinit var dbHelper: ScheduleDbHelper

    // 使用registerForActivityResult来启动HostActivity并处理返回结果
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
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbHelper = ScheduleDbHelper(requireContext())

        // 初始化UI组件
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = ScheduleAdapter(
            schedules,
            this,
            ::onScheduleStatusChanged,
            ::onScheduleDelete // 新增删除回调
        )
        recyclerView.adapter = adapter

        refreshDataFromDatabase()

        // 设置FAB点击事件：启动添加日程的HostActivity
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
            putExtra("position", schedules.indexOf(schedule))
        }
        hostActivityLauncher.launch(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
    }
}