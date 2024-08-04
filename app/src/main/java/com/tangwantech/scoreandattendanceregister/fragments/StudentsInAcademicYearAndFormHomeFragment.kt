package com.tangwantech.scoreandattendanceregister.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tangwantech.scoreandattendanceregister.R
import com.tangwantech.scoreandattendanceregister.adapters.StudentsInAcademicYearAndClassRecyclerAdapter
import com.tangwantech.scoreandattendanceregister.constants.Constants
import com.tangwantech.scoreandattendanceregister.databinding.FragmentStudentsInAcademicYearAndFormHomeBinding
import com.tangwantech.scoreandattendanceregister.databinding.NewStudentBinding
import com.tangwantech.scoreandattendanceregister.models.StudentInfo
import com.tangwantech.scoreandattendanceregister.viewmodels.StudentsInAcademicYearAndFormActivityViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


class StudentsInAcademicYearAndFormHomeFragment : Fragment(), StudentsInAcademicYearAndClassRecyclerAdapter.OnItemPressListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var viewModel: StudentsInAcademicYearAndFormActivityViewModel
    private   var binding: FragmentStudentsInAcademicYearAndFormHomeBinding? = null
    private var fileName: String = ""
    private lateinit var autoCompleteAcademicYear: AutoCompleteTextView
    private lateinit var autoCompleteForm: AutoCompleteTextView
    private lateinit var rv: RecyclerView
    private lateinit var rvAdapter: StudentsInAcademicYearAndClassRecyclerAdapter
    private lateinit var onAddClickListener: OnAddClickListener
    private lateinit var onItemPressListener: OnItemPressListener

