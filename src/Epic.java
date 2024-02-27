import java.util.ArrayList;

public class Epic {
    private String title;
    private String description;
    private int epicId;
    private TaskStatus status;
    private ArrayList<Task> tasks;

    public Epic(String title, String description) {
        this.title = title;
        this.description = description;
        this.tasks = new ArrayList<>();
    }

    public String getTitle() { return title; }
    public void setTitle(String newTitle) { this.title = newTitle; }
    public String getDescription() { return description; }
    public void setDescription(String newDescription) { this.description = newDescription; }
    public int getId() { return this.epicId; }
    public void setId(int newId) { this.epicId = newId; }
    public TaskStatus getStatus() { return this.status; }
    public void setStatus(TaskStatus newStatus) { this.status = newStatus; }
    public void addTask(Task task) { tasks.add(task); }
    public ArrayList<Task> getTasks() { return tasks; }

    public Task getByTaskId(int taskId) {
        for (Task task : tasks) {
            if (task.getId() == taskId) {
                return task;
            }
        }
        return null;
    }

    public void createTask(Task newTask) { tasks.add(newTask); }

    public void updateTask(Task updatedTask) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == updatedTask.getId()) {
                tasks.set(i, updatedTask);
                break;
            }
        }
    }

    public void deleteTask(int taskId) { tasks.removeIf(task -> task.getId() == taskId); }

    public ArrayList<Task> getSubtasksOfEpic(int epicId) {
        ArrayList<Task> subtasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task instanceof Subtask && ((Subtask) task).getEpic().getId() == epicId) {
                subtasks.add(task);
            }
        }
        return subtasks;
    }

    public void addSubtaskToTask(int taskId, Subtask subtask) {
        Task task = getByTaskId(taskId);
        if (task instanceof Task && subtask != null) {
            task.addSubtask(subtask);
        }
    }

    public void getAllTasks() {
        for (Task task : tasks) {
            System.out.println(task.toString());
        }
    }

    public void getAllTasksWithSubtasks() {
        for (Task task : tasks) {
            System.out.println(task.toString());
            if (task.getSubtasks().size() > 0) {
                for (Subtask subtask : task.getSubtasks()) {
                    System.out.println("Подзадачи: " + subtask.toString());
                }
            }
        }
    }
}
