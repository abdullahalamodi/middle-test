package com.abdullahalamodi.todoapp

import android.app.Application

class TasksApplicationClass : Application() {

    override fun onCreate() {
        super.onCreate()
        TasksRepository.initialize(applicationContext); //applicationContext for long object life
    }
}