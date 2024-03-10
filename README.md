### Описание

Структура классов:
- Task: Представляет основную задачу с заголовком и описанием.
- Subtask: Подзадачи, которые могут быть связаны с основной задачей.
- Manager: Класс для управления задачами и подзадачами.

### Примеры выводов в консоль

1. Задачи:

```
Task ID: e40c171d-8646-499a-b040-c5006a884b42, Description: {Title='Travel', Description='Buy airplane tickets, Status=NEW}
Task ID: 17f36d89-33f4-4d1d-b60c-f8ceacd5edd9, Description: {Title='Shopping', Description='Holiday pack, Status=NEW}
Task ID: 12e9d1ba-5ef6-4650-afe1-e7f409f8df69, Description: {Title='Household Chores', Description='Finish by 20:00, Status=NEW}
```

2. Подзадачи без привязки к задачам:
```
Subtask ID: 74050b93-690d-4193-8185-ab5e9f170b3c, Description: {Title='City', Description='Kazan, Status=NEW}
Subtask ID: c80270fa-a774-40b6-9b2b-eab437ec8ada, Description: {Title='Milk', Description='3%, Status=NEW}
```

3. Подзадачи связанные с задачей:
```
Task ID: 12e9d1ba-5ef6-4650-afe1-e7f409f8df69, Description: {Title='Household Chores', Description='Finish by 20:00, Status=NEW}
  Subtask ID: f0445735-4e98-454d-9167-fee5ea5e1717, Description: {Title='Hang', Description='Shelf, Status=NEW}
  Subtask ID: c3a41406-568a-4423-a4b2-8481add6611a, Description: {Title='Clean', Description='Fridge, Status=NEW}
```
4. Эпики:
```
epicId: 565d8fa3-658f-4669-ab1f-7e9c849b7bb6, epicDesc: Epic{title='Темы для спринта 3', mainTask={title='Первая тема', description='Сложные типы данных', status=NEW, subtasks={title='Важности типов в Java', description='Погрузимся глубже в тему', status=NEW}, {title='Введение в тему', description='Рассмотрим основные тезисы', status=NEW}, }}, status: IN_PROGRESS
```

### Методы печати:
1. printTasks - печатает задачи
2. printSubtasks - печатает подзадачи
3. printTasksWithSubtasks - печатает задачи у которых есть подзадачи
4. printEpics - печатает Эпики

### Пример использования

* Для запуска приложения, выполните метод main в классе Main.

> **Создание Задач**
> > ``` java
> > Task task_1 = new Task("Путешествие", "Купить билеты на самолет");
> > manager.addTask(task_1);

> **Создание подзадач без привязки к задаче**
> > ``` java
> > Subtask subtask_1 = new Subtask("Город", "Казань");
> > manager.createSubtask(subtask_1);

> **Создание подзадач в связке с задачью**
> > ``` java
> > Task task_3 = new Task("Домашние дела", "Сделать до 20:00");
> > Subtask subtask_3 = new Subtask("Почистить", "Холодильник");
> > manager.addSubtaskToTask(task_3, subtask_3);

> **Эпик**
> > ```java
> > Task task_5 = new Task("Первая тема", "Сложные типы данных");
> > Subtask subtask_5 = new Subtask("Введение в тему", "Рассмотрим основные тезисы");
> > Subtask subtask_6 = new Subtask("Важности типов в Java", "Погрузимся глубже в тему");
> > manager.createSubtask(subtask_5);
> > manager.createSubtask(subtask_6);
> > manager.addSubtaskToTask(task_5, subtask_5);
> > manager.addSubtaskToTask(task_5, subtask_6);
> > Epic epic = new Epic("Темы для спринта 3", task_5);
> > manager.createEpic(epic);

> **Очистка на примере Эпика**
> > ```java
> > manager.removeAll("epics");
> > manager.printEpics();

