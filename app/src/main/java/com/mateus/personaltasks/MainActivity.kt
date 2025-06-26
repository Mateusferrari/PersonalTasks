package com.mateus.personaltasks.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.mateus.personaltasks.R
import com.mateus.personaltasks.controller.TaskAdapter
import com.mateus.personaltasks.databinding.ActivityMainBinding
import com.mateus.personaltasks.model.Task
import com.mateus.personaltasks.repository.FirebaseTaskRepository

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: TaskAdapter
    private val repository = FirebaseTaskRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        adapter = TaskAdapter(listOf()) { view, task ->
            showTaskOptions(view, task)
        }

        binding.recyclerViewTasks.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewTasks.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
            loadTasks()
        }
    }

    private fun loadTasks() {
        repository.getTasks {
            adapter.updateTasks(it)
        }
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
            R.id.menu_deleted_tasks -> {
                startActivity(Intent(this, DeletedTasksActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showTaskOptions(view: View, task: Task) {
        val popup = PopupMenu(this, view)
        popup.menuInflater.inflate(R.menu.menu_context, popup.menu)
        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_edit -> {
                    val intent = Intent(this, TaskFormActivity::class.java)
                    intent.putExtra("task", task)
                    startActivity(intent)
                    true
                }
                R.id.menu_delete -> {
                    repository.deleteTask(task, {
                        Toast.makeText(this, "Tarefa excluída", Toast.LENGTH_SHORT).show()
                        loadTasks()
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
