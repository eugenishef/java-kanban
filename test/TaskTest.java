import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class TaskTest {
  @Test
  public void createTask() {
    Task task = new Task("Make a report", "Collect data and prepare a report on the project");

    assertNotNull(task.tasks);
  }

  @Test
  public void TaskWithoutSubtask() {
    Task task = new Task("Make a report", "Collect data and prepare a report on the project");

    assertFalse(task.hasSubtasks());
  }

  @Test
  public void TaskHasSubtasks() {
    Task task = new Task("Make a report", "Collect data and prepare a report on the project");
    Subtask subtask = new Subtask("Call technical support", "Find out the courier's full name and number");

    task.createTask();
    subtask.createSubtask();

    String subtaskId = subtask.getId();

    task.addSubtask(subtaskId, subtask);

    assertTrue(task.getSubtasks().containsKey(subtask.getId()));
  }

  @Test
  public void canChangeTaskTitle() {
    Task task = new Task("Make a report", "Collect data and prepare a report on the project");
    task.createTask();

    String previousTitle = task.getTitle();

    task.changeTaskTitle("Tomatoes!");

    assertNotEquals(previousTitle, task.getTitle());
  }

  @Test
  public void canChangeTaskDescription() {
    Task task = new Task("Make a report", "Collect data and prepare a report on the project");
    task.createTask();

    String previousDescription = task.getDescription();

    task.changeTaskTitle("Which book should I buy?");

    assertNotEquals(previousDescription, task.getTitle());
  }
}
