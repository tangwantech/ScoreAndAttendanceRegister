package com.tangwantech.scoreandattendanceregister

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tangwantech.scoreandattendanceregister.adapters.StudentsInAcademicYearAndFormDeleteAdapter
import com.tangwantech.scoreandattendanceregister.constants.Constants
import com.tangwantech.scoreandattendanceregister.databinding.ActivityDeleteStudentsBinding
import com.tangwantech.scoreandattendanceregister.viewmodels.DeleteStudentsActivityViewModel

class DeleteStudentsActivity : AppCompatActivity(), StudentsInAcademicYearAndFormDeleteAdapter.OnItemCheckChangeListener{

    private lateinit var binding: ActivityDeleteStudentsBinding
    private lateinit var viewModel: DeleteStudentsActivityViewModel
    private lateinit var adapter: StudentsInAcademicYearAndFormDeleteAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteStudentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupViewModel()
        setupListeners()
        setupRecyclerView()
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


    private fun setupViewModel(){
        viewModel = ViewModelProvider(this)[DeleteStudentsActivityViewModel::class.java]
        viewModel.initStudentsDataManager(this)

        val yearIndex = intent.extras?.getInt(Constants.ACADEMIC_YEAR_INDEX)
        val academicYear = intent.extras?.getString(Constants.ACADEMIC_YEAR)
        val selectedForm = intent.extras?.getString(Constants.FORM)

        val yearPair = Pair<Int, String>(yearIndex!!, academicYear!!)
//        println(yearPair)
        viewModel.initStudentsFromRoom(yearPair, selectedForm!!)


    }

    private fun setupRecyclerView(){
        val loMan = LinearLayoutManager(this).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
        binding.ryclerView.layoutManager = loMan

        binding.ryclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        adapter = StudentsInAcademicYearAndFormDeleteAdapter(this, this)

        adapter.updateData(viewModel.getStudentsToDelete())
        binding.ryclerView.adapter = adapter
        adapter.notifyDataSetChanged()

    }

    private fun setupListeners(){
        binding.checkboxAll.setOnCheckedChangeListener { _, isChecked ->

            if(!isChecked && viewModel.numberOfItemsSelected.value == 0){
                viewModel.changeItemsCheckState(true)
            } else if(!isChecked && viewModel.numberOfItemsSelected.value == viewModel.getStudentsToDelete().size){
                viewModel.changeItemsCheckState(false)
            }else if(isChecked && viewModel.numberOfItemsSelected.value == 0){
                viewModel.changeItemsCheckState(true)
            }else if(isChecked){
                viewModel.changeItemsCheckState(true)
            }

            adapter.notifyDataSetChanged()
        }

        binding.btnDelete.setOnClickListener {
            viewModel.removeStudents()
        }


    }

    private fun setupObservers(){
        viewModel.allItemsAreSelected.observe(this){
            binding.checkboxAll.isChecked = it
        }


        viewModel.numberOfItemsSelected.observe(this){
            if(it > 0){
                binding.checkboxAll.text = getString(R.string.all, it)
            }else{
                binding.checkboxAll.text = " "
            }

        }

        viewModel.sizeOfStudentsToDeleteList.observe(this){
            if(it == 0){
                finish()
            }
        }

        viewModel.studentsInitFromRoom.observe(this){


            if(it && viewModel.getPreSelectedItemIndex() == -1){
                val temp = intent.extras?.getInt(Constants.SELECT_ITEM_INDEX)!!
                viewModel.updateDeleteItemCheckState(temp, true)
                adapter.notifyDataSetChanged()
            }else{
                println("Datanase is empty")
            }
        }

        viewModel.deleteSuccessful.observe(this){
            adapter.notifyDataSetChanged()
        }


    }


    override fun onItemCheckedStateChanged(itemPosition: Int, state: Boolean) {
        viewModel.updateDeleteItemCheckState(itemPosition, state)

    }

    companion object{

        fun getIntent(context: Context, bundle: Bundle): Intent {
            val intent = Intent(context, DeleteStudentsActivity::class.java)
            intent.putExtras(bundle)
            return intent
        }
    }
}