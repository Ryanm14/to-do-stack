package hack.gt.todostack.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hack.gt.todostack.data.model.Task

object TaskRepository {

    private val tasks: List<Task> = emptyList()
    private val _tasksLiveData = MutableLiveData<List<Task>>().apply {
        value = tasks
    }
    private val tasksLiveData: LiveData<List<Task>> = _tasksLiveData

    fun getTasks() = _tasksLiveData
}