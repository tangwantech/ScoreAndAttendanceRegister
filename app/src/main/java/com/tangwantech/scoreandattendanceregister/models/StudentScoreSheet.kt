package com.tangwantech.scoreandattendanceregister.models

data class StudentScoreSheet(
    val studentId: String,
    val studentName: String,
    val studentGender: String,
    val academicYear:String,
    val formName: String,
    val classNumber: String,
    val subjectName: String,
    val sequenceName: String,
    val score: Double)
