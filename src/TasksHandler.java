import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import utils.HttpHelper;
import utils.JsonHelper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TasksHandler implements HttpHandler {
    private final TaskManager taskManager;

    public TasksHandler(TaskManager taskManager) {
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
        List<Task> allTasks = new ArrayList<>(taskManager.getTasks().values());
        JsonArray tasksArray = new JsonArray();

        for (Task task : allTasks) {
            tasksArray.add(taskToJson(task));
        }
        HttpHelper.sendResponse(exchange, tasksArray.toString(), 200);
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
            String taskId = idElement.getAsString();
            if (!taskId.isEmpty()) {
                Task taskToUpdate = taskManager.findTaskById(taskId);
                if (taskToUpdate != null) {
                    updateTaskFromJson(taskToUpdate, receivedJson);
                    jsonResponse = "{\"message\":\"Task updated\", \"id\":\"" + taskId + "\"}";
                    exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);
                } else {
                    jsonResponse = "{\"error\":\"Task with this ID does not exist\"}";
                    exchange.sendResponseHeaders(404, jsonResponse.getBytes().length);
                }
            } else {
                jsonResponse = "{\"error\":\"Provided ID is empty\"}";
                exchange.sendResponseHeaders(400, jsonResponse.getBytes().length);
            }
        } else {
            Task newTask = createTaskFromJson(receivedJson);

            if (taskManager.getTasks().containsKey(newTask.getId())) {
                jsonResponse = "{\"error\":\"A task with the generated ID already exists\"}";
                exchange.sendResponseHeaders(406, jsonResponse.getBytes().length);
            } else {
                taskManager.addTask(newTask);
                jsonResponse = "{\"message\":\"Task created\", \"id\":\"" + newTask.getId() + "\"}";
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
            String taskId = receivedJson.get("id").getAsString().trim();

            if (!taskId.isEmpty()) {
                if (taskManager.getTasks().containsKey(taskId)) {
                    taskManager.removeTaskById(taskId);
                    jsonResponse = "{\"message\":\"Task with ID " + taskId + " has been deleted\"}";
                    exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);
                } else {
                    jsonResponse = "{\"error\":\"No task found with ID " + taskId + "\"}";
                    exchange.sendResponseHeaders(404, jsonResponse.getBytes().length);
                }
            } else {
                jsonResponse = "{\"error\":\"Provided ID is empty\"}";
                exchange.sendResponseHeaders(406, jsonResponse.getBytes().length);
            }
        } else {
            jsonResponse = "{\"error\":\"Task ID is required\"}";
            exchange.sendResponseHeaders(400, jsonResponse.getBytes().length);
        }

        outputStream.write(jsonResponse.getBytes());
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

    private Task createTaskFromJson(JsonObject jsonObject) {
        try {
            String title = JsonHelper.getTitle(jsonObject);
            String description = JsonHelper.getDescription(jsonObject);
            LocalDateTime startTime = JsonHelper.getStartTime(jsonObject);
            long duration = JsonHelper.getDuration(jsonObject);
            return new Task(title, description, startTime, duration);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private void updateTaskFromJson(Task taskToUpdate, JsonObject jsonObject) {
        try {
            String title = JsonHelper.getTitle(jsonObject);
            String description = JsonHelper.getDescription(jsonObject);
            Task.TaskStatus status = JsonHelper.getStatus(jsonObject);
            LocalDateTime startTime = JsonHelper.getStartTime(jsonObject);
            long duration = JsonHelper.getDuration(jsonObject);

            taskToUpdate.setTitle(title);
            taskToUpdate.setDescription(description);
            taskToUpdate.setStatus(status);
            taskToUpdate.setStartTime(startTime);
            taskToUpdate.setDuration(duration);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
