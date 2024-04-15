import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends ArrayList<Task> {
    private String title;
    private Task mainTask;
    private ArrayList<Task> tasks;

    protected HashMap<String, Epic> localEpics = new HashMap<>();

    public Epic(String title, Task mainTask) {
        this.title = title;
        this.mainTask = mainTask;
        this.tasks = new ArrayList<>();
    }

    public void addTasks(ArrayList<Task> task) {
        this.tasks.addAll(tasks);
    }

    public Task getEpicTask() {
        return mainTask;
    }

    public String getTitle() {
        return this.title;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Epic Name: ").append(title).append("\nEpic Task: ").append(mainTask);
        for (Task task : tasks) {
            stringBuilder.append(task).append("\n");
        }
        return stringBuilder.toString();
    }

}
