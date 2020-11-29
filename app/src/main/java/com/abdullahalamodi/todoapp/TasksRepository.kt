package com.abdullahalamodi.todoapp

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.abdullahalamodi.todoapp.database.TaskDatabase
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "tasks-database"
class TasksRepository private constructor(context: Context) {

    private val database: TaskDatabase = Room.databaseBuilder(
        context.applicationContext,
        TaskDatabase::class.java,
        DATABASE_NAME
    )
        .build()

    private val tasksDao = database.taskDao();
    private val executor = Executors.newSingleThreadExecutor();

    fun getTasks(state:Int?): LiveData<List<Task>> = tasksDao.getTasks(state)

    fun getTask(id: UUID): LiveData<Task?> = tasksDao.getTask(id);

    fun updateTask(task: Task) {
        executor.execute {
            tasksDao.updateTask(task)
        }
    }

    fun addTask(task: Task) {
        executor.execute {
            tasksDao.addTask(task)
        }
    }

    fun deleteTask(id: UUID) {
        executor.execute {
            tasksDao.deleteTask(id)
        }
    }


    companion object {
        private var INSTANCE: TasksRepository? = null;

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = TasksRepository(context)
            }
        }

        fun get(): TasksRepository {
            return INSTANCE ?:
            throw IllegalStateException("com.abdullahalamodi.todoapp.com.abdullahalamodi.todoapp.TasksRepository must be initialized")
        }
    }
}