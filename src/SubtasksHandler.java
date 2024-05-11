import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import utils.HttpHelper;
import utils.JsonHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SubtasksHandler implements HttpHandler {
    private final TaskManager taskManager;

    public SubtasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Headers headers = exchange.getResponseHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");
        String method = exchange.getRequestMethod();

        if ("GET".equals(method)) {
            handleGetRequest(exchange);
        } else if ("POST".equals(method)) {
            handlePostRequest(exchange);
        } else if ("DELETE".equals(method)) {
            handleDeleteRequest(exchange);
        } else {
            headers.set("Allow", "GET, POST, DELETE");
            String response = "Method not supported";
            exchange.sendResponseHeaders(405, response.getBytes().length);
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(response.getBytes());
            outputStream.close();
        }
    }

    private void handleGetRequest(HttpExchange exchange) throws IOException {
        List<Subtask> allSubtasks = new ArrayList<>(taskManager.getSubtasks().values());
        JsonArray subtasksArray = new JsonArray();

        for (Subtask subtask : allSubtasks) {
            subtasksArray.add(subtaskToJson(subtask));
        }
        HttpHelper.sendResponse(exchange, subtasksArray.toString(), 200);
    }
    private void handlePostRequest(HttpExchange exchange) throws IOException {
        InputStreamReader stream = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(stream);
        JsonObject receivedJson = JsonParser.parseReader(reader).getAsJsonObject();
        reader.close();

        String jsonResponse;
        OutputStream outputStream = exchange.getResponseBody();

        JsonElement idElement = receivedJson.get("id");
        if (idElement != null && !idElement.isJsonNull()) {
            String subtaskId = idElement.getAsString();
            if (!subtaskId.isEmpty()) {
                Subtask subtaskToUpdate = taskManager.findSingleSubtask(subtaskId);
                if (subtaskToUpdate != null) {
                    updateSubtaskFromJson(subtaskToUpdate, receivedJson);
                    jsonResponse = "{\"message\":\"Subtask updated\", \"id\":\"" + subtaskId + "\"}";
                    exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);
                } else {
                    jsonResponse = "{\"error\":\"Subtask with this ID does not exist\"}";
                    exchange.sendResponseHeaders(404, jsonResponse.getBytes().length);
                }
            } else {
                jsonResponse = "{\"error\":\"Provided ID is empty\"}";
                exchange.sendResponseHeaders(400, jsonResponse.getBytes().length);
            }
        } else {
            Subtask newSubtask = createSubtaskFromJson(receivedJson);

            if (taskManager.getSubtasks().containsKey(newSubtask.getId())) {
                jsonResponse = "{\"error\":\"A Subtask with the generated ID already exists\"}";
                exchange.sendResponseHeaders(406, jsonResponse.getBytes().length);
            } else {
                taskManager.addSubtask(newSubtask);
                jsonResponse = "{\"message\":\"Subtask created\", \"id\":\"" + newSubtask.getId() + "\"}";
                exchange.sendResponseHeaders(201, jsonResponse.getBytes().length);
            }
        }

        outputStream.write(jsonResponse.getBytes());
        outputStream.close();
    }
    private void handleDeleteRequest(HttpExchange exchange) throws IOException {
        InputStreamReader stream = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(stream);
        JsonObject receivedJson = JsonParser.parseReader(reader).getAsJsonObject();
        reader.close();

        String jsonResponse;
        OutputStream outputStream = exchange.getResponseBody();

        if (receivedJson.has("id") && !receivedJson.get("id").isJsonNull()) {
            String subtaskId = receivedJson.get("id").getAsString().trim();

            if (!subtaskId.isEmpty()) {
                if (taskManager.getSubtasks().containsKey(subtaskId)) {
                    taskManager.removeSubtaskById(subtaskId);
                    jsonResponse = "{\"message\":\"Subtask with ID " + subtaskId + " has been deleted\"}";
                    exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);
                } else {
                    jsonResponse = "{\"error\":\"Subtask not found with ID " + subtaskId + "\"}";
                    exchange.sendResponseHeaders(404, jsonResponse.getBytes().length);
                }
            } else {
                jsonResponse = "{\"error\":\"Provided ID is empty\"}";
                exchange.sendResponseHeaders(406, jsonResponse.getBytes().length);
            }
        } else {
            jsonResponse = "{\"error\":\"subtask ID is required\"}";
            exchange.sendResponseHeaders(400, jsonResponse.getBytes().length);
        }

        outputStream.write(jsonResponse.getBytes());
        outputStream.close();
    }

    private Subtask createSubtaskFromJson(JsonObject jsonObject) {
        try {
            String title = JsonHelper.getTitle(jsonObject);
            String description = JsonHelper.getDescription(jsonObject);
            LocalDateTime startTime = JsonHelper.getStartTime(jsonObject);
            long duration = JsonHelper.getDuration(jsonObject);
            return new Subtask(title, description, startTime, duration);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private void updateSubtaskFromJson(Subtask subtaskToUpdate, JsonObject jsonObject) {
        try {
            String title = JsonHelper.getTitle(jsonObject);
            String description = JsonHelper.getDescription(jsonObject);
            Task.TaskStatus status = JsonHelper.getStatus(jsonObject);
            LocalDateTime startTime = JsonHelper.getStartTime(jsonObject);
            long duration = JsonHelper.getDuration(jsonObject);

            subtaskToUpdate.setTitle(title);
            subtaskToUpdate.setDescription(description);
            subtaskToUpdate.setStatus(status);
            subtaskToUpdate.setStartTime(startTime);
            subtaskToUpdate.setDuration(duration);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private JsonObject subtaskToJson(Subtask subtask) {
        JsonObject jsonSubtask = new JsonObject();
        jsonSubtask.addProperty("id", subtask.getId());
        jsonSubtask.addProperty("title", subtask.getTitle());
        jsonSubtask.addProperty("description", subtask.getDescription());
        jsonSubtask.addProperty("status", subtask.getStatus().toString());
        jsonSubtask.addProperty("startTime", subtask.getStartTime().toString());
        jsonSubtask.addProperty("duration", subtask.getDuration().toMinutes());

        return jsonSubtask;
    }
}
