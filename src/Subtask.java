import java.util.HashMap;

public class Subtask extends Task {
    protected String id;
    protected TaskStatus status;
    private static UUIDGenerator uuidGenerator = new UUIDGenerator();

    protected HashMap<String, Subtask> subtasks = new HashMap<>();

    public Subtask(String title, String description) {
        super(title, description);
        this.id = uuidGenerator.generateUuid();
        this.status = TaskStatus.NEW;
    }

    public void createSubtask() {
        subtasks.put(this.id, subtasks.getOrDefault(this.id, this));
    }

    public String getId() {
        return this.id;
    }

    public void changeStatus(TaskStatus newStatus) {
        this.status = newStatus;
    }
}
