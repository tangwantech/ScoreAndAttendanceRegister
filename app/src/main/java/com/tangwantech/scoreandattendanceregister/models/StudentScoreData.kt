package com.tangwantech.scoreandattendanceregister.models

data class StudentScoreData(
    val studentId: String,
    val studentName: String,
    val studentGender: String,
    val classNumber: String,
    var score: Double)
