package com.tangwantech.scoreandattendanceregister.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tangwantech.scoreandattendanceregister.StudentsInAcademicYearAndFormDataManager
import com.tangwantech.scoreandattendanceregister.filereaders.AssertReader
import com.tangwantech.scoreandattendanceregister.listbuilders.StudentsListBuilder
import com.tangwantech.scoreandattendanceregister.models.DeleteStudentData
import com.tangwantech.scoreandattendanceregister.models.Student
import com.tangwantech.scoreandattendanceregister.models.StudentInfo
import com.tangwantech.scoreandattendanceregister.repositories.RoomDatabaseManager

class StudentsInAcademicYearAndFormActivityViewModel: ViewModel() {
    private lateinit var roomDataManager: RoomDatabaseManager
    private val _studentsInitFromAssert = MutableLiveData<Boolean>()
    val studentsInitFromAssert: LiveData<Boolean> = _studentsInitFromAssert

    private val _studentsInitFromRoom = MutableLiveData<Boolean>()
    val studentsInitFromRoom: LiveData<Boolean> = _studentsInitFromRoom

    val studentsInAcademicYearAndFormDataManager: StudentsInAcademicYearAndFormDataManager = StudentsInAcademicYearAndFormDataManager()
    private val _onCheckAll = MutableLiveData<Boolean>()
    val onCheckAll: LiveData<Boolean> = _onCheckAll

    private val _onUnCheckAll = MutableLiveData<Boolean>()
    val onUnCheckAll: LiveData<Boolean> = _onUnCheckAll

//    private val _onItemChecked = MutableLiveData<Boolean>()
//    val onItemChecked: LiveData<Boolean> = _onItemChecked
//
//    private val _onItemUnchecked = MutableLiveData<Boolean>()
//    val onItemUnchecked: LiveData<Boolean> = _onItemUnchecked

    private val studentsToDelete = ArrayList<DeleteStudentData>()


    fun initRoomDatabaseManager(context: Context){
        roomDataManager = RoomDatabaseManager(context)

    }

    fun initStudentsInAcademicYearAndFormDataManagerFromAssert(context: Context, fileName: String){
        AssertReader.readClassListFromAssert(context, fileName, object: AssertReader.OnResult{

            override fun result(result: List<Student>) {
                studentsInAcademicYearAndFormDataManager.setStudents(result)
                _studentsInitFromAssert.value = true
                println("Reading from assert was successful: $result")
                roomDataManager.insertStudents(result)
                setStudentsToDelete()
            }
            override fun error(error: String?) {
                println(error)
                _studentsInitFromAssert.value = false
            }
        })
    }

    fun initStudentsInAcademicYearAndFormDataManagerFromRoom(academicYearPair: Pair<Int, String>, formName: String){
        roomDataManager.readStudentsFromRoomDatabase(object: RoomDatabaseManager.OnReadFromRoom{
            override fun result(result: List<Student>) {
                if(result.isEmpty()){
                    _studentsInitFromRoom.value = false
                }else{

                    studentsInAcademicYearAndFormDataManager.filterStudentsWhere(academicYearPair, formName, result, object: StudentsInAcademicYearAndFormDataManager.OnFilter{

                        override fun available() {
                            println("Reading from room was successful: ${studentsInAcademicYearAndFormDataManager.getStudents()}")
                            setStudentsToDelete()
                            _studentsInitFromRoom.value = true
                        }

                        override fun empty() {
                            _studentsInitFromRoom.value = false
                        }

                    })

                }

            }

            override fun error() {

            }

        })
    }

    fun addNewStudents(studentsInfo: List<StudentInfo>){
        val temp = StudentsListBuilder.build(studentsInfo)
        studentsInAcademicYearAndFormDataManager.addStudent(temp[0])
        println("Students: $studentsInAcademicYearAndFormDataManager")
        setStudentsToDelete()
        insertStudentsToDatabase(temp)
    }

    fun getStudents():ArrayList<Student>{
        return studentsInAcademicYearAndFormDataManager.getStudents()
    }

    private fun insertStudentsToDatabase(students:List<Student>){
        roomDataManager.insertStudents(students)
    }


    fun clearRoomDatabase(){
        clearStudentsInAcademicYearAndFormDataManager()
        roomDataManager.deleteAll()
    }

    private fun clearStudentsInAcademicYearAndFormDataManager(){
        studentsInAcademicYearAndFormDataManager.clear()
    }

    fun setStudentsToDelete(){
        studentsToDelete.clear()
        studentsInAcademicYearAndFormDataManager.getStudents().forEach {
            studentsToDelete.add(DeleteStudentData(it.studentId, it.studentName, false))
        }
    }

    fun getStudentsToDelete(): ArrayList<DeleteStudentData>{
        return studentsToDelete
    }

    fun checkItemAt(itemPosition: Int, state: Boolean){
        studentsToDelete[itemPosition].isChecked = state
        val allChecked = verifyIfAllItemsChecked()
        if(allChecked){
            _onCheckAll.value = true
        }
    }

    fun unCheckItemAt(itemPosition: Int, state: Boolean){
        studentsToDelete[itemPosition].isChecked = state
        val allChecked = verifyIfAllItemsChecked()
        if(!allChecked){
            _onUnCheckAll.value = true
        }
    }

    fun checkAll(){
        for (index in 0..<studentsToDelete.size){
            studentsToDelete[index].isChecked = true
        }
        _onCheckAll.value = true
    }

    private fun verifyIfAllItemsChecked(): Boolean{
        return studentsToDelete.all { it.isChecked }
    }

    fun uncheckAll(){

        for (index in 0..<studentsToDelete.size){
            studentsToDelete[index].isChecked = false
        }
        _onUnCheckAll.value = true
    }




}