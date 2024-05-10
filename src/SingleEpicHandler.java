import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

public class SingleEpicHandler implements HttpHandler {
    private final TaskManager taskManager;

    public SingleEpicHandler(TaskManager taskManager) {
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

            Epic epic;
            String jsonResponse;

            try {
                epic = taskManager.findEpicById(id);

                JsonObject jsonEpic = new JsonObject();
                jsonEpic.addProperty("id", epic.getId());
                jsonEpic.addProperty("title", epic.getTitle());
                jsonEpic.addProperty("startTime", epic.getStartTime().toString());
                jsonEpic.addProperty("duration", epic.getDuration().toMinutes());
                jsonEpic.addProperty("endTime", epic.getEndTime().toString());


                JsonArray epicTasks = new JsonArray();
                for (Task task : epic) {
                    JsonObject jsonTask = new JsonObject();
                    jsonTask.addProperty("id", task.getId());
                    jsonTask.addProperty("title", task.getTitle());
                    jsonTask.addProperty("description", task.getDescription());
                    jsonTask.addProperty("status", task.getStatus().toString());
                    jsonTask.addProperty("startTime", task.getStartTime().toString());
                    jsonTask.addProperty("duration", task.getDuration().toMinutes());

                    epicTasks.add(jsonTask);
                }

                jsonEpic.add("tasks", epicTasks);

                jsonResponse = jsonEpic.toString();
                exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);

            } catch (Exception e) {
                jsonResponse = "{\"error\":\"epic not found\"}";
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
