package com.example.myapp.ui.Daily

// MyFragment.kt
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.SeekBar
import android.widget.Toast
import kotlin.random.Random
import java.io.IOException
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*
import java.util.TreeMap
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.R
import com.example.myapp.databinding.FragmentDailyBinding // 自动生成的视图绑定类


///​**​
//* 极简Fragment示例
//* 功能：
//* 1. 显示一个按钮和一个文本区
//* 2. 初始文本为空
//* 3. 点击按钮后文本显示"已点击"
//*/


class DailyFragment : Fragment() {

    // 使用伴生对象提供标准化的实例创建方式
    companion object {
        //        /​**​
//        * 创建Fragment实例的工厂方法
//        */
        fun newInstance() = DailyFragment()
    }

    val songs = mapOf(
        "song1" to "一路生花",
        "song2" to "平凡之路",
        "song3" to "海阔天空"
    )

    // 声明媒体播放器对象（延迟初始化）
    private lateinit var mediaPlayer: MediaPlayer
    // 声明进度条控件
    private lateinit var seekBar: SeekBar
    // 声明顺序/随机按钮
    private lateinit var modeOrder: Button
    // 声明播放/暂停按钮
    private lateinit var btnPlayPause: Button
    // 声明显示歌曲列表按钮
    private lateinit var showListButton: Button
    // 显示歌曲列表的列表视图
    private lateinit var songListView: ListView
    // 显示歌曲专辑图片
    private lateinit var myImageView: ImageView
    // 播放状态标志（false=暂停，true=播放中）
    private var isRandom = false
    // 播放状态标志（false=暂停，true=播放中）
    private var isPlaying = false
    // 下一曲按钮
    private lateinit var btnNext: Button
    // 歌词列表视图
    private lateinit var recyclerView: RecyclerView
    // 歌词适配器
    private lateinit var lyricsAdapter: LyricsAdapter
    // 布局管理器
    private lateinit var layoutManager: LinearLayoutManager
    // 使用TreeMap存储时间戳和歌词的映射（自动按时间排序）
    private val lyricsMap = TreeMap<Int, String>()

    // 适配器数据源
    private val lyricItems = ArrayList<LyricItem>()

    // 获取raw目录下的所有文件名
    val rawFiles = getRawResourceFiles()
    // 生成随机数
    var songNum = rawFiles.size
    var currentsongid = Random.nextInt(songNum)
    var currentsongName = rawFiles[currentsongid]

    // 当前高亮位置
    private var currentHighlightPos = -1

    // 主线程Handler，用于UI更新
    private val handler = Handler(Looper.getMainLooper())

    // 定义更新进度条的Runnable任务
    private val updateSeekBar = object : Runnable {
        override fun run() {
            // 如果正在播放，更新进度条位置
            if (isPlaying) {
                // 设置进度条进度为当前播放位置
                seekBar.progress = mediaPlayer.currentPosition
                // 每秒更新一次（1000毫秒）
                handler.postDelayed(this, 1000)
            }
        }
    }

    // 歌词更新任务（每100ms执行一次）
    private val updateLyricsRunnable = object : Runnable {
        override fun run() {
            updateLyrics()
            handler.postDelayed(this, 500)
        }
    }

    private var _binding: FragmentDailyBinding? = null // 可空的绑定对象
    private val binding get() = _binding!! // 非空访问器（仅在视图存在时使用）


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 使用视图绑定类inflate方法加载布局
        _binding = FragmentDailyBinding.inflate(inflater, container, false)
        // 返回根视图
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. 加载布局文件
        // 获取进度条控件
        seekBar = view.findViewById(R.id.seekBar)
        // 获取顺序/随机按钮
        modeOrder = view.findViewById(R.id.modeOrder)
        // 获取播放/暂停按钮
        btnPlayPause = view.findViewById(R.id.btnPlayPause)
        // 绑定下一曲按钮
        btnNext = view.findViewById(R.id.btnNext)
        //绑定歌曲列表按钮
        showListButton = view.findViewById(R.id.showListButton)
        //绑定列表视图
        songListView = view.findViewById(R.id.songListView)
        //绑定专辑图片
        myImageView = view.findViewById(R.id.imageView)
        // 初始状态下隐藏歌曲列表
        songListView.visibility = View.GONE
        // 设置歌曲名称文本
        view.findViewById<TextView>(R.id.tvSongName).text = songs[currentsongName]
        //切换专辑图片
        changeImageByName(myImageView, currentsongName) // 加载 res/drawable/song1.jpg


