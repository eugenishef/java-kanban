import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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
    LocalDateTime startTimeTask_1 = LocalDateTime.of(2024, 4, 29, 13, 30);
    LocalDateTime startTimeTask_2 = LocalDateTime.of(2024, 4, 29, 14, 31);
    long durationMinutesTask_1 = 60;
    long durationMinutesTask_2 = 45;

    Task task_1 = new Task("Delivery of goods", "Call the courier on the day of delivery", startTimeTask_1, durationMinutesTask_1);
    Task task_2 = new Task("Delivery of goods", "Call the courier on the day of delivery", startTimeTask_2, durationMinutesTask_2);
    Subtask subtask = new Subtask("Call technical support", "Find out the courier's full name and number", startTimeTask_2, durationMinutesTask_1);
    Epic epic = new Epic("Difficulties of an introvert", task_1, task_2);
    String epicId = epic.getId();
    epic.addTask(epicId, task_2);

    taskManager.addTask(task_1);
    taskManager.addTask(task_2);
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
