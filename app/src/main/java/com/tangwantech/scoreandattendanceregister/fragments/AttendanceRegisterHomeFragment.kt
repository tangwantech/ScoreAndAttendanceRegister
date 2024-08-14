package com.tangwantech.scoreandattendanceregister.fragments

import android.content.Context
import android.os.Bundle
import android.text.format.DateUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.tangwantech.scoreandattendanceregister.R
import com.tangwantech.scoreandattendanceregister.adapters.AttendanceRegisterHomeFragmentRecyclerAdapter
import com.tangwantech.scoreandattendanceregister.constants.Constants
import com.tangwantech.scoreandattendanceregister.databinding.FragmentAttendanceRegisterHomeBinding
import com.tangwantech.scoreandattendanceregister.databinding.NumberOfPeriodsBinding
import com.tangwantech.scoreandattendanceregister.viewmodels.AttendanceRegisterActivityViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date

class AttendanceRegisterHomeFragment : Fragment() {


    private lateinit var bindingFragment: FragmentAttendanceRegisterHomeBinding
    private lateinit var viewModel: AttendanceRegisterActivityViewModel
    private lateinit var onItemClickListener: AttendanceRegisterHomeFragmentRecyclerAdapter.OnItemClickListener
    private lateinit var onAddButtonClickListener: OnAddButtonClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is AttendanceRegisterHomeFragmentRecyclerAdapter.OnItemClickListener){
            onItemClickListener = context
        }

        if (context is OnAddButtonClickListener){
            onAddButtonClickListener = context
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        bindingFragment = FragmentAttendanceRegisterHomeBinding.inflate(requireActivity().layoutInflater)
        return bindingFragment.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupViewModel()
        setupRecyclerView()
        setupListeners()
        setTitle()

    }

    private fun setTitle(){
        requireActivity().title = requireContext().getString(
            R.string.attendance_register_home_title,
            requireContext().getString(R.string.attendance_register), viewModel.getForm())
    }

    private  fun setupViewModel(){
        viewModel = ViewModelProvider(requireActivity())[AttendanceRegisterActivityViewModel::class.java]
    }

    private fun setupRecyclerView(){
        val layoutMan = LinearLayoutManager(requireContext()).apply {
            orientation = LinearLayoutManager.VERTICAL
        }

        bindingFragment.recyclerViewHome.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))

        bindingFragment.recyclerViewHome.layoutManager = layoutMan
        val adapter = AttendanceRegisterHomeFragmentRecyclerAdapter(requireContext(), onItemClickListener)
        adapter.updateData(viewModel.getStudentsAttendanceDataListForSequence())
        bindingFragment.recyclerViewHome.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun setupListeners(){
        bindingFragment.btnConductRollCall.setOnClickListener {

            showSelectNumberOfPeriodsDialog()
        }
    }

    private fun showSelectNumberOfPeriodsDialog(){
        val numberOfPeriodsBinding = NumberOfPeriodsBinding.inflate(LayoutInflater.from(requireContext()))
        val autoAdapter = ArrayAdapter<Int>(requireContext(), android.R.layout.simple_spinner_dropdown_item, Constants.PERIOD_COUNTS)
        numberOfPeriodsBinding.autoSelectNumberOfPeriods.setAdapter(autoAdapter)
        val dialog = AlertDialog.Builder(requireContext()).apply {
            setView(numberOfPeriodsBinding.root)
            setPositiveButton(requireContext().getString(R.string.ok)){btn, _ ->

                if (numberOfPeriodsBinding.autoSelectNumberOfPeriods.text.toString().isNotEmpty()){
                    val numberOfPeriods = numberOfPeriodsBinding.autoSelectNumberOfPeriods.text.toString().toInt()

                    onAddButtonClickListener.onAddButtonClicked(numberOfPeriods)
                }

                btn.dismiss()
            }

            setNegativeButton(requireContext().getString(R.string.cancel)){btn, _ ->
                btn.dismiss()
            }
        }.create()
        dialog.show()
    }


    companion object {
        const val FRAGMENT_NAME = "AttendanceRegisterHomeFragment"
        @JvmStatic
        fun newInstance() =
            AttendanceRegisterHomeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    interface OnAddButtonClickListener {
        fun onAddButtonClicked(numberOfPeriods: Int)
    }


}