package com.tangwantech.scoreandattendanceregister.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tangwantech.scoreandattendanceregister.R
import com.tangwantech.scoreandattendanceregister.databinding.StudentsRecyclerViewItemBinding
import com.tangwantech.scoreandattendanceregister.models.Student

class StudentsInAcademicYearAndClassRecyclerAdapter(private val context: Context, private val onItemPressListener: OnItemPressListener):
    RecyclerView.Adapter<StudentsInAcademicYearAndClassRecyclerAdapter.ViewHolder>() {
    private var data: ArrayList<Student> = ArrayList()



    fun updateData(students: ArrayList<Student>){
        data = students
    }

    inner class ViewHolder(val binding: StudentsRecyclerViewItemBinding): RecyclerView.ViewHolder(binding.root){
        init {
            binding.main.setOnLongClickListener {
                onItemPressListener.onLongPress(adapterPosition)
                true
            }
            binding.main.setOnClickListener {
                onItemPressListener.onItemTapped(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = StudentsRecyclerViewItemBinding.inflate( LayoutInflater.from(context), parent, false)
//        val view = LayoutInflater.from(context).inflate(R.layout.students_recycler_view_item, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvStudentName.text = data[position].studentName
        holder.binding.tvStudentId.text = data[position].studentId

    }

    interface OnItemPressListener{
        fun onLongPress(itemPosition: Int)
        fun onItemTapped(itemPosition: Int)
    }
}