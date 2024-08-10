package com.tangwantech.scoreandattendanceregister.constants

class Constants {
    companion object{
        val ACADEMIC_YEARS = arrayOf<String>("2024-2025", "2026-2027")
        val SEQUENCE_NAMES = arrayOf<String>(
            "First sequence",
            "Second sequence",
            "Third sequence",
            "Fourth sequence",
            "Fifth sequence",
            "Sixth sequence")
        val TERMS = arrayOf<String>("First term", "Second term", "Third term")
        val FORMS = arrayOf<String>(
            "Form 1A",
            "Form 1B",
            "Form 1C",
            "Form 1Bil",
            "Form 2A",
            "Form 2B",
            "Form 2Bil",
            "Form 3A",
            "Form 3B",
            "Form 3Bil",
            "Form 4Arts",
            "Form 4Bil",
            "Form 4Science",
            "Form 5Arts",
            "Form 5Science",
            "LSS 1",
            "LSS 2",
            "LSS 3",
            "LSS 4",
            "USS 1",
            "USS 2",
            "USS 3",
            "USS 4"
        )

//        val classListFileNames = mapOf<String, String>(FORMS[0] to "Form1A2024-2025.json", FORMS[1] to "Form1B2024-2025.json")
        val SUBJECTS = arrayOf<String>("Biology", "Human Biology")
        val FORM_SUBJECTS = mapOf<String, Array<String>>(
            FORMS[0] to arrayOf(SUBJECTS[0]),
            FORMS[1] to arrayOf(SUBJECTS[0]),
            FORMS[2] to arrayOf(SUBJECTS[0]),
            FORMS[3] to arrayOf(SUBJECTS[0]),
            FORMS[4] to arrayOf(SUBJECTS[0]),
            FORMS[5] to arrayOf(SUBJECTS[0]),
            FORMS[6] to arrayOf(SUBJECTS[0]),
            FORMS[7] to arrayOf(SUBJECTS[0]),
            FORMS[8] to arrayOf(SUBJECTS[0]),
            FORMS[9] to arrayOf(SUBJECTS[0]),
            FORMS[10] to arrayOf(SUBJECTS[0]),
            FORMS[11] to arrayOf(SUBJECTS[0]),
            FORMS[12] to arrayOf(SUBJECTS[0], SUBJECTS[1]),
            FORMS[13] to arrayOf(SUBJECTS[0]),
            FORMS[14] to arrayOf(SUBJECTS[0], SUBJECTS[1]),
            FORMS[15] to arrayOf(SUBJECTS[0]),
            FORMS[16] to arrayOf(SUBJECTS[0]),
            FORMS[17] to arrayOf(SUBJECTS[0]),
            FORMS[18] to arrayOf(SUBJECTS[0]),
            FORMS[19] to arrayOf(SUBJECTS[0]),
            FORMS[20] to arrayOf(SUBJECTS[0]),
            FORMS[21] to arrayOf(SUBJECTS[0]),
            FORMS[22] to arrayOf(SUBJECTS[0])


        )


        val PERIOD_COUNTS = arrayOf<Int>(1, 2)
        val GENDERS = arrayOf<String>("M", "F")

        const val ACADEMIC_YEAR_INDEX = "index"
        const val ACADEMIC_YEAR = "year"
        const val FORM = "form"
        const val SEQUENCE = "sequence"
        const val SUBJECT = "subject"
        const val SELECT_ITEM_INDEX = "selected item index"

        const val MAX_SCORE = 20.0
        const val MIN_SCORE = 0.0
        const val PASS_MIN = 10.0
    }
}