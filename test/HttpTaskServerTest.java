import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import static org.mockito.Mockito.*;

public class HttpTaskServerTest {
    private HttpServer server;
    private TaskManager taskManager;
    private HistoryManager historyManager;

    @BeforeEach
    public void setUp() throws IOException {
        int PORT = 8080;
        taskManager = new InMemoryTaskManager();
        historyManager = new InMemoryHistoryManager();
        server = HttpServer.create(new InetSocketAddress(PORT), 0);

        LocalDateTime startTime_1 = LocalDateTime.of(2024, 4, 29, 13, 30);
        LocalDateTime startTime_2 = LocalDateTime.of(2024, 1, 15, 07, 31);
        long durationMinutes_1 = 60;
        long durationMinutes_2 = 45;
        Task task_1 = new Task("Make a report", "Collect data", startTime_1, durationMinutes_1);
        Task task_2 = new Task("Go to sleep", "at 10 pm", startTime_2, durationMinutes_2);
        taskManager.addTask(task_1);
        taskManager.addTask(task_2);

        LocalDateTime startTime_3 = LocalDateTime.of(2024, 4, 29, 13, 30);
        long durationMinutes_3 = 60;
        Subtask subtask = new Subtask("Call technical support", "Find out the courier's full name and number", startTime_3, durationMinutes_3);

        //taskManager.addSubtask(subtask);

        Epic epic = new Epic("Epic project", task_1);
        epic.add(subtask);
        taskManager.addEpic(epic);

        LocalDateTime startTime_4 = LocalDateTime.of(2024, 1, 15, 20, 30);
        long durationMinutes_4 = 15;
        Task task_4 = new Task("Make a report", "Collect data", startTime_4, durationMinutes_4);
        taskManager.addTask(task_4);
        historyManager.add(task_4);

        server.createContext("/tasks", new TasksHandler(taskManager));
        server.createContext("/tasks/", new SingleTaskHandler(taskManager));
        server.createContext("/subtasks", new SubtasksHandler(taskManager));
        server.createContext("/subtasks/", new SingleSubtaskHandler(taskManager));
        server.createContext("/epics", new EpicsHandler(taskManager));
        server.createContext("/epics/", new SingleEpicHandler(taskManager));
        server.createContext("/history", new HistoryHandler(taskManager, historyManager));
        server.createContext("/prioritized", new PrioritizedHandler(taskManager));
        server.start();
    }
@Test
    public void testWhenGetTasksThenTasksReturned() throws IOException {
        HttpExchange exchange = mock(HttpExchange.class);
        OutputStream os = mock(OutputStream.class);

        when(exchange.getResponseBody()).thenReturn(os);

        TasksHandler handler = new TasksHandler(taskManager);

        handler.handle(exchange);

        verify(exchange).sendResponseHeaders(eq(200), anyLong());

        verify(os).write(any(byte[].class));
        verify(os).close();
    }

    @AfterEach
    public void tearDown() {
        server.stop(0);
    }
}