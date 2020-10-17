package hack.gt.todostack.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hack.gt.todostack.data.api.TaskServerDB
import hack.gt.todostack.data.model.Task

object TaskRepository {

    private val tasks: MutableList<Task> = mutableListOf()
    private val _tasksLiveData = MutableLiveData<List<Task>>().apply {
        value = emptyList()
    }

    var diffFirst = false
    var urgentFirst = false
    var importantFirst = false
    var easyFirst = false
    var effieiency = false

    var timePerDay: List<Float> = emptyList()

    private val getTasksCallback = object : GetTasks {
        override fun onTasksLoaded(t: List<Task>) {
            tasks.addAll(t)
            updateTasks()
        }
    }

    init {
        TaskServerDB.getAllTasks(getTasksCallback)
    }

    val tasksLiveData: LiveData<List<Task>> = _tasksLiveData

    fun addTask(task: Task) {
        tasks.add(task)
        updateTasks()
        TaskServerDB.addTask(task)
    }

    private fun updateTasks() {
        //TODO process and update sorting of tasks
        val todaysTasks = getTodaysTasks()
        _tasksLiveData.postValue(todaysTasks)
    }

    private fun getTodaysTasks(): List<Task> {
        return tasks.filter { !it.completed }
    }

    fun completeTask(completedTask: Task) {
        completedTask.completed = true
        TaskServerDB.completeTask(completedTask)
        _tasksLiveData.postValue(getTodaysTasks())
    }

    interface GetTasks {
        fun onTasksLoaded(tasks: List<Task>)
    }
}