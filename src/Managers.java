public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager(); // Пока используем только InMemoryTaskManager
    }
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager(); // История просмотров
    }
}