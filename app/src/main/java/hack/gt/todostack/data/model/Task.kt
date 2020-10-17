package hack.gt.todostack.data.model

import android.graphics.Color
import com.google.gson.annotations.Expose
import java.time.Duration
import java.util.*

data class Task(
    var title: String,
    var deadline: String,
    var estimatedTimeHours: Int,
    var estimatedTimeMinutes: Int,
    var difficulty: Float = 0f,
    var importance: Float = 0f,
    var completed: Boolean = false
) {
    val id: String = UUID.randomUUID().toString()

    val color: Int = generateRandomColor()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        return id == (other as Task).id
    }

    override fun hashCode(): Int {
        return id.hashCode();
    }

    private fun generateRandomColor(): Int = with(Random()) {
        // This is the base color which will be mixed with the generated one
        val baseColor = Color.WHITE
        val baseRed = Color.red(baseColor)
        val baseGreen = Color.green(baseColor)
        val baseBlue = Color.blue(baseColor)
        val red: Int = (baseRed + this.nextInt(256)) / 2
        val green: Int = (baseGreen + this.nextInt(256)) / 2
        val blue: Int = (baseBlue + this.nextInt(256)) / 2
        return Color.rgb(red, green, blue)
    }
}