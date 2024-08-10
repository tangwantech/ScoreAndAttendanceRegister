package com.tangwantech.scoreandattendanceregister.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.tangwantech.scoreandattendanceregister.R
import com.tangwantech.scoreandattendanceregister.constants.Constants
import com.tangwantech.scoreandattendanceregister.databinding.AcademicYearClassSubjectSequenceBinding
import com.tangwantech.scoreandattendanceregister.viewmodels.AutoCompleteFormDialogViewModel

class AutocompleteFormDialog: DialogFragment() {
    private lateinit var viewBinding: AcademicYearClassSubjectSequenceBinding
    private lateinit var onDialogPositiveButtonClickListener: OnDialogPositiveButtonClickListener
    private lateinit var viewModel: AutoCompleteFormDialogViewModel
    private lateinit var positiveBtn: Button


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnDialogPositiveButtonClickListener){
            onDialogPositiveButtonClickListener = context
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        setupViewBinding()
        setupViewModel()
        setupAutocompleteViews()
        setupAutocompleteListeners()
        val dialog = AlertDialog.Builder(requireContext()).create()
        dialog.apply {
            setView(viewBinding.root)
            setButton(AlertDialog.BUTTON_POSITIVE, "Ok"){ btn, _ ->
                val academicYear = viewBinding.autoCompleteAcademicYear.text.toString()
                val form = viewBinding.autoCompleteForm.text.toString()
                val sequence = viewBinding.autoCompleteSequence.text.toString()
                val subject = viewBinding.autoCompleteSubject.text.toString()

                viewModel.apply {
                    selectedAcademicYear = academicYear
                    selectedClass = form
                    selectedSequence = sequence
                    selectedSubject = subject
                }

                val bundle = Bundle().apply {
                    putString(Constants.ACADEMIC_YEAR, academicYear)
                    putString(Constants.FORM, form)
                    putString(Constants.SEQUENCE, sequence)
                    putString(Constants.SUBJECT, subject)
                }
                onDialogPositiveButtonClickListener.onDialogPositiveButtonClicked(bundle)
                btn.dismiss()

            }
            setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel)){ btn, _ ->
                btn.dismiss()
            }
        }.show()
        positiveBtn = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        positiveBtn.isEnabled = false
        return dialog
    }

    private fun setupViewBinding(){
        viewBinding = AcademicYearClassSubjectSequenceBinding.inflate(requireActivity().layoutInflater)
    }


    private fun setupViewModel(){
        viewModel = ViewModelProvider(requireActivity())[AutoCompleteFormDialogViewModel::class.java]
    }

    private fun setupAutocompleteViews(){
        viewBinding.autoCompleteAcademicYear.setText(viewModel.selectedAcademicYear)
        val academicYearAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, Constants.ACADEMIC_YEARS)
        viewBinding.autoCompleteAcademicYear.setAdapter(academicYearAdapter)


        viewBinding.autoCompleteForm.setText(viewModel.selectedClass)
        val formAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, Constants.FORMS)
        viewBinding.autoCompleteForm.setAdapter(formAdapter)


        viewBinding.autoCompleteSequence.setText(viewModel.selectedSequence)
        val sequenceAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, Constants.SEQUENCE_NAMES)
        viewBinding.autoCompleteSequence.setAdapter(sequenceAdapter)

        viewBinding.autoCompleteSubject.setText(viewModel.selectedSubject)
        val subjectAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, Constants.SUBJECTS)
        viewBinding.autoCompleteSubject.setAdapter(subjectAdapter)

    }
    private fun setupAutocompleteListeners(){
        viewBinding.autoCompleteAcademicYear.doOnTextChanged { text, start, before, count ->
            if(text.toString().isNotEmpty() && viewBinding.autoCompleteForm.text.toString().isNotEmpty()
                && viewBinding.autoCompleteSequence.text.toString().isNotEmpty()
                && viewBinding.autoCompleteSubject.text.toString().isNotEmpty()){
                positiveBtn.isEnabled = true
            }else{
                positiveBtn.isEnabled = false
            }
        }

        viewBinding.autoCompleteForm.doOnTextChanged { text, start, before, count ->
            if((text.toString().isNotEmpty() && viewBinding.autoCompleteAcademicYear.text.toString().isNotEmpty()
                        && viewBinding.autoCompleteSequence.text.toString().isNotEmpty()
                        && viewBinding.autoCompleteSubject.text.toString().isNotEmpty())){
                positiveBtn.isEnabled = true
            }else{
                positiveBtn.isEnabled = false
            }

        }

        viewBinding.autoCompleteSequence.doOnTextChanged { text, start, before, count ->
            if(text.toString().isNotEmpty() && viewBinding.autoCompleteAcademicYear.text.toString().isNotEmpty()
                && viewBinding.autoCompleteForm.text.toString().isNotEmpty()
                && viewBinding.autoCompleteSubject.text.toString().isNotEmpty()){
                positiveBtn.isEnabled = true
            }else{
                positiveBtn.isEnabled = false
            }
        }

        viewBinding.autoCompleteSubject.doOnTextChanged { text, start, before, count ->
            if(text.toString().isNotEmpty() && viewBinding.autoCompleteAcademicYear.text.toString().isNotEmpty()
                && viewBinding.autoCompleteForm.text.toString().isNotEmpty()
                && viewBinding.autoCompleteSequence.text.toString().isNotEmpty()){
                positiveBtn.isEnabled = true
            }else{
                positiveBtn.isEnabled = false
            }
        }

    }
    interface OnDialogPositiveButtonClickListener{
        fun onDialogPositiveButtonClicked(bundle: Bundle)
    }

}