        // 初始化RecyclerView
        recyclerView = view.findViewById(R.id.lyricsRecyclerView)
        // 设置线性布局管理器（垂直列表）
        layoutManager = LinearLayoutManager(requireContext())
        // 创建适配器实例
        lyricsAdapter = LyricsAdapter(lyricItems)
        // 配置RecyclerView
        recyclerView.apply {
            layoutManager = this@DailyFragment.layoutManager
            adapter = lyricsAdapter
            setHasFixedSize(true)  // 优化性能，当item大小固定时使用
        }

        // 初始化媒体播放器
        val field = R.raw::class.java.getDeclaredField(currentsongName)
        val resId = field.getInt(null)

        // 初始化MediaPlayer
        mediaPlayer = MediaPlayer.create(requireContext(), resId).apply {
            //start() // 自动播放
        }
        loadLyrics(currentsongName)


        // 设置进度条最大值为音频总时长
        seekBar.max = mediaPlayer.duration
        //playMusic()
        handler.post(updateLyricsRunnable)


        // 顺序/随机按钮点击事件监听器
        modeOrder.setOnClickListener {
            if (isRandom) {
                // 如果正在播放，则暂停
                orderMusic()
            } else {
                // 如果暂停中，则播放
                randomMusic()
            }        }


        // 设置播放/暂停按钮点击监听
        btnPlayPause.setOnClickListener {
            if (isPlaying) {
                // 如果正在播放，则暂停
                pauseMusic()
            } else {
                // 如果暂停中，则播放
                playMusic()
            }
        }

        // 下一曲按钮点击事件监听器
        btnNext.setOnClickListener {
            nextSong()
        }

        //设置MediaPlayer播放完成监听器

        mediaPlayer.setOnCompletionListener {
            // 停止歌词更新
            nextSong()

        }

        // 设置进度条拖动监听
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            // 进度改变时回调
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // 如果是用户拖动导致的进度改变
                if (fromUser) {
                    // 跳转到指定位置
                    mediaPlayer.seekTo(progress)
                }
            }
            // 开始拖动时回调（空实现）
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            // 停止拖动时回调（空实现）
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // 设置列表适配器
        setupListAdapter()

        // 设置按钮点击事件
        setupButtonClickListener()

        // 设置列表项点击事件
        setupListItemClickListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        // 清除绑定引用，防止内存泄漏
