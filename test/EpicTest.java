import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class EpicTest {
  @Test
  public void createEpic() {
    Task task = new Task("Delivery of goods", "Call the courier on the day of delivery");
    Subtask subtask = new Subtask("Call technical support", "Find out the courier's full name and number");
    Epic epic = new Epic("Difficulties of an introvert", task);

    task.createTask();
    subtask.createSubtask();
    task.addSubtask(subtask.getId(), subtask);

    assertNotNull(epic.epics);
  }
}
