package com.tangwantech.scoreandattendanceregister

import android.content.Context
import com.tangwantech.scoreandattendanceregister.models.Attendance
import com.tangwantech.scoreandattendanceregister.models.Student
import com.tangwantech.scoreandattendanceregister.models.StudentAttendanceSheet
import com.tangwantech.scoreandattendanceregister.models.StudentScoreSheet

class StudentsScoreSheetAndAttendanceManager(private val context: Context){
    private val students = ArrayList<Student>()
    private val studentsScoreSheet = ArrayList<StudentScoreSheet>()
    private val studentsAttendanceSheet = ArrayList<StudentAttendanceSheet>()
    private val studentsInAcademicYearFormAndSubject = ArrayList<Student>()
    private var academicYearIndex: Int? =  null
    private var subjectIndex: Int? = null
    private var sequenceIndex: Int? = null

    fun updateStudents(tempStudents: List<Student>) {
        students.clear()
        students.addAll(tempStudents)
    }

    private fun setStudentsInAcademicYearFormAndSubject(academicYearPair: Pair<Int, String>, formName: String, subjectPair: Pair<Int, String>){
        val temp = students.filter { student ->
            student.academicYears[academicYearPair.first].yearName == academicYearPair.second && student.academicYears[academicYearPair.first].formName == formName && student.academicYears[academicYearPair.first].subjects[subjectPair.first].subjectName == subjectPair.second
        }
        studentsInAcademicYearFormAndSubject.clear()
        studentsInAcademicYearFormAndSubject.addAll(temp)
    }

    fun setStudentsScoreSheetToDisplay(academicYearPair: Pair<Int, String>, formName: String, subjectPair: Pair<Int, String>, sequencePair: Pair<Int, String>){
        updateAcademicYearSubjectSequenceIndexes(academicYearPair.first, subjectPair.first, sequencePair.first)
        setStudentsInAcademicYearFormAndSubject(academicYearPair, formName, subjectPair)

        val temp = studentsInAcademicYearFormAndSubject.filter { student ->
            student.academicYears[academicYearPair.first].yearName == academicYearPair.second && student.academicYears[academicYearPair.first].formName == formName && student.academicYears[academicYearPair.first].subjects[subjectPair.first].subjectName == subjectPair.second
        }
//
        studentsScoreSheet.clear()
        temp.forEach {
            val classNumber = it.academicYears[academicYearPair.first].clasNumber
            val studentScore = it.academicYears[academicYearPair.first].subjects[subjectPair.first].termSequences[sequencePair.first].score
            val studentScoreSheet = StudentScoreSheet(it.studentId, it.studentName, it.studentGender, academicYearPair.second, formName, classNumber, subjectPair.second, sequencePair.second, studentScore)
            studentsScoreSheet.add(studentScoreSheet)
        }
        println("selected class students: $studentsInAcademicYearFormAndSubject")
        println("studentsScoreSheet: $studentsScoreSheet")
    }


    fun getStudents(): List<Student>{
        return students
    }

    private fun updateAcademicYearSubjectSequenceIndexes(academicYearIndex: Int, subjectIndex: Int, sequenceIndex: Int){
        this.academicYearIndex = academicYearIndex
        this.subjectIndex = subjectIndex
        this.sequenceIndex = sequenceIndex
    }

    fun updateStudentScoreAt(studentIndex: Int, score: Double){
        studentsInAcademicYearFormAndSubject[studentIndex].academicYears[academicYearIndex!!].subjects[subjectIndex!!].termSequences[sequenceIndex!!].score = score
    }

    fun setStudentsAttendanceSheetToDisplay(academicYearPair: Pair<Int, String>, formName: String, subjectPair: Pair<Int, String>, sequencePair: Pair<Int, String>){
        updateAcademicYearSubjectSequenceIndexes(academicYearPair.first, subjectPair.first, sequencePair.first)
        setStudentsInAcademicYearFormAndSubject(academicYearPair, formName, subjectPair)

        val temp = studentsInAcademicYearFormAndSubject.filter { student ->
            student.academicYears[academicYearPair.first].yearName == academicYearPair.second && student.academicYears[academicYearPair.first].formName == formName && student.academicYears[academicYearPair.first].subjects[subjectPair.first].subjectName == subjectPair.second
        }
//
        studentsAttendanceSheet.clear()
        temp.forEach {
            val classNumber = it.academicYears[academicYearPair.first].clasNumber
            val studentScore = it.academicYears[academicYearPair.first].subjects[subjectPair.first].termSequences[sequencePair.first].score
            val studentAttendanceSheet = StudentAttendanceSheet(it.studentId, it.studentName, it.studentGender, academicYearPair.second, formName, classNumber, subjectPair.second, sequencePair.second)
            studentsAttendanceSheet.add(studentAttendanceSheet)
        }
    }

    fun updateStudentAttendanceAt(studentIndex: Int, attendance: Attendance){
        val temp = studentsInAcademicYearFormAndSubject[studentIndex].academicYears[academicYearIndex!!].subjects[subjectIndex!!].termSequences[sequenceIndex!!].attendances.filter { it.date == attendance.date  }
        if (temp.isEmpty()){
            studentsInAcademicYearFormAndSubject[studentIndex].academicYears[academicYearIndex!!].subjects[subjectIndex!!].termSequences[sequenceIndex!!].attendances.add(attendance)
        }else{
            val tempIndex = studentsInAcademicYearFormAndSubject[studentIndex].academicYears[academicYearIndex!!].subjects[subjectIndex!!].termSequences[sequenceIndex!!].attendances.indexOf(temp[0])
            studentsInAcademicYearFormAndSubject[studentIndex].academicYears[academicYearIndex!!].subjects[subjectIndex!!].termSequences[sequenceIndex!!].attendances[tempIndex] = attendance
        }


    }

    fun getStudentsScoreSheet(): List<StudentScoreSheet>{
        return studentsScoreSheet
    }

    fun getStudentsAttendanceSheet(): List<StudentAttendanceSheet>{
        return studentsAttendanceSheet
    }


    interface OnReadFromAssert{
        fun result(result: Boolean)
    }
}