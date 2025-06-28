package com.example.myapp.data


import com.example.myapp.model.ExamSubject

object ExamDataProvider {
    fun getExamSubjects(): List<ExamSubject> {
        return listOf(
            // 清华大学 计算机科学与技术
            ExamSubject(
                id = "es1",
                universityId = "1",
                universityName = "清华大学",
                majorId = "m1",
                majorName = "计算机科学与技术",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("301", "数学一", true, "统考"),
                    ExamSubject.SubjectDetail("912", "计算机专业基础综合", true, "自命题")
                )
            ),

            // 北京大学 法学
            ExamSubject(
                id = "es2",
                universityId = "2",
                universityName = "北京大学",
                majorId = "m3",
                majorName = "法学",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("398", "法硕联考专业基础", true, "统考"),
                    ExamSubject.SubjectDetail("498", "法硕联考综合", true, "统考")
                )
            ),

            // 北京信息科技大学 计算机科学与技术
            ExamSubject(
                id = "es3",
                universityId = "38",
                universityName = "北京信息科技大学",
                majorId = "m1",
                majorName = "计算机科学与技术",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("204", "英语二", true, "统考"),
                    ExamSubject.SubjectDetail("302", "数学二", true, "统考"),
                    ExamSubject.SubjectDetail("803", "数据结构与算法", true, "自命题")
                )
            )
            // 可继续添加其他院校专业...
        )
    }
}