import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class TasksHandler implements HttpHandler {
    private Gson gson = new Gson();
    private TaskManager taskManager = new InMemoryTaskManager();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Headers headers = exchange.getResponseHeaders();

        if ("GET".equals(exchange.getRequestMethod())) {
            handleGetTasks(exchange);
            headers.set("Content-Type", "text/plain; charset=utf-8");
        } else if ("POST".equals(exchange.getRequestMethod())) {
            handlePostTask(exchange);
        } else if ("DELETE".equals(exchange.getRequestMethod())) {
            handleDeleteTask(exchange);
        }
    }

    private void handleGetTasks(HttpExchange exchange) throws IOException {
        LocalDateTime startTime_1 = LocalDateTime.of(2024, 4, 29, 13, 30);
        long durationMinutes = 60;
        Task task = new Task("Make a report", "Collect data and prepare a report on the project", startTime_1, durationMinutes);
        taskManager.addTask(task);
        Task findTask = taskManager.findTaskById(task.getId());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String response = gson.toJson(findTask);

        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private void handlePostTask(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        byte[] bytes = inputStream.readAllBytes();
        String requestBody = new String(bytes, StandardCharsets.UTF_8);
        Task task = gson.fromJson(requestBody, Task.class);
        taskManager.addTask(task);
        exchange.sendResponseHeaders(200, 0);
        exchange.getResponseBody().close();
    }

    private void handleDeleteTask(HttpExchange exchange) throws IOException {
        String uriPath = exchange.getRequestURI().getPath();
        String[] parts = uriPath.split("/");
        if (parts.length != 3) {
            exchange.sendResponseHeaders(400, 0);
            exchange.getResponseBody().close();
        } else {
            String taskIdStr = parts[2];
            int taskId;
            try {
                taskId = Integer.parseInt(taskIdStr);
            } catch (NumberFormatException e) {
                exchange.sendResponseHeaders(400, 0);
                exchange.getResponseBody().close();
                return;
            }
            taskManager.removeTaskById(String.valueOf(taskId));
            exchange.sendResponseHeaders(200, 0);
            exchange.getResponseBody().close();
        }
    }
}
