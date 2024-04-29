import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

public class HistoryManagerTest {
  @Test
  public void testAddAndRemoveFromHistory() {
    HistoryManager historyManager = new InMemoryHistoryManager();
    TaskManager manager = new InMemoryTaskManager();

    LocalDateTime startTime = LocalDateTime.of(2024, 4, 29, 13, 30);
    long durationMinutes = 60;
    Task task1 = new Task("Make a report", "Collect data and prepare a report on the project", startTime, durationMinutes);
    Task task2 = new Task("Prepare materials", "Collect all the necessary documents", startTime, durationMinutes);

    manager.addTask(task1);
    manager.addTask(task2);

    historyManager.add(task1);
    historyManager.add(task2);

    List<Task> history = historyManager.getHistory();
    assertEquals(2, history.size());

    historyManager.remove(task1.getId());
    history = historyManager.getHistory();
    assertEquals(1, history.size());

    assertFalse(history.contains(task1));
  }
}
