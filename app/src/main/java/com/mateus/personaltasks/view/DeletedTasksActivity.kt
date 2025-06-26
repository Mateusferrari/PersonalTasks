package com.mateus.personaltasks.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mateus.personaltasks.R
import com.mateus.personaltasks.controller.TaskAdapter
import com.mateus.personaltasks.databinding.ActivityDeletedTasksBinding
import com.mateus.personaltasks.model.Task
import com.mateus.personaltasks.repository.FirebaseTaskRepository

class DeletedTasksActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeletedTasksBinding
    private lateinit var adapter: TaskAdapter
    private val repository = FirebaseTaskRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeletedTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = TaskAdapter(listOf()) { view, task ->
            showDeletedTaskOptions(view, task)
        }

        binding.recyclerViewDeletedTasks.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewDeletedTasks.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        repository.getDeletedTasks {
            adapter.updateTasks(it)
        }
    }

    private fun showDeletedTaskOptions(view: View, task: Task) {
        val popup = PopupMenu(this, view)
        popup.menuInflater.inflate(R.menu.menu_deleted_context, popup.menu)
        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_restore -> {
                    repository.restoreTask(task, {
                        Toast.makeText(this, "Tarefa reativada", Toast.LENGTH_SHORT).show()
                        onResume()
                    }, {
                        Toast.makeText(this, "Erro: ${it.message}", Toast.LENGTH_SHORT).show()
                    })
                    true
                }
                R.id.menu_details -> {
                    val intent = Intent(this, TaskFormActivity::class.java)
                    intent.putExtra("task", task)
                    intent.putExtra("readOnly", true)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
        popup.show()
    }
}
