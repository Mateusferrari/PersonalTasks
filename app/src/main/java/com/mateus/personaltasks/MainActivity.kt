package com.mateus.personaltasks.view

import android.content.Intent
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
    private var selectedTask: Task? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        adapter = TaskAdapter(listOf()) { view, task ->
            selectedTask = task
            view.showContextMenu()
        }

        binding.recyclerViewTasks.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewTasks.adapter = adapter

        registerForContextMenu(binding.recyclerViewTasks)
    }

    override fun onResume() {
        super.onResume()
        val tasks = AppDatabase.getDatabase(this).taskDao().getAll()
        adapter.updateTasks(tasks)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_add -> {
                startActivity(Intent(this, TaskFormActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu_context, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_edit -> {
                selectedTask?.let {
                    val intent = Intent(this, TaskFormActivity::class.java)
                    intent.putExtra("task", it)
                    startActivity(intent)
                }
                true
            }
            R.id.menu_delete -> {
                selectedTask?.let {
                    AppDatabase.getDatabase(this).taskDao().delete(it)
                    adapter.updateTasks(AppDatabase.getDatabase(this).taskDao().getAll())
                }
                true
            }
            R.id.menu_details -> {
                selectedTask?.let {
                    val intent = Intent(this, TaskFormActivity::class.java)
                    intent.putExtra("task", it)
                    intent.putExtra("readOnly", true)
                    startActivity(intent)
                }
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }
}
