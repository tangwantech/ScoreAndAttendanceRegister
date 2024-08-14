package com.tangwantech.scoreandattendanceregister.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.tangwantech.scoreandattendanceregister.R
import com.tangwantech.scoreandattendanceregister.databinding.ScoreStatisticsDialogBinding
import com.tangwantech.scoreandattendanceregister.models.ScoreStatisticsData

class ScoreStatisticsDialog(private val scoreStatisticsData: ScoreStatisticsData,
                            private val title: String): DialogFragment() {

    private var binding: ScoreStatisticsDialogBinding? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        updateBinding()
        setupViews()

        val dialog = AlertDialog.Builder(requireContext()).apply {
            setTitle(title)
            setView(binding!!.root)
            setPositiveButton(requireContext().getString(R.string.ok)){btn, _ ->
                btn.dismiss()
            }

        }.create()


        return dialog

    }

    override fun onDetach() {
        super.onDetach()
        binding = null
    }

    private fun updateBinding(){
        binding = ScoreStatisticsDialogBinding.inflate(requireActivity().layoutInflater)
    }

    private fun setupViews(){
        binding!!.tvNumSat.text = requireContext().getString(R.string.number_sat, scoreStatisticsData.numberSat)
        binding!!.tvNumPassed.text = requireContext().getString(R.string.number_passed, scoreStatisticsData.numberPassed)
        binding!!.tvPercentPassed.text = requireContext().getString(R.string.percentage_passed, scoreStatisticsData.percentagePass)

    }


}