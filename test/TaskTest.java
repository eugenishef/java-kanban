import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class TaskTest {
  @Test
  public void createTask() {
    LocalDateTime startTime = LocalDateTime.of(2024, 4, 29, 13, 30);
    long durationMinutes = 60;
    Task task = new Task("Make a report", "Collect data and prepare a report on the project", startTime, durationMinutes);

    assertNotNull(task.tasks);
  }

  @Test
  public void TaskWithoutSubtask() {
    LocalDateTime startTime = LocalDateTime.of(2024, 4, 29, 13, 30);
    long durationMinutes = 60;
    Task task = new Task("Make a report", "Collect data and prepare a report on the project", startTime, durationMinutes);

    assertFalse(task.hasSubtasks());
  }

  @Test
  public void TaskHasSubtasks() {
    LocalDateTime startTime = LocalDateTime.of(2024, 4, 29, 13, 30);
    long durationMinutes = 60;
    Task task = new Task("Make a report", "Collect data and prepare a report on the project", startTime, durationMinutes);
    Subtask subtask = new Subtask("Call technical support", "Find out the courier's full name and number", startTime, durationMinutes);

    task.createTask();
    subtask.createSubtask();

    String subtaskId = subtask.getId();

    task.addSubtask(subtaskId, subtask);

    assertTrue(task.getSubtasks().containsKey(subtask.getId()));
  }

  @Test
  public void canChangeTaskTitle() {
    LocalDateTime startTime = LocalDateTime.of(2024, 4, 29, 13, 30);
    long durationMinutes = 60;
    Task task = new Task("Make a report", "Collect data and prepare a report on the project", startTime, durationMinutes);
    task.createTask();

    String previousTitle = task.getTitle();

    task.changeTaskTitle("Tomatoes!");

    assertNotEquals(previousTitle, task.getTitle());
  }

  @Test
  public void canChangeTaskDescription() {
    LocalDateTime startTime = LocalDateTime.of(2024, 4, 29, 13, 30);
    long durationMinutes = 60;
    Task task = new Task("Make a report", "Collect data and prepare a report on the project", startTime, durationMinutes);
    task.createTask();

    String previousDescription = task.getDescription();

    task.changeTaskTitle("Which book should I buy?");

    assertNotEquals(previousDescription, task.getTitle());
  }
}
