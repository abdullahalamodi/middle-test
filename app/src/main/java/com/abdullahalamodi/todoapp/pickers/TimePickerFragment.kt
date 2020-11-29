package com.abdullahalamodi.todoapp.pickers

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.*

private const val ARG_TIME = "time"
private const val ARG_OPENER = "opener"

class TimePickerFragment : DialogFragment(){
    private lateinit var date: Date;
    private var opener: Int? = null;
    private val calendar = Calendar.getInstance()

    interface Callbacks {
        fun onTimeSelected(date: Date, opener: Int?)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        date = arguments?.getSerializable(ARG_TIME) as Date
        opener = arguments?.getInt(ARG_OPENER, 1)
        calendar.time = date
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dateListener =
            TimePickerDialog.OnTimeSetListener { _: TimePicker?, hourOfDay: Int, minute: Int ->
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                (targetFragment as Callbacks).onTimeSelected(calendar.time, opener)

            }

        val initialHour = calendar.get(Calendar.HOUR)
        val initialMinute = calendar.get(Calendar.MINUTE)
        return TimePickerDialog(
            requireContext(),
            dateListener,
            initialHour,
            initialMinute,
            true
        )
    }


    companion object {
        fun newInstance(date: Date, opener: Int?): TimePickerFragment {
            val args = Bundle().apply {
                putSerializable(ARG_TIME, date)
                opener?.let { putInt(ARG_OPENER, it) }
            }
            return TimePickerFragment().apply {
                arguments = args
            }
        }
    }
}