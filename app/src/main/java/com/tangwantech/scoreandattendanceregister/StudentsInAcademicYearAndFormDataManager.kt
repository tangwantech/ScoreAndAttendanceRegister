package com.tangwantech.scoreandattendanceregister

import com.tangwantech.scoreandattendanceregister.models.Student

class StudentsInAcademicYearAndFormDataManager {
    private val students: ArrayList<Student> = ArrayList()

    fun setStudents(students: List<Student>){
        this.students.clear()
        this.students.addAll(students)
    }

    fun getStudents(): ArrayList<Student>{
        return students
    }

    fun addStudent(student: Student){
        students.add(student)
    }

    fun filterStudentsWhere(academicYearPair: Pair<Int, String>, formName: String, students: List<Student>, onFilter: OnFilter){

        val studentsWithAcademicYears = students.filter { student ->
            academicYearPair.first in student.academicYears.indices && student.academicYears[academicYearPair.first].yearName == academicYearPair.second && student.academicYears[academicYearPair.first].formName == formName
        }

        if(studentsWithAcademicYears.isEmpty()){
            onFilter.empty()

        }else{
//            val tempStudents = students.filter { student: Student ->
//
//                student.academicYears[academicYearPair.first].yearName == academicYearPair.second && student.academicYears[academicYearPair.first].formName == formName
//            }
            setStudents(studentsWithAcademicYears)
            onFilter.available()


        }

    }

    fun clear() {
        students.clear()
    }

    interface OnFilter{
        fun available()
        fun empty()
    }
}