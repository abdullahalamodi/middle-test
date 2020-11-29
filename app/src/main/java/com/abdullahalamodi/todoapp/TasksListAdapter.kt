package com.abdullahalamodi.todoapp

import android.icu.text.DateFormat
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class TasksListAdapter(private val context: Fragment) :
    ListAdapter<Task, TasksListAdapter.TasksHolder>(TasksDiffUtil()) {
    lateinit var task: Task

    interface Callbacks {
        fun onTaskSelected(taskId: UUID)
        fun onDeleteClick(taskId: UUID)
    }

    private var formatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        DateFormat.getPatternInstance(DateFormat.YEAR_ABBR_MONTH_WEEKDAY_DAY)
    } else {
        null;  //for now no need to format date for old versions.
    };

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksHolder {
        val view = context.layoutInflater.inflate(R.layout.list_item_tasks, parent, false)
        return TasksHolder(view)
    }

    override fun onBindViewHolder(holder: TasksHolder, position: Int) {
        val task = getItem(position)
        holder.bind(task)
    }


    inner class TasksHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.task_name)
        private val periodTextView: TextView = itemView.findViewById(R.id.task_period)
        private val deleteBtn: Button = itemView.findViewById(R.id.delete_btn)
//

        init {
            itemView.setOnClickListener {
                (context as Callbacks).onTaskSelected(task.id)
            }

            deleteBtn.setOnClickListener {
                (context as Callbacks).onDeleteClick(task.id)
            }
        }

        fun bind(t: Task) {
            task = t
            nameTextView.text = task.name;
            periodTextView.apply {
                when(task.state) {
                    1 -> setTextColor(context.resources.getColor(R.color.yellow))
                    2 -> setTextColor(context.resources.getColor(R.color.blue))
                    3 -> setTextColor(context.resources.getColor(R.color.green))
                }
                text = setPeriod(task.period, task.date)
            }
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
            val minute = if (minute1 >= minute2) minute1 - minute2
            else {
                0
            }
            val month = if (month1 >= month2) month1 - minute2 else 0
            val day = if (day1 >= day2) day1 - day2 else 0
            val hour = if (hour1 >= hour2) hour1 - hour2 else 0

            if (month > 0) period += "$month month  "
            if (day > 0) period += "$day day  "
            if (hour > 0) period += "$hour hour  "
            if (minute > 0) period += "$minute minute .. "
            period += " left"

            return period;
        }
//        private fun setPeriod(date1: Date, date2: Date): String {
//            var period = "";
//            //get different
//            val diff: Long = date1.time - date2.time
//            var minute = diff / (1000 * 60);
//            var hour = minute / 60;
//            var day = hour / 24;
//            var month = day / 30;
//
//            if (minute > 0) {
//                if (hour > 0) {
//                    minute %= 60;
//                    if (day > 0) {
//                        hour %= 24
//                        if (month > 0) {
//                            day %= 30
//                        }
//                    } else {
//                        //close to expire and not done
//                        if (task.state != 3){
//                            periodTextView.setTextColor(context.resources.getColor(R.color.red))
//                        }
//                    }
//                }
//            } else {
//                //expired
//                return "expired"
//            }
//
//            if (month > 0) period += "$month month  "
//            if (day > 0) period += "$day day  "
//            if (hour > 0) period += "$hour hour  "
//            if (minute > 0) period += "$minute minute .. "
//            period += " left"
//
//            return period;
//
//        }

        //to format date
        private fun dateFormat(date: Date): String? {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                formatter?.run {
                    format(date);
                }
            } else {
                date.toString();
            };
        }
    }

    class TasksDiffUtil : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id === newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }

    }
}



