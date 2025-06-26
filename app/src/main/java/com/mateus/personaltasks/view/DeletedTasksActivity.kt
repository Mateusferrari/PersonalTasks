import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mateus.personaltasks.R
import com.mateus.personaltasks.controller.TaskAdapter
import com.mateus.personaltasks.databinding.ActivityDeletedTasksBinding
import com.mateus.personaltasks.model.Task
import com.mateus.personaltasks.repository.FirebaseTaskRepository
import com.mateus.personaltasks.view.TaskFormActivity

class DeletedTasksActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeletedTasksBinding
    private lateinit var adapter: TaskAdapter
    private val repository = FirebaseTaskRepository()
    private var selectedTask: Task? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeletedTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = TaskAdapter(listOf()) { task ->
            selectedTask = task
            binding.recyclerViewDeletedTasks.showContextMenu()
        }

        binding.recyclerViewDeletedTasks.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewDeletedTasks.adapter = adapter
        registerForContextMenu(binding.recyclerViewDeletedTasks)
    }

    override fun onResume() {
        super.onResume()
        repository.getDeletedTasks {
            adapter.updateTasks(it)
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu_deleted_context, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_restore -> {
                selectedTask?.let {
                    repository.restoreTask(it, {
                        Toast.makeText(this, "Tarefa reativada", Toast.LENGTH_SHORT).show()
                        onResume()
                    }, {
                        Toast.makeText(this, "Erro: ${it.message}", Toast.LENGTH_SHORT).show()
                    })
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