//    private lateinit var onAddClickListener: OnAddClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
        arguments?.let {
//            get values from argument using appropriate keys
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        // Inflate the layout for this fragment
        binding = FragmentStudentsInAcademicYearAndFormHomeBinding.inflate(inflater, container, false)
        return binding!!.root
//        inflater.inflate(
//            R.layout.fragment_students_in_academic_year_and_form_home,
//            container,
//            false
//        )
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnAddClickListener){
            onAddClickListener = context
        }

        if (context is OnItemPressListener){
            onItemPressListener = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupAutoCompleteViews()
        setupListeners()
        setupRecyclerView()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun setupViewModel(){
        viewModel = ViewModelProvider(requireActivity())[StudentsInAcademicYearAndFormActivityViewModel::class.java]
    }

    private fun setupRecyclerView() {
        rv = binding!!.recyclerView

        val loMan = LinearLayoutManager(requireContext()).apply{
            orientation = LinearLayoutManager.VERTICAL
        }
        rv.layoutManager = loMan

        rvAdapter = StudentsInAcademicYearAndClassRecyclerAdapter(requireContext(), this)
        rvAdapter.updateData(viewModel.getStudents())
        rv.adapter = rvAdapter
    }

    private fun setupListeners(){
        binding!!.loYearFormSelection.btnLoadFromDatabase.setOnClickListener {
            val academicYear = autoCompleteAcademicYear.text.toString()
            val formName = autoCompleteForm.text.toString()

            if (academicYear.isNotEmpty() && formName.isNotEmpty()){
//                setFileName(form, academicYear)
                val yearIndex = Constants.ACADEMIC_YEARS.indexOf(academicYear)
                val academicYearPair = Pair<Int, String>(yearIndex, academicYear)
//                loadStudentsFromDatabase(academicYearPair, form)
                viewModel.initStudentsInAcademicYearAndFormDataManagerFromRoom(academicYearPair,
                    formName
                )
            }else{
                selectAcademicYearAndFormDialog()
            }
        }

        binding!!.loYearFormSelection.btnLoadFromFile.setOnClickListener {
            val academicYear = autoCompleteAcademicYear.text.toString()
            val form = autoCompleteForm.text.toString()
            if (academicYear.isNotEmpty() && form.isNotEmpty()){
                setFileName(form, academicYear)
                viewModel.initStudentsInAcademicYearAndFormDataManagerFromAssert(requireContext(), fileName)
            }else{
                selectAcademicYearAndFormDialog()
            }


        }

        binding!!.fab.setOnClickListener {
            val academicYear = autoCompleteAcademicYear.text.toString()
            val form = autoCompleteForm.text.toString()
            if (academicYear.isNotEmpty() && form.isNotEmpty()){
                newStudentDialog()
            }else{
                selectAcademicYearAndFormDialog()
            }

        }
    }

    private fun setupAutoCompleteViews() {
        autoCompleteAcademicYear = binding!!.loYearFormSelection.autoCompleteAcademicYear
        val academicYearsAdapter = ArrayAdapter<String>(requireContext(), R.layout.dropdownitem, Constants.ACADEMIC_YEARS)
        autoCompleteAcademicYear.setAdapter(academicYearsAdapter)

        autoCompleteForm =  binding!!.loYearFormSelection.autoCompleteForm
        val formsAdapter = ArrayAdapter<String>(requireContext(), R.layout.dropdownitem, Constants.FORMS)
        autoCompleteForm.setAdapter(formsAdapter)
    }

    private fun setupObservers() {
        viewModel.studentsInitFromAssert.observe(requireActivity()){
            if(it){
                showRecyclerView()
            }else{
                hideRecyclerView()
            }
        }

        viewModel.studentsInitFromRoom.observe(requireActivity()){
            if (it){

                showRecyclerView()
            }else{
                hideRecyclerView()
            }
        }
    }


    private fun showRecyclerView(){

        rvAdapter.notifyDataSetChanged()
        rv.visibility = View.VISIBLE
        binding!!.tvNoData.visibility = View.INVISIBLE
    }
    private fun hideRecyclerView(){
        rv.visibility = View.GONE
        binding!!.tvNoData.visibility = View.VISIBLE
        rvAdapter.notifyDataSetChanged()
    }

    private fun newStudentDialog(){

        val newStudentBinding = NewStudentBinding.inflate(LayoutInflater.from(requireContext()))
        val arrayAdapter = ArrayAdapter<String>(requireContext(), R.layout.dropdownitem, Constants.GENDERS)
        newStudentBinding.autoStudentGender.setAdapter(arrayAdapter)
        val dialog = AlertDialog.Builder(requireContext()).apply {
            setTitle("New student")
            setView(newStudentBinding.root)
            setPositiveButton("Add"){btn, _ ->
                val academicYear = autoCompleteAcademicYear.text.toString()
                val form = autoCompleteForm.text.toString()
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
            setNegativeButton("Cancel"){btn, _ ->
                btn.dismiss()
            }
        }.create()
        dialog.show()
    }

    private fun selectAcademicYearAndFormDialog() {
        val dialog = AlertDialog.Builder(requireContext()).apply{
            setMessage("Please select Academic year and Form")
            setPositiveButton("Ok"){btn, _ ->
                btn.dismiss()
            }
        }.create()
        dialog.show()
    }

    private fun setFileName(formName: String, academicYear: String){
        val temp = formName.split(" ")
        fileName = temp[0] + temp[1].trim() + academicYear + ".json"
        println(fileName)
    }

    companion object {
        const val FRAGMENT_NAME = "Home fragment"
        fun newInstance() =
            StudentsInAcademicYearAndFormHomeFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    interface OnAddClickListener{
        fun onAddStudents(studentsInfo: List<StudentInfo>)
    }
//    interface OnLoadFromFileClickListener{
//        fun onLoadFromFileClicked()
//    }
//
//    interface OnLoadFromDatabaseClickListener{
//        fun onLoadFromDatabaseClicked()
//    }

    override fun onLongPress(itemPosition: Int) {
        onItemPressListener.onLongPress(itemPosition)
    }

    override fun onItemTapped(itemPosition: Int) {
        onItemPressListener.onItemTapped(itemPosition)
    }

    interface OnItemPressListener{
        fun onLongPress(itemPosition: Int)
        fun onItemTapped(itemPosition: Int)
    }
}