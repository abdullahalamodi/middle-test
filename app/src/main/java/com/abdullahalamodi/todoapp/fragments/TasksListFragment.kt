package com.abdullahalamodi.todoapp.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abdullahalamodi.todoapp.R
import com.abdullahalamodi.todoapp.Task
import com.abdullahalamodi.todoapp.TasksListAdapter
import com.abdullahalamodi.todoapp.viewModels.TasksListViewModel
import java.util.*

const val ARG_TASK_STATE = "tasks_state";
class TasksListFragment : Fragment(),TasksListAdapter.Callbacks {
    private var callbacks: Callbacks? = null;
    private lateinit var tasksRecyclerView: RecyclerView;
    private lateinit var adapter: TasksListAdapter;
    private var state:Int? = 0;
    private val tasksListViewModel : TasksListViewModel by lazy{
        ViewModelProviders.of(this).get(TasksListViewModel::class.java);
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        state = arguments?.getInt(ARG_TASK_STATE,0);
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list_tasks, container, false);
        tasksRecyclerView = view.findViewById(R.id.tasks_recycler_view);
        tasksRecyclerView.layoutManager = LinearLayoutManager(context);
        adapter = TasksListAdapter(this);
        tasksRecyclerView.adapter = adapter;

        return view;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tasksListViewModel.tasksListViewData(state).observe(
            viewLifecycleOwner,
            { tasks ->
                tasks?.let{
                    updateUI(it);
                }
            }
        )
    }

    private fun updateUI(tasks: List<Task>) {
        adapter?.submitList(tasks);
        tasksRecyclerView.adapter = adapter;
    }


    //menu inflate
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_task_list, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_task -> {
                val task = Task()
                tasksListViewModel.addTask(task)
                callbacks?.onTaskSelected(task.id)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    interface Callbacks {
        fun onTaskSelected(taskId: UUID)
    }

    companion object {
        @JvmStatic
        fun newInstance(state:Int) = TasksListFragment().apply {
            val args = Bundle().apply {
                putInt(ARG_TASK_STATE, state)
            }
            arguments = args;
        };
    }

    override fun onTaskSelected(taskId: UUID) {
        callbacks?.onTaskSelected(taskId)
    }

    override fun onDeleteClick(taskId: UUID) {
        tasksListViewModel.deleteTask(taskId)
    }

}