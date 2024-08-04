package com.tangwantech.scoreandattendanceregister.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tangwantech.scoreandattendanceregister.R
import com.tangwantech.scoreandattendanceregister.adapters.StudentsInAcademicYearAndFormDeleteAdapter
import com.tangwantech.scoreandattendanceregister.databinding.FragmentStudentsInAcademicYearAndFormDeleteBinding
import com.tangwantech.scoreandattendanceregister.viewmodels.StudentsInAcademicYearAndFormActivityViewModel


class StudentsInAcademicYearAndFormDeleteFragment : Fragment(), StudentsInAcademicYearAndFormDeleteAdapter.OnItemCheckChangeListener {
    private lateinit var viewModel: StudentsInAcademicYearAndFormActivityViewModel
    private var binding: FragmentStudentsInAcademicYearAndFormDeleteBinding? = null
    private lateinit var adapter: StudentsInAcademicYearAndFormDeleteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentStudentsInAcademicYearAndFormDeleteBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupListeners()
        setupRecyclerView()
        setupObservers()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun setupViewModel(){
        viewModel = ViewModelProvider(requireActivity())[StudentsInAcademicYearAndFormActivityViewModel::class.java]
    }

    private fun setupRecyclerView(){
        val loMan = LinearLayoutManager(requireContext()).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
        binding!!.ryclerView.layoutManager = loMan

        adapter = StudentsInAcademicYearAndFormDeleteAdapter(requireContext(), this)
        println(viewModel.getStudentsToDelete())
        adapter.updateData(viewModel.getStudentsToDelete())
        binding!!.ryclerView.adapter = adapter
        adapter.notifyDataSetChanged()

    }

    private fun setupListeners(){
        binding!!.checkboxAll.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                viewModel.checkAll()
//                adapter.notifyDataSetChanged()

            }else{
                viewModel.uncheckAll()
//                adapter.notifyDataSetChanged()
            }

        }
    }

    private fun setupObservers(){
        viewModel.onCheckAll.observe(requireActivity()){
            if (!binding!!.checkboxAll.isChecked){
                binding!!.checkboxAll.isChecked = true
            }
//            adapter.notifyDataSetChanged()

        }

        viewModel.onUnCheckAll.observe(requireActivity()){
            if (binding!!.checkboxAll.isChecked){
                binding!!.checkboxAll.isChecked = false
            }
//            adapter.notifyDataSetChanged()
        }


    }

    companion object {

        const val FRAGMENT_NAME = "Delete fragment"
        fun newInstance() =
            StudentsInAcademicYearAndFormDeleteFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onItemChecked(itemPosition: Int, state: Boolean) {
        viewModel.checkItemAt(itemPosition, state)
    }

    override fun onItemUnChecked(itemPosition: Int, state: Boolean) {
        viewModel.unCheckItemAt(itemPosition, state)
    }
}