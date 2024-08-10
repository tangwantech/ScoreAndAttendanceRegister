package com.tangwantech.scoreandattendanceregister

import android.content.Context
import android.content.Intent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tangwantech.scoreandattendanceregister.adapters.StudentsInAcademicYearAndClassRecyclerAdapter
import com.tangwantech.scoreandattendanceregister.constants.Constants
import com.tangwantech.scoreandattendanceregister.databinding.ActivityStudentsInDatabaseBinding
import com.tangwantech.scoreandattendanceregister.databinding.NewStudentBinding
import com.tangwantech.scoreandattendanceregister.models.StudentInfo
import com.tangwantech.scoreandattendanceregister.viewmodels.StudentsInDatabaseActivityViewModel

class StudentsInDatabaseActivity : AppCompatActivity(),
    StudentsInAcademicYearAndClassRecyclerAdapter.OnItemPressListener{
    private lateinit var binding: ActivityStudentsInDatabaseBinding
    private lateinit var viewModel: StudentsInDatabaseActivityViewModel
//    private  var fragmentMan: FragmentManager? = null

    private lateinit var rvAdapter: StudentsInAcademicYearAndClassRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentsInDatabaseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        title = getString(R.string.students_database)
        setupViewModel()
        setupRecyclerView()
        setupListeners()
        setupObservers()


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home -> {
                    finish()
                }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        setupAutoCompleteViews()
    }

    private fun setupViewModel(){
        viewModel = ViewModelProvider(this)[StudentsInDatabaseActivityViewModel::class.java]
        viewModel.initStudentsDataManager(this)

    }

    private fun setupRecyclerView() {

        binding.recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        val loMan = LinearLayoutManager(this).apply{
            orientation = LinearLayoutManager.VERTICAL
        }
        binding.recyclerView.layoutManager = loMan

        rvAdapter = StudentsInAcademicYearAndClassRecyclerAdapter(this, this)
        rvAdapter.updateData(viewModel.getStudents())
        binding.recyclerView.adapter = rvAdapter
    }

    private fun setupListeners(){

        binding.loYearFormSelection.btnLoadFromFile.setOnClickListener {
            val academicYear = binding.loYearFormSelection.autoCompleteAcademicYear.text.toString()
            val form = binding.loYearFormSelection.autoCompleteForm.text.toString()
            if (academicYear.isNotEmpty() && form.isNotEmpty()){

                viewModel.initStudentsFromAssert(this, getFileName(form, academicYear))
            }else{
                academicYearAndFormSelectionDialog()
            }


        }

        binding.fab.setOnClickListener {
            val academicYear = binding.loYearFormSelection.autoCompleteAcademicYear.text.toString()
            val form = binding.loYearFormSelection.autoCompleteForm.text.toString()
            if (academicYear.isNotEmpty() && form.isNotEmpty()){
                newStudentDialog()
            }else{
                academicYearAndFormSelectionDialog()
            }

        }

        binding.loYearFormSelection.autoCompleteAcademicYear.setOnItemClickListener { _, _, i, _ ->
            viewModel.setSelectedAcademicYearIndex(i)
        }

        binding.loYearFormSelection.autoCompleteAcademicYear.doOnTextChanged { text, _, _, _  ->

            viewModel.setSelectedAcademicYear(text.toString())
            loadStudentsFromRoom()

        }

        binding.loYearFormSelection.autoCompleteForm.doOnTextChanged { text, _, _, _ ->
            viewModel.setSelectedForm(text.toString())
            loadStudentsFromRoom()
        }
    }

    private fun setupAutoCompleteViews() {
        binding.loYearFormSelection.autoCompleteAcademicYear.setText(viewModel.getSelectedAcademicYear())
        val academicYearsAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Constants.ACADEMIC_YEARS)
        binding.loYearFormSelection.autoCompleteAcademicYear.setAdapter(academicYearsAdapter)




        binding.loYearFormSelection.autoCompleteForm.setText(viewModel.getSelectedForm())
        val formsAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Constants.FORMS)
        binding.loYearFormSelection.autoCompleteForm.setAdapter(formsAdapter)


    }

    private fun setupObservers() {
        viewModel.studentsInitFromAssert.observe(this){
            if(it){
                showRecyclerView()
            }else{
                hideRecyclerView()
            }
            changeLoadStudentsFromFileButtonState(false)
            changeAddButtonState(true)
        }

        viewModel.studentsInitFromRoom.observe(this){
            if (it){
                showRecyclerView()
                changeLoadStudentsFromFileButtonState(false)
                changeAddButtonState(true)

            }else{
                hideRecyclerView()
                changeLoadStudentsFromFileButtonState(true)
                changeAddButtonState(false)

            }

        }
    }


    private fun showRecyclerView(){

        binding.recyclerView.visibility = View.VISIBLE
        binding.tvNoData.visibility = View.GONE
        rvAdapter.notifyDataSetChanged()
    }
    private fun hideRecyclerView(){
        binding.recyclerView.visibility = View.GONE
        binding.tvNoData.visibility = View.VISIBLE
        rvAdapter.notifyDataSetChanged()
    }

    private fun newStudentDialog(){

        val newStudentBinding = NewStudentBinding.inflate(LayoutInflater.from(this))
        val arrayAdapter = ArrayAdapter<String>(this, R.layout.dropdownitem, Constants.GENDERS)
        newStudentBinding.autoStudentGender.setAdapter(arrayAdapter)
        val dialog = AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.new_student))
            setView(newStudentBinding.root)
            setPositiveButton(getString(R.string.add)){btn, _ ->
                val academicYear = binding.loYearFormSelection.autoCompleteAcademicYear.text.toString()
                val form = binding.loYearFormSelection.autoCompleteForm.text.toString()
                if (academicYear.isNotEmpty() && form.isNotEmpty()){
                    val yearIndex = Constants.ACADEMIC_YEARS.indexOf(academicYear)
                    val academicYearPair = Pair<Int, String>(yearIndex, academicYear)
                    val studentId = newStudentBinding.tiStudentId.text.toString()
                    val studentName = newStudentBinding.tiStudentName.text.toString()
                    val gender = newStudentBinding.autoStudentGender.text.toString()
                    val subjects = Constants.FORM_SUBJECTS[form]!!.toList()
                    val studentInfo = arrayOf<StudentInfo>(
                        StudentInfo(academicYear, studentId, studentName, gender, form, subjects)
                    )
//                    onAddClickListener.onAddStudents(studentInfo.toList())
                    viewModel.addNewStudents(studentInfo.toList())
                    rvAdapter.notifyDataSetChanged()

                }

                btn.dismiss()
            }
            setNegativeButton(getString(R.string.cancel)){btn, _ ->
                btn.dismiss()
            }
        }.create()
        dialog.show()
    }

    private fun academicYearAndFormSelectionDialog() {
        val dialog = AlertDialog.Builder(this).apply{
            setMessage(getString(R.string.select_academic_year))
            setPositiveButton("Ok"){btn, _ ->
                btn.dismiss()
            }
        }.create()
        dialog.show()
    }

    private fun getFileName(formName: String, academicYear: String): String{
        val temp = formName.split(" ")
        return temp[0] + temp[1].trim() + academicYear + ".json"
    }

    private fun loadStudentsFromRoom(){
        val academicYear = viewModel.getSelectedAcademicYear()
        val formName = viewModel.getSelectedForm()
        if (academicYear != null && formName != null){
            val yearIndex = Constants.ACADEMIC_YEARS.indexOf(academicYear)
            val academicYearPair = Pair<Int, String>(yearIndex, academicYear)
            viewModel.initStudentsFromRoom(academicYearPair,
                formName
            )
        }
    }

    private fun changeLoadStudentsFromFileButtonState(state: Boolean){
        binding.loYearFormSelection.btnLoadFromFile.isEnabled = state
    }

    private fun changeAddButtonState(state: Boolean){
        binding.fab.isEnabled = state
    }

    private fun gotoDeleteStudentActivity(itemIndex: Int){
        val bundle = Bundle().apply {
            putInt(Constants.ACADEMIC_YEAR_INDEX, viewModel.getSelectedAcademicYearIndex()!!)
            putString(Constants.ACADEMIC_YEAR, viewModel.getSelectedAcademicYear())
            putString(Constants.FORM, viewModel.getSelectedForm())
            putInt(Constants.SELECT_ITEM_INDEX, itemIndex)
        }

        val intent = DeleteStudentsActivity.getIntent(this, bundle)
        startActivity(intent)
    }

    companion object{

        fun getIntent(context: Context): Intent {

            return Intent(context, StudentsInDatabaseActivity::class.java)
        }
    }

    override fun onLongPress(itemPosition: Int) {
        gotoDeleteStudentActivity(itemPosition)
    }

    override fun onItemTapped(itemPosition: Int) {
        println("Item position: $itemPosition")
    }


}