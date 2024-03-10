import java.util.HashMap;

public class Epic {
    private String title;
    private Task mainTask;

    public Epic(String title, Task mainTask) {
        this.title = title;
        this.mainTask = mainTask;
    }

    public Task getMainTask() {
        return mainTask;
    }

    public TaskStatus getStatus() {
        for (Subtask subtask : mainTask.getSubtasks().values()) {
            if (subtask.getStatus() != TaskStatus.DONE) {
                return TaskStatus.IN_PROGRESS;
            }
        }
        return TaskStatus.DONE;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Epic{" +
                "title='" + title + '\'' +
                ", mainTask={" +
                "title='" + mainTask.getTitle() + '\'' +
                ", description='" + mainTask.getDescription() + '\'' +
                ", status=" + mainTask.getStatus() +
                ", subtasks=");
        for (Subtask subtask : mainTask.getSubtasks().values()) {
            sb.append("{title='" + subtask.getTitle() + '\'' +
                    ", description='" + subtask.getDescription() + '\'' +
                    ", status=" + subtask.getStatus() + "}, ");
        }
        sb.append("}}");
        return sb.toString();
    }
}
