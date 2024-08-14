package com.tangwantech.scoreandattendanceregister.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tangwantech.scoreandattendanceregister.R
import com.tangwantech.scoreandattendanceregister.databinding.StudentAttendancesFragmentRecyclerItemBinding
import com.tangwantech.scoreandattendanceregister.models.Attendance
import java.util.ArrayList

class StudentAttendancesFragmentRecyclerAdapter(private val context: Context): RecyclerView.Adapter<StudentAttendancesFragmentRecyclerAdapter.ViewHolder>() {
    private val data = ArrayList<Attendance>()
    fun updateData(temp: List<Attendance>){
        data.addAll(temp)
    }
    inner class ViewHolder(val binding: StudentAttendancesFragmentRecyclerItemBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = StudentAttendancesFragmentRecyclerItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val presentOrAbsent = if (data[position].isAbsent) context.getString(R.string.absent) else context.getString(R.string.present)
        holder.binding.tvDate.text = context.getString(R.string.attendance_date_record, (position + 1).toString(), data[position].date, presentOrAbsent)
    }
}