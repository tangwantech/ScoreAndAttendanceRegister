package com.tangwantech.scoreandattendanceregister.repositories

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tangwantech.scoreandattendanceregister.models.Student

@Dao
interface StudentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStudents(students: List<Student>)

    @Query("SELECT * FROM students")
    fun getAllStudents(): List<Student>

    @Update
    fun updateStudents(students: List<Student>)

    @Delete
    fun deleteStudents(students: List<Student>)

    @Query("DELETE FROM students")
    fun deleteAll()
}