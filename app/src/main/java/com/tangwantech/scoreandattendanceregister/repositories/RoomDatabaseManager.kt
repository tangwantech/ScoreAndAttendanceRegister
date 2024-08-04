package com.tangwantech.scoreandattendanceregister.repositories

import android.content.Context
import com.tangwantech.scoreandattendanceregister.models.Student
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RoomDatabaseManager(private val context: Context) {

    private val database = StudentRoomDatabase.getDatabase(context)


    fun readStudentsFromRoomDatabase(onReadFromRoom: OnReadFromRoom){
        CoroutineScope(Dispatchers.IO).launch{
            val temp=  database.studentDao().getAllStudents()
            withContext(Dispatchers.Main){
                onReadFromRoom.result(temp)
            }
        }


    }



    fun insertStudents(students: List<Student>){
        CoroutineScope(Dispatchers.IO).launch {
            database.studentDao().insertStudents(students)
        }

    }

    fun updateStudents(students: List<Student>){
        CoroutineScope(Dispatchers.IO).launch {
            database.studentDao().updateStudents(students)
        }

    }

    fun deleteStudents(students: List<Student>){
        CoroutineScope(Dispatchers.IO).launch {
            database.studentDao().deleteStudents(students)
        }


    }

    fun deleteAll() {
        CoroutineScope(Dispatchers.IO).launch {
            database.studentDao().deleteAll()
        }

    }

    interface OnReadFromRoom{
        fun result(result: List<Student>)
        fun error()
    }
}