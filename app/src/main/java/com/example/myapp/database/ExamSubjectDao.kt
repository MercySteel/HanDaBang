package com.example.myapp.database

import android.content.ContentValues
import com.example.myapp.database.extensions.insertExamSubject
import com.example.myapp.database.extensions.insertSubjectDetail
import com.example.myapp.model.ExamSubject



class ExamSubjectDao(private val dbHelper: KaoyanDatabaseHelper) {

    // 表名和列名字符串常量
    private companion object {
        const val TABLE_EXAM_SUBJECTS = "exam_subjects"
        const val TABLE_SUBJECT_DETAILS = "subject_details"
        const val COL_ID = "id"
        const val COL_UNIVERSITY_ID = "university_id"
        const val COL_UNIVERSITY_NAME = "university_name"
        const val COL_MAJOR_ID = "major_id"
        const val COL_MAJOR_NAME = "major_name"
        const val COL_SUBJECT_ID = "subject_id"
        const val COL_CODE = "code"
        const val COL_NAME = "name"
        const val COL_IS_REQUIRED = "is_required"
        const val COL_EXAM_TYPE = "exam_type"
    }

    fun insertExamSubject(examSubject: ExamSubject) {
        val db = dbHelper.writableDatabase.apply {
            beginTransaction()
            try {
                insert(TABLE_EXAM_SUBJECTS, null, ContentValues().apply {
                    put(COL_ID, examSubject.id)
                    put(COL_UNIVERSITY_ID, examSubject.universityId)
                    put(COL_UNIVERSITY_NAME, examSubject.universityName)
                    put(COL_MAJOR_ID, examSubject.majorId)
                    put(COL_MAJOR_NAME, examSubject.majorName)
                })

                examSubject.subjects.forEach { subject ->
                    insert(TABLE_SUBJECT_DETAILS, null, ContentValues().apply {
                        put(COL_SUBJECT_ID, examSubject.id)
                        put(COL_CODE, subject.code)
                        put(COL_NAME, subject.name)
                        put(COL_IS_REQUIRED, subject.isRequired)
                        put(COL_EXAM_TYPE, subject.examType)
                    })
                }
                setTransactionSuccessful()
            } finally {
                endTransaction()
            }
        }
    }

    fun getExamSubjectsByUniversity(universityId: String): List<ExamSubject> {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            TABLE_EXAM_SUBJECTS,
            null,
            "$COL_UNIVERSITY_ID = ?",
            arrayOf(universityId),
            null, null, null
        )

        return cursor.use {
            val result = mutableListOf<ExamSubject>()
            while (it.moveToNext()) {
                result.add(ExamSubject(
                    id = it.getString(it.getColumnIndexOrThrow(COL_ID)),
                    universityId = it.getString(it.getColumnIndexOrThrow(COL_UNIVERSITY_ID)),
                    universityName = it.getString(it.getColumnIndexOrThrow(COL_UNIVERSITY_NAME)),
                    majorId = it.getString(it.getColumnIndexOrThrow(COL_MAJOR_ID)),
                    majorName = it.getString(it.getColumnIndexOrThrow(COL_MAJOR_NAME)),
                    subjects = getSubjectDetails(it.getString(it.getColumnIndexOrThrow(COL_ID)))
                ))
            }
            result
        }
    }

    private fun getSubjectDetails(subjectId: String): List<ExamSubject.SubjectDetail> {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            TABLE_SUBJECT_DETAILS,
            null,
            "$COL_SUBJECT_ID = ?",
            arrayOf(subjectId),
            null, null, null
        )

        return cursor.use {
            val result = mutableListOf<ExamSubject.SubjectDetail>()
            while (it.moveToNext()) {
                result.add(ExamSubject.SubjectDetail(
                    code = it.getString(it.getColumnIndexOrThrow(COL_CODE)),
                    name = it.getString(it.getColumnIndexOrThrow(COL_NAME)),
                    isRequired = it.getInt(it.getColumnIndexOrThrow(COL_IS_REQUIRED)) == 1,
                    examType = it.getString(it.getColumnIndexOrThrow(COL_EXAM_TYPE))
                ))
            }
            result
        }
    }
}