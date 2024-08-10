package com.tangwantech.scoreandattendanceregister.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tangwantech.scoreandattendanceregister.StudentsDataManager
import com.tangwantech.scoreandattendanceregister.models.DeleteStudentData
import com.tangwantech.scoreandattendanceregister.models.Student
import com.tangwantech.scoreandattendanceregister.models.StudentInfo

class StudentsInDatabaseActivityViewModel: ViewModel() {
    private val _studentsInitFromAssert = MutableLiveData<Boolean>()
    val studentsInitFromAssert: LiveData<Boolean> = _studentsInitFromAssert

    private val _studentsInitFromRoom = MutableLiveData<Boolean>()
    val studentsInitFromRoom: LiveData<Boolean> = _studentsInitFromRoom

    private lateinit var studentsDataManager: StudentsDataManager

    private val studentsToDelete = ArrayList<DeleteStudentData>()

    private val _onStudentAdded = MutableLiveData<Boolean>()
    val onStudentAdded: LiveData<Boolean> = _onStudentAdded

    private var selectedAcademicYearIndex: Int? = null
    private var selectedAcademicYear : String? = null
    private var selectedForm: String? = null

    fun setSelectedAcademicYearIndex(index: Int){
        selectedAcademicYearIndex = index
    }

    fun getSelectedAcademicYearIndex(): Int?{
        return selectedAcademicYearIndex
    }

    fun setSelectedAcademicYear(academicYear: String){
        selectedAcademicYear = academicYear
    }

    fun setSelectedForm(form: String){
        selectedForm = form
    }

    fun getSelectedAcademicYear(): String?{
        return selectedAcademicYear
    }

    fun getSelectedForm(): String?{
        return selectedForm
    }


    fun initStudentsDataManager(context: Context){
        studentsDataManager = StudentsDataManager(context)

    }

    fun initStudentsFromAssert(context: Context, fileName: String){

        studentsDataManager.initStudentsFormAssert(context, fileName, object : StudentsDataManager.OnReadFromAssertFileListener{
            override fun onStudentsAvailable() {
                _studentsInitFromAssert.value = true
            }

            override fun onStudentsUnavailable() {
                _studentsInitFromAssert.value = true

            }

        })
    }

    fun initStudentsFromRoom(academicYearPair: Pair<Int, String>, formName: String){
        studentsDataManager.initStudentsFromRoom(academicYearPair, formName, object: StudentsDataManager.OnReadFromRoomListener{
            override fun onStudentsAvailable() {
                _studentsInitFromRoom.value = true
            }

            override fun onStudentsUnAvailable() {
                _studentsInitFromRoom.value = false
            }

        })
    }

    fun addNewStudents(studentsInfo: List<StudentInfo>){
        studentsDataManager.addStudent(studentsInfo, object : StudentsDataManager.OnAddStudentListener{
            override fun onStudentAdded() {
                _onStudentAdded.value = true
            }

            override fun onStudentAlreadyExist() {
                _onStudentAdded.value = false
                println("Student with id already exists in database")
            }

        })


    }

    fun getStudents():ArrayList<Student>{
        return studentsDataManager.getStudents()
    }



//    private fun clearStudentsInAcademicYearAndFormDataManager(){
//        studentsDataManager.clear()
//    }
//
//    fun setStudentsToDelete(){
//        studentsToDelete.clear()
//        studentsDataManager.getStudents().forEach {
//            studentsToDelete.add(DeleteStudentData(it.studentId, it.studentName, false))
//        }
//    }
//
//    fun getStudentsToDelete(): ArrayList<DeleteStudentData>{
//        return studentsToDelete
//    }
//
//    fun removeStudents() {
//        studentsDataManager.removeStudents(studentsToDelete, object : StudentsDataManager.OnStudentsRemoveListener{
//            override fun onStudentsRemoved(count: Int) {
//                TODO("Not yet implemented")
//            }
//
//
//        })
//    }


}