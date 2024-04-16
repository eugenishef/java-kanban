import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private Node head;
    private Node tail;
    private HashMap<String, Node> nodeMap;
    private LinkedList<Task> history;

    public InMemoryHistoryManager() {
        this.head = null;
        this.tail = null;
        this.nodeMap = new HashMap<>();
        this.history = new LinkedList<>();
    }

    private class Node {
        Task task;
        Node prev;
        Node next;

        Node(Task task) {
            this.task = task;
            this.prev = null;
            this.next = null;
        }
    }

    private void linkLast(Task task) {
        Node newNode = new Node(task);
        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        nodeMap.put(task.getId(), newNode);
    }

    private List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node current = head;
        while (current != null) {
            tasks.add(current.task);
            current = current.next;
        }
        return tasks;
    }

    private void removeNode(Node node) {
        if (node == null) return;

        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }

        nodeMap.remove(node.task.getId());
    }

    @Override
    public void add(Task task) {
        Node existingNode = nodeMap.get(task.getId());

        if (existingNode != null) {
            removeNode(existingNode);
        }
        linkLast(task);

        if (history.size() >= 10) {
            history.removeFirst();
        }
        history.addLast(task);
    }

    @Override
    public void remove(String taskId) {
        for (Iterator<Task> iterator = history.iterator(); iterator.hasNext();) {
            Task task = iterator.next();
            if (task.getId().equals(taskId)) {
                iterator.remove();
                break;
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }

    public void printNodeMap() {
        for (Map.Entry<String, Node> entry : nodeMap.entrySet()) {
            String taskId = entry.getKey();
            Node node = entry.getValue();
            System.out.println(String.format("Task ID: %s, Node Value: %s", taskId, node.task));
        }
    }
}