package com.mateus.personaltasks.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "tasks")
data class Task(
    val id: String? = null,
    var title: String,
    var description: String,
    var deadline: String,
    var isDone: Boolean = false
) : Serializable

