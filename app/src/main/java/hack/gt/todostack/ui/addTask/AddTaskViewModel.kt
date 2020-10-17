package hack.gt.todostack.ui.addTask

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hack.gt.todostack.data.TaskRepository
import hack.gt.todostack.data.model.Task

class AddTaskViewModel : ViewModel() {
    fun addTask(task: Task) {
        TaskRepository.addTask(task)
    }
}