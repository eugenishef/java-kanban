import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileBackedTaskManager extends InMemoryTaskManager {
  private final String filePath;

  public FileBackedTaskManager(String filePath) {
    super();
    this.filePath = filePath;
  }

  public void saveToFile() {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
      writer.write("type,id,title,description,status,startTime,duration\n");

      for (Task task : tasks.values()) {
        String startTime = String.valueOf(task.getStartTime());
        Duration duration = task.getDuration();
        String taskInfo = String.join(",",
            "TASK",
            getId(task),
            getTitle(task),
            getDescription(task),
            task.getStatus().toString(),
            startTime,
            String.valueOf(duration.toMinutes())
        );

        taskInfo += "\n";
        writer.write(taskInfo);
      }

      for (Subtask subtask : subtasks.values()) {
        String startTime = String.valueOf(subtask.getStartTime());
        Duration duration = subtask.getDuration();
        String subtaskInfo = String.join(",",
            "SUBTASK",
            getId(subtask),
            getTitle(subtask),
            getDescription(subtask),
            subtask.getStatus().toString(),
            startTime,
            String.valueOf(duration.toMinutes())
        );
        subtaskInfo += "\n";
        writer.write(subtaskInfo);
      }

      for (Epic epic : epics.values()) {
        String startTime = String.valueOf(epic.getStartTime());
        String endTime = String.valueOf(epic.getEndTime());
        Duration duration = epic.getDuration();

        String epicInfo = String.join(",",
            "EPIC",
            getId(epic),
            getTitle(epic),
            getStatusFromTasks(epic),
            startTime,
            String.valueOf(duration.toMinutes()),
            endTime
        );
        epicInfo += "\n";
        writer.write(epicInfo);
      }
    } catch (IOException e) {
      System.out.println(String.format("Error when saving to a file: %s", e.getMessage()));
    }
  }

  public void loadFromFile(File file) {
    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      String line;
      boolean isFirstLine = true;
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

      while ((line = reader.readLine()) != null) {
        if (isFirstLine) {
          isFirstLine = false;
          continue;
        }
        String[] elemFromCSV = line.split(",");
        if (elemFromCSV.length < 4) {
          continue;
        }
        String type = elemFromCSV[0];
        String title = elemFromCSV[2];
        String description = elemFromCSV[3];
        LocalDateTime startTime = LocalDateTime.parse(elemFromCSV[5], formatter);
        long durationMinutes = Long.parseLong(elemFromCSV[6]);

        if (type.equals("TASK") || type.equals("EPIC")) {
          Task task = new Task(title, description, startTime, durationMinutes);
          changeTaskStatus(task, Task.TaskStatus.valueOf(elemFromCSV[4]));
          addTask(task);

          Epic epic = new Epic(title, task);
          addEpic(epic);
        } if (type.equals("SUBTASK")) {
          Subtask subtask = new Subtask(title, description, startTime, durationMinutes);
          addSubtask(subtask);
        }
      }
    } catch (IOException e) {
      System.out.println(String.format("Error reading file: %s", e.getMessage()));
    }
  }
}