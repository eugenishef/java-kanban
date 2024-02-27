import java.util.ArrayList;

public class Subtask extends Task {
    private int subtaskId;
    private String subtaskDescription;
    private TaskStatus subtaskStatus;
    private Epic epic;
    public Subtask(String title, String description) {
        super(title, description);
    }
    public int getSubtaskId() { return this.subtaskId; }
    public void setSubtaskId(int newId) { this.subtaskId = newId; }
    public String getSubtaskDescription() { return this.subtaskDescription; }
    public void setSubtaskDescription(String newDescription) { this.subtaskDescription = newDescription; }
    public TaskStatus getSubtaskStatus() { return subtaskStatus; }
    public void setSubtaskStatus(TaskStatus newStatus) { this.subtaskStatus = newStatus; }
    public Epic getEpic() { return epic; }
    public void setEpic(Epic epic) { this.epic = epic; }
}