//        _binding = null
//        // 移除所有未执行的Handler任务
//        handler.removeCallbacks(updateSeekBar)
//        // 释放媒体播放器资源
//        mediaPlayer.release()
    }


    //    * 获取res/raw目录下的所有文件名
    private fun getRawResourceFiles(): List<String> {
        return try {
            // 使用反射获取R.raw类的所有字段
            R.raw::class.java.fields.map { it.name }  // 提取字段名（即文件名）
        } catch (e: Exception) {
            // 捕获异常（如raw目录不存在），返回空列表
            emptyList()
        }
    }

    // 设置列表适配器
    private fun setupListAdapter() {
        // 将rawFiles中的文件名转换为歌曲名列表
        val songNames = rawFiles.map { fileName ->
            songs[fileName] ?: fileName // 如果找不到映射，则显示原文件名
        }

        // 创建ArrayAdapter适配器，使用系统自带的简单列表项布局
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            songNames // 使用歌曲名列表替代原始文件名列表
        )

        // 为列表视图设置适配器
        songListView.adapter = adapter
    }


    //    * 设置按钮点击事件
    private fun setupButtonClickListener() {
        showListButton.setOnClickListener {
            // 切换列表的显示/隐藏状态
            toggleSongList()
        }
    }

    //    * 设置列表项点击事件
    private fun setupListItemClickListener() {
        songListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->

            // 停止当前播放
            mediaPlayer.stop()

            handler.removeCallbacks(updateLyricsRunnable)  // 停止歌词更新

            // 释放MediaPlayer资源
            mediaPlayer.release()
            currentsongName = rawFiles[position]
            loadLyrics(currentsongName)

            // 更新文本视图显示
            binding.tvSongName.text = songs[currentsongName]

            //切换专辑图片
            changeImageByName(myImageView, currentsongName) // 加载 res/drawable/song1.jpg

            // 初始化媒体播放器
            val field = R.raw::class.java.getDeclaredField(currentsongName)
            val resId = field.getInt(null)
            // 初始化MediaPlayer
            mediaPlayer = MediaPlayer.create(requireContext(), resId).apply {
                start() // 自动播放
            }

            // 重新设置播放完成监听器
            mediaPlayer.setOnCompletionListener {
                nextSong()
            }

            // 开始播放新歌曲
            playMusic()
            songListView.visibility = View.GONE
            // 启动歌词更新任务
            handler.post(updateLyricsRunnable)
        }
    }

    //    * 切换歌曲列表的显示/隐藏状态
    private fun toggleSongList() {
        // 如果没有歌曲，显示提示并返回
        if (rawFiles.isEmpty()) {
            Toast.makeText(requireContext(), "没有可显示的歌曲", Toast.LENGTH_SHORT).show()
            return
        }

        // 切换列表的可见性（VISIBLE显示/GONE隐藏）
        songListView.visibility = if (songListView.visibility == View.VISIBLE) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }


    // 随机播放音乐
    private fun randomMusic() {
        // 如果当前正在播放
        if (!isRandom) {
            // 更新播放状态
            isRandom = true
            // 更新按钮文本
            modeOrder.text = "随机播放"
            // 停止更新进度条
        }
    }

    // 顺序播放音乐
    private fun orderMusic() {
        // 如果当前正在播放
        if (isRandom) {
            // 更新播放状态
            isRandom = false
            // 更新按钮文本
            modeOrder.text = "顺序播放"
            // 停止更新进度条
        }
    }

    // 播放音乐
    private fun playMusic() {
        // 如果当前未播放
        if (!isPlaying) {
            // 开始播放
            mediaPlayer.start()
            // 更新播放状态
            isPlaying = true
            // 更新按钮文本
            btnPlayPause.text = "暂停"
            // 开始更新进度条
            handler.post(updateSeekBar)
        }
    }

    // 暂停音乐
    private fun pauseMusic() {
        // 如果当前正在播放
        if (isPlaying) {
            // 暂停播放
            mediaPlayer.pause()
            // 更新播放状态
            isPlaying = false
            // 更新按钮文本
            btnPlayPause.text = "播放"
            // 停止更新进度条
            handler.removeCallbacks(updateSeekBar)
        }
    }

    //    * 切换到下一首歌曲
    private fun nextSong() {

        // 停止歌词更新
        handler.removeCallbacks(updateLyricsRunnable)  // 停止歌词更新
        // 停止当前播放
        mediaPlayer.stop()

        // 释放MediaPlayer资源
        mediaPlayer.release()

        // 计算下一首歌曲的索引（循环播放）
        if(isRandom){
            currentsongid = (currentsongid + Random.nextInt(songNum)) % songNum
        }
        currentsongid = (currentsongid + 1) % songNum
        currentsongName = rawFiles[currentsongid]
        loadLyrics(currentsongName)


        // 更新文本视图显示
        binding.tvSongName.text = songs[currentsongName]

        //切换专辑图片
        changeImageByName(myImageView, currentsongName) // 加载 res/drawable/song1.jpg

        // 初始化媒体播放器
        val field = R.raw::class.java.getDeclaredField(currentsongName)
        val resId = field.getInt(null)
        // 初始化MediaPlayer
        mediaPlayer = MediaPlayer.create(requireContext(), resId).apply {
            start() // 自动播放
        }


        // 重新设置播放完成监听器
        mediaPlayer.setOnCompletionListener {
            nextSong()
        }

        // 开始播放新歌曲
        playMusic()

        // 启动歌词更新任务
        handler.post(updateLyricsRunnable)
    }

    //切换专辑图片
    fun changeImageByName(imageView: ImageView, imageName: String) {
        // 获取图片的资源 ID
        val resId = imageView.context.resources
            .getIdentifier(imageName, "drawable", imageView.context.packageName)

        if (resId != 0) {
            // 如果找到图片，则设置
            imageView.setImageResource(resId)
        } else {
            // 如果找不到图片，可以设置默认图片或打印错误
            Log.e("ImageUtils", "图片 $imageName 未找到！")
        }
    }


    // 加载LRC歌词文件
    private fun loadLyrics(imageName: String) {
        try {
            lyricItems.clear()
            lyricsMap.clear()  // 确保时间戳映射表也被清空
            // 打开assets目录下的歌词文件
            val inputStream = requireContext().assets.open(imageName + ".lrc")

            // 使用BufferedReader按行读取
            val reader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))

            var line: String?
            while (reader.readLine().also { line = it } != null) {
                parseLyricLine(line!!)  // 解析每一行歌词
            }
            reader.close()

            // 将解析后的歌词添加到数据源
            lyricItems.addAll(lyricsMap.values.map { LyricItem(it, false) })
            // 通知适配器数据变化
            lyricsAdapter.notifyDataSetChanged()
        } catch (e: IOException) {
            // 出错时显示错误信息
            lyricItems.add(LyricItem("歌词加载失败", true))
            lyricsAdapter.notifyDataSetChanged()
        }
    }

    // 解析单行LRC歌词
    private fun parseLyricLine(line: String) {
        // LRC格式校验（必须以[开头）
        if (!line.startsWith("[")) return

        // 查找时间标签结束位置
        val closeBracketIndex = line.indexOf("]")
        if (closeBracketIndex == -1) return

        // 提取时间标签（如[00:01.50]中的00:01.50）
        val timeTag = line.substring(1, closeBracketIndex)

        // 提取歌词文本（时间标签后的内容）
        val lyricText = if (line.length > closeBracketIndex + 1) {
            line.substring(closeBracketIndex + 1).trim()  // 去除前后空格
        } else ""

        // 将时间标签转换为毫秒
        val timeInMillis = parseTimeTag(timeTag)

        // 有效歌词才存入映射表
        if (timeInMillis != -1 && lyricText.isNotEmpty()) {
            lyricsMap[timeInMillis] = lyricText
        }
    }

    // 将时间标签转换为毫秒
    private fun parseTimeTag(timeTag: String): Int {
        // 分割时间标签（格式：[mm:ss.xx]）
        val parts = timeTag.split(":", ".")
        if (parts.size != 3) return -1  // 格式错误

        return try {
            // 解析分钟、秒、毫秒
            val minutes = parts[0].toInt()
            val seconds = parts[1].toInt()
            val millis = parts[2].toInt()

            // 计算总毫秒数
            (minutes * 60 + seconds) * 1000 + millis * 10
        } catch (e: NumberFormatException) {
            -1  // 数字解析失败
        }
    }

    // 更新歌词显示
    private fun updateLyrics() {
        // 如果媒体播放器未播放则直接返回
        if (!mediaPlayer.isPlaying) return

        // 获取当前播放位置（毫秒）
        val currentPosition = mediaPlayer.currentPosition

        // 查找当前时间对应的歌词（floorEntry返回小于等于当前时间的最近条目）
        val entry = lyricsMap.floorEntry(currentPosition)
        val currentLyric = entry?.value ?: ""

        // 查找歌词在列表中的位置
        val newPos = lyricItems.indexOfFirst { it.text == currentLyric }.takeIf { it != -1 } ?: return

        // 如果位置发生变化
        if (newPos != currentHighlightPos) {
            // 清除旧高亮
            if (currentHighlightPos in lyricItems.indices) {
                lyricItems[currentHighlightPos].isHighlighted = false
                lyricsAdapter.notifyItemChanged(currentHighlightPos)  // 局部刷新
            }

            // 设置新高亮
            lyricItems[newPos].isHighlighted = true
            lyricsAdapter.notifyItemChanged(newPos)

            // 获取当前可见项范围
            val firstVisible = layoutManager.findFirstVisibleItemPosition()
            val lastVisible = layoutManager.findLastVisibleItemPosition()

            // 计算中心位置（可见项数量的一半）
            val centerPosition = (lastVisible - firstVisible) / 2

            // 判断是否需要滚动（新位置不在中央区域时）
            if (newPos <= firstVisible + centerPosition || newPos >= lastVisible - centerPosition) {
                // 先快速滚动到目标位置附近
                recyclerView.smoothScrollToPosition(newPos)

                // 延迟300ms后微调确保完全居中
                handler.postDelayed({
                    // 获取目标项的视图对象
                    val targetView = layoutManager.findViewByPosition(newPos)

                    // 计算居中需要的偏移量（列表高度/2 - 项高度/2）
                    val centerOffset = (recyclerView.height / 2) - (targetView?.height ?: 0) / 2

                    // 精确滚动到居中位置
                    layoutManager.scrollToPositionWithOffset(newPos, centerOffset)
                }, 300)
            }

            // 更新当前高亮位置
            currentHighlightPos = newPos
        }
    }



    // 歌词项数据类
    data class LyricItem(
        val text: String,         // 歌词文本
        var isHighlighted: Boolean // 是否高亮
    )

    // RecyclerView适配器
    inner class LyricsAdapter(private val items: List<LyricItem>) :
        RecyclerView.Adapter<LyricsAdapter.LyricViewHolder>() {

        // ViewHolder类
        inner class LyricViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
            val textView: android.widget.TextView = view.findViewById(android.R.id.text1)
        }

        override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): LyricViewHolder {
            // 使用系统自带的simple_list_item_1布局
            val view = layoutInflater.inflate(
                android.R.layout.simple_list_item_1,
                parent,
                false
            ).apply {
                // 配置TextView样式
                findViewById<android.widget.TextView>(android.R.id.text1).apply {
                    gravity = android.view.Gravity.CENTER  // 文本居中
                    setPadding(0, 16.dpToPx(), 0, 16.dpToPx()) // 设置上下间距
                }
            }
            return LyricViewHolder(view)
        }

        override fun onBindViewHolder(holder: LyricViewHolder, position: Int) {
            val item = items[position]

            // 设置歌词文本
            holder.textView.text = item.text

            // 根据高亮状态设置样式
            holder.textView.setTextColor(
                if (item.isHighlighted) android.graphics.Color.RED  // 高亮红色
                else android.graphics.Color.GRAY                      // 普通灰色
            )

            // 高亮时放大字体
            holder.textView.textSize = if (item.isHighlighted) 20f else 16f

            // 高亮时加粗字体
            holder.textView.typeface = android.graphics.Typeface.DEFAULT_BOLD.takeIf { item.isHighlighted }
        }

        override fun getItemCount() = items.size
    }

    // 扩展函数：dp转px
    private fun Int.dpToPx(): Int = (this * resources.displayMetrics.density).toInt()
}