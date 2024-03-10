import java.util.Map;
import java.util.HashMap;
import java.util.UUID;
import java.util.Objects;

public class Manager {
    UUIDGenerator uuidGenerator;
    private Map<Task, String> taskIndex;
    private HashMap<String, Task> tasks;
    private HashMap<String, Subtask> subtasks;
    private HashMap<String, Epic> epics;
    public Manager() {
        uuidGenerator = new UUIDGenerator();
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
        taskIndex = new HashMap<>();
    }
    public void addTask(Task task) {
        String taskId = uuidGenerator.generateUuid();
        tasks.put(taskId, task);
        taskIndex.put(task, taskId);
    }

    public String getTaskId(Task task) {
        return taskIndex.get(task);
    }



    public Task findTaskById(String taskId) {
        return tasks.get(taskId);
    }

    public void removeById(String taskId) {
        Task removedTask = tasks.remove(taskId);
        if (removedTask != null) {
            System.out.println("Задача с id: " + taskId + " успешно удалена.");
        } else {
            System.out.println("Задача с id: " + taskId + " не найдена.");
        }
    }

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
                System.out.println("Произошла ошибка при очистке. Проверьте тип удаляемых данных tasks/subtasks/epic");
        }
    }

    public void createSubtask(Subtask subtask) {
        String subtaskId = uuidGenerator.generateUuid();
        subtasks.put(subtaskId, subtask);
    }

    public void addSubtaskToTask(Task task, Subtask subtask) {
        String subtaskId = UUID.randomUUID().toString();;
        task.addSubtask(subtaskId, subtask);
    }

    public void createEpic(Epic epic) {
        String epicId = uuidGenerator.generateUuid();
        epics.put(epicId, epic);
    }

    public void printTasks() {
        for (Map.Entry<String, Task> entry : tasks.entrySet()) {
            String taskId = entry.getKey();
            Task taskValue = entry.getValue();
            System.out.println(String.format("taskId: %s, taskDesc: %s", taskId, taskValue));
        }
    }

    public void printSubtasks() {
        for (Map.Entry<String, Subtask> entry : subtasks.entrySet()) {
            String subtaskId = entry.getKey();
            Subtask subtaskValue = entry.getValue();
            System.out.println(String.format("subtaskId: %s, subtaskDesc: %s", subtaskId, subtaskValue));
        }
    }

    public void printTasksWithSubtasks() {
        for (Map.Entry<String, Task> entry : tasks.entrySet()) {
            String taskId = entry.getKey();
            Task taskValue = entry.getValue();
                if (!taskValue.getSubtasks().isEmpty()) {
                    System.out.println("task with subtask:");
                    System.out.println(String.format("taskId: %s, taskDesc: %s", taskId, taskValue));
                    for (Map.Entry<String, Subtask> subtaskEntry : taskValue.getSubtasks().entrySet()) {
                        String subtaskId = subtaskEntry.getKey();
                        Subtask subtaskValue = subtaskEntry.getValue();
                        System.out.println(String.format("  subtaskId: %s, subtaskDesc: %s", subtaskId, subtaskValue));
                    }
                }
        }
    }

    public void printEpics() {
        for (Map.Entry<String, Epic> entry : epics.entrySet()) {
            String epicId = entry.getKey();
            Epic epicValue = entry.getValue();
            System.out.println(String.format("epicId: %s, epicDesc: %s, status: %s", epicId, epicValue.toString(), epicValue.getStatus()));
        }
    }
}
