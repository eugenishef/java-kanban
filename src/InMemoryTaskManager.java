import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected HashMap<String, Task> tasks;
    protected HashMap<String, Subtask> subtasks;
    protected HashMap<String, ArrayList<Task>> epics;
    private LinkedList<Task> history;

    UUIDGenerator uuidGenerator;

    public InMemoryTaskManager() {
        uuidGenerator = new UUIDGenerator();
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
        history = new LinkedList<>();
    }

    public HashMap<String, Task> getTasks() {
        return tasks;
    }

    public HashMap<String, Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public void addTask(Task task) {
        String taskId = task.getId();
        tasks.put(taskId, tasks.getOrDefault(taskId, task));
    }

    @Override
    public String getId(Task task) {
        return task.getId();
    }

    @Override
    public String getTitle(Task task) {
        return task.getTitle();
    }

    @Override
    public String getDescription(Task task) {
        return task.getDescription();
    }

    @Override
    public void changeTaskStatus(Task task, Task.TaskStatus newStatus) {
        task.setStatus(newStatus);
        updateHistory(task);
    }

    @Override
    public Task findTaskById(String id) {
        try {
            Task task = tasks.get(id);
            if (task != null) {
                updateHistory(task);
                return task;
            } else {
                throw new IllegalArgumentException("Task with ID " + id + " not found.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error finding task by ID: " + e.getMessage(), e);
        }
    }

    @Override
    public void removeTaskById(String id) {
        Task removedTask = tasks.remove(id);

        if (removedTask != null) {
            tasks.remove(id);
        }

        if (history.contains(removedTask)) {
            history.remove(removedTask);
        }
    }

    @Override
    public void addSubtask(Subtask subtask) {
        String subtaskId = subtask.getId();

        subtasks.put(subtaskId, subtasks.getOrDefault(subtaskId, subtask));
    }

    @Override
    public void attachSubtask(Task task, Subtask subtask) {
        if (tasks.containsKey(task.getId())) {
            task.addSubtask(subtask.getId(), subtask);
            updateHistory(task);
        } else {
            System.out.println("Can not to attach subtask to task");
        }
    }

    @Override
    public boolean hasSubtasks(Task task, Subtask subtask) {
        HashMap<String, Subtask> subtasks = task.getSubtasks();
        return subtasks.containsKey(subtask.getId());
    }

    @Override
    public String getId(Subtask subtask) {
        return subtask.getId();
    }

    @Override
    public String getTitle(Subtask subtask) {
        return subtask.getTitle();
    }

    @Override
    public String getDescription(Subtask subtask) {
        return subtask.getDescription();
    }

    @Override
    public Subtask findSubtaskById(String taskId, String id) {
        List<Subtask> foundRecords = new ArrayList<>();
        if (tasks.containsKey(taskId)) {
            try {
                Subtask subtask = subtasks.get(id);
                foundRecords.add(subtask);
                return subtask;
            } catch (Exception e) {
                throw new RuntimeException("Error finding task bu Id: " + e.getMessage(), e);
            }
        }
        return null;
    }

    @Override
    public String getSubtaskIdFromTask(Task foundRecords) {
        StringBuilder subtaskIds = new StringBuilder();

        if (foundRecords.hasSubtasks()) {
            for (Subtask subtask : foundRecords.getSubtasks().values()) {
                subtaskIds.append(subtask.getId()).append(", ");
            }
            subtaskIds.delete(subtaskIds.length() - 2, subtaskIds.length());
        }

        return subtaskIds.toString();
    }

    @Override
    public void removeSubtaskById(String subtaskId) {

    }

    @Override
    public void addEpic(Epic epic, Task... tasks) {
        String epicId = uuidGenerator.generateUuid();
        ArrayList<Task> epicTasks = new ArrayList<>();

        for (Task task : tasks) {
            epicTasks.add(task);
        }

        epics.put(epicId, epic);
        epic.addTasks(epicTasks);
    }

    @Override
    public String getTitle(Epic epic) {
        return epic.getTitle();
    }

    @Override
    public void removeEpicById(String id) {

    }

    @Override
    public void printTasks() {
        System.out.println("Tasks:");
        for (Task task : tasks.values()) {
            System.out.println(task);
        }
    }

    @Override
    public void printSubtasks() {
        System.out.println("Subtasks:");
        for (Subtask subtask : subtasks.values()) {
            System.out.println(subtask);
        }
    }

    @Override
    public void printTasksWithSubtasks() {
        System.out.println("Tasks with subtasks:");
        for (Task task : tasks.values()) {
            if (task.hasSubtasks()) {
                System.out.println(task);
            }
        }
    }

    @Override
    public void printEpics() {
        System.out.println("Epics:");
        for (ArrayList<Task> epic : epics.values()) {
            System.out.println(epic);
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }

    @Override
    public void updateHistory(Task task) {
        boolean taskExists = history.contains(task);

        if (!taskExists) {
            if (history.size() >= 10) {
                history.removeFirst();
            }
            history.addLast(task);
        }
    }

    @Override
    public void printHistory() {
        System.out.println("History:");
        for (Task task : history) {
            System.out.println(task);
        }
    }

    @Override
    public void printFoundSubtask(Subtask subtask) {
        if (subtask != null) {
            System.out.println("Found subtask:");
            System.out.println(subtask);
        } else {
            System.out.println("Subtask not found.");
        }
    }

    @Override
    public void removeAll(String taskType) {
        switch (taskType.toLowerCase()) {
            case "tasks":
                tasks.clear();
                System.out.println("Tasks clear!");
                break;
            case "subtasks":
                subtasks.clear();
                System.out.println("Subtasks clear!");
                break;
            case "epics":
                epics.clear();
                System.out.println("Epics clear!");
                break;
            default:
                System.out.println("Error, you can use: (tasks/subtasks/epic).");
        }
    }

}
