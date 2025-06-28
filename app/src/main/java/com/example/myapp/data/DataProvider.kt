package com.example.myapp.data

import model.ExamSubject
import model.Major
import model.University

object DataProvider {
    // 模拟院校数据
    fun getUniversities(): List<University> {
        return listOf(
            University(
                1,
                "清华大学",
                "北京",
                1,
                10,
                "计算机科学与技术系，国家重点学科...",
                "https://example.com/tsinghua.jpg"
            ),
            University(
                2,
                "北京大学",
                "北京",
                2,
                9,
                "信息科学技术学院，拥有多个国家重点实验室...",
                "https://example.com/pku.jpg"
            ),
            University(
                3,
                "浙江大学",
                "杭州",
                3,
                9,
                "计算机科学与技术学院，学科评估A+...",
                "https://example.com/zju.jpg"
            ),
            University(
                4,
                "上海交通大学",
                "上海",
                4,
                8,
                "电子信息与电气工程学院...",
                "https://example.com/sjtu.jpg"
            ),
            University(
                5,
                "复旦大学",
                "上海",
                5,
                8,
                "计算机科学技术学院...",
                "https://example.com/fudan.jpg"
            )
        )
    }

    // 模拟专业数据
    fun getMajors(): List<Major> {
        return listOf(
            Major(
                1,
                "计算机科学与技术",
                "计算机系统结构、计算机软件与理论、计算机应用技术...",
                listOf(1, 2, 3)
            ),
            Major(2, "软件工程", "软件开发、软件测试、软件项目管理...", listOf(1, 2, 4)),
            Major(3, "人工智能", "机器学习、深度学习、计算机视觉...", listOf(1, 3, 5)),
            Major(4, "网络空间安全", "网络安全、密码学、信息隐藏...", listOf(2, 4))
        )
    }

    // 模拟考试科目数据
    fun getExamSubjects(): List<ExamSubject> {
        return listOf(
            ExamSubject(
                1,
                1,
                1,
                listOf("数据结构", "操作系统", "计算机网络"),
                listOf("《数据结构》严蔚敏", "《现代操作系统》Andrew S.Tanenbaum")
            ),
            ExamSubject(
                2,
                1,
                2,
                listOf("数据结构", "操作系统", "软件工程"),
                listOf("《数据结构》严蔚敏", "《软件工程》Roger S.Pressman")
            ),
            ExamSubject(
                3,
                2,
                1,
                listOf("数据结构", "计算机组成原理", "计算机网络"),
                listOf("《数据结构》严蔚敏", "《计算机组成原理》唐朔飞")
            ),
            ExamSubject(
                4,
                3,
                3,
                listOf("机器学习", "深度学习", "概率统计"),
                listOf("《机器学习》周志华", "《深度学习》Ian Goodfellow")
            )
        )
    }
}