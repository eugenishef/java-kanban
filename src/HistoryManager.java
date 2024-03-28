import java.util.List;

public interface HistoryManager {
    void add(Task task);
    void remove(String id);
    List<Task> getHistory();
}