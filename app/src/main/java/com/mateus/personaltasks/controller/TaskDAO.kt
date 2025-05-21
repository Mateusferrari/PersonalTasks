package com.mateus.personaltasks.controller

import androidx.room.*
import com.mateus.personaltasks.model.Task

@Dao
interface TaskDAO {
    @Insert fun insert(task: Task)
    @Update fun update(task: Task)
    @Delete fun delete(task: Task)
    @Query("SELECT * FROM tasks ORDER BY deadline ASC")
    fun getAll(): List<Task>
}
