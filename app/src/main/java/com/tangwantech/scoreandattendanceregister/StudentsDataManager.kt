package com.tangwantech.scoreandattendanceregister

import android.content.Context
import com.tangwantech.scoreandattendanceregister.filereaders.ClassListFileReader
import com.tangwantech.scoreandattendanceregister.listbuilders.StudentsListBuilder
import com.tangwantech.scoreandattendanceregister.models.DeleteStudentData
import com.tangwantech.scoreandattendanceregister.models.Student
import com.tangwantech.scoreandattendanceregister.models.StudentInfo
import com.tangwantech.scoreandattendanceregister.repositories.RoomDatabaseManager

class StudentsDataManager(private val context: Context) {
    private var roomDataManager: RoomDatabaseManager = RoomDatabaseManager(context)
    private val students: ArrayList<Student> = ArrayList()

    fun initStudentsFormAssert(context: Context, fileName: String, onReadFromAssertFileListener: OnReadFromAssertFileListener){
        ClassListFileReader.readClassListFromAssert(context, fileName, object: ClassListFileReader.OnResult{

            override fun result(result: List<Student>) {
                setStudents(result)
                roomDataManager.insertStudents(students)
                onReadFromAssertFileListener.onStudentsUnavailable()
//                println("Reading from assert was successful: $result")
            }
            override fun error(error: String?) {
                onReadFromAssertFileListener.onStudentsUnavailable()
                println(error)
            }
        })
    }

    fun initStudentsFromRoom(academicYearPair: Pair<Int, String>, formName: String, onReadFromRoomListener: OnReadFromRoomListener){
        roomDataManager.readStudentsFromRoomDatabase(object: RoomDatabaseManager.OnReadFromRoom{
            override fun result(result: List<Student>) {
                if(result.isEmpty()){
                    onReadFromRoomListener.onStudentsUnAvailable()
                }else{
                    filterStudentsWhere(academicYearPair, formName, result)
                    if (students.isEmpty()){
                        onReadFromRoomListener.onStudentsUnAvailable()
                    }else{
                        onReadFromRoomListener.onStudentsAvailable()
//                        println("Reading from room was successful: $students")
                    }

                }

            }

            override fun error() {
                onReadFromRoomListener.onStudentsUnAvailable()
            }

        })
    }

    private fun setStudents(students: List<Student>){
        this.students.clear()
        this.students.addAll(students)
    }

    fun getStudents(): ArrayList<Student>{
        return students
    }

    fun addStudent(studentsInfo: List<StudentInfo>, onAddStudentListener: OnAddStudentListener){
        val newStudentList = StudentsListBuilder.build(studentsInfo)
        val newStudent = newStudentList[0]
        val studentsWithId = students.filter { it.studentId == newStudent.studentId }

        if(studentsWithId.isEmpty()){
            students.add(newStudent)
            onAddStudentListener.onStudentAdded()
            roomDataManager.insertStudents(newStudentList)
//            println("student with id: ${newStudent.studentId} was added successfully")
        }else{
            onAddStudentListener.onStudentAlreadyExist()
//            println("student with id: ${newStudent.studentId} already exist")
        }

    }

    fun filterStudentsWhere(academicYearPair: Pair<Int, String>, formName: String, students: List<Student>){

        val studentsWithAcademicYears = students.filter { student ->
            academicYearPair.first in student.academicYears.indices && student.academicYears[academicYearPair.first].yearName == academicYearPair.second && student.academicYears[academicYearPair.first].formName == formName
        }
        setStudents(studentsWithAcademicYears)


    }


    fun removeStudents(temp: List<DeleteStudentData>, onStudentsRemoveListener: OnStudentsRemoveListener){
        val studentsToRemove = ArrayList<Student>()
        temp.forEachIndexed { index, deleteStudentData ->
            if (deleteStudentData.isChecked){
                studentsToRemove.add(students[index])
            }
        }

        students.removeAll(studentsToRemove.toSet())
        roomDataManager.deleteStudents(studentsToRemove)
        onStudentsRemoveListener.onStudentsRemoved(studentsToRemove.size)
        onStudentsRemoveListener.onDeleteSuccessful()



    }

    fun getStudentNameAt(index: Int): String{
        return students[index].studentName
    }

    fun getStudentIdAt(index: Int): String{
        return students[index].studentId
    }

    fun getStudentGenderAt(index: Int): String{
        return students[index].studentGender
    }

    fun getStudentClassNumberAt(studentIndex: Int, academicYearIndex: Int): String{
        return students[studentIndex].academicYears[academicYearIndex].clasNumber
    }

    fun getStudentScoreAt(studentIndex: Int, academicYearIndex: Int,  subjectIndex: Int, sequenceIndex: Int): Double{
        val score = students[studentIndex].academicYears[academicYearIndex].subjects[subjectIndex].termSequences[sequenceIndex].score
        return score
    }

    fun updateStudentScoreAt(studentIndex: Int, academicYearIndex: Int, subjectIndex: Int, sequenceIndex: Int, score: Double){
//        println("$studentIndex, $academicYearIndex, $subjectIndex, $sequenceIndex")
//        println(students)
//
//        println(students[studentIndex])
        if(students.isNotEmpty()){
            students[studentIndex].academicYears[academicYearIndex].subjects[subjectIndex].termSequences[sequenceIndex].score = score
            val temp = arrayOf<Student>(students[studentIndex]).toList()
            roomDataManager.updateStudents(temp)
        }

    }

    fun updateStudentsInRoom(){
        roomDataManager.updateStudents(students)
    }


    interface OnAddStudentListener{
        fun onStudentAdded()
        fun onStudentAlreadyExist()
    }

    interface OnStudentsRemoveListener{
        fun onStudentsRemoved(count: Int)
        fun onDeleteSuccessful()
    }

    interface OnReadFromRoomListener{
        fun onStudentsAvailable()
        fun onStudentsUnAvailable()
    }

    interface OnReadFromAssertFileListener{
        fun onStudentsAvailable()
        fun onStudentsUnavailable()
    }
}