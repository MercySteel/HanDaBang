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
            ExamSubject(
                id = "es2",
                universityId = "1",
                universityName = "清华大学",
                majorId = "m2",
                majorName = "电子工程",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("301", "数学一", true, "统考"),
                    ExamSubject.SubjectDetail("828", "信号与系统", true, "自命题")
                )
            ),
            ExamSubject(
                id = "es3",
                universityId = "1",
                universityName = "清华大学",
                majorId = "m3",
                majorName = "建筑学",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("355", "建筑学基础", true, "自命题"),
                    ExamSubject.SubjectDetail("501", "建筑设计", true, "自命题")
                )
            ),

            // 北京大学
            ExamSubject(
                id = "es4",
                universityId = "2",
                universityName = "北京大学",
                majorId = "m4",
                majorName = "法学",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("398", "法硕联考专业基础", true, "统考"),
                    ExamSubject.SubjectDetail("498", "法硕联考综合", true, "统考")
                )
            ),
            ExamSubject(
                id = "es5",
                universityId = "2",
                universityName = "北京大学",
                majorId = "m5",
                majorName = "临床医学",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("306", "临床医学综合能力(西医)", true, "统考"),
                    ExamSubject.SubjectDetail("645", "西医综合", true, "自命题")
                )
            ),

            // 复旦大学
            ExamSubject(
                id = "es6",
                universityId = "3",
                universityName = "复旦大学",
                majorId = "m6",
                majorName = "新闻传播学",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("334", "新闻与传播专业综合能力", true, "统考"),
                    ExamSubject.SubjectDetail("440", "新闻与传播专业基础", true, "统考")
                )
            ),

            // 上海交通大学
            ExamSubject(
                id = "es7",
                universityId = "4",
                universityName = "上海交通大学",
                majorId = "m7",
                majorName = "机械工程",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("301", "数学一", true, "统考"),
                    ExamSubject.SubjectDetail("809", "机械设计基础", true, "自命题")
                )
            ),

            // 浙江大学
            ExamSubject(
                id = "es8",
                universityId = "5",
                universityName = "浙江大学",
                majorId = "m8",
                majorName = "电气工程",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("301", "数学一", true, "统考"),
                    ExamSubject.SubjectDetail("840", "电路", true, "自命题")
                )
            ),

            // 中国科学技术大学
            ExamSubject(
                id = "es9",
                universityId = "6",
                universityName = "中国科学技术大学",
                majorId = "m9",
                majorName = "物理学",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("301", "数学一", true, "统考"),
                    ExamSubject.SubjectDetail("828", "量子力学", true, "自命题")
                )
            ),

            // 南京大学
            ExamSubject(
                id = "es10",
                universityId = "7",
                universityName = "南京大学",
                majorId = "m10",
                majorName = "天文学",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("301", "数学一", true, "统考"),
                    ExamSubject.SubjectDetail("811", "普通物理", true, "自命题")
                )
            ),

            // 武汉大学
            ExamSubject(
                id = "es11",
                universityId = "8",
                universityName = "武汉大学",
                majorId = "m11",
                majorName = "测绘工程",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("301", "数学一", true, "统考"),
                    ExamSubject.SubjectDetail("891", "测绘科学基础", true, "自命题")
                )
            ),

            // 华中科技大学
            ExamSubject(
                id = "es12",
                universityId = "9",
                universityName = "华中科技大学",
                majorId = "m12",
                majorName = "光学工程",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("301", "数学一", true, "统考"),
                    ExamSubject.SubjectDetail("810", "材料成形原理", true, "自命题")
                )
            ),

            // 中山大学
            ExamSubject(
                id = "es13",
                universityId = "10",
                universityName = "中山大学",
                majorId = "m13",
                majorName = "临床医学",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("306", "临床医学综合能力(西医)", true, "统考"),
                    ExamSubject.SubjectDetail("645", "西医综合", true, "自命题")
                )
            ),

            // 北京航空航天大学
            ExamSubject(
                id = "es14",
                universityId = "11",
                universityName = "北京航空航天大学",
                majorId = "m14",
                majorName = "航空宇航科学与技术",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("301", "数学一", true, "统考"),
                    ExamSubject.SubjectDetail("841", "自动控制原理", true, "自命题")
                )
            ),

            // 北京师范大学
            ExamSubject(
                id = "es15",
                universityId = "12",
                universityName = "北京师范大学",
                majorId = "m15",
                majorName = "教育学",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("311", "教育学专业基础综合", true, "统考"),
                    ExamSubject.SubjectDetail("703", "教育学基础综合", true, "自命题")
                )
            ),

            // 东南大学
            ExamSubject(
                id = "es16",
                universityId = "13",
                universityName = "东南大学",
                majorId = "m16",
                majorName = "土木工程",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("301", "数学一", true, "统考"),
                    ExamSubject.SubjectDetail("832", "工程力学", true, "自命题")
                )
            ),

            // 西安交通大学
            ExamSubject(
                id = "es17",
                universityId = "14",
                universityName = "西安交通大学",
                majorId = "m17",
                majorName = "能源与动力工程",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("301", "数学一", true, "统考"),
                    ExamSubject.SubjectDetail("805", "工程热力学", true, "自命题")
                )
            ),

            // 哈尔滨工业大学
            ExamSubject(
                id = "es18",
                universityId = "15",
                universityName = "哈尔滨工业大学",
                majorId = "m18",
                majorName = "材料科学与工程",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("302", "数学二", true, "统考"),
                    ExamSubject.SubjectDetail("821", "材料科学与工程基础", true, "自命题")
                )
            ),

            // 天津大学
            ExamSubject(
                id = "es19",
                universityId = "16",
                universityName = "天津大学",
                majorId = "m19",
                majorName = "化学工程与技术",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("302", "数学二", true, "统考"),
                    ExamSubject.SubjectDetail("826", "化工原理", true, "自命题")
                )
            ),

            // 同济大学
            ExamSubject(
                id = "es20",
                universityId = "17",
                universityName = "同济大学",
                majorId = "m20",
                majorName = "土木工程",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("301", "数学一", true, "统考"),
                    ExamSubject.SubjectDetail("808", "材料力学与结构力学", true, "自命题")
                )
            ),

            // 北京理工大学
            ExamSubject(
                id = "es21",
                universityId = "18",
                universityName = "北京理工大学",
                majorId = "m21",
                majorName = "兵器科学与技术",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("301", "数学一", true, "统考"),
                    ExamSubject.SubjectDetail("835", "物理化学", true, "自命题")
                )
            ),

            // 南开大学
            ExamSubject(
                id = "es22",
                universityId = "19",
                universityName = "南开大学",
                majorId = "m22",
                majorName = "经济学",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("303", "数学三", true, "统考"),
                    ExamSubject.SubjectDetail("878", "经济学综合", true, "自命题")
                )
            ),

            // 四川大学
            ExamSubject(
                id = "es23",
                universityId = "20",
                universityName = "四川大学",
                majorId = "m23",
                majorName = "口腔医学",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("306", "临床医学综合能力(西医)", true, "统考"),
                    ExamSubject.SubjectDetail("652", "口腔综合", true, "自命题")
                )
            ),

            // 重庆大学
            ExamSubject(
                id = "es24",
                universityId = "21",
                universityName = "重庆大学",
                majorId = "m24",
                majorName = "电气工程",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("301", "数学一", true, "统考"),
                    ExamSubject.SubjectDetail("840", "电路原理", true, "自命题")
                )
            ),

            // 西北工业大学
            ExamSubject(
                id = "es25",
                universityId = "22",
                universityName = "西北工业大学",
                majorId = "m25",
                majorName = "航空宇航科学与技术",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("301", "数学一", true, "统考"),
                    ExamSubject.SubjectDetail("821", "自动控制原理", true, "自命题")
                )
            ),

            // 兰州大学
            ExamSubject(
                id = "es26",
                universityId = "23",
                universityName = "兰州大学",
                majorId = "m26",
                majorName = "草学",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("314", "数学(农)", true, "统考"),
                    ExamSubject.SubjectDetail("414", "植物生理学与生物化学", true, "统考")
                )
            ),

            // 中国人民大学
            ExamSubject(
                id = "es27",
                universityId = "24",
                universityName = "中国人民大学",
                majorId = "m27",
                majorName = "理论经济学",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("303", "数学三", true, "统考"),
                    ExamSubject.SubjectDetail("802", "经济学综合", true, "自命题")
                )
            ),

            // 中南大学
            ExamSubject(
                id = "es28",
                universityId = "25",
                universityName = "中南大学",
                majorId = "m28",
                majorName = "冶金工程",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("301", "数学一", true, "统考"),
                    ExamSubject.SubjectDetail("819", "材料科学基础", true, "自命题")
                )
            ),

            // 东北大学
            ExamSubject(
                id = "es29",
                universityId = "26",
                universityName = "东北大学",
                majorId = "m29",
                majorName = "控制科学与工程",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("301", "数学一", true, "统考"),
                    ExamSubject.SubjectDetail("839", "自动控制原理", true, "自命题")
                )
            ),

            // 湖南大学
            ExamSubject(
                id = "es30",
                universityId = "27",
                universityName = "湖南大学",
                majorId = "m30",
                majorName = "土木工程",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("301", "数学一", true, "统考"),
                    ExamSubject.SubjectDetail("801", "结构力学", true, "自命题")
                )
            ),

            // 北京科技大学
            ExamSubject(
                id = "es31",
                universityId = "28",
                universityName = "北京科技大学",
                majorId = "m31",
                majorName = "材料科学与工程",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("302", "数学二", true, "统考"),
                    ExamSubject.SubjectDetail("814", "材料科学基础", true, "自命题")
                )
            ),

            // 武汉理工大学
            ExamSubject(
                id = "es32",
                universityId = "29",
                universityName = "武汉理工大学",
                majorId = "m32",
                majorName = "材料科学与工程",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("302", "数学二", true, "统考"),
                    ExamSubject.SubjectDetail("833", "材料科学基础", true, "自命题")
                )
            ),

            // 华南理工大学
            ExamSubject(
                id = "es33",
                universityId = "30",
                universityName = "华南理工大学",
                majorId = "m33",
                majorName = "轻工技术与工程",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("302", "数学二", true, "统考"),
                    ExamSubject.SubjectDetail("820", "有机化学", true, "自命题")
                )
            ),

            // 四川大学
            ExamSubject(
                id = "es34",
                universityId = "31",
                universityName = "四川大学",
                majorId = "m34",
                majorName = "材料科学与工程",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("302", "数学二", true, "统考"),
                    ExamSubject.SubjectDetail("848", "材料科学基础", true, "自命题")
                )
            ),

            // 电子科技大学
            ExamSubject(
                id = "es35",
                universityId = "32",
                universityName = "电子科技大学",
                majorId = "m35",
                majorName = "电子科学与技术",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("301", "数学一", true, "统考"),
                    ExamSubject.SubjectDetail("832", "微电子器件", true, "自命题")
                )
            ),

            // 西安电子科技大学
            ExamSubject(
                id = "es36",
                universityId = "33",
                universityName = "西安电子科技大学",
                majorId = "m36",
                majorName = "信息与通信工程",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("301", "数学一", true, "统考"),
                    ExamSubject.SubjectDetail("821", "电路、信号与系统", true, "自命题")
                )
            ),

            // 东北师范大学
            ExamSubject(
                id = "es37",
                universityId = "34",
                universityName = "东北师范大学",
                majorId = "m37",
                majorName = "教育学",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("311", "教育学专业基础综合", true, "统考"),
                    ExamSubject.SubjectDetail("703", "教育学基础综合", true, "自命题")
                )
            ),

            // 华东师范大学
            ExamSubject(
                id = "es38",
                universityId = "35",
                universityName = "华东师范大学",
                majorId = "m38",
                majorName = "教育学",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("311", "教育学专业基础综合", true, "统考"),
                    ExamSubject.SubjectDetail("703", "教育学基础综合", true, "自命题")
                )
            ),

            // 南京航空航天大学
            ExamSubject(
                id = "es39",
                universityId = "36",
                universityName = "南京航空航天大学",
                majorId = "m39",
                majorName = "航空宇航科学与技术",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("301", "数学一", true, "统考"),
                    ExamSubject.SubjectDetail("823", "电工电子学", true, "自命题")
                )
            ),

            // 南京理工大学
            ExamSubject(
                id = "es40",
                universityId = "37",
                universityName = "南京理工大学",
                majorId = "m40",
                majorName = "兵器科学与技术",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("301", "数学一", true, "统考"),
                    ExamSubject.SubjectDetail("812", "机械原理", true, "自命题")
                )
            ),

            // 西北农林科技大学
            ExamSubject(
                id = "es41",
                universityId = "38",
                universityName = "西北农林科技大学",
                majorId = "m41",
                majorName = "农学",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("315", "化学(农)", true, "统考"),
                    ExamSubject.SubjectDetail("414", "植物生理学与生物化学", true, "统考")
                )
            ),

            // 北京工业大学
            ExamSubject(
                id = "es42",
                universityId = "39",
                universityName = "北京工业大学",
                majorId = "m42",
                majorName = "土木工程",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("301", "数学一", true, "统考"),
                    ExamSubject.SubjectDetail("841", "结构力学", true, "自命题")
                )
            ),

            // 北京化工大学
            ExamSubject(
                id = "es43",
                universityId = "40",
                universityName = "北京化工大学",
                majorId = "m43",
                majorName = "化学工程与技术",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("302", "数学二", true, "统考"),
                    ExamSubject.SubjectDetail("810", "化工原理", true, "自命题")
                )
            ),

            // 北京邮电大学
            ExamSubject(
                id = "es44",
                universityId = "41",
                universityName = "北京邮电大学",
                majorId = "m44",
                majorName = "信息与通信工程",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("301", "数学一", true, "统考"),
                    ExamSubject.SubjectDetail("801", "通信原理", true, "自命题")
                )
            ),

            // 合肥工业大学
            ExamSubject(
                id = "es45",
                universityId = "42",
                universityName = "合肥工业大学",
                majorId = "m45",
                majorName = "管理科学与工程",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("303", "数学三", true, "统考"),
                    ExamSubject.SubjectDetail("816", "管理学", true, "自命题")
                )
            ),

            // 南昌大学
            ExamSubject(
                id = "es46",
                universityId = "43",
                universityName = "南昌大学",
                majorId = "m46",
                majorName = "食品科学与工程",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("302", "数学二", true, "统考"),
                    ExamSubject.SubjectDetail("852", "微生物学", true, "自命题")
                )
            ),

            // 北京林业大学
            ExamSubject(
                id = "es47",
                universityId = "44",
                universityName = "北京林业大学",
                majorId = "m47",
                majorName = "林学",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("314", "数学(农)", true, "统考"),
                    ExamSubject.SubjectDetail("414", "植物生理学与生物化学", true, "统考")
                )
            ),

            // 华中农业大学
            ExamSubject(
                id = "es48",
                universityId = "45",
                universityName = "华中农业大学",
                majorId = "m48",
                majorName = "园艺学",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("315", "化学(农)", true, "统考"),
                    ExamSubject.SubjectDetail("414", "植物生理学与生物化学", true, "统考")
                )
            ),

            // 南京农业大学
            ExamSubject(
                id = "es49",
                universityId = "46",
                universityName = "南京农业大学",
                majorId = "m49",
                majorName = "作物学",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("315", "化学(农)", true, "统考"),
                    ExamSubject.SubjectDetail("414", "植物生理学与生物化学", true, "统考")
                )
            ),

            // 东北林业大学
            ExamSubject(
                id = "es50",
                universityId = "47",
                universityName = "东北林业大学",
                majorId = "m50",
                majorName = "林业工程",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("302", "数学二", true, "统考"),
                    ExamSubject.SubjectDetail("813", "林业工程基础", true, "自命题")
                )
            ),

            // 北京协和医学院
            ExamSubject(
                id = "es51",
                universityId = "48",
                universityName = "北京协和医学院",
                majorId = "m51",
                majorName = "基础医学",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("306", "临床医学综合能力(西医)", true, "统考"),
                    ExamSubject.SubjectDetail("654", "基础医学综合", true, "自命题")
                )
            ),

            // 中国药科大学
            ExamSubject(
                id = "es52",
                universityId = "49",
                universityName = "中国药科大学",
                majorId = "m52",
                majorName = "药学",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("349", "药学综合", true, "自命题"),
                    ExamSubject.SubjectDetail("611", "药学基础综合", true, "自命题")
                )
            ),

            // 中国海洋大学
            ExamSubject(
                id = "es53",
                universityId = "50",
                universityName = "中国海洋大学",
                majorId = "m53",
                majorName = "海洋科学",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("302", "数学二", true, "统考"),
                    ExamSubject.SubjectDetail("835", "海洋科学导论", true, "自命题")
                )
            ),

            // 中央民族大学
            ExamSubject(
                id = "es54",
                universityId = "51",
                universityName = "中央民族大学",
                majorId = "m54",
                majorName = "民族学",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("313", "历史学基础", true, "统考"),
                    ExamSubject.SubjectDetail("719", "民族学通论", true, "自命题")
                )
            ),

            // 云南大学
            ExamSubject(
                id = "es55",
                universityId = "52",
                universityName = "云南大学",
                majorId = "m55",
                majorName = "民族学",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("313", "历史学基础", true, "统考"),
                    ExamSubject.SubjectDetail("704", "民族学理论与方法", true, "自命题")
                )
            ),

            // 陕西师范大学
            ExamSubject(
                id = "es56",
                universityId = "53",
                universityName = "陕西师范大学",
                majorId = "m56",
                majorName = "中国语言文学",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("311", "教育学专业基础综合", true, "统考"),
                    ExamSubject.SubjectDetail("716", "文学综合", true, "自命题")
                )
            ),

            // 华南师范大学
            ExamSubject(
                id = "es57",
                universityId = "54",
                universityName = "华南师范大学",
                majorId = "m57",
                majorName = "心理学",
                subjects = listOf(
                    ExamSubject.SubjectDetail("101", "政治", true, "统考"),
                    ExamSubject.SubjectDetail("201", "英语一", true, "统考"),
                    ExamSubject.SubjectDetail("312", "心理学专业基础综合", true, "统考"),
                    ExamSubject.SubjectDetail("707", "心理学综合", true, "自命题")
                )
            )
        )
    }
}