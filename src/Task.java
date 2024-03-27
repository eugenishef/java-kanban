import java.util.HashMap;

public class Task {
    private String id;
    private String title;
    private String description;
    private TaskStatus status;
    private HashMap<String, Subtask> subtasks;
    private static UUIDGenerator uuidGenerator = new UUIDGenerator();

    public Task(String title, String description) {
        this.id = uuidGenerator.generateUuid();
        this.title = title;
        this.description = description;
        this.status = TaskStatus.NEW;
        this.subtasks = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public void addSubtask(String subtaskId, Subtask subtask) {
        subtasks.put(subtaskId, subtask);
    }
    public HashMap<String, Subtask> getSubtasks() {
        return subtasks;
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

    @Override
    public String toString() {
        return "{" + "title='" + title + ", description='" + description + ", status=" + status + '}';
    }
}

enum TaskStatus {
    NEW, IN_PROGRESS, DONE
}