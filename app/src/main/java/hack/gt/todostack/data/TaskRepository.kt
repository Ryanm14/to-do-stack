package hack.gt.todostack.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hack.gt.todostack.data.model.Task

object TaskRepository {

    private val tasks: MutableList<Task> = mutableListOf()
    private val _tasksLiveData = MutableLiveData<List<Task>>().apply {
        value = emptyList()
    }

    val tasksLiveData: LiveData<List<Task>> = _tasksLiveData

    fun addTask(task: Task) {
        tasks.add(task)
        updateAddedTask()
    }

    private fun updateAddedTask() {
        //TODO process and update sorting of tasks
        val todaysTasks = tasks
        _tasksLiveData.postValue(todaysTasks)
    }

    fun completeTask(completedTask: Task) {
        completedTask.completed = true
        tasks.remove(completedTask)
        _tasksLiveData.postValue(tasks)
    }
}