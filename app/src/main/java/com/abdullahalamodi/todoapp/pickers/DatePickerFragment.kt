package com.abdullahalamodi.todoapp.pickers

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

private const val ARG_DATE = "date"
private const val ARG_OPENER = "opener"
private const val DIALOG_TIME = "DialogTime"
private const val REQUEST_TIME = 1

class DatePickerFragment : DialogFragment(), TimePickerFragment.Callbacks {

    interface Callbacks {
        fun onTimeSelected(date: Date, opener: Int?)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val opener = arguments?.getInt(ARG_OPENER)
        val dateListener =
            DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, day: Int ->
                val resultDate: Date = GregorianCalendar(year, month, day).time
                openTimePicker(resultDate, opener)
            }
        val date = arguments?.getSerializable(ARG_DATE) as Date
        val calendar = Calendar.getInstance()
        calendar.time = date
        val initialYear = calendar.get(Calendar.YEAR)
        val initialMonth = calendar.get(Calendar.MONTH)
        val initialDay = calendar.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(
            requireContext(),
            dateListener,
            initialYear,
            initialMonth,
            initialDay
        )
    }

    private fun openTimePicker(date: Date, opener: Int?) {
        TimePickerFragment.newInstance(date, opener).apply {
            setTargetFragment(this@DatePickerFragment, REQUEST_TIME)
            show(this@DatePickerFragment.requireFragmentManager(), DIALOG_TIME)
        }
    }


    companion object {
        fun newInstance(date: Date, opener: Int?): DatePickerFragment {
            val args = Bundle().apply {
                putSerializable(ARG_DATE, date)
                opener?.let { putInt(ARG_OPENER, it) }
            }
            return DatePickerFragment().apply {
                arguments = args
            }
        }
    }

    override fun onTimeSelected(date: Date, opener: Int?) {
        targetFragment?.let {
            (it as Callbacks).onTimeSelected(date, opener)
        }
    }
}