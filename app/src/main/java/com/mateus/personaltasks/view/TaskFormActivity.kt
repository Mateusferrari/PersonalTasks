package com.mateus.personaltasks.view

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mateus.personaltasks.databinding.ActivityTaskFormBinding
import java.util.*

class TaskFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

            finish()
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
