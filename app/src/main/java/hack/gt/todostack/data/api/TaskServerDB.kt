package hack.gt.todostack.data.api

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import hack.gt.todostack.data.TaskRepository
import hack.gt.todostack.data.model.Task
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException
import java.lang.reflect.Type


object TaskServerDB {
    val JSON: MediaType = "application/json; charset=utf-8".toMediaType()
    private val client = OkHttpClient()

    fun addTask(task: Task){
        val json = """{"title": "${task.title}", "completed": "${task.completed}", "deadline": "${task.deadline}","difficulty": "${task.difficulty}","estimatedTimeMinutes": "${task.estimatedTimeMinutes}", "estimatedTimeHours": "${task.estimatedTimeHours}", "id": "${task.id}", "importance": "${task.importance}"}""".trim()
        val request = Request.Builder()
            .url("http://flask-env.eba-jrfcemxe.us-east-1.elasticbeanstalk.com/add_tasks")
            .post(json.toRequestBody(JSON))
            .build()

        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: java.io.IOException) {
                println()
            }

            override fun onResponse(call: Call, response: Response) {
                println(response.body!!.string())
            }
        })
    }

    fun getAllTasks(callback: TaskRepository.GetTasks) {
        val request = Request.Builder()
            .url("http://flask-env.eba-jrfcemxe.us-east-1.elasticbeanstalk.com/get_all_tasks")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    val tasksJson = response.body!!.string()
                    val listOfTasks: Type =
                        object : TypeToken<ArrayList<Task>?>() {}.type

                    val tasks: List<Task> = Gson().fromJson(tasksJson, listOfTasks)
                    callback.onTasksLoaded(tasks)
                }
            }
        })
    }
}