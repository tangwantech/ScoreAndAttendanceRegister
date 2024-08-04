package com.tangwantech.scoreandattendanceregister.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tangwantech.scoreandattendanceregister.StudentsScoreSheetAndAttendanceManager
import com.tangwantech.scoreandattendanceregister.filereaders.AssertReader
import com.tangwantech.scoreandattendanceregister.models.Student
import com.tangwantech.scoreandattendanceregister.models.StudentScoreSheet
import com.tangwantech.scoreandattendanceregister.repositories.RoomDatabaseManager

class MainActivityViewModel: ViewModel(){
    private lateinit var studentsScoreSheetAndAttendanceManager: StudentsScoreSheetAndAttendanceManager


    fun setStudentsScoreSheetToDisplay(academicYearPair: Pair<Int, String>, formName: String, subjectPair: Pair<Int, String>, sequencePair: Pair<Int, String>){
        studentsScoreSheetAndAttendanceManager.setStudentsScoreSheetToDisplay(academicYearPair, formName, subjectPair, sequencePair)
    }

    fun getStudentsScoreSheetToDisplay(): List<StudentScoreSheet>{
        return studentsScoreSheetAndAttendanceManager.getStudentsScoreSheet()
    }

//    fun insertStudentsToRoomDatabase() {
//        roomDataManager.insertStudents(studentsScoreSheetAndAttendanceManager.getStudents())
//    }
}