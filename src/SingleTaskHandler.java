import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.Headers;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

public class SingleTaskHandler implements HttpHandler {
    private final TaskManager taskManager;

    public SingleTaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            URI requestURI = exchange.getRequestURI();
            String path = requestURI.getPath();
            String id = path.substring(path.lastIndexOf('/') + 1);

            Headers headers = exchange.getResponseHeaders();
            headers.set("Content-Type", "application/json; charset=utf-8");

            Task task;
            String jsonResponse;

            try {
                task = taskManager.findTaskById(id);

                JsonObject jsonTask = new JsonObject();
                jsonTask.addProperty("id", task.getId());
                jsonTask.addProperty("title", task.getTitle());
                jsonTask.addProperty("description", task.getDescription());
                jsonTask.addProperty("status", task.getStatus().toString());
                jsonTask.addProperty("startTime", task.getStartTime().toString());
                jsonTask.addProperty("duration", task.getDuration().toMinutes());

                jsonResponse = jsonTask.toString();
                exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);

            } catch (Exception e) {
                jsonResponse = "{\"error\":\"task not found\"}";
                exchange.sendResponseHeaders(404, jsonResponse.getBytes().length);
            }

            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(jsonResponse.getBytes());
            outputStream.close();
        } else {
            String response = "This method is not allowed";
            exchange.sendResponseHeaders(405, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}