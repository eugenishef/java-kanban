import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {
  private TaskManager taskManager;

  @BeforeEach
  void setUp() {
    taskManager = new InMemoryTaskManager();
  }

  @Test
  void testAddTask() {
    Task task = new Task("Задача 1", "Описание");

    taskManager.addTask(task);

    assertFalse(taskManager.getTasks().isEmpty());
  }

  @Test
  void testGetTaskId() {
    Task task = new Task("Задача 1", "Описание");

    taskManager.addTask(task);

    String currentId = task.getId();

    assertEquals(task.getId(), currentId, "Значения id должны совпадать");
  }

  @Test
  void testGetTaskTitle() {
    Task task = new Task("Задача 1", "Описание");

    taskManager.addTask(task);

    String currentTitle = task.getTitle();

    assertEquals(task.getTitle(), currentTitle, "Title должны совпадать");
  }

  @Test
  void testGetTaskDescription() {
    Task task = new Task("Задача 1", "Описание");

    taskManager.addTask(task);

    String currentDescription = task.getDescription();

    assertEquals(task.getDescription(), currentDescription, "Description должны совпадать");
  }

  @Test
  void testChangeTaskStatus() {
    Task task = new Task("Задача 1", "Описание");

    taskManager.addTask(task);

    Task.TaskStatus defaultStatus = task.getStatus();

    taskManager.changeTaskStatus(task, Task.TaskStatus.IN_PROGRESS);

    Task.TaskStatus changedStatus  = task.getStatus();

    assertNotEquals(defaultStatus, changedStatus, "Статусы должны различаться");
  }

  @Test
  void testFindTaskById() {
    Task task = new Task("Задача 1", "Описание");

    taskManager.addTask(task);

    String requiredId = task.getId();

    Task result = taskManager.findTaskById(requiredId);

    assertNotNull(result, "Задача не найдена по id");
    assertEquals(task, result, "Найденная и добавленная задача должны совпадать");
  }

  @Test
  void testRemoveTaskById() {
    Task task_1 = new Task("Задача 1", "Описание");
    Task task_2 = new Task("Задача 2", "Описание");

    taskManager.addTask(task_1);
    taskManager.addTask(task_2);

    int lengthBefore = taskManager.getTasks().size();

    String taskToDelete = task_2.getId();

    taskManager.removeTaskById(taskToDelete);

    int lengthAfter = taskManager.getTasks().size();

    assertNotEquals(lengthBefore, lengthAfter, "Длинна tasks осталось без изменений, механизм удаления задачи не сработал");
  }

  @Test
  void testAddSubtask() {
    Subtask subtask = new Subtask("Подзадача", "К задаче");

    taskManager.addSubtask(subtask);

    assertFalse(taskManager.getSubtasks().isEmpty());
  }

  @Test
  void testAddSubtaskToTask() {
    Task task = new Task("Задача 1", "Описание");
    Subtask subtask = new Subtask("Подзадача", "К задаче");

    taskManager.addTask(task);
    taskManager.addSubtask(subtask);
    taskManager.attachSubtask(task, subtask);

    boolean hasSubtasks = taskManager.hasSubtasks(task, subtask);

    assertTrue(hasSubtasks);
  }

  @Test
  void testGetSubtaskId() {
    Subtask subtask = new Subtask("Подзадача", "К задаче");

    taskManager.addSubtask(subtask);

    String currentId = subtask.getId();

    assertEquals(subtask.getId(), currentId, "Значения id должны совпадать");
  }

  @Test
  void testGetSubtaskTitle() {
    Subtask subtask = new Subtask("Подзадача", "К задаче");

    taskManager.addSubtask(subtask);

    String currentTitle = subtask.getTitle();

    assertEquals(subtask.getTitle(), currentTitle, "Title должны совпадать");
  }

  @Test
  void testGetSubtaskDescription() {
    Subtask subtask = new Subtask("Подзадача", "К задаче");

    taskManager.addSubtask(subtask);

    String currentDescription = subtask.getDescription();

    assertEquals(subtask.getDescription(), currentDescription, "Description должны совпадать");
  }

  @Test
  void testFindSubtaskInTaskById() {
    Task task = new Task("Задача 1", "Описание");
    Subtask subtask = new Subtask("Подзадача", "К задаче");

    taskManager.addTask(task);
    taskManager.addSubtask(subtask);

    String taskId = task.getId();
    String subtaskId = task.getId();

    Subtask resultWhenSubtaskNotAddedToTask = taskManager.findSubtaskById(taskId, subtaskId);
    assertEquals(null, resultWhenSubtaskNotAddedToTask,"Как сюда попала подзадача?");

    taskManager.attachSubtask(task, subtask);
    Subtask resultWhenSubtaskAddedToTask = taskManager.findSubtaskById(taskId, subtaskId);
    //assertEquals(subtask, resultWhenSubtaskAddedToTask, "Подзадача не найдена в задаче");
  }

  @Test
  void addEpic() {
    Task task_1 = new Task("Задача 1", "Описание");
    Task task_2 = new Task("Задача 2", "Описание");
    Subtask subtask = new Subtask("Подзадача", "К задаче");
    Epic epic = new Epic("Эпический проект", task_1);

    taskManager.addTask(task_1);
    taskManager.addSubtask(subtask);
    taskManager.attachSubtask(task_1, subtask);

    taskManager.addEpic(epic);
    taskManager.printEpics();
  }
}
