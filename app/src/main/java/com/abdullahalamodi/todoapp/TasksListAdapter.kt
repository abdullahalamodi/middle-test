package com.abdullahalamodi.todoapp

import android.icu.text.DateFormat
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
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
                when (task.state) {
                    1 -> setTextColor(context.resources.getColor(R.color.yellow))
                    2 -> setTextColor(context.resources.getColor(R.color.blue))
                    3 -> setTextColor(context.resources.getColor(R.color.green))
                }
                text = setPeriod(task.period)
            }
        }

        private fun setPeriod(date1: Date): String {
            var period = "";
            //get different
            val diff: Long = date1.time - Date().time
            var minute = diff / (1000 * 60);
            var hour = minute / 60;
            var day = hour / 24;
            var month = day / 30;
            //if not expired
            if (minute > 0) {
                if (minute > 60) {
                    hour = minute / 60
                    minute %= 60
                }
                if (hour > 24) {
                    day = hour / 24
                    hour %= 24;
                    if (day > 30) {
                        month = day / 30
                        day %= 30
                    }
                }else{
                    //close to expire and not done
                    if (task.state != 3) {
                        periodTextView.setTextColor(context.resources.getColor(R.color.red))
                    }
                }
            } else {
                //expired
                if (task.state != 3) {
                    periodTextView.setTextColor(context.resources.getColor(R.color.red))
                return "expired"
                }
                return "completed"
            }

            if (month > 0) period += "$month month  "
            if (day > 0) period += "$day day  "
            if (hour > 0) period += "$hour hour  "
            if (minute > 0) period += "$minute minute .. "
            period += " left"

            return period;

        }

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



