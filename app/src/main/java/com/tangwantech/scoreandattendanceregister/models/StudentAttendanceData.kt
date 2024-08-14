package com.tangwantech.scoreandattendanceregister.models

data class StudentAttendanceData(
    val classNumber: String,
    val studentId: String,
    val studentName: String,
    var attendanceDate: String,
    var absentCount: Int = 0,
    var isAbsent: Boolean = true,
    var totalAbsencesForSequence: Int = 0
)
