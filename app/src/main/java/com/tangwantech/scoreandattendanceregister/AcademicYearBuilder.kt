package com.tangwantech.scoreandattendanceregister

import com.tangwantech.scoreandattendanceregister.listbuilders.SubjectsListBuilder
import com.tangwantech.scoreandattendanceregister.models.AcademicYear

class AcademicYearBuilder {
    companion object{
        fun build(yearName: String, formName: String, subjectNames:List<String>, classNumber:String):AcademicYear{
            val subjects = SubjectsListBuilder.build(subjectNames)
            return AcademicYear(yearName, formName, subjects, classNumber)
        }
    }
}