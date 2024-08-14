package com.tangwantech.scoreandattendanceregister.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tangwantech.scoreandattendanceregister.R
import com.tangwantech.scoreandattendanceregister.databinding.FragmentAttendanceRegisterHomeRecyclerItemBinding
import com.tangwantech.scoreandattendanceregister.models.StudentAttendanceData

class AttendanceRegisterHomeFragmentRecyclerAdapter(
    private val context: Context, private val onItemClickListener: OnItemClickListener
): RecyclerView.Adapter<AttendanceRegisterHomeFragmentRecyclerAdapter.ViewHolder>() {
    private var data = ArrayList<StudentAttendanceData>()
    fun updateData(temp: List<StudentAttendanceData>){
        data.addAll(temp)
    }
    inner class ViewHolder(val binding: FragmentAttendanceRegisterHomeRecyclerItemBinding): RecyclerView.ViewHolder(binding.root){
        init {
            binding.main.setOnClickListener {
                onItemClickListener.onHomeFragmentRecyclerItemClicked(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FragmentAttendanceRegisterHomeRecyclerItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        println("within AttendanceRegisterHomeFragmentRecyclerAdapter: $data")
        holder.binding.tvClassNumberIdAndName.text = context.getString(R.string.number_id_and_name,
            data[position].classNumber,
            data[position].studentId,
            data[position].studentName)
        holder.binding.tvTotalAbsences.text = context.getString(R.string.total_absences, data[position].totalAbsencesForSequence)
    }

    interface OnItemClickListener{
        fun onHomeFragmentRecyclerItemClicked(itemPosition: Int)
    }

}