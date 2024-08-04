package com.tangwantech.scoreandattendanceregister.listbuilders

import com.tangwantech.scoreandattendanceregister.constants.Constants
import com.tangwantech.scoreandattendanceregister.models.TermSequence

class SequencesListBuilder {
    companion object{
        fun build():List<TermSequence>{
            val sequences = ArrayList<TermSequence>()
            for (name in Constants.SEQUENCE_NAMES){
                sequences.add(TermSequence(name))
            }
            return sequences
        }
    }

}