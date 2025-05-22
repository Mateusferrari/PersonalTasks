package com.mateus.personaltasks.controller

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mateus.personaltasks.R
import com.mateus.personaltasks.model.Task

class TaskAdapter(
    private var taskList: List<Task>,
    private val onItemLongClick: (View, Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textTitle: TextView = itemView.findViewById(R.id.textTitle)
        val textDescription: TextView = itemView.findViewById(R.id.textDescription)
        val textDeadline: TextView = itemView.findViewById(R.id.textDeadline)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        holder.textTitle.text = task.title
        holder.textDescription.text = task.description
        holder.textDeadline.text = "Data limite: ${task.deadline}"
        holder.itemView.setOnLongClickListener {
            onItemLongClick(it, task)
            true
        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    fun updateTasks(newTasks: List<Task>) {
        taskList = newTasks
        notifyDataSetChanged()
    }
}
