package com.tangwantech.scoreandattendanceregister.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tangwantech.scoreandattendanceregister.R
import com.tangwantech.scoreandattendanceregister.adapters.StudentAttendancesFragmentRecyclerAdapter
import com.tangwantech.scoreandattendanceregister.databinding.FragmentStudentAttendancesForSequenceBinding
import com.tangwantech.scoreandattendanceregister.viewmodels.AttendanceRegisterActivityViewModel


class StudentAttendancesForSequenceFragment : Fragment() {
    private lateinit var binding: FragmentStudentAttendancesForSequenceBinding
    private lateinit var activityViewModel: AttendanceRegisterActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =FragmentStudentAttendancesForSequenceBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle()
        setupViewModel()
        setupRecyclerView()
        setStudentNameHeader()
    }

    private fun setTitle(){
        requireActivity().title = requireContext().getString(R.string.attendance_record)
    }

    private fun setStudentNameHeader(){
        val selectedStudentIndex = arguments?.getInt(STUDENT_INDEX)
        binding.tvStudentName.text = activityViewModel.getStudentNameAtIndex(selectedStudentIndex!!)
    }

    private fun setupViewModel(){
        activityViewModel = ViewModelProvider(requireActivity())[AttendanceRegisterActivityViewModel::class.java]
    }

    private fun setupRecyclerView(){
        val layoutMan = LinearLayoutManager(requireContext()).apply {
            orientation = LinearLayoutManager.VERTICAL
        }
        binding.recyclerView.layoutManager = layoutMan

        binding.recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))

        val adapter = StudentAttendancesFragmentRecyclerAdapter(requireContext())

        val selectedStudentIndex = arguments?.getInt(STUDENT_INDEX)
        adapter.updateData(activityViewModel.getStudentAttendancesAtIndex(selectedStudentIndex!!))
        binding.recyclerView.adapter = adapter
    }


    companion object {
        const val STUDENT_INDEX = "student_index"
        const val FRAGMENT_NAME = "StudentAttendancesForSequenceFragment"
        @JvmStatic
        fun newInstance(studentIndex: Int) =
            StudentAttendancesForSequenceFragment().apply {
                arguments = Bundle().apply {
                    putInt(STUDENT_INDEX, studentIndex)
                }
            }
    }
}