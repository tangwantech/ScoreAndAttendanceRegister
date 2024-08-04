package com.tangwantech.scoreandattendanceregister.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson


data class Attendance(val date: String, var totalAbsences: Int, var isPresent: Boolean)
data class TermSequence(val sequenceName: String, var score: Double = 0.0, var totalAbsences: Int = 0, val attendances: ArrayList<Attendance> = ArrayList())
data class Subject(val subjectName  : String, var isRegistered: Boolean, val termSequences: List<TermSequence>)

data class AcademicYear(val yearName: String, val formName: String,  val subjects: List<Subject>, var clasNumber: String = "")
class AcademicYearListConverter{
    @TypeConverter
    fun fromListToJson(value: List<AcademicYear>) = Gson().toJson(value)
    @TypeConverter
    fun fromJsonToList(value: String) = Gson().fromJson(value, Array<AcademicYear>::class.java).toList()
}

@Entity(tableName="students")
data class Student(
    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(name = "studentId")
    var studentId: String,

    @ColumnInfo(name = "studentName")
    var studentName: String,

    @ColumnInfo(name = "studentGender")
    var studentGender: String,

    @ColumnInfo(name = "academicYears")
    val academicYears: List<AcademicYear>)

