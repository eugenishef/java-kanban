import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.List;

public class HistoryManagerTest {
  @Test
  public void testAddAndRemoveFromHistory() {
    HistoryManager historyManager = new InMemoryHistoryManager();
    TaskManager manager = new InMemoryTaskManager();

    Task task1 = new Task("Задача 1", "Описание 1");
    Task task2 = new Task("Задача 2", "Описание 2");

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
