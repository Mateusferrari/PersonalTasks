package com.mateus.personaltasks.view

import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mateus.personaltasks.R
import com.mateus.personaltasks.controller.TaskAdapter
import com.mateus.personaltasks.database.AppDatabase
import com.mateus.personaltasks.databinding.ActivityMainBinding
import com.mateus.personaltasks.model.Task

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: TaskAdapter
    private lateinit var taskList: List<Task>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        taskList = AppDatabase.getDatabase(this).taskDao().getAll()

        adapter = TaskAdapter(taskList) { view, task ->
            selectedTask = task
            view.showContextMenu()
        }

        binding.recyclerViewTasks.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewTasks.adapter = adapter

        registerForContextMenu(binding.recyclerViewTasks)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_add -> {
                Toast.makeText(this, "Nova Tarefa clicada", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private var selectedTask: Task? = null

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu_context, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_edit -> {
                Toast.makeText(this, "Editar: ${selectedTask?.title}", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.menu_delete -> {
                Toast.makeText(this, "Excluir: ${selectedTask?.title}", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.menu_details -> {
                Toast.makeText(this, "Detalhes: ${selectedTask?.title}", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }



}
