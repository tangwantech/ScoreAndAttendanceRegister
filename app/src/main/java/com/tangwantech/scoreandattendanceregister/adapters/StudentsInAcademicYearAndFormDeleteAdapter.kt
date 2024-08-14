package com.tangwantech.scoreandattendanceregister.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tangwantech.scoreandattendanceregister.databinding.DeleteItemBinding
import com.tangwantech.scoreandattendanceregister.models.DeleteStudentData

class StudentsInAcademicYearAndFormDeleteAdapter(private val context: Context, private val onItemCheckChangeListener: OnItemCheckChangeListener):
    RecyclerView.Adapter<StudentsInAcademicYearAndFormDeleteAdapter.ViewHolder>() {
    private var students = ArrayList<DeleteStudentData>()


    inner class ViewHolder(val binding: DeleteItemBinding): RecyclerView.ViewHolder(binding.root){
        init {

            binding.main.setOnClickListener {
                binding.checkBoxItem.isChecked = !binding.checkBoxItem.isChecked

            }
            binding.checkBoxItem.setOnCheckedChangeListener { _, state ->
                onItemCheckChangeListener.onItemCheckedStateChanged(adapterPosition, state)

            }
        }
    }

    fun updateData(students: ArrayList<DeleteStudentData>){
        this.students = students
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DeleteItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return students.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvIdAndName.text = "${students[position].studentId} ${students[position].studentName}"
        holder.binding.checkBoxItem.isChecked = students[position].isChecked

    }

    interface OnItemCheckChangeListener{
        fun onItemCheckedStateChanged(itemPosition: Int, state: Boolean)
    }

}