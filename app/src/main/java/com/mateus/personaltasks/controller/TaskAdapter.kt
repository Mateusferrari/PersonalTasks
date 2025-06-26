package com.mateus.personaltasks.controller

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mateus.personaltasks.databinding.ItemTaskBinding
import com.mateus.personaltasks.model.Task

class TaskAdapter(
    private var tasks: List<Task>,
    private val onTaskOptionsClick: (View, Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount(): Int = tasks.size

    fun updateTasks(newTasks: List<Task>) {
        tasks = newTasks
        notifyDataSetChanged()
    }

    inner class TaskViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.textTitle.text = task.title
            binding.textDeadline.text = task.deadline
            binding.textStatus.text = if (task.isDone) "✅ Cumprida" else "🕗 Pendente"

            binding.root.setOnLongClickListener {
                onTaskOptionsClick(it, task)
                true
            }

            binding.root.setOnClickListener(null) // desativa clique simples
        }
    }
}
