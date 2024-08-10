package com.tangwantech.scoreandattendanceregister.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tangwantech.scoreandattendanceregister.StudentsDataManager
import com.tangwantech.scoreandattendanceregister.constants.Constants
import com.tangwantech.scoreandattendanceregister.models.ScoreStatisticsData
import com.tangwantech.scoreandattendanceregister.models.StudentScoreData

class ScoreRegisterActivityViewModel: ViewModel() {
    private var academicYear: String? = null
    private var academicYearIndex: Int? = null

    private var form: String? = null
    private var formIndex: Int? = null

    private var sequence: String? = null
    private var sequenceIndex: Int? = null

    private var subject: String? = null
    private var subjectIndex: Int? = null

    private lateinit var studentsDataManager: StudentsDataManager

    private var studentsScoreList = ArrayList<StudentScoreData>()

    private val _studentsScoreListAvailable = MutableLiveData<Boolean>()
    val studentsScoreListAvailable: LiveData<Boolean> = _studentsScoreListAvailable

    private val _currentStudentIndex = MutableLiveData<Int>()
    val currentStudentIndex: LiveData<Int> = _currentStudentIndex

    private var firstIndex: Int? = null

    private var lastIndex: Int? = null

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



    fun initStudentsDataManager(context: Context){
        studentsDataManager = StudentsDataManager(context)
        initStudentsFromRoom()

    }

    private fun initStudentsFromRoom(){
        val academicYearPair = Pair<Int, String>(academicYearIndex!!, academicYear!!)
        studentsDataManager.initStudentsFromRoom(academicYearPair, form!!, object: StudentsDataManager.OnReadFromRoomListener{
            override fun onStudentsAvailable() {
                updateStudentsScoreList()
            }

            override fun onStudentsUnAvailable() {
                _studentsScoreListAvailable.value = false
            }

        })
    }

    private fun updateStudentsScoreList(){
        val temp = ArrayList<StudentScoreData>()
        studentsDataManager.getStudents().forEachIndexed { index, student ->
            val name = student.studentName
            val id = student.studentId
            val gender = student.studentGender
            val classNumber = studentsDataManager.getStudentClassNumberAt(index, academicYearIndex!!)
            val score = studentsDataManager.getStudentScoreAt(index, academicYearIndex!!, subjectIndex!!, sequenceIndex!!)
            temp.add(StudentScoreData(id, name, gender, classNumber, score))
        }
        studentsScoreList.clear()
        studentsScoreList.addAll(temp)
        _studentsScoreListAvailable.value = true
    }

    fun getStudentsScoreList(): ArrayList<StudentScoreData>{
        return studentsScoreList
    }

    fun getStudentScoreDataAt(index: Int): StudentScoreData{
        return studentsScoreList[index]
    }

    fun setFirstAndLastIndices(){
        firstIndex = 0
        _currentStudentIndex.value = firstIndex!!
        lastIndex = studentsScoreList.size - 1
        println(firstIndex)
    }

    fun getFirstIndex(): Int?{
        return firstIndex
    }

    fun getLastIndex(): Int?{
        return lastIndex
    }

    fun updateCurrentStudentIndex(studentIndex: Int){
        _currentStudentIndex.value = studentIndex
    }

    fun incrementCurrentIndex(){
        _currentStudentIndex.value = _currentStudentIndex.value!! + 1
    }

    fun decrementCurrentStudentIndex(){
        _currentStudentIndex.value = _currentStudentIndex.value!! - 1
    }

    fun updateStudentScore(score: Double) {
//        println("Current student index: ${_currentStudentIndex.value!!}")
        studentsScoreList[_currentStudentIndex.value!!].score = score
        studentsDataManager.updateStudentScoreAt(_currentStudentIndex.value!!, academicYearIndex!!, subjectIndex!!, sequenceIndex!!, score)
    }

    fun getMaleStatics(): ScoreStatisticsData{
        val boys =
            studentsScoreList.filter { student -> student.studentGender == Constants.GENDERS[0] }
        val numOfBoys = boys.size
        val numberOfBoysPassed = boys.count { student -> student.score >= Constants.PASS_MIN }
        val percentPassed = "${(numberOfBoysPassed.toDouble() / numOfBoys) * 100}".format(2)
        val maleStatistics = ScoreStatisticsData(numOfBoys, numberOfBoysPassed, percentPassed)

        println(maleStatistics)
        return maleStatistics
    }

    fun getFemaleStatistics(): ScoreStatisticsData{
        val girls =
            studentsScoreList.filter { student -> student.studentGender == Constants.GENDERS[1] }
        val numOfGirls = girls.size
        val numberOfGirlsPassed = girls.count { student -> student.score >= Constants.PASS_MIN }
        val percentPassed = "${(numberOfGirlsPassed.toDouble() / numOfGirls) * 100}".format(2)
        val femaleStatistics = ScoreStatisticsData(numOfGirls, numberOfGirlsPassed, percentPassed)
        println(femaleStatistics)
        return femaleStatistics
    }

    fun getGlobalStatistics(): ScoreStatisticsData{
        val totalSat = studentsScoreList.size
        val totalPassed = studentsScoreList.count { student -> student.score >= Constants.PASS_MIN }
        val percentPassed = "${ (totalPassed.toDouble() / totalSat.toDouble()) * 100 }".format(2)
        val overallStats = ScoreStatisticsData(totalSat, totalPassed, percentPassed)
        println(overallStats)
        return overallStats

    }
}