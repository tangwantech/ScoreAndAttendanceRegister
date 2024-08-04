package com.tangwantech.scoreandattendanceregister

import android.content.Context
import android.content.Intent

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.tangwantech.scoreandattendanceregister.databinding.ActivityStudentsInDatabaseManagerBinding
import com.tangwantech.scoreandattendanceregister.fragments.StudentsInAcademicYearAndFormDeleteFragment
import com.tangwantech.scoreandattendanceregister.fragments.StudentsInAcademicYearAndFormHomeFragment
import com.tangwantech.scoreandattendanceregister.models.StudentInfo
import com.tangwantech.scoreandattendanceregister.viewmodels.StudentsInAcademicYearAndFormActivityViewModel

class StudentsInAcademicYearAndFormActivity : AppCompatActivity(),
    StudentsInAcademicYearAndFormHomeFragment.OnAddClickListener,
    StudentsInAcademicYearAndFormHomeFragment.OnItemPressListener
{
    private lateinit var binding: ActivityStudentsInDatabaseManagerBinding
    private lateinit var viewModel: StudentsInAcademicYearAndFormActivityViewModel
//    private var formName: String = ""
//    private var fileName: String = ""
//    private lateinit var autoCompleteAcademicYear: AutoCompleteTextView
//    private lateinit var autoCompleteForm: AutoCompleteTextView
//    private lateinit var rv: RecyclerView
//    private lateinit var rvAdapter: StudentsInAcademicYearAndClassRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentsInDatabaseManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupViewModel()
        showHomeFragment()
//        setupObservers()
//        setupAutoCompleteViews()
//        setupListeners()
//        setupRecyclerView()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.students_in_database_settings, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home -> {
                finish()
            }
            R.id.studentsInAcademicYearAndFormActivity_settings -> {
                clearDatabaseDialog()

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showHomeFragment(){
        val homeFragment = StudentsInAcademicYearAndFormHomeFragment.newInstance()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.apply {
            replace(binding.fragmentHolder.id, homeFragment)
            addToBackStack(StudentsInAcademicYearAndFormHomeFragment.FRAGMENT_NAME)
        }.commit()
    }

    private fun showDeleteFragment(){
        val deleteFragment = StudentsInAcademicYearAndFormDeleteFragment.newInstance()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.apply {
            replace(binding.fragmentHolder.id, deleteFragment)
            addToBackStack(StudentsInAcademicYearAndFormHomeFragment.FRAGMENT_NAME)
        }.commit()
    }


    private fun setupViewModel(){
        viewModel = ViewModelProvider(this)[StudentsInAcademicYearAndFormActivityViewModel::class.java]
        viewModel.initRoomDatabaseManager(this)

    }

    private fun clearDatabaseDialog(){
        val dial = AlertDialog.Builder(this).apply{
            setTitle("Clear database")
            setMessage("Are you sure you want to delete all students in database?")
            setPositiveButton("Clear"){d, _ ->
                viewModel.clearRoomDatabase()
//                rvAdapter.notifyDataSetChanged()
                d.dismiss()
            }
            setNegativeButton("Cancel"){d, _ ->
                d.dismiss()
            }
        }.create()
        dial.show()
    }

    companion object{
        fun getIntent(context: Context): Intent {
            return Intent(context, StudentsInAcademicYearAndFormActivity::class.java)
        }
    }

    override fun onAddStudents(studentsInfo: List<StudentInfo>) {
        viewModel.addNewStudents(studentsInfo)
    }

    override fun onLongPress(itemPosition: Int) {
        showDeleteFragment()
    }

    override fun onItemTapped(itemPosition: Int) {
        println("Item position: $itemPosition")
    }

}