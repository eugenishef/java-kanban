import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class TaskTest {
  @Test
  public void testCreateTask() {
    Task task = new Task("Доставка товаров", "Позвонить курьеру в день доставки");

    assertNotNull(task.tasks);
  }

  @Test
  public void testTaskWithoutSubtask() {
    Task task = new Task("Доставка товаров", "Позвонить курьеру в день доставки");

    assertFalse(task.hasSubtasks());
  }

  @Test
  public void testTaskHasSubtasks() {
    Task task = new Task("Доставка товаров", "Позвонить курьеру в день доставки");
    Subtask subtask = new Subtask("Позвонить в тех. поддержку", "Узнать ФИО и номер курьера");

    task.createTask();
    subtask.createSubtask();

    String subtaskId = subtask.getId();

    task.addSubtask(subtaskId, subtask);

    assertTrue(task.getSubtasks().containsKey(subtask.getId()));
  }

  @Test
  public void testCanChangeTaskTitle() {
    Task task = new Task("Доставка товаров", "Позвонить курьеру в день доставки");
    task.createTask();

    String previousTitle = task.getTitle();

    task.changeTaskTitle("Помидоры!");

    assertNotEquals(previousTitle, task.getTitle());
  }

  @Test
  public void testCanChangeTaskDescription() {
    Task task = new Task("Доставка товаров", "Позвонить курьеру в день доставки");
    task.createTask();

    String previousDescription = task.getDescription();

    task.changeTaskTitle("Какую книгу купить?");

    assertNotEquals(previousDescription, task.getTitle());
  }

  @Test
  public void testTaskHasBeenRemoved() {
  }
}
