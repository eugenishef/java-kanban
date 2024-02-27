import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Manager {
    private ArrayList<Task> tasks;
    private int taskCounter = 0;
    public Manager() {
        tasks = new ArrayList<>();
    }

    public void addTask(Task task) {
        taskCounter++;
        task.setId(taskCounter);
        task.setTaskStatus(TaskStatus.NEW);
        tasks.add(task);
    }

    public void addSubtaskToTask(int taskId, Subtask subtask) {
        Task parentTask = getTaskById(taskId);
        if (parentTask != null && parentTask instanceof Task) {
            ((Task) parentTask).addSubtask(subtask);
        } else {
            System.out.println("Ошибка: Задача с ID " + taskId + " не является задачей с подзадачами.");
        }
    }

    public Task getTaskById(int taskId) {
        for (Task task : tasks) {
            if (task.getId() == taskId) {
                // System.out.println("Найдена задача: " + task); DEBUG вывод
                return task;
            }
        }
        // System.out.println("Такой задачи не найдено"); DEBUG вывод
        return null;
    }

    public void updateTask(String newTitle, String newDescription, int oldId, int newId) {
        for (Task task : tasks) {
            if (task.getId() == oldId) {
                task.setTitle(newTitle);
                task.setDescription(newDescription);
                task.setId(newId);
                task.setTaskStatus(TaskStatus.NEW); // из подсказок про обновление статуса задачи
            }
        }
    }

    public void removeTaskById(int requiredId) {
        Task taskForDelete = getTaskById(requiredId);
        tasks.remove(taskForDelete);
        System.out.println("Задача с id: " + requiredId + " была удалена.");
        getAllTasks(); // вызов для демонстрации
    }

    public void removeAllTasks() {
        tasks.clear();
        if (!tasks.isEmpty()) {
            getAllTasks();
        }
        System.out.println("Задачи были удалены");
    }

    public void getAllTasks() {
        System.out.println("Список всех задач: ");
        for (Task task : tasks) {
            System.out.println(task);
        }
    }

    public void getAllTasksWithSubtasks() {
        System.out.println("Список всех задач с подзадачами: ");
        for (Task task : tasks) {
            for (Subtask subtask : ((Task) task).getSubtasks()) {
                System.out.println("Задача: " + task + " подзадача: " + subtask);
            }
        }
    }
}
