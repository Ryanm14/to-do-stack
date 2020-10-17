package hack.gt.todostack.ui.tasks

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import hack.gt.todostack.R
import hack.gt.todostack.data.TaskRepository
import hack.gt.todostack.data.model.Task
import kotlinx.android.synthetic.main.fragment_tasks.*
import java.util.*

class TasksFragment : Fragment(), CardStackListener {

    private lateinit var tasksViewModel: TasksViewModel
    private val cardStackAdapter = CardStackAdapter()
    private var tasks: List<Task> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tasksViewModel =
            ViewModelProvider(this).get(TasksViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_tasks, container, false)

        return root
    }

    override fun onResume() {
        super.onResume()
        updateRepo()
    }

    private fun updateRepo() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val list = mutableListOf<Int>()
        list.add(sharedPreferences.getInt("sunday_time", 1))
        list.add(sharedPreferences.getInt("monday_time", 1))
        list.add(sharedPreferences.getInt("tuesday_time", 1))
        list.add(sharedPreferences.getInt("wednesday_time", 1))
        list.add(sharedPreferences.getInt("thursday_time", 1))
        list.add(sharedPreferences.getInt("friday_time", 1))
        list.add(sharedPreferences.getInt("saturday_time", 1))
        TaskRepository.timePerDay = list
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cardStackView.layoutManager = CardStackLayoutManager(context, this)
        cardStackView.adapter = cardStackAdapter

        tasksViewModel.tasks.observe(viewLifecycleOwner, Observer {
            updateTasks(it)
        })
    }

    private fun updateTasks(tasks: List<Task>) {
        val estimatedTotal = tasks.sumBy { it.estimatedTimeHours * 60 + it.estimatedTimeMinutes }
        val estimatedHours = estimatedTotal / 60
        val estimatedMinutes = estimatedTotal % 60
        estimatedTimeTextView.text =
            "Estimated Time Left: $estimatedHours Hours and $estimatedMinutes Minutes"
        tasksLeftTextView.text = "Remaining Today: ${tasks.size}"

        if (this.tasks.isEmpty() && tasks.isNotEmpty()) {
            cardStackAdapter.setTasks(tasks.toList())
        }
        this.tasks = tasks
    }

    class CardStackAdapter(
        private var tasks: List<Task> = emptyList()
    ) : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return ViewHolder(inflater.inflate(R.layout.item_task, parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(tasks[position])
        }

        override fun getItemCount(): Int {
            return tasks.size
        }

        fun setTasks(tasks: List<Task>) {
            this.tasks = tasks
            notifyDataSetChanged()
        }

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            fun bind(task: Task) {
                val taskTitleTextView = itemView.findViewById<TextView>(R.id.taskItemTitle)
                val cardBackground = itemView.findViewById<CardView>(R.id.cardBackground)
                cardBackground.setBackgroundColor(task.color)
                taskTitleTextView.text = task.title
            }
        }

    }

    override fun onCardDisappeared(view: View?, position: Int) {
        val completedTask = tasks.first{!it.completed}
        tasksViewModel.completeTask(completedTask)
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {

    }

    override fun onCardSwiped(direction: Direction?) {

    }

    override fun onCardCanceled() {

    }

    override fun onCardAppeared(view: View?, position: Int) {

    }

    override fun onCardRewound() {
    }
}