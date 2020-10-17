package hack.gt.todostack.data.model

import java.time.Duration
import java.util.*

data class Task(
    var title: String,
    var deadline: String,
    var estimatedTimeHours: Int,
    var estimatedTimeMinutes: Int,
    var difficulty: Float = 0f,
    var importance: Float = 0f
)