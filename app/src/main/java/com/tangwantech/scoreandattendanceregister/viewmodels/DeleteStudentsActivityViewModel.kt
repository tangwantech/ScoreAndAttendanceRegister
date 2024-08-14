package com.tangwantech.scoreandattendanceregister.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tangwantech.scoreandattendanceregister.StudentsDataManager
import com.tangwantech.scoreandattendanceregister.models.DeleteStudentData

class DeleteStudentsActivityViewModel: ViewModel() {
    private val _studentsInitFromRoom = MutableLiveData<Boolean>()
    val studentsInitFromRoom: LiveData<Boolean> = _studentsInitFromRoom

    private lateinit var studentsDataManager: StudentsDataManager

    private val deleteStudentsList = ArrayList<DeleteStudentData>()

    private val _numberOfItemsSelected = MutableLiveData<Int>()
    val numberOfItemsSelected: LiveData<Int> = _numberOfItemsSelected

    private val _allItemsAreSelected = MutableLiveData<Boolean>()
    val allItemsAreSelected: LiveData<Boolean> = _allItemsAreSelected

    private val _sizeOfStudentsToDeleteList = MutableLiveData<Int>()
    val sizeOfStudentsToDeleteList: LiveData<Int> = _sizeOfStudentsToDeleteList

    private val _deleteSuccessful = MutableLiveData<Boolean>()
    val deleteSuccessful: LiveData<Boolean> = _deleteSuccessful

    private var preSelectedItemIndex: Int = -1



    private fun updatePreSelectedItemIndex(index: Int){
        preSelectedItemIndex = index
    }

    fun getPreSelectedItemIndex(): Int{
        return preSelectedItemIndex
    }

    private fun updateNumberOfItemsSelected(){

        _numberOfItemsSelected.value = deleteStudentsList.count { it.isChecked }

    }

    fun getStudentsToDelete(): ArrayList<DeleteStudentData>{
        return deleteStudentsList
    }

    fun updateDeleteItemCheckState(itemPosition: Int, state: Boolean){

        deleteStudentsList[itemPosition].isChecked = state

        updateNumberOfItemsSelected()
        updateAllItemsChecked()
    }

    fun changeItemsCheckState(checked: Boolean) {
//        sets each items check state to the new value referenced by checked
        for (index in deleteStudentsList.indices){
            deleteStudentsList[index].isChecked = checked
        }
        updateNumberOfItemsSelected()
        updateAllItemsChecked()
    }

    private fun updateAllItemsChecked(){
        _allItemsAreSelected.value = _numberOfItemsSelected.value == deleteStudentsList.size
    }

    fun updateSizeOfStudentsToDelete(){
//        method called after an item is removed from deleteStudentList
        _sizeOfStudentsToDeleteList.value = deleteStudentsList.size
    }


    fun initStudentsDataManager(context: Context){
        studentsDataManager = StudentsDataManager(context)

    }

    fun initStudentsFromRoom(academicYearPair: Pair<Int, String>, formName: String){
        studentsDataManager.initStudentsFromRoom(academicYearPair, formName, object: StudentsDataManager.OnReadFromRoomListener{
            override fun onStudentsAvailable() {
                setStudentsToDelete()
                _studentsInitFromRoom.value = true

            }

            override fun onStudentsUnAvailable() {
                _studentsInitFromRoom.value = false
            }

        })
    }


    fun setStudentsToDelete(){

        if (deleteStudentsList.isEmpty()){
            studentsDataManager.getStudents().forEach {
                deleteStudentsList.add(DeleteStudentData(it.studentId, it.studentName, false))
            }
        }
        updateSizeOfStudentsToDelete()
    }

    fun removeStudents() {
        studentsDataManager.removeStudents(deleteStudentsList, object : StudentsDataManager.OnStudentsRemoveListener{
            override fun onStudentsRemoved(count: Int) {
//                println("Number of items deleted: $count")
            }

            override fun onDeleteSuccessful() {
                setStudentsToDelete()
                val temp = ArrayList<DeleteStudentData>()
                temp.addAll(deleteStudentsList)
                temp.forEach {
                    if(it.isChecked){
                        deleteStudentsList.remove(it)
                    }
                }
                updateSizeOfStudentsToDelete()
                _deleteSuccessful.value = true

            }


        })
    }
}