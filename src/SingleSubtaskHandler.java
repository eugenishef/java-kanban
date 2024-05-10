import com.google.gson.JsonObject;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

public class SingleSubtaskHandler implements HttpHandler {
    private final TaskManager taskManager;

    public SingleSubtaskHandler(TaskManager taskManager) {
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

            Subtask subtask;
            String jsonResponse;

            try {
                subtask = taskManager.findSingleSubtask(id);

                JsonObject jsonSubtask = new JsonObject();
                jsonSubtask.addProperty("id", subtask.getId());
                jsonSubtask.addProperty("title", subtask.getTitle());
                jsonSubtask.addProperty("description", subtask.getDescription());
                jsonSubtask.addProperty("status", subtask.getStatus().toString());
                jsonSubtask.addProperty("startTime", subtask.getStartTime().toString());
                jsonSubtask.addProperty("duration", subtask.getDuration().toMinutes());

                jsonResponse = jsonSubtask.toString();
                exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);

            } catch (Exception e) {
                jsonResponse = "{\"error\":\"subtask not found\"}";
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
