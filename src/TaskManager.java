import java.util.List;

public interface TaskManager {
    void addTask(Task task);
    String getTaskId(Task task);
    List<Task> getHistory();
    Task findTaskById(String taskId);
    Subtask findSubtaskById(String subtaskId);
    //Epic findEpicById(String epicId);
    void removeById(String taskId);
    void removeAll(String taskType);
    void createSubtask(Subtask subtask);
    void addSubtaskToTask(Task task, Subtask subtask);
    void removeSubtaskFromTask(Task task, Subtask subtask);
    void createEpic(Epic epic);
    void printTasks();
    void printSubtasks();
    void printTasksWithSubtasks();
    void printEpics();
}