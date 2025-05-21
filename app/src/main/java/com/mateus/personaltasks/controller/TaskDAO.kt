package com.seunome.personaltasks.controller

import androidx.room.*
import com.seunome.personaltasks.model.Task

@Dao
interface TaskDAO {
    @Insert fun insert(task: Task)
    @Update fun update(task: Task)
    @Delete fun delete(task: Task)
    @Query("SELECT * FROM tasks ORDER BY deadline ASC")
    fun getAll(): List<Task>
}
