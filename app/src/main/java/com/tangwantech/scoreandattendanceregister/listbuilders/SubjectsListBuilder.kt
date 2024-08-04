package com.tangwantech.scoreandattendanceregister.listbuilders

import com.tangwantech.scoreandattendanceregister.constants.Constants
import com.tangwantech.scoreandattendanceregister.models.Subject

class SubjectsListBuilder {
    companion object{
        fun build(subjectNames: List<String>): List<Subject>{
            val subjects = ArrayList<Subject>()
            val sequences = SequencesListBuilder.build()
            for(name in subjectNames){
                subjects.add(Subject(name, true, sequences))
            }
            return subjects
        }
    }
}