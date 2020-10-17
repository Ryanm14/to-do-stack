package hack.gt.todostack.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hack.gt.todostack.data.model.Task

object TaskRepository {

    private val tasks: MutableList<Task> = mutableListOf()
    private val _tasksLiveData = MutableLiveData<List<Task>>().apply {
        value = tasks
    }

    public val tasksLiveData: LiveData<List<Task>> = _tasksLiveData

    fun addTask(task: Task) {
        tasks.add(task)
        _tasksLiveData.postValue(tasks)
    }
}