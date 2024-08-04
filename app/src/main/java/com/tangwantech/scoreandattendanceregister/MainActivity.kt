package com.tangwantech.scoreandattendanceregister

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.tangwantech.scoreandattendanceregister.constants.Constants
import com.tangwantech.scoreandattendanceregister.databinding.ActivityMainBinding
import com.tangwantech.scoreandattendanceregister.viewmodels.MainActivityViewModel

class MainActivity : AppCompatActivity() {

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
            println("Navigating to students in database activity")
            startActivity(StudentsInAcademicYearAndFormActivity.getIntent(this))
        }
    }
}