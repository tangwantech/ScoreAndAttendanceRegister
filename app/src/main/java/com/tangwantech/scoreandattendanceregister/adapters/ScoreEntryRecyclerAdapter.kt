package com.tangwantech.scoreandattendanceregister.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tangwantech.scoreandattendanceregister.R
import com.tangwantech.scoreandattendanceregister.databinding.StudentScoreRecyclerItemBinding
import com.tangwantech.scoreandattendanceregister.databinding.StudentsRecyclerViewItemBinding
import com.tangwantech.scoreandattendanceregister.models.StudentScoreData

class ScoreEntryRecyclerAdapter(private val context: Context, private val onItemClickListener: OnItemClickListener): RecyclerView.Adapter<ScoreEntryRecyclerAdapter.ViewHolder>() {
    private var studentsScoreList = ArrayList<StudentScoreData>()

    fun updateStudentsScoreList(tempScoreList: ArrayList<StudentScoreData>){
        studentsScoreList = tempScoreList
    }

    inner class ViewHolder(val binding: StudentScoreRecyclerItemBinding): RecyclerView.ViewHolder(binding.root){
        init {
            binding.main.setOnClickListener {
                onItemClickListener.onItemClicked(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = StudentScoreRecyclerItemBinding.inflate( LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return studentsScoreList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvStudentName.text = context.getString(R.string.class_number_and_name, studentsScoreList[position].classNumber, studentsScoreList[position].studentName )
//        holder.binding.tvStudentName.text = "${studentsScoreList[position].classNumber} ${studentsScoreList[position].studentName}"
        holder.binding.tvStudentId.text = studentsScoreList[position].studentId
        holder.binding.tvStudentGender.text = studentsScoreList[position].studentGender
        holder.binding.tvScore.text = context.getString(R.string.score, studentsScoreList[position].score.toString())
    }

    interface OnItemClickListener{
        fun onItemClicked(itemPosition: Int)
    }
}