package com.tangwantech.scoreandattendanceregister.filereaders

import android.content.Context
import com.google.gson.Gson
import com.tangwantech.scoreandattendanceregister.listbuilders.StudentsListBuilder
import com.tangwantech.scoreandattendanceregister.models.Student
import com.tangwantech.scoreandattendanceregister.models.StudentsClassList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.nio.charset.Charset

class ClassListFileReader {
    companion object{
        private fun getJsonFromAssets(context: Context, fileName: String): String? {
            var json: String? = null
            val charset: Charset = Charsets.UTF_8

            try {
                val jsonFile = context.assets.open(fileName)
                val size = jsonFile.available()
                val buffer = ByteArray(size)

                jsonFile.read(buffer)
                jsonFile.close()
                json = String(buffer, charset)

            } catch (e: IOException) {

                return null
            }
            return json
        }
        fun readClassListFromAssert(context: Context, fileName: String, onResult: OnResult){

            CoroutineScope(Dispatchers.IO).launch {
//                val studentsInfoList: ArrayList<StudentInfo> = ArrayList()
                val jsonString = getJsonFromAssets(context, fileName)
                if (jsonString != null){
                    val tempList = Gson().fromJson(jsonString, StudentsClassList::class.java).studentList
                    withContext(Dispatchers.Main){
                        val students = StudentsListBuilder.build(tempList).sortedBy { it.studentName }
                        onResult.result(students)}
                }else{
                    withContext(Dispatchers.Main) {
                        onResult.error("Error reading from assert")
                    }
                }
            }
        }
    }
    interface OnResult{
        fun result(result: List<Student>)
        fun error(error: String?)
    }
}