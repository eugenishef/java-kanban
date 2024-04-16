import org.junit.jupiter.api.Test;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileBackedTaskManagerTest {
  @Test
  void testSaveToFile() {
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy'_'HH-mm");
    String currentTimeStamp = dateFormat.format(new Date());
    String fileName = "file_" + currentTimeStamp + ".csv";

    FileBackedTaskManager taskManager = new FileBackedTaskManager(fileName);

    Task task = new Task("Доставка товаров", "Позвонить курьеру в день доставки");
    Subtask subtask = new Subtask("Позвонить в тех. поддержку", "Узнать ФИО и номер курьера");
    Epic epic = new Epic("Трудности интроверта", task);

    taskManager.addTask(task);
    taskManager.addSubtask(subtask);
    taskManager.attachSubtask(task, subtask);
    taskManager.addEpic(epic);

    taskManager.saveToFile();

    File file = new File(fileName);
    assertTrue(file.exists());
    assertTrue(file.length() > 0);
  }
}
