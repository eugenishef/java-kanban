public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();

        Task task_1 = new Task("Путешествие", "Купить билеты на самолет");
        Subtask subtask_1 = new Subtask("Выбрать город", "полет не дольше 3 суток");
        Task task_2 = new Task("Покормить кота", "В 14:00");
        Task task_3 = new Task("Зайти в магазин", "купить хлеб");
        Epic epic_1 = new Epic("Путешествие", "Организация путешествия");
        Epic epic_2 = new Epic("Домашние дела", "Повседневные обязанности");

        manager.addTask(task_1);
        manager.addTask(task_2);
        manager.addTask(task_3);

        manager.addSubtaskToTask(task_1.getId(), subtask_1);
        manager.getAllTasks();
        manager.getAllTasksWithSubtasks();

        epic_1.createTask(task_1);
        epic_1.addTask(task_2);
        epic_2.addTask(task_3);

        epic_1.addSubtaskToTask(task_1.getId(), subtask_1);

        epic_1.getAllTasks();
        epic_1.getAllTasksWithSubtasks();
    }
}
