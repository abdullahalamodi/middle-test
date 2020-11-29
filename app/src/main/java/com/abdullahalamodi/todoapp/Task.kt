package com.abdullahalamodi.todoapp

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Task(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    var name: String = "",
    var details: String = "",
    var date: Date = Date(),
    var period: Date = Date(),
    var state: Int = 1,
)