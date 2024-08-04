package com.tangwantech.scoreandattendanceregister.repositories

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tangwantech.scoreandattendanceregister.models.AcademicYearListConverter
import com.tangwantech.scoreandattendanceregister.models.Student

@Database (entities = [Student::class], version = 1)
@TypeConverters(AcademicYearListConverter::class)
abstract class StudentRoomDatabase: RoomDatabase() {
    abstract fun studentDao(): StudentDao

    companion object{

        private var INSTANCE: StudentRoomDatabase? = null

        fun getDatabase(context: Context): StudentRoomDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }

            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StudentRoomDatabase::class.java,
                    "students"
                ).build()
                INSTANCE = instance
                return instance
            }



        }
    }
}