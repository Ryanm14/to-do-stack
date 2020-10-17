import java.util.LinkedList;
import java.util.List;

public class TaskStack {
    private List<Task> tasks;

    public TaskStack(List<Task> tasks) {
        this.tasks = tasks;
    }

    public TaskStack() {
        this(new LinkedList<>());
    }

    public Task getTasks() {
        return tasks;
    }
}
