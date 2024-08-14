package com.tangwantech.scoreandattendanceregister.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tangwantech.scoreandattendanceregister.StudentsDataManager
import com.tangwantech.scoreandattendanceregister.constants.Constants
import com.tangwantech.scoreandattendanceregister.models.Attendance
import com.tangwantech.scoreandattendanceregister.models.StudentAttendanceData
import java.text.SimpleDateFormat
import java.util.Date

class AttendanceRegisterActivityViewModel: ViewModel() {
    private var academicYear: String? = null
    private var academicYearIndex: Int? = null

    private var form: String? = null
    private var formIndex: Int? = null

    private var sequence: String? = null
    private var sequenceIndex: Int? = null

    private var subject: String? = null
    private var subjectIndex: Int? = null

    private var numberOfPeriods: Int = 2

    private lateinit var studentsDataManager: StudentsDataManager

    private val studentsAttendanceDataListForSequence = ArrayList<StudentAttendanceData>()

    private  var currentFragmentIndex = 0

    private val _studentsAttendanceDataListForSequenceAvailable = MutableLiveData<Boolean>()
    val studentsAttendanceDataListForSequenceAvailable: LiveData<Boolean> = _studentsAttendanceDataListForSequenceAvailable

    private val fragmentNames:MutableMap<Int, String> = mutableMapOf()

    private val currentDate = SimpleDateFormat.getDateInstance().format(Date())

    private val _numberOfStudentsAbsent = MutableLiveData<Int>()
    val numberOfStudentsAbsent: LiveData<Int> = _numberOfStudentsAbsent

    private val _studentTotalNumberOfAbsencesForSequenceUpdatedAtIndex = MutableLiveData<Int>()
    val studentTotalNumberOfAbsencesSequenceForSequenceUpdatedAtIndex: LiveData<Int> = _studentTotalNumberOfAbsencesForSequenceUpdatedAtIndex

    fun setFragmentName(index: Int, name: String){
        currentFragmentIndex = index
        fragmentNames[currentFragmentIndex] = name
    }

    fun getFragmentName(): String?{
        return fragmentNames[currentFragmentIndex]
    }

    fun getCurrentDate(): String{
        return currentDate
    }

    fun getCurrentFragmentIndex(): Int{
        return currentFragmentIndex
    }
    fun decrementCurrentFragmentIndex(){
        currentFragmentIndex -= 1
    }

    fun setUpdateAcademicYearIndex(academicYear: String){
        this.academicYear = academicYear
        academicYearIndex = Constants.ACADEMIC_YEARS.indexOf(academicYear)
    }

    fun updateFormIndex(form: String){
        this.form = form
        formIndex = Constants.FORMS.indexOf(form)
    }


    fun updateSequenceIndex(sequence: String){
        this.sequence = sequence
        sequenceIndex = Constants.SEQUENCE_NAMES.indexOf(sequence)
    }

    fun updateSubjectIndex(subject: String){
        this.subject = subject
        subjectIndex = Constants.SUBJECTS.indexOf(subject)
    }

    fun getAcademicYear(): String?{
        return academicYear
    }

    fun getForm(): String?{
        return form
    }

    fun getSequence(): String?{
        return sequence
    }

    fun getSubject(): String?{
        return subject
    }

    fun updateNumberOfPeriods(num: Int){
        numberOfPeriods = num
    }



    fun initStudentsDataManager(context: Context){
        studentsDataManager = StudentsDataManager(context)
        initStudentsFromRoom()

    }

    private fun initStudentsFromRoom(){
        val academicYearPair = Pair<Int, String>(academicYearIndex!!, academicYear!!)
        studentsDataManager.initStudentsFromRoom(academicYearPair, form!!, object: StudentsDataManager.OnReadFromRoomListener{
            override fun onStudentsAvailable() {
                updateStudentsAttendanceDataListForSequence()
            }

            override fun onStudentsUnAvailable() {

                _studentsAttendanceDataListForSequenceAvailable.value = false
            }

        })
    }

