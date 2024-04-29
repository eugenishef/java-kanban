import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends ArrayList<Task> {
    private String id;
    private String title;
    private Task task;
    private ArrayList<Task> tasks;
    private static UUIDGenerator uuidGenerator = new UUIDGenerator();

    protected HashMap<String, Epic> epics = new HashMap<>();

    public Epic(String title, Task task) {
        this.id = uuidGenerator.generateUuid();
        this.title = title;
        this.task = task;
        this.tasks = new ArrayList<>();
    }

    public String getId() {
        return this.id;
    }

    public void addTasks(ArrayList<Task> task) {
        this.tasks.addAll(task);
    }

    public String getTitle() {
        return this.title;
    }

    public LocalDateTime getStartTime() {
        return this.stream()
            .filter(task -> task instanceof Subtask)
            .map(task -> ((Subtask) task).getStartTime())
            .min(LocalDateTime::compareTo)
            .orElse(null);
    }

    public Duration getDuration() {
        return this.stream()
            .filter(task -> task instanceof Subtask)
            .map(task -> ((Subtask) task).getDuration())
            .reduce(Duration.ZERO, Duration::plus);
    }

    public LocalDateTime getEndTime() {
        return this.stream()
            .filter(task -> task instanceof Subtask)
            .map(task -> ((Subtask) task).getEndTime())
            .max(LocalDateTime::compareTo)
            .orElse(null);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Epic{")
            .append("id='").append(id).append('\'')
            .append(", title='").append(title).append('\'')
            .append(", startTime=").append(getStartTime())
            .append(", duration=").append(getDuration().toMinutes()).append(" minutes")
            .append(", endTime=").append(getEndTime())
            .append(", tasks=").append(super.toString())
            .append('}');
        return stringBuilder.toString();
    }
}
