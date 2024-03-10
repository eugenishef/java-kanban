import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private LinkedList<Task> history;

    public InMemoryHistoryManager() {
        this.history = new LinkedList<>();
    }

    @Override
    public void add(Task task) {
        if (history.size() >= 10) {
            history.removeFirst();
        }
        history.addLast(task);
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }
}
