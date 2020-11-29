package com.abdullahalamodi.todoapp.fragments

import android.icu.text.DateFormat
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.abdullahalamodi.todoapp.R
import com.abdullahalamodi.todoapp.Task
import com.abdullahalamodi.todoapp.pickers.DatePickerFragment
import com.abdullahalamodi.todoapp.viewModels.TaskDetailsViewModel
import java.util.*

const val ARG_TASK_ID = "task_id";
private const val DIALOG_DATE = "DialogDate"
private const val REQUEST_DATE = 0
private const val DATE_OPENER = 101
private const val PERIOD_OPENER = 102

class TaskFragment : Fragment(), DatePickerFragment.Callbacks {
    private lateinit var task: Task;
    private lateinit var taskName: EditText
    private lateinit var taskDetails: EditText
    private lateinit var taskDate: Button
    private lateinit var taskPeriod: Button
    private lateinit var toDo: TextView
    private lateinit var inProgress: TextView
    private lateinit var done: TextView
    private val tasksDetailsViewModel: TaskDetailsViewModel by lazy {
        ViewModelProviders.of(this).get(TaskDetailsViewModel::class.java);
    }
    private var formatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        DateFormat.getPatternInstance(DateFormat.YEAR_ABBR_MONTH_WEEKDAY_DAY)
    } else {
        null
    };

    override fun onStart() {
        super.onStart()
        val nameWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                task.name = s.toString(); }

            override fun afterTextChanged(s: Editable?) {}
        }
        taskName.addTextChangedListener(nameWatcher);

        val detailsWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                task.details = s.toString(); }

            override fun afterTextChanged(s: Editable?) {}
        }

        taskDetails.addTextChangedListener(detailsWatcher)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        task = Task();
        val taskId: UUID = arguments?.getSerializable(ARG_TASK_ID) as UUID;
        tasksDetailsViewModel.loadTask(taskId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_task, container, false)
        taskName = view.findViewById(R.id.task_name)
        taskDetails = view.findViewById(R.id.task_details)
        taskDate = view.findViewById(R.id.task_date)
        taskPeriod = view.findViewById(R.id.task_period)
        toDo = view.findViewById(R.id.to_do)
        inProgress = view.findViewById(R.id.in_progress)
        done = view.findViewById(R.id.done)



        taskDate.setOnClickListener {
            openDateTimePicker(DATE_OPENER)
        }

        taskPeriod.setOnClickListener {
            openDateTimePicker(PERIOD_OPENER)
        }

        toDo.setOnClickListener {
            task.state = 1;
            setTaskState(task.state)
        }

        inProgress.setOnClickListener {
            task.state = 2;
            setTaskState(task.state)
        }

        done.setOnClickListener {
            task.state = 3;
            setTaskState(task.state)
        }
        return view;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tasksDetailsViewModel.taskLiveData.observe(
            viewLifecycleOwner,
            { task ->
                task?.let {
                    updateUI(it)
                }
            }
        )
    }

    override fun onStop() {
        super.onStop()
        tasksDetailsViewModel.updateTask(task)
    }

    private fun updateUI(t: Task) {
        task = t;
        taskName.setText(task.name)
        taskDetails.setText(task.details)
        taskDate.text = dateFormat(task.date)
        taskPeriod.text = setPeriod(task.period, task.date)
        setTaskState(task.state)
    }

    private fun openDateTimePicker(opener: Int) {
        DatePickerFragment.newInstance(task.date, opener).apply {
            setTargetFragment(this@TaskFragment, REQUEST_DATE)
            show(this@TaskFragment.requireFragmentManager(), DIALOG_DATE)
        }
    }

    private fun setTaskState(state: Int) {
        when (state) {
            1 -> {
                refreshState()
                toDo.background = resources.getDrawable(R.drawable.todo_shape)
            }
            2 -> {
                refreshState()
                inProgress.background = resources.getDrawable(R.drawable.in_progress_shape)
            }
            3 -> {
                refreshState()
                done.background = resources.getDrawable(R.drawable.done_shape)
            }
        }
    }

    private fun refreshState() {
        toDo.background = resources.getDrawable(R.drawable.state_shape)
        inProgress.background = resources.getDrawable(R.drawable.state_shape)
        done.background = resources.getDrawable(R.drawable.state_shape)
    }

    private fun dateFormat(date: Date): String? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            formatter?.run {
                format(date);
            }
        } else {
            date.toString();
        };
    }

    private fun setPeriod(date1: Date, date2: Date): String {
        var period = "";
        //period date
        val calendar1 = Calendar.getInstance()
        calendar1.time = date1
        val month1 = calendar1.get(Calendar.MONTH)
        val day1 = calendar1.get(Calendar.DAY_OF_MONTH)
        val hour1 = calendar1.get(Calendar.HOUR_OF_DAY)
        val minute1 = calendar1.get(Calendar.MINUTE)
        //create date
        val calendar2 = Calendar.getInstance()
        calendar1.time = date2
        val month2 = calendar2.get(Calendar.MONTH)
        val day2 = calendar2.get(Calendar.DAY_OF_MONTH)
        val hour2 = calendar2.get(Calendar.HOUR_OF_DAY)
        val minute2 = calendar2.get(Calendar.MINUTE)

        //get different
        val month = if (month1 > month2) month1 - minute2 else 0
        val day = if (day1 > day2) day1 - day2 else 0
        val hour = if (hour1 > hour2) hour1 - hour2 else 0
        val minute = if (minute1 > minute2) minute1 - minute2
        else {
            if (month == 0 && day == 0 && hour == 0) {
                taskPeriod.setTextColor(resources.getColor(R.color.red));
                return "expired"
            }
            0
        }

        if (month > 0) period += "$month month  "
        if (day > 0) period += "$day day  "
        if (hour > 0) period += "$hour hour  "
        if (minute > 0) period += "$minute minute .. "
        period += " left"

        return period;
    }

    companion object {
        @JvmStatic
        fun newInstance(taskId: UUID) = TaskFragment().apply {
            val args = Bundle().apply {
                putSerializable(ARG_TASK_ID, taskId)
            }
            arguments = args;
        }
    }

    override fun onTimeSelected(date: Date, opener: Int?) {
        Toast.makeText(context, date.toString(), Toast.LENGTH_SHORT).show()
        when (opener) {
            DATE_OPENER -> task.date = date
            PERIOD_OPENER -> task.period = date
        }
        updateUI(task)
    }
}