import java.util.HashMap;

public class Task {
    private String title;
    private String description;
    private TaskStatus status;
    private HashMap<String, Subtask> subtasks;

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.status = TaskStatus.NEW;
        this.subtasks = new HashMap<>();
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
