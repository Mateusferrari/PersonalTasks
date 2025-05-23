package com.mateus.personaltasks.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.mateus.personaltasks.database.AppDatabase
import com.mateus.personaltasks.databinding.ActivityTaskFormBinding
import com.mateus.personaltasks.model.Task
import java.util.*

class TaskFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskFormBinding
    private var taskId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val receivedTask = intent.getSerializableExtra("task") as? Task
        val readOnly = intent.getBooleanExtra("readOnly", false)

        if (receivedTask != null) {
            taskId = receivedTask.id
            binding.editTitle.setText(receivedTask.title)
            binding.editDescription.setText(receivedTask.description)
            binding.editDeadline.setText(receivedTask.deadline)

            if (readOnly) {
                binding.editTitle.isEnabled = false
                binding.editDescription.isEnabled = false
                binding.editDeadline.isEnabled = false
                binding.buttonSave.visibility = View.GONE
            }
        }


        binding.editDeadline.setOnClickListener {
            showDatePicker()
        }


        binding.buttonCancel.setOnClickListener {
            finish()
        }


        binding.buttonSave.setOnClickListener {
            val title = binding.editTitle.text.toString()
            val description = binding.editDescription.text.toString()
            val deadline = binding.editDeadline.text.toString()

            val dao = AppDatabase.getDatabase(this).taskDao()

            if (taskId != null) {
                dao.update(Task(id = taskId!!, title = title, description = description, deadline = deadline))
            } else {
                dao.insert(Task(title = title, description = description, deadline = deadline))
            }

            finish()
        }

        if (!readOnly) {
            binding.editDescription.requestFocus()
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.editDescription, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun showDatePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, { _, y, m, d ->
            val date = String.format("%02d/%02d/%04d", d, m + 1, y)
            binding.editDeadline.setText(date)
        }, year, month, day)

        dpd.show()
    }
}
