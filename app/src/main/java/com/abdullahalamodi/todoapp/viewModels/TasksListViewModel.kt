package com.abdullahalamodi.todoapp.viewModels

import androidx.lifecycle.ViewModel
import com.abdullahalamodi.todoapp.Task
import com.abdullahalamodi.todoapp.TasksRepository
import java.util.*

class TasksListViewModel: ViewModel() {

    private val tasksRepository = TasksRepository.get();

    fun tasksListViewData(state:Int?) = tasksRepository.getTasks(state);

    fun addTask(task: Task) = tasksRepository.addTask(task);
    fun deleteTask(id: UUID) = tasksRepository.deleteTask(id);
}