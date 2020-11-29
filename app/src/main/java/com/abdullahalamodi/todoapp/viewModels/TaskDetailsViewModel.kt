package com.abdullahalamodi.todoapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.abdullahalamodi.todoapp.Task
import com.abdullahalamodi.todoapp.TasksRepository
import java.util.*

class TaskDetailsViewModel: ViewModel() {

    private val tasksRepository = TasksRepository.get();
    private val taskIdLiveData = MutableLiveData<UUID>();
    //prepare liveDate to git task if id change
    val taskLiveData: LiveData<Task?> =
        Transformations.switchMap(taskIdLiveData){ taskId ->
            tasksRepository.getTask(taskId)
        }

    fun loadTask(taskId: UUID) {
        taskIdLiveData.value = taskId
    }
    fun updateTask(task: Task) = tasksRepository.updateTask(task);

}