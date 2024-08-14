package com.tangwantech.scoreandattendanceregister.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tangwantech.scoreandattendanceregister.R
import com.tangwantech.scoreandattendanceregister.databinding.FragmentRollCallRecyclerItemBinding
import com.tangwantech.scoreandattendanceregister.models.StudentAttendanceData

class FragmentRollCallRecyclerAdapter(private val context: Context, private val onSwitchButtonStateChangeListener: OnSwitchButtonStateChangeListener): RecyclerView.Adapter<FragmentRollCallRecyclerAdapter.ViewHolder>() {
    private var data = ArrayList<StudentAttendanceData>()

    fun updateData(temp: List<StudentAttendanceData>){
        data.addAll(temp)
    }
    inner class ViewHolder(val binding:FragmentRollCallRecyclerItemBinding): RecyclerView.ViewHolder(binding.root){
        init {
            binding.itemSwitch.setOnCheckedChangeListener { compoundButton, state ->
                onSwitchButtonStateChangeListener.onItemSwitchButtonStateChanged(adapterPosition, state)

            }

            binding.main.setOnClickListener {
                binding.itemSwitch.isChecked = !binding.itemSwitch.isChecked
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FragmentRollCallRecyclerItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvClassNumberIdAndName.text = context.getString(
            R.string.number_id_and_name,
            data[position].classNumber,
            data[position].studentId,
            data[position].studentName)
        holder.binding.itemSwitch.isChecked = data[position].isAbsent

    }

    interface OnSwitchButtonStateChangeListener{
        fun onItemSwitchButtonStateChanged(studentIndex: Int, state: Boolean)
    }
}