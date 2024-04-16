import java.io.*;
import java.util.ArrayList;

public class FileBackedTaskManager extends InMemoryTaskManager {
  private final String filePath;

  public FileBackedTaskManager(String filePath) {
    super();
    this.filePath = filePath;
  }

  public void saveToFile() {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
      writer.write("type,id,title,description,status,nestedElement\n");
      for (Task task : tasks.values()) {
        String nestedElement = task.getSubtasks().isEmpty() ? "" : "HAS_SUBTASK";
        writer.write(  "TASK," + getId(task) + "," + getTitle(task) + "," + getDescription(task) + "," + task.getStatus() + "," + nestedElement + "\n");
        for (Subtask subtask : subtasks.values()) {
          writer.write("SUBTASK," + getId(subtask) + "," + getTitle(subtask) + "," + getDescription(subtask) + "," + subtask.getStatus() + "\n");
        }
      }
      for (ArrayList<Task> epic : epics.values()) {
        writer.write("EPIC," + getId((Epic) epic) + "," + getTitle((Epic) epic) + ",," + getStatusFromTasks((Epic) epic) + "," + getNestedElements((Epic) epic) + "\n");
      }
    } catch (IOException e) {
      System.out.println(String.format("Error when saving to a file: %s", e.getMessage()));
    }
  }

  public String getNestedElements(Epic elements) {
    if (elements.isEmpty()) {
      return "";
    }
    StringBuilder result = new StringBuilder();
    for (Task element : elements) {
      result.append(element).append(",");
    }

    result.deleteCharAt(result.length() - 1);
    return result.toString();
  }

  public void loadFromFile(File file) {
    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      String line;
      boolean isFirstLine = true;
      while ((line = reader.readLine()) != null) {
        if (isFirstLine) {
          isFirstLine = false;
          continue;
        }
        String[] parts = line.split(",");
        if (parts.length < 4) {
          continue;
        }
        String type = parts[0];
        String title = parts[2];
        String description = parts[3];

        if (type.equals("TASK") || type.equals("EPIC")) {
          Task task = new Task(title, description);
          changeTaskStatus(task, Task.TaskStatus.valueOf(parts[4]));
          addTask(task);

          Epic epic = new Epic(title, task);
          addEpic(epic);
        } if (type.equals("SUBTASK")) {
          Subtask subtask = new Subtask(title, description);
          addSubtask(subtask);
        }
      }
    } catch (IOException e) {
      System.out.println(String.format("Error reading file: %s", e.getMessage()));
    }
  }
}