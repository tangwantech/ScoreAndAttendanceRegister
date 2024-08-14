package com.tangwantech.scoreandattendanceregister.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tangwantech.scoreandattendanceregister.R
import com.tangwantech.scoreandattendanceregister.adapters.FragmentRollCallRecyclerAdapter
import com.tangwantech.scoreandattendanceregister.databinding.FragmentRollCallBinding
import com.tangwantech.scoreandattendanceregister.models.Attendance
import com.tangwantech.scoreandattendanceregister.viewmodels.AttendanceRegisterActivityViewModel


class RollCallFragment : Fragment(), FragmentRollCallRecyclerAdapter.OnSwitchButtonStateChangeListener {
    private lateinit var binding: FragmentRollCallBinding
    private  lateinit var activityViewModel: AttendanceRegisterActivityViewModel
    private lateinit var rvAdapter: FragmentRollCallRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentRollCallBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupRecyclerView()
        setupObservers()
        setTitle()
        setDateHeader()
    }

    override fun onResume() {
        super.onResume()
        binding.tvNumberAbsent.text = "Number absent: ${activityViewModel.getNumberOfStudentsAbsent()}"
    }

    private fun setTitle(){
        requireActivity().title = requireContext().getString(R.string.roll_call, activityViewModel.getForm())
    }

    private fun setDateHeader(){
        binding.tvDate.text = activityViewModel.getCurrentDate()
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

        rvAdapter = FragmentRollCallRecyclerAdapter(requireContext(), this)
        rvAdapter.updateData(activityViewModel.getStudentsAttendanceDataListForSequence())
        binding.recyclerView.adapter = rvAdapter
        rvAdapter.notifyDataSetChanged()

    }

    private fun setupObservers(){
        activityViewModel.numberOfStudentsAbsent.observe(requireActivity()){
            binding.tvNumberAbsent.text = "Number absent: $it"
        }

//        activityViewModel.studentTotalNumberOfAbsencesSequenceForSequenceUpdatedAtIndex.observe(requireActivity()){
////            rvAdapter.notifyItemChanged(it)
//        }

    }


    companion object {
        const val FRAGMENT_NAME = "RollCallFragment"

        @JvmStatic
        fun newInstance() =
            RollCallFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onItemSwitchButtonStateChanged(studentIndex: Int, state: Boolean) {
        activityViewModel.updateStudentAttendancesForCurrentSequenceForToday(studentIndex, state)


    }
}