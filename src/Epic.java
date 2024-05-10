import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Epic extends ArrayList<Task> {
    private static UUIDGenerator uuidGenerator = new UUIDGenerator();
    private String id;
    private String title;
    private ArrayList<Task> tasks;
    private Duration duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    protected HashMap<String, Epic> epics = new HashMap<>();

    public Epic(String title, Task... tasks) {
        this.id = uuidGenerator.generateUuid();
        this.title = title;
        this.tasks = new ArrayList<>();
        this.tasks.addAll(Arrays.asList(tasks));
    }

    public String getId() {
        return this.id;
    }

    public void addTask(String epicId, Task task) {
        Epic epic = epics.get(epicId);

        if (epic != null) {
            epic.addTask(epicId, task);
        } else {
            System.out.println("Epic with ID " + epicId + " not found.");
        }

    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public String getTitle() {
        return this.title;
    }
    public void setTitle(String newTitle) {this.title = newTitle;}

    public Duration calculateDuration() {
        return this.stream()
                .map(task -> Duration.between(task.getStartTime(), task.getEndTime()))
                .reduce(Duration.ZERO, Duration::plus);
    }

    public LocalDateTime calculateStartTime() {
        return this.stream()
            .filter(task -> task instanceof Subtask)
            .map(task -> ((Subtask) task).getStartTime())
            .min(LocalDateTime::compareTo)
            .orElse(null);
    }

    public LocalDateTime calculateEndTime() {
        return this.stream()
            .filter(task -> task instanceof Subtask)
            .map(task -> ((Subtask) task).getEndTime())
            .max(LocalDateTime::compareTo)
            .orElse(null);
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{")
            .append("id='").append(id).append('\'')
            .append(", title='").append(title).append('\'')
            .append(", startTime=").append(calculateStartTime())
            .append(", duration=").append(calculateDuration())
            .append(", endTime=").append(calculateEndTime())
            .append(", tasks=").append(super.toString())
            .append('}');
        return stringBuilder.toString();
    }
}
