import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

public class InMemoryTaskManager implements TaskManager {
    private UUIDGenerator uuidGenerator;
    private Map<String, Task> tasks;
    private Map<String, Subtask> subtasks;
    private Map<String, Epic> epics;
    private Map<Task, String> taskIndex;
    private LinkedList<Task> history;

    public InMemoryTaskManager() {
        uuidGenerator = new UUIDGenerator();
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
        taskIndex = new HashMap<>();
        history = new LinkedList<>();
    }

    @Override
    public void addTask(Task task) {
        String taskId = task.getId();
        tasks.put(taskId, task);
        taskIndex.put(task, taskId);
    }

    @Override
    public String getTaskId(Task task) {
        return taskIndex.get(task);
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }

    private void updateHistory(Task task) {
        if (history.size() >= 10) {
            history.removeFirst();
        }
        history.addLast(task);
    }

    @Override
    public Task findTaskById(String taskId) {
        Task task = tasks.get(taskId);
        if (task != null) {
            updateHistory(task);
        }
        return task;
    }

    @Override
    public Subtask findSubtaskById(String subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);
        if (subtask != null) {
            updateHistory(subtask);
        }
        return subtask;
    }

    @Override
    public void removeById(String taskId) {
        Task removedTask = tasks.remove(taskId);
        if (removedTask != null) {
            System.out.println("Задача с id: " + taskId + " удалена");
        } else {
            System.out.println("Задача с id: " + taskId + " не найдена");
        }
    }

    @Override
    public void removeAll(String taskType) {
        switch (taskType.toLowerCase()) {
            case "tasks":
                tasks.clear();
                System.out.println("Задачи очищены!");
                break;
            case "subtasks":
                subtasks.clear();
                System.out.println("Подзадачи очищены!");
                break;
            case "epics":
                epics.clear();
                System.out.println("Эпики очищены!");
                break;
            default:
                System.out.println("Произошла ошибка, используйте слова (tasks/subtasks/epic).");
        }
    }

    @Override
    public void createSubtask(Subtask subtask) {
        String subtaskId = uuidGenerator.generateUuid();
        subtasks.put(subtaskId, subtask);
    }

    @Override
    public void addSubtaskToTask(Task task, Subtask subtask) {
        String subtaskId = UUID.randomUUID().toString();
        task.addSubtask(subtaskId, subtask);
    }

    @Override
    public void removeSubtaskFromTask(Task task, Subtask subtask) {
        if (task == null || subtask == null) {
            System.out.println("Ошибка: задача или подзадача не существует.");
            return;
        }

        String subtaskId = getSubtaskIdFromTask(task, subtask);
        if (subtaskId == null) {
            System.out.println("Ошибка: подзадача не найдена в задаче.");
            return;
        }

        task.getSubtasks().remove(subtaskId);
        System.out.println("Подзадача успешно удалена из задачи.");
    }

    private String getSubtaskIdFromTask(Task task, Subtask subtask) {
        for (Map.Entry<String, Subtask> entry : task.getSubtasks().entrySet()) {
            if (entry.getValue().equals(subtask)) {
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    public void createEpic(Epic epic) {
        String epicId = uuidGenerator.generateUuid();
        epics.put(epicId, epic);
    }

    @Override
    public void printTasks() {
        for (Map.Entry<String, Task> entry : tasks.entrySet()) {
            String taskId = entry.getKey();
            Task taskValue = entry.getValue();
            System.out.println(String.format("TaskId: %s, TaskDesc: %s", taskId, taskValue));
        }
    }

    @Override
    public void printSubtasks() {
        for (Map.Entry<String, Subtask> entry : subtasks.entrySet()) {
            String subtaskId = entry.getKey();
            Subtask subtaskValue = entry.getValue();
            System.out.println(String.format("SubtaskId: %s, SubtaskDesc: %s", subtaskId, subtaskValue));
        }
    }

    @Override
    public void printTasksWithSubtasks() {
        for (Map.Entry<String, Task> entry : tasks.entrySet()) {
            String taskId = entry.getKey();
            Task taskValue = entry.getValue();
            if (!taskValue.getSubtasks().isEmpty()) {
                System.out.println("Задача с подзадачей:");
                System.out.println(String.format("TaskId: %s, TaskDesc: %s", taskId, taskValue));
                for (Map.Entry<String, Subtask> subtaskEntry : taskValue.getSubtasks().entrySet()) {
                    String subtaskId = subtaskEntry.getKey();
                    Subtask subtaskValue = subtaskEntry.getValue();
                    System.out.println(String.format("  SubtaskId: %s, SubtaskDesc: %s", subtaskId, subtaskValue));
                }
            }
        }
    }

    @Override
    public void printEpics() {
        for (Map.Entry<String, Epic> entry : epics.entrySet()) {
            String epicId = entry.getKey();
            Epic epicValue = entry.getValue();
            System.out.println(String.format("EpicId: %s, EpicDesc: %s, Status: %s", epicId, epicValue.toString(), epicValue.getStatus()));
        }
    }
}