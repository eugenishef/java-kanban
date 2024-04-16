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
    Task task = new Task("Make a report", "Collect data and prepare a report on the project");

    taskManager.addTask(task);

    assertFalse(taskManager.getTasks().isEmpty());
  }

  @Test
  void testGetTaskId() {
    Task task = new Task("Make a report", "Collect data and prepare a report on the project");

    taskManager.addTask(task);

    String currentId = task.getId();

    assertEquals(task.getId(), currentId, "The id values must be match");
  }

  @Test
  void testGetTaskTitle() {
    Task task = new Task("Make a report", "Collect data and prepare a report on the project");

    taskManager.addTask(task);

    String currentTitle = task.getTitle();

    assertEquals(task.getTitle(), currentTitle, "Title values must be match");
  }

  @Test
  void testGetTaskDescription() {
    Task task = new Task("Make a report", "Collect data and prepare a report on the project");

    taskManager.addTask(task);

    String currentDescription = task.getDescription();

    assertEquals(task.getDescription(), currentDescription, "Description values must be match");
  }

  @Test
  void testChangeTaskStatus() {
    Task task = new Task("Make a report", "Collect data and prepare a report on the project");

    taskManager.addTask(task);

    Task.TaskStatus defaultStatus = task.getStatus();

    taskManager.changeTaskStatus(task, Task.TaskStatus.IN_PROGRESS);

    Task.TaskStatus changedStatus  = task.getStatus();

    assertNotEquals(defaultStatus, changedStatus, "The statuses should be different");
  }

  @Test
  void testFindTaskById() {
    Task task = new Task("Make a report", "Collect data and prepare a report on the project");

    taskManager.addTask(task);

    String requiredId = task.getId();

    Task result = taskManager.findTaskById(requiredId);

    assertNotNull(result, "The task was not found by id");
    assertEquals(task, result, "The found and added task must match");
  }

  @Test
  void testRemoveTaskById() {
    Task task_1 = new Task("Make a report", "Collect data and prepare a report on the project");
    Task task_2 = new Task("Prepare materials", "Collect all the necessary documents");

    taskManager.addTask(task_1);
    taskManager.addTask(task_2);

    int lengthBefore = taskManager.getTasks().size();

    String taskToDelete = task_2.getId();

    taskManager.removeTaskById(taskToDelete);

    int lengthAfter = taskManager.getTasks().size();

    assertNotEquals(lengthBefore, lengthAfter, "The length of tasks remained unchanged, the task deletion mechanism did not work");
  }

  @Test
  void testAddSubtask() {
    Subtask subtask = new Subtask("Call technical support", "Find out the courier's full name and number");

    taskManager.addSubtask(subtask);

    assertFalse(taskManager.getSubtasks().isEmpty());
  }

  @Test
  void testAddSubtaskToTask() {
    Task task = new Task("Make a report", "Collect data and prepare a report on the project");
    Subtask subtask = new Subtask("Call technical support", "Find out the courier's full name and number");

    taskManager.addTask(task);
    taskManager.addSubtask(subtask);
    taskManager.attachSubtask(task, subtask);

    boolean hasSubtasks = taskManager.hasSubtasks(task, subtask);

    assertTrue(hasSubtasks);
  }

  @Test
  void testGetSubtaskId() {
    Subtask subtask = new Subtask("Call technical support", "Find out the courier's full name and number");

    taskManager.addSubtask(subtask);

    String currentId = subtask.getId();

    assertEquals(subtask.getId(), currentId, "The ID values must be match");
  }

  @Test
  void testGetSubtaskTitle() {
    Subtask subtask = new Subtask("Call technical support", "Find out the courier's full name and number");

    taskManager.addSubtask(subtask);

    String currentTitle = subtask.getTitle();

    assertEquals(subtask.getTitle(), currentTitle, "Title values must be match");
  }

  @Test
  void testGetSubtaskDescription() {
    Subtask subtask = new Subtask("Call technical support", "Find out the courier's full name and number");

    taskManager.addSubtask(subtask);

    String currentDescription = subtask.getDescription();

    assertEquals(subtask.getDescription(), currentDescription, "Description values must be match");
  }

  @Test
  void testFindSubtaskInTaskById() {
    Task task = new Task("Make a report", "Collect data and prepare a report on the project");
    Subtask subtask = new Subtask("Call technical support", "Find out the courier's full name and number");

    taskManager.addTask(task);
    taskManager.addSubtask(subtask);

    String taskId = task.getId();
    String subtaskId = task.getId();

    Subtask resultWhenSubtaskNotAddedToTask = taskManager.findSubtaskById(taskId, subtaskId);
    assertEquals(null, resultWhenSubtaskNotAddedToTask,"How did the subtask get here?");
  }

  @Test
  void addEpic() {
    Task task_1 = new Task("Make a report", "Collect data and prepare a report on the project");
    Subtask subtask = new Subtask("Call technical support", "Find out the courier's full name and number");
    Epic epic = new Epic("Эпический проект", task_1);

    taskManager.addTask(task_1);
    taskManager.addSubtask(subtask);
    taskManager.attachSubtask(task_1, subtask);

    taskManager.addEpic(epic);
    taskManager.printEpics();
  }
}
