package com.tangwantech.scoreandattendanceregister

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tangwantech.scoreandattendanceregister.adapters.ScoreEntryRecyclerAdapter
import com.tangwantech.scoreandattendanceregister.constants.Constants
import com.tangwantech.scoreandattendanceregister.databinding.ActivityScoreRegisterBinding
import com.tangwantech.scoreandattendanceregister.viewmodels.ScoreRegisterActivityViewModel

class ScoreRegisterActivity : AppCompatActivity(), ScoreEntryRecyclerAdapter.OnItemClickListener {
    private lateinit var binding: ActivityScoreRegisterBinding
    private lateinit var viewModel: ScoreRegisterActivityViewModel
    private lateinit var rvAdapter: ScoreEntryRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScoreRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupViewModel()
        setupHeader()
        setupObservers()
        setupRecyclerView()
        setupListeners()
        setActivityTitle()


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.score_statistics_settings, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> finish()
            R.id.maleStatistics -> showMaleStatistics()
            R.id.femaleStatistics -> showFemaleStatistics()
            R.id.overallStatistics -> showOverallStatistics()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupViewModel(){
        viewModel = ViewModelProvider(this)[ScoreRegisterActivityViewModel::class.java]
        viewModel.apply {
            setUpdateAcademicYearIndex(intent.extras?.getString(Constants.ACADEMIC_YEAR)!!)
            updateFormIndex(intent.extras?.getString(Constants.FORM)!!)
            updateSequenceIndex(intent.extras?.getString(Constants.SEQUENCE)!!)
            updateSubjectIndex(intent.extras?.getString(Constants.SUBJECT)!!)

        }.initStudentsDataManager(this)
    }

    private fun setActivityTitle(){
        title = viewModel.getForm()
    }

    private fun setupHeader(){
        binding.scoreRegisterHeader.tvAcademicYear.text = viewModel.getAcademicYear()
        binding.scoreRegisterHeader.tvSequence.text = viewModel.getSequence()
        binding.scoreRegisterHeader.tvSubject.text = viewModel.getSubject()
    }

    private fun setupListeners(){
        binding.scoreEntry.btnPrevious.setOnClickListener {
            viewModel.decrementCurrentStudentIndex()
        }

        binding.scoreEntry.btnNext.setOnClickListener {
            viewModel.incrementCurrentIndex()
        }
        binding.scoreEntry.etScore.doOnTextChanged { text, start, before, count ->
            if (text.toString().isNotEmpty() && text.toString().toDouble() >= 0 && text.toString().toDouble() <= 20){

                viewModel.updateStudentScore(text.toString().toDouble())
                rvAdapter.notifyItemChanged(viewModel.currentStudentIndex.value!!)
                binding.recyclerView.scrollToPosition(viewModel.currentStudentIndex.value!!)
            }else{
                binding.scoreEntry.etScore.error = "Invalid score"
                viewModel.updateStudentScore(0.0)

            }
            rvAdapter.notifyItemChanged(viewModel.currentStudentIndex.value!!)
            binding.recyclerView.scrollToPosition(viewModel.currentStudentIndex.value!!)
        }
    }

    private fun updatePreviousAndNextButtonStates(){
        if(viewModel.getFirstIndex() != null && viewModel.getLastIndex() != null){
            binding.scoreEntry.btnPrevious.isEnabled = viewModel.currentStudentIndex.value != viewModel.getFirstIndex()
            binding.scoreEntry.btnNext.isEnabled = viewModel.currentStudentIndex.value != viewModel.getLastIndex()
        }

    }

    private fun setupObservers(){
        viewModel.studentsScoreListAvailable.observe(this){
            if(it){
                viewModel.setFirstAndLastIndices()
                updateScoreEntryFields(viewModel.getFirstIndex()!!)
                updatePreviousAndNextButtonStates()

            }

        }

        viewModel.currentStudentIndex.observe(this){
            updateScoreEntryFields(it)
            updatePreviousAndNextButtonStates()
        }
    }

    private fun updateScoreEntryFields(currentStudentIndex: Int){
        val studentScoreData = viewModel.getStudentScoreDataAt(currentStudentIndex)
        binding.scoreEntry.tvStudentName.text = getString(R.string.student_name, studentScoreData.classNumber, studentScoreData.studentName)
        binding.scoreEntry.etScore.setText(studentScoreData.score.toString())
    }

    private fun setupRecyclerView(){
        val layoutMan = LinearLayoutManager(this).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
        binding.recyclerView.layoutManager = layoutMan
        binding.recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        rvAdapter = ScoreEntryRecyclerAdapter(this, this).apply {
            updateStudentsScoreList(viewModel.getStudentsScoreList())
        }

        binding.recyclerView.adapter = rvAdapter


    }

    private fun showMaleStatistics(){
        viewModel.getMaleStatics()
    }

    private fun showFemaleStatistics(){
        viewModel.getFemaleStatistics()
    }

    private fun showOverallStatistics(){
        viewModel.getGlobalStatistics()
    }

    companion object{
        fun getIntent(context: Context, params: Bundle): Intent{
            val intent = Intent(context, ScoreRegisterActivity::class.java)
            intent.putExtras(params)
            return intent
        }
    }

    override fun onItemClicked(itemPosition: Int) {
//        Toast.makeText(this, itemPosition.toString(), Toast.LENGTH_SHORT).show()
        viewModel.updateCurrentStudentIndex(itemPosition)

    }
}