    private fun updateStudentsAttendanceDataListForSequence(){
        val temp = ArrayList<StudentAttendanceData>()
        studentsDataManager.getStudents().forEachIndexed { index, student ->
            val classNumber = studentsDataManager.getStudentClassNumberAt(index, academicYearIndex!!)
            val name = student.studentName
            val id = student.studentId
            val attendance = studentsDataManager.getStudentsAttendanceForSequenceOnDate(index, academicYearIndex!!, subjectIndex!!, sequenceIndex!!, currentDate)
            val totalAbsencesForSequence = studentsDataManager.getTotalAbsencesForStudentInSequence(index, academicYearIndex!!, subjectIndex!!, sequenceIndex!!)
            println("Total absences for sequence: $totalAbsencesForSequence")

            if (attendance != null){
                temp.add(StudentAttendanceData(classNumber, id, name, currentDate, attendance.totalAbsences, attendance.isAbsent, totalAbsencesForSequence))
            }else{
                temp.add(StudentAttendanceData(classNumber, id, name, currentDate, 0, false, totalAbsencesForSequence))
            }

        }
        studentsAttendanceDataListForSequence.clear()
        studentsAttendanceDataListForSequence.addAll(temp)
        _studentsAttendanceDataListForSequenceAvailable.value = true

    }

    fun getStudentsAttendanceDataListForSequence(): List<StudentAttendanceData>{
        return studentsAttendanceDataListForSequence
    }


    fun updateStudentAttendancesForCurrentSequenceForToday(studentIndex: Int, state: Boolean){
        if (state){
            studentsAttendanceDataListForSequence[studentIndex].absentCount = numberOfPeriods
        }else{
            studentsAttendanceDataListForSequence[studentIndex].absentCount = 0
        }
        studentsAttendanceDataListForSequence[studentIndex].isAbsent = state

        val absentCount = studentsAttendanceDataListForSequence[studentIndex].absentCount
        val isPresent = studentsAttendanceDataListForSequence[studentIndex].isAbsent

        val attendance = Attendance(currentDate, absentCount, isPresent)

//        updateTotalNumberOfAbsencesForStudentInSequence(studentIndex)

        updateNumberOfStudentsAbsent()

        updateStudentAttendancesForCurrentSequenceInStudentsDataManager(studentIndex, attendance)
        updateTotalNumberOfAbsencesForStudentInSequence(studentIndex)
    }

    private fun updateStudentAttendancesForCurrentSequenceInStudentsDataManager(studentIndex: Int, attendance: Attendance){
        studentsDataManager.updateStudentAttendancesForCurrentSequence(studentIndex,
            academicYearIndex!!, subjectIndex!!, sequenceIndex!!, attendance)
    }

    private fun updateNumberOfStudentsAbsent(){
        _numberOfStudentsAbsent.value = studentsAttendanceDataListForSequence.count { it.isAbsent }
    }

    fun getNumberOfStudentsAbsent(): Int{
        return studentsAttendanceDataListForSequence.count { it.isAbsent }
    }

    private fun updateTotalNumberOfAbsencesForStudentInSequence(studentIndex: Int){
        val totalAbsences = studentsDataManager.getTotalAbsencesForStudentInSequence(studentIndex, academicYearIndex!!, subjectIndex!!, sequenceIndex!!)
        studentsAttendanceDataListForSequence[studentIndex].totalAbsencesForSequence = totalAbsences
//        _studentTotalNumberOfAbsencesForSequenceUpdatedAtIndex.value = studentIndex
    }

    fun getStudentAttendancesAtIndex(studentIndex: Int): List<Attendance>{
        return studentsDataManager.getStudentAttendancesAtIndex(studentIndex, academicYearIndex!!, subjectIndex!!, sequenceIndex!!)
    }

    fun getStudentNameAtIndex(studentIndex: Int): String{
        return studentsAttendanceDataListForSequence[studentIndex].studentName
    }



}