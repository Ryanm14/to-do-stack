package hack.gt.todostack.ui.addTask

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import hack.gt.todostack.R
import hack.gt.todostack.data.model.Task
import kotlinx.android.synthetic.main.fragment_add_task.*
import mobi.upod.timedurationpicker.TimeDurationPickerDialog
import java.text.SimpleDateFormat
import java.util.*


class AddTaskFragment : Fragment() {

    private lateinit var addTaskViewModel: AddTaskViewModel
    private val calDueDate = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addTaskViewModel =
            ViewModelProvider(this).get(AddTaskViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_add_task, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpDatePicker()
        addTaskButton.setOnClickListener {
            val title = taskTitleEditText.text.toString()
            val dueDate = taskDueDateEditText.text.toString()
            val durationHours = taskDurationHoursEditText.text.toString().toIntOrNull() ?: 0
            val durationMinutes = taskDurationMinutesEditText.text.toString().toIntOrNull() ?: 0
            val diff = taskDiffucultyBar.rating
            val importance = taskImportanceBar.rating

            val task = Task(title=title, deadline = dueDate, difficulty = diff, importance = importance, estimatedTimeHours = durationHours, estimatedTimeMinutes = durationMinutes)
            addTaskViewModel.addTask(task)
            Toast.makeText(context, "Task Was Added!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUpDatePicker() {
        val dateSetListener = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            calDueDate.set(Calendar.YEAR, year)
            calDueDate.set(Calendar.MONTH, monthOfYear)
            calDueDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            taskDueDateEditText.updateDueDateLabel(calDueDate)
        }

        taskDueDateEditText.setOnClickListener {
            DatePickerDialog(
                taskDueDateEditText.context,
                dateSetListener,
                calDueDate.get(Calendar.YEAR),
                calDueDate.get(Calendar.MONTH),
                calDueDate.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }
}

private fun TextInputEditText.updateDueDateLabel(calendar: Calendar) {
    val myFormat = "MM/dd/yy"
    val sdf = SimpleDateFormat(myFormat, Locale.US)
    setText(sdf.format(calendar.time))
}
