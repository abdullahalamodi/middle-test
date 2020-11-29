package com.abdullahalamodi.todoapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.abdullahalamodi.todoapp.Task
import java.util.*

@Dao
interface TasksDao {

    @Query("SELECT * FROM Task where state=:state")
    fun getTasks(state:Int?): LiveData<List<Task>>

    @Query("SELECT * FROM Task WHERE id=(:id)")
    fun getTask(id: UUID): LiveData<Task?>

    @Update
    fun updateTask(task: Task)

    @Insert
    fun addTask(task: Task)

    @Query("Delete from Task WHERE id=:id")
    fun deleteTask(id: UUID)
}