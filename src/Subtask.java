public class Subtask extends Task {
    private TaskStatus status;
    public Subtask(String title, String description) {
        super(title, description);
        this.status = TaskStatus.NEW;
    }
}