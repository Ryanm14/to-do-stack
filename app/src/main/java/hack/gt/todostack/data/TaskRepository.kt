package hack.gt.todostack.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hack.gt.todostack.data.api.TaskServerDB
import hack.gt.todostack.data.model.Task
import java.util.*

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
    var hoursWorkedToday = 0.0
    var today: Date = Date()


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

    private fun currentDayOfWeek(): Int { // should be static
        var calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        return (dayOfWeek - 1) % 7
    }

    @Suppress("DEPRECATION")
    class TimeIgnoringComparator : Comparator<Date?> {
        override fun compare(d1: Date?, d2: Date?): Int {
            if (d1 == null || d2 == null) return 0;
            var result = 0
            if (d1.year != d2.year) {
                result = d1.year - d2.year
            } else if (d1.month != d2.month) {
                result = d1.month - d2.month
            } else {
                result = d1.date - d2.date
            }
            return result
        }
    }

    private fun didDateChange(): Boolean {
        val comparator: TimeIgnoringComparator = TimeIgnoringComparator()
        var outdatedToday = today
        var rn = Date()
        // return today.compareTo(rn) == 0
        return comparator.compare(today, rn) != 0
    }

    class OrderByUrgency: Comparator<Task> {
        override fun compare(a: Task?, b: Task?): Int {
            if (a == null && b == null) return 0;
            if (a == null) return -1;
            if (b == null) return 1;

            var aDate = a.extractDate()
            var bDate = b.extractDate()

            if (aDate == null && bDate == null) return 0;
            if (aDate == null) return -1;
            if (bDate == null) return 1;

            var aDuration = a.calculateEstimatedTime()
            var bDuration = b.calculateEstimatedTime()

            var result = 0

            if (aDate.compareTo(bDate) != 0) {
                result = aDate.compareTo(bDate)
            } else if (a.importance != b.importance) {
                result = (a.importance - b.importance).toInt()
            } else if ((a.difficulty + 1) * aDuration != (b.difficulty + 1) * bDuration) {
                result = (((a.difficulty + 1) * aDuration) - ((b.difficulty + 1) * bDuration)).toInt() // do difficult tasks first
            } else {
                result = a.title.compareTo(b.title)
            }

            return aDate.compareTo(bDate)
        }
    }

    private fun getTodaysTasks(): List<Task> {
        val comparator: Comparator<Task> = OrderByUrgency()

        tasks.sortWith(comparator)
        // after tasks have been sorted, only show the ones you have time for
        var shownTasks: MutableList<Task> = tasks
        shownTasks.removeAll { it.completed }

        // need to keep track of how many tasks have been completed in order for this to work
        if (didDateChange()) { // these values are not stored persistently and will cause issues if you restart the app
            hoursWorkedToday = 0.0
            today = Date()
            println("Date changed!")
        }

        // var tasksCompleted = getTasksCompletedToday()
        val dayOfWeek = currentDayOfWeek()
        val maxHours = timePerDay.get(dayOfWeek).toDouble() //
        var totalHours = hoursWorkedToday // 0.0 // getDurationOfTasksCompletedToday
        var k = 0
        if (!shownTasks.isEmpty()) {
            var temp: MutableList<Task> = mutableListOf()
            while (k < shownTasks.size && totalHours < maxHours) {
                var task = shownTasks.get(k)
                var estimatedHours: Double = task.calculateEstimatedTime()
                temp.add(task)
                totalHours += estimatedHours
                k++
            }
            shownTasks = temp
        }

        // return tasks.filter { !it.completed }
        return shownTasks
    }

    fun completeTask(completedTask: Task) {
        completedTask.completed = true
        // update the total time worked today
        hoursWorkedToday += completedTask.calculateEstimatedTime()

        TaskServerDB.completeTask(completedTask)
        _tasksLiveData.postValue(getTodaysTasks())
    }

    interface GetTasks {
        fun onTasksLoaded(tasks: List<Task>)
    }
}