package com.tangwantech.scoreandattendanceregister.models


data class StudentInfo(
    val academicYear: String,
    val id: String,
    val studentName: String,
    val gender: String,
    val formName: String,
    val subjects: List<String>)

data class StudentsClassList(val studentList: List<StudentInfo>)
