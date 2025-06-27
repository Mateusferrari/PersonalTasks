package com.mateus.personaltasks.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mateus.personaltasks.databinding.ActivityTaskFormBinding
import com.mateus.personaltasks.model.Task
import com.mateus.personaltasks.repository.FirebaseTaskRepository
import java.util.*

class TaskFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskFormBinding
    private var currentTask: Task? = null
    private val repository = FirebaseTaskRepository()
    private val priorities = listOf("Alta", "Média", "Baixa")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, priorities)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerPriority.adapter = adapter

        val receivedTask = intent.getSerializableExtra("task") as? Task
        val readOnly = intent.getBooleanExtra("readOnly", false)

        if (receivedTask != null) {
            currentTask = receivedTask
            binding.editTitle.setText(receivedTask.title)
            binding.editDescription.setText(receivedTask.description)
            binding.editDeadline.setText(receivedTask.deadline)
            binding.checkIsDone.isChecked = receivedTask.isDone
            binding.spinnerPriority.setSelection(priorities.indexOf(receivedTask.prioridade))

            if (readOnly) {
                binding.editTitle.isEnabled = false
                binding.editDescription.isEnabled = false
                binding.editDeadline.isEnabled = false
                binding.checkIsDone.isEnabled = false
                binding.spinnerPriority.isEnabled = false
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
            val isDone = binding.checkIsDone.isChecked
            val prioridade = binding.spinnerPriority.selectedItem.toString()

            if (currentTask != null) {
                val updated = currentTask!!.copy(
                    title = title,
                    description = description,
                    deadline = deadline,
                    isDone = isDone,
                    prioridade = prioridade
                )
                repository.updateTask(updated, {
                    Toast.makeText(this, "Atualizada com sucesso", Toast.LENGTH_SHORT).show()
                    finish()
                }, {
                    Toast.makeText(this, "Erro: ${it.message}", Toast.LENGTH_SHORT).show()
                })
            } else {
                val newTask = Task(
                    title = title,
                    description = description,
                    deadline = deadline,
                    isDone = isDone,
                    prioridade = prioridade
                )
                repository.addTask(newTask, {
                    Toast.makeText(this, "Tarefa salva", Toast.LENGTH_SHORT).show()
                    finish()
                }, {
                    Toast.makeText(this, "Erro: ${it.message}", Toast.LENGTH_SHORT).show()
                })
            }
        }

        if (!readOnly) {
            binding.editTitle.requestFocus()
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.editTitle, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun showDatePicker() {
        val c = Calendar.getInstance()
        val dpd = DatePickerDialog(this, { _, y, m, d ->
            val date = String.format("%02d/%02d/%04d", d, m + 1, y)
            binding.editDeadline.setText(date)
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))
        dpd.show()
    }
}
