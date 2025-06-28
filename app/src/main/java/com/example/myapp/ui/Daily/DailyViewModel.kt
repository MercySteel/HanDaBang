package com.example.myapp.ui.Daily

import androidx.lifecycle.ViewModel

class DailyViewModel : ViewModel() {
    // 如果将来需要跨配置更改保存数据（如播放状态）
    // 但目前所有状态都可以放在Fragment中

    // 可添加如下状态：
    // var currentSongId: Int = 0
    // var isPlaying: Boolean = false
    // var isRandomMode: Boolean = false
}