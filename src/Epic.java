import java.util.HashMap;

public class Epic {
    private String title;
    private Task mainTask;

    protected HashMap<String, Epic> localEpics = new HashMap<>();

    public Epic(String title, Task mainTask) {
        this.title = title;
        this.mainTask = mainTask;
    }

    public Task getEpicTask() {
        return mainTask;
    }

    public String getTitle() {
        return this.title;
    }

    @Override
    public String toString() {
        return "Epic: " + title + "\nMain Epic Task:" + mainTask;
    }
}
