import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class InMemoryHistoryManagerTest {

  @Test
  public void testGetTasks() {
    HistoryManager historyManager = new InMemoryHistoryManager();
    TaskManager taskManager = new InMemoryTaskManager();

    LocalDateTime startTime = LocalDateTime.of(2024, 4, 29, 13, 30);
    long durationMinutes = 60;
    Task task_1 = new Task("Make a report", "Collect data and prepare a report on the project", startTime, durationMinutes);
    taskManager.addTask(task_1);
    historyManager.add(task_1);

    taskManager.printTasks();
    historyManager.getHistory();
    ((InMemoryHistoryManager) historyManager).printHistoryState();

  }
}
