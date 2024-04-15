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

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Epic Name: ").append(title).append("\nEpic Task: ").append(task);
        for (Task task : tasks) {
            stringBuilder.append(task).append("\n");
        }
        return stringBuilder.toString();
    }

}
