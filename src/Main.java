import java.util.List;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault(); // Получаем экземпляр менеджера
        HistoryManager historyManager = Managers.getDefaultHistory(); // Получаем экземпляр менеджера истории

        // Создаем несколько задач
        Task task1 = new Task("Покупки", "Купить молоко");
        Task task2 = new Task("Уборка", "Почистить кухню");
        Task task3 = new Task("Уроки", "Подготовить презентацию");

        // Добавляем задачи в менеджер
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);

        // Получаем идентификаторы задач
        String taskId1 = manager.getTaskId(task1);
        String taskId2 = manager.getTaskId(task2);
        String taskId3 = manager.getTaskId(task3);

        // Просматриваем задачи, добавляя их в историю просмотров
        Task viewedTask1 = manager.findTaskById(taskId1);
        Task viewedTask2 = manager.findTaskById(taskId2);
        Task viewedTask3 = manager.findTaskById(taskId3);

        // Добавляем задачи в историю просмотров
        if (viewedTask1 != null) {
            historyManager.add(viewedTask1);
        }
        if (viewedTask2 != null) {
            historyManager.add(viewedTask2);
        }
        if (viewedTask3 != null) {
            historyManager.add(viewedTask3);
        }

        // Получаем историю просмотров
        List<Task> history = historyManager.getHistory();

        // Выводим историю просмотров
        System.out.println("История просмотров:");
        for (Task task : history) {
            System.out.println(task.getTitle() + ": " + task.getDescription());
        }
    }
}
