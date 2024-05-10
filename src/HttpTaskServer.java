import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    public static void main(String[] args) throws IOException {
        final int PORT = 8080;
        TaskManager taskManager = new InMemoryTaskManager();
        HistoryManager historyManager = new InMemoryHistoryManager();

        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        server.createContext("/tasks", new TasksHandler(taskManager));
        server.createContext("/tasks/", new SingleTaskHandler(taskManager));
        server.createContext("/subtasks", new SubtasksHandler(taskManager));
        server.createContext("/subtasks/", new SingleSubtaskHandler(taskManager));
        server.createContext("/epics", new EpicsHandler(taskManager));
        server.createContext("/epics/", new SingleEpicHandler(taskManager));
        server.createContext("/history", new HistoryHandler(taskManager, historyManager));
        server.createContext("/prioritized", new PrioritizedHandler(taskManager));
        server.start();
        System.out.println("Server started on port: " + PORT);
    }
}
