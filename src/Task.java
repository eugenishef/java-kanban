import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;

public class Task {
    private String id;
    private String title;
    private String description;
    private TaskStatus status;
    private static UUIDGenerator uuidGenerator = new UUIDGenerator();
    private LocalDateTime startTime;
    private Duration duration;

    protected HashMap<String, Task> tasks = new HashMap<>();
    protected HashMap<String, Subtask> localTasksWithSubtasks = new HashMap<>();

    public Task(String title, String description, LocalDateTime startTime, long durationMinutes) {
        this.id = uuidGenerator.generateUuid();
        this.title = title;
        this.description = description;
        this.status = TaskStatus.NEW;
        this.startTime = startTime;
        this.duration = Duration.ofMinutes(durationMinutes);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(long durationMinutes) {
        this.duration = Duration.ofMinutes(durationMinutes);
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return this.status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public void createTask() {
        tasks.put(this.id, this);
    }

    public void changeTaskTitle(String value) {
        this.title = value;
    }

    public void changeTaskDescription(String value) {
        this.title = value;
    }

    public void removeTask(String taskId) {
        if (tasks.containsKey(taskId)) {
            tasks.remove(taskId);
        } else {
            throw new IllegalArgumentException("Task with id: " + taskId + " now found :(");
        }
    }

    public void addSubtask(String subtaskId, Subtask subtask) {
        localTasksWithSubtasks.put(subtaskId, subtask);
    }

    public HashMap<String, Subtask> getSubtasks() {
        return localTasksWithSubtasks;
    }

    public boolean hasSubtasks() {
        return !localTasksWithSubtasks.isEmpty();
    }

    public void printTasksWithSubtasks() {
        for (Task task : tasks.values()) {
            if (task.hasSubtasks() || !task.localTasksWithSubtasks.isEmpty()) {
                System.out.println(task);
            }
        }
    }

    public void printTasks() {
        for (Task task : tasks.values()) {
            System.out.println(task);
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{")
            .append("id: ").append(id)
            .append(", title='").append(title).append('\'')
            .append(", description='").append(description).append('\'')
            .append(", status=").append(status)
            .append(", startTime=").append(startTime)
            .append(", duration=").append(duration.toMinutes());

        if (!localTasksWithSubtasks.isEmpty()) {
            stringBuilder.append(", subtasks=[");
            for (Subtask subtask : localTasksWithSubtasks.values()) {
                stringBuilder.append(subtask.toString()).append(", ");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            stringBuilder.append("]");
        }

        stringBuilder.append("}");
        return stringBuilder.toString();
    }


    public enum TaskStatus {
        NEW, IN_PROGRESS, DONE
    }

}
