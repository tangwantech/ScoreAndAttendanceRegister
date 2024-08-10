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
            showAcademicYearClassSequenceSubjectDialog()
        }

        binding.btnAttendanceRegister.setOnClickListener {

        }


    }

    private fun showAcademicYearClassSequenceSubjectDialog(){
        AutocompleteFormDialog().show(supportFragmentManager, "")
    }

    override fun onDialogPositiveButtonClicked(bundle: Bundle) {
        startActivity(ScoreRegisterActivity.getIntent(this, bundle))
    }

    private fun gotoScoreRegisterActivity(){

    }


}
