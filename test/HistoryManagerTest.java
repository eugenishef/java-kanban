import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.List;

public class HistoryManagerTest {
  @Test
  public void testAddAndRemoveFromHistory() {
    HistoryManager historyManager = new InMemoryHistoryManager();
    TaskManager manager = new InMemoryTaskManager();

    Task task1 = new Task("Make a report", "Collect data and prepare a report on the project");
    Task task2 = new Task("Prepare materials", "Collect all the necessary documents");

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
