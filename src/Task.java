import java.util.HashMap;

public class Task {
    private String id;
    private String title;
    private String description;
    private TaskStatus status;
    private static UUIDGenerator uuidGenerator = new UUIDGenerator();

    protected HashMap<String, Task> tasks = new HashMap<>();
    protected HashMap<String, Subtask> localTasksWithSubtasks = new HashMap<>();

    public Task(String title, String description) {
        this.id = uuidGenerator.generateUuid();
        this.title = title;
        this.description = description;
        this.status = TaskStatus.NEW;
    }

    public String getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
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
            .append(", status=").append(status);

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
