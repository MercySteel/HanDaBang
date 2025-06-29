package com.example.myapp.database.extensions

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.example.myapp.model.*

// 定义与KaoyanDatabaseHelper一致的常量
private const val TABLE_UNIVERSITIES = "universities"
private const val TABLE_SCORE_LINES = "score_lines"
private const val TABLE_MAJORS = "majors"
private const val TABLE_MAJOR_UNIVERSITY_LINK = "major_university_link"
private const val TABLE_EXAM_SUBJECTS = "exam_subjects"
private const val TABLE_SUBJECT_DETAILS = "subject_details"

// 字段名常量
private const val COL_ID = "id"
private const val COL_NAME = "name"
private const val COL_IS_985 = "is985"
private const val COL_IS_211 = "is211"
private const val COL_UNIVERSITY_ID = "university_id"
private const val COL_MAJOR_NAME_FK = "major_name_fk"
private const val COL_YEAR = "year"
private const val COL_SCORE = "score"
private const val COL_CODE = "code"
private const val COL_DESCRIPTION = "description"
private const val COL_CATEGORY = "category"
private const val COL_TYPICAL_SCORE = "typical_score"
private const val COL_UNIVERSITY_NAME = "university_name"
private const val COL_MAJOR_ID = "major_id"
private const val COL_MAJOR_NAME = "major_name"
private const val COL_SUBJECT_ID = "subject_id"
private const val COL_IS_REQUIRED = "is_required"
private const val COL_EXAM_TYPE = "exam_type"

// 院校相关扩展
fun SQLiteDatabase.insertUniversity(university: University) {
    insert(TABLE_UNIVERSITIES, null, ContentValues().apply {
        put(COL_ID, university.id)
        put(COL_NAME, university.name)
        put(COL_IS_985, university.is985)
        put(COL_IS_211, university.is211)
    })
}

fun SQLiteDatabase.insertScoreLine(universityId: String, scoreLine: University.ScoreLine) {
    insert(TABLE_SCORE_LINES, null, ContentValues().apply {
        put(COL_UNIVERSITY_ID, universityId)
        put(COL_MAJOR_NAME_FK, scoreLine.major)
        put(COL_YEAR, scoreLine.year)
        put(COL_SCORE, scoreLine.score)
    })
}

// 专业相关扩展
fun SQLiteDatabase.insertMajor(major: Major) {
    insert(TABLE_MAJORS, null, ContentValues().apply {
        put(COL_ID, major.id)
        put(COL_CODE, major.code)
        put(COL_NAME, major.name)
        put(COL_DESCRIPTION, major.description)
        put(COL_CATEGORY, major.category)
        put(COL_TYPICAL_SCORE, major.typicalScore)
    })
}

fun SQLiteDatabase.insertMajorUniversityLink(majorId: Int, universityId: String) {
    insert(TABLE_MAJOR_UNIVERSITY_LINK, null, ContentValues().apply {
        put(COL_MAJOR_ID, majorId)
        put(COL_UNIVERSITY_ID, universityId)
    })
}

// 考试科目相关扩展
fun SQLiteDatabase.insertExamSubject(examSubject: ExamSubject) {
    insert(TABLE_EXAM_SUBJECTS, null, ContentValues().apply {
        put(COL_ID, examSubject.id)
        put(COL_UNIVERSITY_ID, examSubject.universityId)
        put(COL_UNIVERSITY_NAME, examSubject.universityName)
        put(COL_MAJOR_ID, examSubject.majorId)
        put(COL_MAJOR_NAME, examSubject.majorName)
    })
}

fun SQLiteDatabase.insertSubjectDetail(subjectId: String, detail: ExamSubject.SubjectDetail) {
    insert(TABLE_SUBJECT_DETAILS, null, ContentValues().apply {
        put(COL_SUBJECT_ID, subjectId)
        put(COL_CODE, detail.code)
        put(COL_NAME, detail.name)
        put(COL_IS_REQUIRED, detail.isRequired)
        put(COL_EXAM_TYPE, detail.examType)
    })
}

// 批量操作扩展
fun SQLiteDatabase.bulkInsertUniversities(universities: List<University>) {
    beginTransaction()
    try {
        universities.forEach { university ->
            insertUniversity(university)
            university.scoreLines.forEach { scoreLine ->
                insertScoreLine(university.id, scoreLine)
            }
        }
        setTransactionSuccessful()
    } finally {
        endTransaction()
    }
}

// 游标扩展
fun android.database.Cursor.getIntOrNull(columnIndex: Int): Int? {
    return if (isNull(columnIndex)) null else getInt(columnIndex)
}