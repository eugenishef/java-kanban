import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.List;

public class TaskManagerTest {
    @Test
    public void testTaskEqualityById() {
        TaskManager manager = new InMemoryTaskManager();

        Task task1 = new Task("Заголовок 1", "Описание 1");
        Task task2 = new Task("Заголовок 2", "Описание 2");

        manager.addTask(task1);
        manager.addTask(task2);

        String taskId1 = manager.getTaskId(task1);
        String taskId2 = manager.getTaskId(task2);

        assertEquals(taskId1, taskId1);
        assertNotEquals(taskId1, taskId2);
    }

    @Test
    public void testHistoryManager() {
        TaskManager manager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();

        Task task1 = new Task("Покупки", "Купить молоко");
        Task task2 = new Task("Уборка", "Почистить кухню");
        Task task3 = new Task("Уроки", "Подготовить презентацию");

        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);

        Task viewedTask1 = manager.findTaskById(manager.getTaskId(task1));
        Task viewedTask2 = manager.findTaskById(manager.getTaskId(task2));
        Task viewedTask3 = manager.findTaskById(manager.getTaskId(task3));

        if (viewedTask1 != null) {
            historyManager.add(viewedTask1);
        }
        if (viewedTask2 != null) {
            historyManager.add(viewedTask2);
        }
        if (viewedTask3 != null) {
            historyManager.add(viewedTask3);
        }

        List<Task> history = historyManager.getHistory();

        assertEquals(3, history.size());
    }

    @Test
    public void testSubtaskRemoval() {
        TaskManager manager = Managers.getDefault();

        Task task = new Task("Main Task", "Description of the main task");
        Subtask subtask = new Subtask("Subtask", "Description of the subtask");
        manager.addTask(task);
        manager.addSubtaskToTask(task, subtask);

        assertEquals(1, task.getSubtasks().size());

        manager.removeSubtaskFromTask(task, subtask);
        assertTrue(task.getSubtasks().isEmpty());
    }
}
