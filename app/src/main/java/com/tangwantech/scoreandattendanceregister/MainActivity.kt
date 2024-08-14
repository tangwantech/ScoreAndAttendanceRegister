package com.tangwantech.scoreandattendanceregister

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.tangwantech.scoreandattendanceregister.databinding.ActivityMainBinding
import com.tangwantech.scoreandattendanceregister.fragments.AutocompleteFormDialog
import com.tangwantech.scoreandattendanceregister.viewmodels.MainActivityViewModel

class MainActivity : AppCompatActivity(), AutocompleteFormDialog.OnDialogPositiveButtonClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private var activityDestination: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupListeners()
    }

    private fun setupListeners(){
        binding.btnStudentDbManager.setOnClickListener {
            startActivity(StudentsInDatabaseActivity.getIntent(this))
        }

        binding.btnScoreRegister.setOnClickListener {
            activityDestination = ScoreRegisterActivity.ACTIVITY_NAME
            showAcademicYearClassSequenceSubjectDialog()
        }

        binding.btnAttendanceRegister.setOnClickListener {
            activityDestination = AttendanceRegisterActivity.ACTIVITY_NAME
            showAcademicYearClassSequenceSubjectDialog()
        }


    }

    private fun showAcademicYearClassSequenceSubjectDialog(){
        AutocompleteFormDialog().show(supportFragmentManager, "")
    }

    override fun onDialogPositiveButtonClicked(bundle: Bundle) {
        when (activityDestination){
            ScoreRegisterActivity.ACTIVITY_NAME -> {
               startActivity(ScoreRegisterActivity.getIntent(this, bundle))
            }
           AttendanceRegisterActivity.ACTIVITY_NAME ->{
               startActivity(AttendanceRegisterActivity.getIntent(this, bundle))
           }


        }

    }

    private fun gotoScoreRegisterActivity(){

    }


}
