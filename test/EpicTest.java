import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class EpicTest {
  @Test
  public void createEpic() {
    LocalDateTime startTime = LocalDateTime.of(2024, 4, 29, 13, 30);
    long durationMinutes = 60;
    Task task = new Task("Delivery of goods", "Call the courier on the day of delivery", startTime, durationMinutes);
    Subtask subtask = new Subtask("Call technical support", "Find out the courier's full name and number", startTime, durationMinutes);
    Epic epic = new Epic("Difficulties of an introvert", task);

    task.createTask();
    subtask.createSubtask();
    task.addSubtask(subtask.getId(), subtask);

    assertNotNull(epic.epics);
  }
}
