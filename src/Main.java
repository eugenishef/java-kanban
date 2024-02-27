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

        //manager.updateTask("Путешествие","Арендовать транспорт", 1, 100); +
        //manager.removeAllTasks(); +
        //manager.getTaskById(1); +
        //manager.removeTaskById(2); +

        manager.addSubtaskToTask(task_1.getId(), subtask_1);
        manager.getAllTasks();
        manager.getAllTasksWithSubtasks();

        epic_1.createTask(task_1);
        epic_1.addTask(task_2);
        epic_2.addTask(task_3);

        epic_1.addSubtaskToTask(task_1.getId(), subtask_1);

        epic_1.getAllTasks();
        epic_1.getAllTasksWithSubtasks();



        //Возможность хранить задачи всех типов -
        //Получение списка всех задач + getAllTasks() (Задача + /Эпик/Подзадача)
        //Удаление всех задач + removeAllTasks()
        //Получение по идентификатору + getTaskById()
        //Создание. Сам объект должен передаваться в качестве параметра + addTask()
        //Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра + updateTask()
        //Удаление по идентификатору + removeTaskById()
        //Менеджер сам не выбирает статус для задачи +

    }
}
