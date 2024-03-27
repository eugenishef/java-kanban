import java.util.List;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();

        Task task1 = new Task("Покупки", "Купить молоко");
        Task task2 = new Task("Уборка", "Почистить кухню");
        Task task3 = new Task("Уроки", "Подготовить презентацию");

        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);

        Task viewedTask1 = manager.findTaskById(manager.getTaskId(task1));
        Task viewedTask2 = manager.findTaskById(manager.getTaskId(task2));
        Task viewedTask3 = manager.findTaskById(manager.getTaskId(task3));

        if (viewedTask1 != null) {
            historyManager.add(viewedTask1);
        }
        if (viewedTask2 != null) {
            historyManager.add(viewedTask2);
        }
        if (viewedTask3 != null) {
            historyManager.add(viewedTask3);
        }

        List<Task> history = historyManager.getHistory();

        System.out.println("История просмотров:");
        for (Task task : history) {
            System.out.println(task.getTitle() + ": " + task.getDescription());
        }

        String taskId3 = manager.getTaskId(task3);
        historyManager.remove(taskId3);

        List<Task> updatedHistory = historyManager.getHistory();

        System.out.println("История просмотров после изменений:");
        for (Task task : updatedHistory) {
            System.out.println(task.getTitle() + ": " + task.getDescription());
        }

        ((InMemoryHistoryManager) historyManager).printNodeMap();
    }
}
