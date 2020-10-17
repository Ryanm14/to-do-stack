package hack.gt.todostack.ui.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hack.gt.todostack.data.TaskRepository
import hack.gt.todostack.data.model.Task

class TasksViewModel : ViewModel() {
    fun completeTask(completedTask: Task) = TaskRepository.completeTask(completedTask)

    val tasks: LiveData<List<Task>> = TaskRepository.tasksLiveData
}