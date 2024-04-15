import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class EpicTest {
  @Test
  public void testCreateEpic() {
    Task task = new Task("Доставка товаров", "Позвонить курьеру в день доставки");
    Subtask subtask = new Subtask("Позвонить в тех. поддержку", "Узнать ФИО и номер курьера");
    Epic epic = new Epic("Разобраться с доставкой", task);

    task.createTask();
    subtask.createSubtask();
    task.addSubtask(subtask.getId(), subtask);

    assertNotNull(epic.localEpics);
  }
}
