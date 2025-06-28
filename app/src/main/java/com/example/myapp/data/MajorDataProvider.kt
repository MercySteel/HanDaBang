package com.example.myapp.data



import com.example.myapp.model.Major

object MajorDataProvider {
    fun getMajorsByCategory(): Map<String, List<Major>> {
        return mapOf(
            "工学" to listOf(
                // 计算机类
                Major(
                    id = 801,
                    code = "080901",
                    name = "计算机科学与技术",
                    description = "培养计算机系统设计与开发人才",
                    category = "工学-计算机类",
                    relatedUniversities = listOf(1, 2, 31),
                    typicalScore = 350
                ),
                Major(
                    id = 802,
                    code = "080902",
                    name = "软件工程",
                    description = "培养软件系统设计与开发人才",
                    category = "工学-计算机类",
                    relatedUniversities = listOf(2, 10, 31),
                    typicalScore = 345
                ),

                // 电子信息类
                Major(
                    id = 803,
                    code = "080701",
                    name = "电子信息工程",
                    description = "培养电子信息系统设计人才",
                    category = "工学-电子信息类",
                    relatedUniversities = listOf(2, 15, 19),
                    typicalScore = 340
                ),

                // 机械类
                Major(
                    id = 804,
                    code = "080202",
                    name = "机械设计制造及其自动化",
                    description = "培养机械设计与制造高级人才",
                    category = "工学-机械类",
                    relatedUniversities = listOf(2, 5, 10),
                    typicalScore = 335
                )
            ),
            "哲学" to listOf(
                Major(
                    id = 101,
                    code = "010101",
                    name = "哲学",
                    description = "培养系统掌握哲学理论的专业人才",
                    category = "哲学-哲学类",
                    relatedUniversities = listOf(1, 6, 18), // 北大、复旦、南大
                    typicalScore = 320
                ),
                Major(
                    id = 102,
                    code = "010102",
                    name = "逻辑学",
                    description = "培养逻辑思维与论证分析能力",
                    category = "哲学-哲学类",
                    relatedUniversities = listOf(1, 18),
                    typicalScore = 315
                )
            ),
            "经济学" to listOf(
                Major(
                    id = 201,
                    code = "020101",
                    name = "经济学",
                    description = "研究资源配置和经济发展规律",
                    category = "经济学-经济学类",
                    relatedUniversities = listOf(1, 2, 22), // 北大、清华、厦大
                    typicalScore = 350
                ),
                Major(
                    id = 202,
                    code = "020301",
                    name = "金融学",
                    description = "培养金融分析与投资管理人才",
                    category = "经济学-金融学类",
                    relatedUniversities = listOf(1, 3, 22), // 人大、央财、上财
                    typicalScore = 355
                )
            ),
            "法学" to listOf(
                Major(
                    id = 301,
                    code = "030101",
                    name = "法学",
                    description = "培养法律实务与理论研究人才",
                    category = "法学-法学类",
                    relatedUniversities = listOf(1, 3, 24), // 北大、人大、武大
                    typicalScore = 345
                ),
                Major(
                    id = 302,
                    code = "030201",
                    name = "政治学与行政学",
                    description = "培养政治分析和公共管理人才",
                    category = "法学-政治学类",
                    relatedUniversities = listOf(1, 3, 28),
                    typicalScore = 335
                )
            ),
            "教育学" to listOf(
                Major(
                    id = 401,
                    code = "040101",
                    name = "教育学",
                    description = "培养教育研究与教学管理人才",
                    category = "教育学-教育学类",
                    relatedUniversities = listOf(1, 17, 24), // 北师大、华东师大
                    typicalScore = 330
                ),
                Major(
                    id = 402,
                    code = "040201",
                    name = "体育教育",
                    description = "培养体育教学与训练专业人才",
                    category = "教育学-体育学类",
                    relatedUniversities = listOf(11, 24),
                    typicalScore = 310
                )
            ),
            "文学" to listOf(
                Major(
                    id = 501,
                    code = "050101",
                    name = "汉语言文学",
                    description = "培养语言文字研究与文学创作人才",
                    category = "文学-中国语言文学类",
                    relatedUniversities = listOf(1, 6, 18),
                    typicalScore = 340
                ),
                Major(
                    id = 502,
                    code = "050201",
                    name = "英语",
                    description = "培养英语语言文学与翻译人才",
                    category = "文学-外国语言文学类",
                    relatedUniversities = listOf(1, 2, 14),
                    typicalScore = 335
                )
            ),
            "历史学" to listOf(
                Major(
                    id = 601,
                    code = "060101",
                    name = "历史学",
                    description = "培养历史研究与文化遗产保护人才",
                    category = "历史学-历史学类",
                    relatedUniversities = listOf(1, 6, 18),
                    typicalScore = 330
                ),
                Major(
                    id = 602,
                    code = "060103",
                    name = "考古学",
                    description = "培养考古发掘与文物研究人才",
                    category = "历史学-考古学类",
                    relatedUniversities = listOf(1, 18, 23),
                    typicalScore = 325
                )
            ),
            "理学" to listOf(
                Major(
                    id = 701,
                    code = "070101",
                    name = "数学与应用数学",
                    description = "培养数学理论与应用研究人才",
                    category = "理学-数学类",
                    relatedUniversities = listOf(1, 2, 18),
                    typicalScore = 350
                ),
                Major(
                    id = 702,
                    code = "070201",
                    name = "物理学",
                    description = "培养物理理论与实验研究人才",
                    category = "理学-物理学类",
                    relatedUniversities = listOf(1, 2, 21),
                    typicalScore = 345
                )
            ),
            "农学" to listOf(
                Major(
                    id = 901,
                    code = "090101",
                    name = "农学",
                    description = "培养农作物生产与遗传育种人才",
                    category = "农学-植物生产类",
                    relatedUniversities = listOf(6, 23, 35),
                    typicalScore = 310
                ),
                Major(
                    id = 902,
                    code = "090401",
                    name = "动物医学",
                    description = "培养动物疾病防治专业人才",
                    category = "农学-动物医学类",
                    relatedUniversities = listOf(6, 23),
                    typicalScore = 315
                )
            ),
            "医学" to listOf(
                Major(
                    id = 1001,
                    code = "100201",
                    name = "临床医学",
                    description = "培养临床医疗高级专门人才",
                    category = "医学-临床医学类",
                    relatedUniversities = listOf(1, 2, 14),
                    typicalScore = 365
                ),
                Major(
                    id = 1002,
                    code = "100301",
                    name = "口腔医学",
                    description = "培养口腔疾病防治专业人才",
                    category = "医学-口腔医学类",
                    relatedUniversities = listOf(1, 30),
                    typicalScore = 370
                )
            ),
            "管理学" to listOf(
                Major(
                    id = 1101,
                    code = "120201",
                    name = "工商管理",
                    description = "培养企业经营管理复合型人才",
                    category = "管理学-工商管理类",
                    relatedUniversities = listOf(1, 3, 28),
                    typicalScore = 345
                ),
                Major(
                    id = 1102,
                    code = "120203",
                    name = "会计学",
                    description = "培养财务核算与审计人才",
                    category = "管理学-工商管理类",
                    relatedUniversities = listOf(3, 22, 28),
                    typicalScore = 350
                )
            ),
            "艺术学" to listOf(
                Major(
                    id = 1201,
                    code = "130201",
                    name = "音乐表演",
                    description = "培养音乐表演与创作专业人才",
                    category = "艺术学-音乐与舞蹈学类",
                    relatedUniversities = listOf(7, 24),
                    typicalScore = 300
                ),
                Major(
                    id = 1202,
                    code = "130401",
                    name = "美术学",
                    description = "培养美术创作与理论研究人才",
                    category = "艺术学-美术学类",
                    relatedUniversities = listOf(1, 7, 27),
                    typicalScore = 310
                )
            ),"交叉学科" to listOf(
                Major(
                    id = 1301,
                    code = "140101",
                    name = "数据科学与大数据技术",
                    description = "培养大数据分析处理专业人才",
                    category = "交叉学科-数据科学类",
                    relatedUniversities = listOf(1, 2, 31),
                    typicalScore = 355
                ),
                Major(
                    id = 1302,
                    code = "140201",
                    name = "人工智能",
                    description = "培养人工智能算法与应用人才",
                    category = "交叉学科-人工智能类",
                    relatedUniversities = listOf(2, 15, 31),
                    typicalScore = 360
                )
            )







            // 可继续添加其他学科门类...
        )
    }
}