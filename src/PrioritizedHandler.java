import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.util.TreeSet;

public class PrioritizedHandler implements HttpHandler {
    private final TaskManager taskManager;

    public PrioritizedHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Headers headers = exchange.getResponseHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");
        String method = exchange.getRequestMethod();

        if ("GET".equals(method)) {
            handleGetRequest(exchange);
        } else {
            headers.set("Allow", "GET");
            String response = "Method not supported";
            exchange.sendResponseHeaders(405, response.getBytes().length);
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(response.getBytes());
            outputStream.close();
        }
    }

    private void handleGetRequest(HttpExchange exchange) throws IOException {
        TreeSet<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        JsonArray jsonArray = new JsonArray();

        for (Task task : prioritizedTasks) {
            JsonObject jsonTask = taskToJson(task);
            jsonArray.add(jsonTask);
        }

        String response = jsonArray.toString();
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(response.getBytes());
        outputStream.close();
    }

    private JsonObject taskToJson(Task task) {
        JsonObject jsonTask = new JsonObject();
        jsonTask.addProperty("id", task.getId());
        jsonTask.addProperty("title", task.getTitle());
        jsonTask.addProperty("description", task.getDescription());
        jsonTask.addProperty("status", task.getStatus().toString());
        jsonTask.addProperty("startTime", task.getStartTime().toString());
        jsonTask.addProperty("duration", task.getDuration().toMinutes());

        return jsonTask;
    }
}