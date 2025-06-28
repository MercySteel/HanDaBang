package com.example.myapp.ui.Info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class InfoViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "研招网信息"
    }
    val text: LiveData<String> = _text

    // 新增：定义要打开的URL
    val yzwUrl = "https://yz.chsi.com.cn/sch/"

    // 新增：处理URL相关逻辑（示例）
    fun getSchCountInfo(): String {
        // 这里可以添加网络请求逻辑
        return "全国研究生招生单位查询"
    }
}