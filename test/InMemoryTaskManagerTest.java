import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {
  private TaskManager taskManager;

  @BeforeEach
  void setUp() {
    taskManager = new InMemoryTaskManager();
  }

  @Test
  void testAddTask() {
    LocalDateTime startTime_1 = LocalDateTime.of(2024, 4, 29, 13, 30);
    LocalDateTime startTime_2 = LocalDateTime.of(2024, 4, 29, 13, 30);
    long durationMinutes = 60;
    Task task_1 = new Task("Make a report", "Collect data and prepare a report on the project", startTime_1, durationMinutes);
    Task task_2 = new Task("Make a report", "Collect data and prepare a report on the project", startTime_2, durationMinutes);

    taskManager.addTask(task_1);
    taskManager.addTask(task_2);
    taskManager.printTasks();

    assertFalse(taskManager.getTasks().isEmpty());
  }

  @Test
  void testTaskDurationAndStartTime() {
    LocalDateTime startTime = LocalDateTime.of(2024, 4, 29, 13, 30);
    long durationMinutes = 60;
    Task task = new Task("Make a report", "Collect data and prepare a report on the project", startTime, durationMinutes);

    assertEquals(startTime, task.getStartTime());
    assertEquals(Duration.ofMinutes(durationMinutes), task.getDuration());
    assertEquals(startTime.plusMinutes(durationMinutes), task.getEndTime());
  }

  @Test
  void testGetTaskId() {
    LocalDateTime startTime = LocalDateTime.of(2024, 4, 29, 13, 30);
    long durationMinutes = 60;
    Task task = new Task("Make a report", "Collect data and prepare a report on the project", startTime, durationMinutes);

    taskManager.addTask(task);

    String currentId = task.getId();

    assertEquals(task.getId(), currentId, "The id values must be match");
  }

  @Test
  void testGetTaskTitle() {
    LocalDateTime startTime = LocalDateTime.of(2024, 4, 29, 13, 30);
    long durationMinutes = 60;
    Task task = new Task("Make a report", "Collect data and prepare a report on the project", startTime, durationMinutes);

    taskManager.addTask(task);

    String currentTitle = task.getTitle();

    assertEquals(task.getTitle(), currentTitle, "Title values must be match");
  }

  @Test
  void testGetTaskDescription() {
    LocalDateTime startTime = LocalDateTime.of(2024, 4, 29, 13, 30);
    long durationMinutes = 60;
    Task task = new Task("Make a report", "Collect data and prepare a report on the project", startTime, durationMinutes);

    taskManager.addTask(task);

    String currentDescription = task.getDescription();

    assertEquals(task.getDescription(), currentDescription, "Description values must be match");
  }

  @Test
  void testChangeTaskStatus() {
    LocalDateTime startTime = LocalDateTime.of(2024, 4, 29, 13, 30);
    long durationMinutes = 60;
    Task task = new Task("Make a report", "Collect data and prepare a report on the project", startTime, durationMinutes);

    taskManager.addTask(task);

    Task.TaskStatus defaultStatus = task.getStatus();

    taskManager.changeTaskStatus(task, Task.TaskStatus.IN_PROGRESS);

    Task.TaskStatus changedStatus  = task.getStatus();

    assertNotEquals(defaultStatus, changedStatus, "The statuses should be different");
  }

  @Test
  void testFindTaskById() {
    LocalDateTime startTime = LocalDateTime.of(2024, 4, 29, 13, 30);
    long durationMinutes = 60;
    Task task = new Task("Make a report", "Collect data and prepare a report on the project", startTime, durationMinutes);

    taskManager.addTask(task);

    String requiredId = task.getId();

    Task result = taskManager.findTaskById(requiredId);

    assertNotNull(result, "The task was not found by id");
    assertEquals(task, result, "The found and added task must match");
  }

  @Test
  void testRemoveTaskById() {
    LocalDateTime startTime = LocalDateTime.of(2024, 4, 29, 13, 30);
    long durationMinutes = 60;
    Task task_1 = new Task("Make a report", "Collect data and prepare a report on the project", startTime, durationMinutes);
    Task task_2 = new Task("Prepare materials", "Collect all the necessary documents", startTime, durationMinutes);

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
    LocalDateTime startTime = LocalDateTime.of(2024, 4, 29, 13, 30);
    long durationMinutes = 60;
    Subtask subtask = new Subtask("Call technical support", "Find out the courier's full name and number", startTime, durationMinutes);

    taskManager.addSubtask(subtask);

    assertFalse(taskManager.getSubtasks().isEmpty());
  }

  @Test
  void testAddSubtaskToTask() {
    LocalDateTime startTime = LocalDateTime.of(2024, 4, 29, 13, 30);
    long durationMinutes = 60;
    Task task = new Task("Make a report", "Collect data and prepare a report on the project", startTime, durationMinutes);
    Subtask subtask = new Subtask("Call technical support", "Find out the courier's full name and number", startTime, durationMinutes);

    taskManager.addTask(task);
    taskManager.addSubtask(subtask);
    taskManager.attachSubtask(task, subtask);

    boolean hasSubtasks = taskManager.hasSubtasks(task, subtask);

    assertTrue(hasSubtasks);
  }

  @Test
  void testGetSubtaskId() {
    LocalDateTime startTime = LocalDateTime.of(2024, 4, 29, 13, 30);
    long durationMinutes = 60;
    Subtask subtask = new Subtask("Call technical support", "Find out the courier's full name and number", startTime, durationMinutes);

    taskManager.addSubtask(subtask);

    String currentId = subtask.getId();

    assertEquals(subtask.getId(), currentId, "The ID values must be match");
  }

  @Test
  void testGetSubtaskTitle() {
    LocalDateTime startTime = LocalDateTime.of(2024, 4, 29, 13, 30);
    long durationMinutes = 60;
    Subtask subtask = new Subtask("Call technical support", "Find out the courier's full name and number", startTime, durationMinutes);

    taskManager.addSubtask(subtask);

    String currentTitle = subtask.getTitle();

    assertEquals(subtask.getTitle(), currentTitle, "Title values must be match");
  }

  @Test
  void testGetSubtaskDescription() {
    LocalDateTime startTime = LocalDateTime.of(2024, 4, 29, 13, 30);
    long durationMinutes = 60;
    Subtask subtask = new Subtask("Call technical support", "Find out the courier's full name and number", startTime, durationMinutes);

    taskManager.addSubtask(subtask);

    String currentDescription = subtask.getDescription();

    assertEquals(subtask.getDescription(), currentDescription, "Description values must be match");
  }

  @Test
  void testFindSubtaskInTaskById() {
    LocalDateTime startTime = LocalDateTime.of(2024, 4, 29, 13, 30);
    long durationMinutes = 60;
    Task task = new Task("Make a report", "Collect data and prepare a report on the project", startTime, durationMinutes);
    Subtask subtask = new Subtask("Call technical support", "Find out the courier's full name and number", startTime, durationMinutes);

    taskManager.addTask(task);
    taskManager.addSubtask(subtask);

    String taskId = task.getId();
    String subtaskId = task.getId();

    Subtask resultWhenSubtaskNotAddedToTask = taskManager.findSubtaskById(taskId, subtaskId);
    assertEquals(null, resultWhenSubtaskNotAddedToTask,"How did the subtask get here?");
  }

  @Test
  void addEpic() {
    LocalDateTime startTime_1 = LocalDateTime.of(2024, 4, 29, 13, 30);
    long durationMinutes_1 = 60;

    LocalDateTime startTime_2 = LocalDateTime.of(2024, 4, 29, 14, 30);
    long durationMinutes_2 = 30;

    Task task_1 = new Task("Make a report", "Collect data and prepare a report on the project", startTime_1, durationMinutes_1);
    Subtask subtask_1 = new Subtask("Call technical support", "Find out the courier's full name and number", startTime_1, durationMinutes_1);
    Subtask subtask_2 = new Subtask("Write introduction", "Write an introduction for the report", startTime_2, durationMinutes_2);
    Epic epic = new Epic("Epic project", task_1);

    taskManager.addSubtask(subtask_1);
    taskManager.addSubtask(subtask_2);

    epic.add(subtask_1);
    epic.add(subtask_2);

    taskManager.addEpic(epic);
    taskManager.printEpics();
  }

  @Test
  void testGetPrioritizedTasks() {
    LocalDateTime startTime_1 = LocalDateTime.of(2024, 4, 29, 9, 0);
    LocalDateTime startTime_2 = LocalDateTime.of(2024, 4, 29, 10, 0);
    LocalDateTime startTime_3 = LocalDateTime.of(2024, 4, 29, 11, 0);

    Task task_1 = new Task("Call technical support", "Find out the courier's full name and number", startTime_1, 30);
    Task task_2 = new Task("Write introduction", "Write an introduction for the report", startTime_2, 45);
    Subtask subtask_1 = new Subtask("Subtask 1", "Collect data and prepare a report on the project", startTime_3, 15);

    taskManager.addTask(task_1);
    taskManager.addTask(task_2);
    taskManager.addSubtask(subtask_1);

    TreeSet<Task> prioritizedTasks = taskManager.getPrioritizedTasks();

    assertNotNull(prioritizedTasks);
    assertEquals(3, prioritizedTasks.size());

    System.out.println("Prioritized Tasks:");
    Iterator<Task> iterator = prioritizedTasks.iterator();
    assertTrue(iterator.hasNext());
    Task first = iterator.next();
    assertEquals(task_1, first);
    System.out.println(first.getTitle() + " - " + first.getStartTime());

    assertTrue(iterator.hasNext());
    Task second = iterator.next();
    assertEquals(task_2, second);
    System.out.println(second.getTitle() + " - " + second.getStartTime());

    assertTrue(iterator.hasNext());
    Task third = iterator.next();
    assertEquals(subtask_1, third);
    System.out.println(third.getTitle() + " - " + third.getStartTime());

    assertFalse(iterator.hasNext());
  }
}
