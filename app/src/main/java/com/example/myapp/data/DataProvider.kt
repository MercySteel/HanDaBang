package com.example.myapp.data

import com.example.myapp.model.University

object DataProvider {
    fun getUniversitiesWithScores(): List<University> {
        return listOf(
            // 华北地区
            University(
                id = "38",
                name = "北京信息科技大学",
                is985 = false,
                is211 = false,
                officialWebsite = "http://www.bistu.edu.cn",  // 添加官网地址
                scoreLines = listOf(
                    University.ScoreLine("计算机科学与技术", 2023, 320),
                    University.ScoreLine("电子信息工程", 2023, 315),
                    University.ScoreLine("自动化", 2023, 310),
                    University.ScoreLine("信息管理与信息系统", 2023, 305)
                )
            ),

            University(
                id = "1",
                name = "清华大学",
                is985 = true,
                is211 = true,
                officialWebsite = "https://www.tsinghua.edu.cn",  // 添加官网地址
                scoreLines = listOf(
                    University.ScoreLine("计算机科学与技术", 2023, 350),
                    University.ScoreLine("电子工程", 2023, 340),
                    University.ScoreLine("建筑学", 2023, 345)
                )
            ),
            University(
                id = "2",
                name = "北京大学",
                is985 = true,
                is211 = true,
                officialWebsite = "https://www.pku.edu.cn",  // 添加官网地址
                scoreLines = listOf(
                    University.ScoreLine("法学", 2023, 355),
                    University.ScoreLine("经济学", 2023, 360),
                    University.ScoreLine("临床医学", 2023, 365)
                )
            ),
            University(
                id = "3",
                name = "中国人民大学",
                is985 = true,
                is211 = true,
                officialWebsite = "http://www.ruc.edu.cn",  // 添加官网地址
                scoreLines = listOf(
                    University.ScoreLine("金融学", 2023, 350),
                    University.ScoreLine("新闻传播学", 2023, 345),
                    University.ScoreLine("法学", 2023, 355)
                )
            ),
            University(
                id = "4",
                name = "北京航空航天大学",
                is985 = true,
                is211 = true,
                officialWebsite = "https://www.buaa.edu.cn",  // 添加官网地址
                scoreLines = listOf(
                    University.ScoreLine("航空航天工程", 2023, 340),
                    University.ScoreLine("计算机科学", 2023, 345),
                    University.ScoreLine("材料科学", 2023, 335)
                )
            ),
            University(
                id = "5",
                name = "北京理工大学",
                is985 = true,
                is211 = true,
                officialWebsite = "https://www.bit.edu.cn",  // 添加官网地址
                scoreLines = listOf(
                    University.ScoreLine("机械工程", 2023, 335),
                    University.ScoreLine("光学工程", 2023, 330),
                    University.ScoreLine("控制科学", 2023, 340)
                )
            ),
            University(
                id = "6",
                name = "中国农业大学",
                is985 = true,
                is211 = true,
                officialWebsite = "http://www.cau.edu.cn",  // 添加官网地址
                scoreLines = listOf(
                    University.ScoreLine("农业工程", 2023, 320),
                    University.ScoreLine("食品科学", 2023, 325),
                    University.ScoreLine("生物技术", 2023, 330)
                )
            ),
            University(
                id = "7",
                name = "中央民族大学",
                is985 = true,
                is211 = true,
                officialWebsite = "http://www.muc.edu.cn",  // 添加官网地址
                scoreLines = listOf(
                    University.ScoreLine("民族学", 2023, 325),
                    University.ScoreLine("社会学", 2023, 330),
                    University.ScoreLine("语言学", 2023, 335)
                )
            ),
            University(
                id = "8",
                name = "天津大学",
                is985 = true,
                is211 = true,
                officialWebsite = "http://www.tju.edu.cn",  // 添加官网地址
                scoreLines = listOf(
                    University.ScoreLine("化学工程", 2023, 340),
                    University.ScoreLine("建筑学", 2023, 345),
                    University.ScoreLine("精密仪器", 2023, 335)
                )
            ),
            University(
                id = "9",
                name = "南开大学",
                is985 = true,
                is211 = true,
                officialWebsite = "https://www.nankai.edu.cn",  // 添加官网地址
                scoreLines = listOf(
                    University.ScoreLine("数学", 2023, 350),
                    University.ScoreLine("经济学", 2023, 355),
                    University.ScoreLine("历史学", 2023, 345)
                )
            ),

            // 东北地区
            University(
                id = "10",
                name = "哈尔滨工业大学",
                is985 = true,
                is211 = true,
                officialWebsite = "https://www.hit.edu.cn",  // 添加官网地址
                scoreLines = listOf(
                    University.ScoreLine("航天工程", 2023, 335),
                    University.ScoreLine("计算机科学", 2023, 340),
                    University.ScoreLine("机器人工程", 2023, 345)
                )
            ),
            University(
                id = "11",
                name = "吉林大学",
                is985 = true,
                is211 = true,
                officialWebsite = "https://www.jlu.edu.cn",  // 添加官网地址
                scoreLines = listOf(
                    University.ScoreLine("车辆工程", 2023, 330),
                    University.ScoreLine("化学", 2023, 335),
                    University.ScoreLine("法学", 2023, 340)
                )
            ),
            University(
                id = "12",
                name = "东北大学",
                is985 = true,
                is211 = true,
                officialWebsite = "https://www.neu.edu.cn",  // 添加官网地址
                scoreLines = listOf(
                    University.ScoreLine("冶金工程", 2023, 325),
                    University.ScoreLine("计算机科学", 2023, 335),
                    University.ScoreLine("自动化", 2023, 330)
                )
            ),
            University(
                id = "13",
                name = "大连理工大学",
                is985 = true,
                is211 = true,
                officialWebsite = "https://www.dlut.edu.cn",  // 添加官网地址
                scoreLines = listOf(
                    University.ScoreLine("化学工程", 2023, 335),
                    University.ScoreLine("机械工程", 2023, 330),
                    University.ScoreLine("船舶与海洋工程", 2023, 325)
                )
            ),

            // 华东地区
            University(
                id = "14",
                name = "复旦大学",
                is985 = true,
                is211 = true,
                officialWebsite = "https://www.fudan.edu.cn",  // 添加官网地址
                scoreLines = listOf(
                    University.ScoreLine("临床医学", 2023, 365),
                    University.ScoreLine("经济学", 2023, 360),
                    University.ScoreLine("新闻传播学", 2023, 355)
                )
            ),
            University(
                id = "15",
                name = "上海交通大学",
                is985 = true,
                is211 = true,
                officialWebsite = "https://www.sjtu.edu.cn",  // 添加官网地址
                scoreLines = listOf(
                    University.ScoreLine("电子信息", 2023, 350),
                    University.ScoreLine("船舶与海洋工程", 2023, 345),
                    University.ScoreLine("临床医学", 2023, 360)
                )
            ),
            University(
                id = "16",
                name = "同济大学",
                is985 = true,
                is211 = true,
                officialWebsite = "https://www.tongji.edu.cn",  // 添加官网地址
                scoreLines = listOf(
                    University.ScoreLine("建筑学", 2023, 350),
                    University.ScoreLine("土木工程", 2023, 345),
                    University.ScoreLine("汽车工程", 2023, 340)
                )
            ),
            University(
                id = "17",
                name = "华东师范大学",
                is985 = true,
                is211 = true,
                officialWebsite = "https://www.ecnu.edu.cn",  // 添加官网地址
                scoreLines = listOf(
                    University.ScoreLine("教育学", 2023, 350),
                    University.ScoreLine("心理学", 2023, 345),
                    University.ScoreLine("软件工程", 2023, 340)
                )
            ),
            University(
                id = "18",
                name = "南京大学",
                is985 = true,
                is211 = true,
                officialWebsite = "https://www.nju.edu.cn",  // 添加官网地址
                scoreLines = listOf(
                    University.ScoreLine("天文学", 2023, 350),
                    University.ScoreLine("计算机科学", 2023, 355),
                    University.ScoreLine("经济学", 2023, 360)
                )
            ),
            University(
                id = "19",
                name = "东南大学",
                is985 = true,
                is211 = true,
                officialWebsite = "https://www.seu.edu.cn",  // 添加官网地址
                scoreLines = listOf(
                    University.ScoreLine("建筑学", 2023, 345),
                    University.ScoreLine("电子信息", 2023, 350),
                    University.ScoreLine("交通运输", 2023, 340)
                )
            ),
            University(
                id = "20",
                name = "浙江大学",
                is985 = true,
                is211 = true,
                officialWebsite = "https://www.zju.edu.cn",  // 添加官网地址
                scoreLines = listOf(
                    University.ScoreLine("计算机科学", 2023, 355),
                    University.ScoreLine("农业工程", 2023, 340),
                    University.ScoreLine("临床医学", 2023, 360)
                )
            ),
            University(
                id = "21",
                name = "中国科学技术大学",
                is985 = true,
                is211 = true,
                officialWebsite = "https://www.ustc.edu.cn",  // 添加官网地址
                scoreLines = listOf(
                    University.ScoreLine("物理学", 2023, 350),
                    University.ScoreLine("计算机科学", 2023, 355),
                    University.ScoreLine("量子信息", 2023, 360)
                )
            ),
            University(
                id = "22",
                name = "厦门大学",
                is985 = true,
                is211 = true,
                officialWebsite = "https://www.xmu.edu.cn",  // 添加官网地址
                scoreLines = listOf(
                    University.ScoreLine("经济学", 2023, 355),
                    University.ScoreLine("海洋科学", 2023, 345),
                    University.ScoreLine("会计学", 2023, 360)
                )
            ),
            University(
                id = "23",
                name = "山东大学",
                is985 = true,
                is211 = true,
                officialWebsite = "https://www.sdu.edu.cn",  // 添加官网地址
                scoreLines = listOf(
                    University.ScoreLine("数学", 2023, 350),
                    University.ScoreLine("医学", 2023, 355),
                    University.ScoreLine("材料科学", 2023, 345)
                )
            ),

            // 中南地区
            University(
                id = "24",
                name = "武汉大学",
                is985 = true,
                is211 = true,
                officialWebsite = "https://www.whu.edu.cn",  // 添加官网地址
                scoreLines = listOf(
                    University.ScoreLine("法学", 2023, 355),
                    University.ScoreLine("测绘科学", 2023, 350),
                    University.ScoreLine("临床医学", 2023, 360)
                )
            ),
            University(
                id = "25",
                name = "华中科技大学",
                is985 = true,
                is211 = true,
                officialWebsite = "https://www.hust.edu.cn",  // 添加官网地址
                scoreLines = listOf(
                    University.ScoreLine("机械工程", 2023, 345),
                    University.ScoreLine("光电信息", 2023, 350),
                    University.ScoreLine("临床医学", 2023, 360)
                )
            ),
            University(
                id = "26",
                name = "中南大学",
                is985 = true,
                is211 = true,
                officialWebsite = "https://www.csu.edu.cn",  // 添加官网地址
                scoreLines = listOf(
                    University.ScoreLine("冶金工程", 2023, 340),
                    University.ScoreLine("医学", 2023, 355),
                    University.ScoreLine("轨道交通", 2023, 345)
                )
            ),
            University(
                id = "27",
                name = "湖南大学",
                is985 = true,
                is211 = true,
                officialWebsite = "https://www.hnu.edu.cn",  // 添加官网地址
                scoreLines = listOf(
                    University.ScoreLine("机械工程", 2023, 340),
                    University.ScoreLine("土木工程", 2023, 345),
                    University.ScoreLine("设计艺术", 2023, 350)
                )
            ),
            University(
                id = "28",
                name = "中山大学",
                is985 = true,
                is211 = true,
                officialWebsite = "https://www.sysu.edu.cn",  // 添加官网地址
                scoreLines = listOf(
                    University.ScoreLine("医学", 2023, 360),
                    University.ScoreLine("管理学", 2023, 355),
                    University.ScoreLine("海洋科学", 2023, 350)
                )
            ),
            University(
                id = "29",
                name = "华南理工大学",
                is985 = true,
                is211 = true,
                officialWebsite = "https://www.scut.edu.cn",  // 添加官网地址
                scoreLines = listOf(
                    University.ScoreLine("轻工技术", 2023, 340),
                    University.ScoreLine("建筑学", 2023, 345),
                    University.ScoreLine("食品科学", 2023, 335)
                )
            ),

            // 西南地区
            University(
                id = "30",
                name = "四川大学",
                is985 = true,
                is211 = true,
                officialWebsite = "https://www.scu.edu.cn",  // 添加官网地址
                scoreLines = listOf(
                    University.ScoreLine("口腔医学", 2023, 365),
                    University.ScoreLine("高分子材料", 2023, 350),
                    University.ScoreLine("历史学", 2023, 345)
                )
            ),
            University(
                id = "31",
                name = "电子科技大学",
                is985 = true,
                is211 = true,
                officialWebsite = "https://www.uestc.edu.cn",  // 添加官网地址
                scoreLines = listOf(
                    University.ScoreLine("电子信息", 2023, 350),
                    University.ScoreLine("计算机科学", 2023, 355),
                    University.ScoreLine("人工智能", 2023, 360)
                )
            ),
            University(
                id = "32",
                name = "重庆大学",
                is985 = true,
                is211 = true,
                officialWebsite = "https://www.cqu.edu.cn",  // 添加官网地址
                scoreLines = listOf(
                    University.ScoreLine("建筑学", 2023, 345),
                    University.ScoreLine("电气工程", 2023, 350),
                    University.ScoreLine("机械工程", 2023, 340)
                )
            ),

            // 西北地区
            University(
                id = "33",
                name = "西安交通大学",
                is985 = true,
                is211 = true,
                officialWebsite = "https://www.xjtu.edu.cn",  // 添加官网地址
                scoreLines = listOf(
                    University.ScoreLine("能源动力", 2023, 345),
                    University.ScoreLine("电气工程", 2023, 350),
                    University.ScoreLine("管理科学", 2023, 355)
                )
            ),
            University(
                id = "34",
                name = "西北工业大学",
                is985 = true,
                is211 = true,
                officialWebsite = "https://www.nwpu.edu.cn",  // 添加官网地址
                scoreLines = listOf(
                    University.ScoreLine("航空航天", 2023, 345),
                    University.ScoreLine("材料科学", 2023, 350),
                    University.ScoreLine("计算机科学", 2023, 355)
                )
            ),
            University(
                id = "35",
                name = "西北农林科技大学",
                is985 = true,
                is211 = true,
                officialWebsite = "https://www.nwafu.edu.cn",  // 添加官网地址
                scoreLines = listOf(
                    University.ScoreLine("农业科学", 2023, 330),
                    University.ScoreLine("葡萄酒工程", 2023, 335),
                    University.ScoreLine("植物保护", 2023, 340)
                )
            ),
            University(
                id = "36",
                name = "兰州大学",
                is985 = true,
                is211 = true,
                officialWebsite = "https://www.lzu.edu.cn",  // 添加官网地址
                scoreLines = listOf(
                    University.ScoreLine("化学", 2023, 340),
                    University.ScoreLine("生态学", 2023, 345),
                    University.ScoreLine("大气科学", 2023, 350)
                )
            ),

            // 军事院校
            University(
                id = "37",
                name = "国防科技大学",
                is985 = true,
                is211 = true,
                officialWebsite = "http://www.nudt.edu.cn",  // 添加官网地址
                scoreLines = listOf(
                    University.ScoreLine("计算机科学", 2023, 355),
                    University.ScoreLine("信息工程", 2023, 350),
                    University.ScoreLine("系统工程", 2023, 345)
                )
            )
        )
    }
}