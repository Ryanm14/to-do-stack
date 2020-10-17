public class Task implements Comparable<Task> {
    public Date deadline; // Date class could be LocalDateTime or whatever floats your boat
    public String title;
    // private String description;
    public Duration estimatedTime; // could be an int in hours or whatever floats your boat
    public int difficulty; // out of 5
    public int importance; // out of 5
    // Category category;

    public Task(Date deadline, String title, Duration estimatedTime, int difficulty, int importance) {
        this.deadline = deadline;
        this.title = title;
        this.estimatedTime = estimatedTime;
        this.difficulty = difficulty;
        this.importance = importance;
    }

    public Task() {
        // rafhackgt2020
    }

    @Override
    public boolean equals(Object object) {
        boolean result = false;

        if (this == object) {
            result = true;
        } else if (object != null && object instanceof Task) {
            Task task = (Task) object;
            result = deadline.equals(task.deadline)
                && title.equals(task.title)
                && estimatedTime.equals(task.estimatedTime)
                && difficulty == task.difficulty
                && imporatance == task.importance;
        }

        return result;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public int compareTo(Task task) {
        return deadline.compareTo(task.deadline);
    }

    @Override
    public String toString() {
        String result = "";
        String DELIMINATOR = ", ";
        result += "title = " + title;
        result += DELIMINATOR;
        result += "due = " + deadline;
        result += DELIMINATOR;
        result += "estimatedTime = " + estimatedTime;
        result += DELIMINATOR;
        result += "difficulty = " + difficulty;
        result += DELIMINATOR;
        result += "importance = " + importance;
        return result;
    }
}
