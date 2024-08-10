package com.tangwantech.scoreandattendanceregister.listbuilders

import com.tangwantech.scoreandattendanceregister.AcademicYearBuilder
import com.tangwantech.scoreandattendanceregister.models.AcademicYear
import com.tangwantech.scoreandattendanceregister.models.Student
import com.tangwantech.scoreandattendanceregister.models.StudentInfo

class StudentsListBuilder {
    companion object{

        fun build(studentsInfoList: List<StudentInfo>): List<Student>{
            val students = ArrayList<Student>()
            for(temp in studentsInfoList){
                val academicYears = ArrayList<AcademicYear>()
                academicYears.add(AcademicYearBuilder.build(temp.academicYear, temp.formName, temp.subjects, temp.classNumber))
                students.add(Student(0, temp.id, temp.studentName, temp.gender, academicYears))
            }
            return students
        }
    }

}