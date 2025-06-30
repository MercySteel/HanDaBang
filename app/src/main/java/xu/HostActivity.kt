package com.example.myapp

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class HostActivity : AppCompatActivity(),
    AddFragment.OnScheduleAddedListener,
    EditFragment.OnScheduleUpdatedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host)

        // 根据Intent参数加载不同Fragment
        val fragmentName = intent.getStringExtra("fragment_name")
        val fragment = when (fragmentName) {
            "AddFragment" -> AddFragment()
            "EditFragment" -> {
                val schedule = intent.getSerializableExtra("schedule") as Schedule
                EditFragment.newInstance(schedule) // 传递必要的参数
            }
            else -> null
        }

        fragment?.let {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, it) // 使用正确的容器ID
                .addToBackStack(null)
                .commit()
        }

    }

    override fun onScheduleAdded() {
        // 当添加日程完成后，关闭HostActivity并通知MainFragment刷新
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onScheduleUpdated(schedule: Schedule) {
        // 当更新日程完成后，关闭HostActivity并通知MainFragment刷新
        setResult(Activity.RESULT_OK)
        finish()
    }
}