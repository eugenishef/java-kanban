public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();
        //создаем задачи
        Task task_1 = new Task("Путешествие", "Купить билеты на самолет");
        Task task_2 = new Task("Покупки", "Набор для праздника");
        Task task_3 = new Task("Домашние дела", "Сделать до 20:00");
        //добавляем задачи в tasks
        manager.addTask(task_1);
        manager.addTask(task_2);
        manager.addTask(task_3);
        //печатаем задачи
        manager.printTasks();
        //создаем подзадачи без привязки к задачам
        Subtask subtask_1 = new Subtask("Город", "Казань");
        Subtask subtask_2 = new Subtask("Молоко", "3%");
        //добавляем подзадачи в subtasks
        manager.createSubtask(subtask_1);
        manager.createSubtask(subtask_2);
        //печатаем подзадачи
        manager.printSubtasks();
        //создаем подзадачи которые будут частью задач
        Subtask subtask_3 = new Subtask("Почистить", "Холодильник");
        Subtask subtask_4 = new Subtask("Повесить", "Полку");
        //добавляем подзадачи к задачам
        manager.addSubtaskToTask(task_3, subtask_3);
        manager.addSubtaskToTask(task_3, subtask_4);
        //печатаем задачи у которых есть подзадачи
        manager.printTasksWithSubtasks();
        //создаем эпик
        Task task_5 = new Task("Первая тема", "Сложные типы данных");
        Subtask subtask_5 = new Subtask("Введение в тему", "Рассмотрим основные тезисы");
        Subtask subtask_6 = new Subtask("Важности типов в Java", "Погрузимся глубже в тему");
        manager.createSubtask(subtask_5);
        manager.createSubtask(subtask_6);
        manager.addSubtaskToTask(task_5, subtask_5);
        manager.addSubtaskToTask(task_5, subtask_6);
        Epic epic = new Epic("Темы для спринта 3", task_5);
        manager.createEpic(epic);
        manager.printEpics();
        //удаляем все задачи
        manager.removeAll("epics");
        manager.printEpics();
        //занимаемся поиском
        Task task_6 = new Task("Прогулка в лесу", "Возьми лукошко и набери грибы");
        manager.addTask(task_6);
        String taskId = manager.getTaskId(task_6);
        System.out.println("find task with id: " + taskId);
        //находим задачу по индексу
        Task findElement = manager.findTaskById(taskId);
        System.out.println(findElement);
        //удаляем задачу
        manager.removeById(taskId);
        manager.printTasks();
    }
}
