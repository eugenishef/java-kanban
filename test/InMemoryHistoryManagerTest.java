import org.junit.jupiter.api.Test;

public class InMemoryHistoryManagerTest {

  @Test
  public void testGetTasks() {
    HistoryManager historyManager = new InMemoryHistoryManager();
    TaskManager taskManager = new InMemoryTaskManager();

    Task task_1 = new Task("Make a report", "Collect data and prepare a report on the project");
    taskManager.addTask(task_1);
    historyManager.add(task_1);

    taskManager.printTasks();
    historyManager.getHistory();
    ((InMemoryHistoryManager) historyManager).printHistoryState();

  }
}
