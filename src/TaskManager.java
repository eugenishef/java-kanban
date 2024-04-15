import java.util.HashMap;
import java.util.List;

public interface TaskManager {
    HashMap<String, Task> getTasks();
    HashMap<String, Subtask> getSubtasks();
    void addTask(Task task);
    String getId(Task task);
    String getTitle(Task task);
    String getDescription(Task task);
    void changeTaskStatus(Task task, Task.TaskStatus newStatus);
    Task findTaskById(String id);
    void removeTaskById(String id);
    void addSubtask(Subtask subtask);
    void attachSubtask(Task task, Subtask subtask);
    boolean hasSubtasks(Task task, Subtask subtask);
    String getId(Subtask subtask);
    String getTitle(Subtask subtask);
    String getDescription(Subtask subtask);
    Subtask findSubtaskById(String taskId, String subtaskId);
    String getSubtaskIdFromTask(Task foundRecords);
    void removeSubtaskById(String subtaskId);
    void addEpic(Task... tasks);
    String getTitle(Epic epic);
    void removeEpicById(String id);
    void printTasks();
    void printSubtasks();
    void printTasksWithSubtasks();
    void printEpics();
    List<Task> getHistory();
    void updateHistory(Task task);
    void printHistory();
    void printFoundSubtask(Subtask subtask);
    void removeAll(String taskType);
}
