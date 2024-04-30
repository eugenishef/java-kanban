import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected HashMap<String, Task> tasks;
    protected HashMap<String, Subtask> subtasks;
    protected HashMap<String, Epic> epics;
    private LinkedList<Task> history;
    private TreeSet<Task> prioritizedTasks;

    UUIDGenerator uuidGenerator;

    public InMemoryTaskManager() {
        uuidGenerator = new UUIDGenerator();
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
        history = new LinkedList<>();
        prioritizedTasks = new TreeSet<>(Comparator.comparing((Task task) -> {
            if (task instanceof Subtask) {
                return ((Subtask) task).getStartTime();
            } else {
                return task.getStartTime();
            }
        }, Comparator.nullsLast(Comparator.naturalOrder())));
    }

    private boolean isTimeOverlap(Task task_1, Task task_2) {
        LocalDateTime start_1 = task_1.getStartTime();
        LocalDateTime end_1 = task_1.getEndTime();
        LocalDateTime start_2 = task_2.getStartTime();
        LocalDateTime end_2 = task_2.getEndTime();

        if (start_1 == null || start_2 == null) {
            return false;
        }

        return start_1.isBefore(end_2) && start_2.isBefore(end_1);
    }

    public HashMap<String, Task> getTasks() {
        return tasks;
    }

    public HashMap<String, Subtask> getSubtasks() {
        return subtasks;
    }

    public HashMap<String, Epic> getEpics() {
        return epics;
    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    @Override
    public void addTask(Task task) {
        String taskId = task.getId();
        boolean isOverlap = prioritizedTasks.stream().anyMatch(existingTask -> isTimeOverlap(task, existingTask));

        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }

        if (!isOverlap) {
            tasks.put(taskId, tasks.getOrDefault(taskId, task));
            prioritizedTasks.add(task);
        } else {
            System.out.println("Task time overlaps with an existing task.");
        }
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
        boolean isOverlap = prioritizedTasks.stream().anyMatch(existingTask -> isTimeOverlap(subtask, existingTask));

        if (subtask.getStartTime() != null) {
            prioritizedTasks.add(subtask);
        }

        if (!isOverlap) {
            if (subtask.getStartTime() != null) {
                prioritizedTasks.add(subtask);
            }
        } else {
            System.out.println("Subtask time overlaps with an existing task.");
        }
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
    public void addEpic(Epic epic) {
        String epicId = epic.getId();

        epic.setDuration(epic.calculateDuration());
        epic.setStartTime(epic.calculateStartTime());
        epic.setEndTime(epic.calculateEndTime());

        epics.put(epicId, epic);
    }

    @Override
    public String getId(Epic epic) {
        return epic.getId();
    }

    @Override
    public String getTitle(Epic epic) {
        return epic.getTitle();
    }

    @Override
    public String getStatusFromTasks(Epic epic) {
        for (Task task : epic) {
            if (task.getStatus() == Task.TaskStatus.IN_PROGRESS) {
                return Task.TaskStatus.IN_PROGRESS.toString();
            } else if (task.getStatus() == Task.TaskStatus.DONE) {
                return Task.TaskStatus.DONE.toString();
            }
        }
        return Task.TaskStatus.NEW.toString();
    }

    @Override
    public void removeEpicById(String id) {
        epics.remove(id);
    }

    @Override
    public void printTasks() {
        for (Task task : tasks.values()) {
            System.out.println(String.format("Tasks:\n%s", task));
        }
    }

    @Override
    public void printSubtasks() {
        for (Subtask subtask : subtasks.values()) {
            System.out.println(String.format("Subtasks:\n%s", subtask));
        }
    }

    @Override
    public void printTasksWithSubtasks() {
        for (Task task : tasks.values()) {
            if (task.hasSubtasks()) {
                System.out.println(String.format("Tasks with subtasks:\n%s", task));
            }
        }
    }

    @Override
    public void printEpics() {
        for (ArrayList<Task> epic : epics.values()) {
            System.out.println(String.format("Epics:\n%s", epic));
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
        for (Task task : history) {
            System.out.println(String.format("History:\n%s",task));
        }
    }

    @Override
    public void printFoundSubtask(Subtask subtask) {
        if (subtask != null) {
            System.out.println(String.format("Found subtask:\n%s",subtask));
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
