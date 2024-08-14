package com.tangwantech.scoreandattendanceregister

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tangwantech.scoreandattendanceregister.adapters.AttendanceRegisterHomeFragmentRecyclerAdapter
import com.tangwantech.scoreandattendanceregister.constants.Constants
import com.tangwantech.scoreandattendanceregister.databinding.ActivityAttendanceRegisterBinding
import com.tangwantech.scoreandattendanceregister.fragments.AttendanceRegisterHomeFragment
import com.tangwantech.scoreandattendanceregister.fragments.RollCallFragment
import com.tangwantech.scoreandattendanceregister.fragments.StudentAttendancesForSequenceFragment
import com.tangwantech.scoreandattendanceregister.viewmodels.AttendanceRegisterActivityViewModel

class AttendanceRegisterActivity : AppCompatActivity(),
    AttendanceRegisterHomeFragmentRecyclerAdapter.OnItemClickListener, AttendanceRegisterHomeFragment.OnAddButtonClickListener{
    private lateinit var binding: ActivityAttendanceRegisterBinding
    private lateinit var viewModel: AttendanceRegisterActivityViewModel
//    private var currentFragmentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttendanceRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupViewModel()
        setupHeader()
        setupObservers()
        switchFragment()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            android.R.id.home -> {
                back()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun back(){
        viewModel.decrementCurrentFragmentIndex()
        supportFragmentManager.popBackStack()
        if (viewModel.getCurrentFragmentIndex() >= 0){
            switchFragment()
        }else {
            finish()
        }
    }

    private fun gotoHomeFragment(){
        viewModel.setFragmentName(0, AttendanceRegisterHomeFragment.FRAGMENT_NAME)
        val homeFragment = AttendanceRegisterHomeFragment.newInstance()
        gotoFragment(homeFragment)
    }

    private fun gotoRollCallFragment(){

        viewModel.setFragmentName(1, RollCallFragment.FRAGMENT_NAME)
        val rollCallFragment = RollCallFragment.newInstance()
        gotoFragment(rollCallFragment)

    }

    private fun gotoStudentAttendancesListFragment(studentIndex: Int){
        viewModel.setFragmentName(1, StudentAttendancesForSequenceFragment.FRAGMENT_NAME)
        val studentAttendancesForSequenceFragment = StudentAttendancesForSequenceFragment.newInstance(studentIndex)
        gotoFragment(studentAttendancesForSequenceFragment)
    }

    private fun gotoFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.attendanceRegisterActivityFragmentContainer, fragment)
            addToBackStack(null)
        }.commit()
    }

    private fun setupViewModel(){
        viewModel = ViewModelProvider(this)[AttendanceRegisterActivityViewModel::class.java]
        viewModel.apply {
            setUpdateAcademicYearIndex(intent.extras?.getString(Constants.ACADEMIC_YEAR)!!)
            updateFormIndex(intent.extras?.getString(Constants.FORM)!!)
            updateSequenceIndex(intent.extras?.getString(Constants.SEQUENCE)!!)
            updateSubjectIndex(intent.extras?.getString(Constants.SUBJECT)!!)

        }.initStudentsDataManager(this)
    }

    private fun setupObservers(){
        viewModel.studentsAttendanceDataListForSequenceAvailable.observe(this){
            if(it){
                switchFragment()
            }else{
                println("Data not available")
            }
        }

    }

    private fun switchFragment(){
        if(viewModel.getFragmentName() == null){
            gotoHomeFragment()
        }else{
            when (viewModel.getFragmentName()){
                AttendanceRegisterHomeFragment.FRAGMENT_NAME -> {
                    gotoHomeFragment()
                }
                RollCallFragment.FRAGMENT_NAME -> {
                    gotoRollCallFragment()
                }
            }
        }

    }

    private fun setupHeader(){
        binding.header.tvAcademicYear.text = viewModel.getAcademicYear()
        binding.header.tvSequence.text = viewModel.getSequence()
        binding.header.tvSubject.text = viewModel.getSubject()
    }

    companion object{
        const val ACTIVITY_NAME = "AttendanceRegisterActivity"
        fun getIntent(context: Context, params: Bundle): Intent{
            val intent = Intent(context, AttendanceRegisterActivity::class.java)
            intent.putExtras(params)
            return intent
        }
    }

    override fun onHomeFragmentRecyclerItemClicked(itemPosition: Int) {
        gotoStudentAttendancesListFragment(itemPosition)
    }

    override fun onAddButtonClicked(numberOfPeriods: Int) {
        viewModel.updateNumberOfPeriods(numberOfPeriods)
        gotoRollCallFragment()

    }

}

