import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileBackedTaskManagerTest {
  private String fileName;
  @BeforeEach
  void setUp() {
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy'_'HH-mm");
    String currentTimeStamp = dateFormat.format(new Date());
    fileName = "file_" + currentTimeStamp + ".csv";
  }

  @Test
  void testSaveToFile() {
    FileBackedTaskManager taskManager = new FileBackedTaskManager(fileName);

    Task task = new Task("Delivery of goods", "Call the courier on the day of delivery");
    Subtask subtask = new Subtask("Call technical support", "Find out the courier's full name and number");
    Epic epic = new Epic("Difficulties of an introvert", task);

    taskManager.addTask(task);
    taskManager.addSubtask(subtask);
    taskManager.addEpic(epic);

    taskManager.saveToFile();

    File file = new File(fileName);
    assertTrue(file.exists());
    assertTrue(file.length() > 0);
  }

  @Test
  void loadFromFile() {
    File file = new File("file_16-04-2024_17-46.csv");
    FileBackedTaskManager taskManager = new FileBackedTaskManager("file_16-04-2024_17-46.csv");

    taskManager.loadFromFile(file);

    assertFalse(taskManager.getTasks().isEmpty(), "failed to read Tasks from file");
    assertFalse(taskManager.getSubtasks().isEmpty(), "failed to read Subtasks from file");
    assertFalse(taskManager.getEpics().isEmpty(), "failed to read Epics from file");
  }
}
