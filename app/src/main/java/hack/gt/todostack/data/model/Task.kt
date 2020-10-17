package hack.gt.todostack.data.model

import java.time.Duration
import java.util.*

data class Task(
    var title: String,
    var deadline: Date,
    var estimatedTime: Duration,
    var difficulty: Int = 0,
    var importance: Int = 0
)