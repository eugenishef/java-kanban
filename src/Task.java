import java.util.ArrayList;

public class Task {
    private String title;
    private String description;
    private int taskId;
    private TaskStatus status;
    private ArrayList<Subtask> subtasks;

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.subtasks = new ArrayList<>();
    }

    public String getTitle() { return title; }
    public void setTitle(String newTitle) { this.title = newTitle; }
    public String getDescription() { return description; }
    public void setDescription(String newDescription) { this.description = newDescription; }
    public int getId() { return this.taskId; }
    public void setId(int newId) { this.taskId = newId; }
    public TaskStatus getTaskStatus() { return this.status; }
    public void setTaskStatus(TaskStatus newStatus) { this.status = newStatus; }
    public void addSubtask(Subtask subtask) { subtasks.add(subtask); }
    public ArrayList<Subtask> getSubtasks() {return subtasks;}

    @Override
    public String toString() {
        return "{" + "title='" + title + '\'' + ", description='" + description + '\'' + ", taskId=" + taskId + ", status=" + status + '}';
    }
}

enum TaskStatus {
    NEW, IN_PROGRESS, DONE
}
