import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EpicsHandler implements HttpHandler {
    private final TaskManager taskManager;

    public EpicsHandler(TaskManager taskManager) {
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
        List<Epic> allEpics = new ArrayList<>(taskManager.getEpics().values());
        JsonArray tasksArray = new JsonArray();

        for (Epic epic : allEpics) {
            tasksArray.add(epicToJson(epic));
        }
        sendResponse(exchange, tasksArray.toString(), 200);
    }

    private void handlePostRequest(HttpExchange exchange) throws IOException {
        InputStreamReader stream = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(stream);
        JsonObject receivedJson = JsonParser.parseReader(reader).getAsJsonObject();
        reader.close();

        String jsonResponse;
        OutputStream os = exchange.getResponseBody();

        JsonElement idElement = receivedJson.get("id");
        if (idElement != null && !idElement.isJsonNull()) {
            String epicId = idElement.getAsString();
            if (!epicId.isEmpty()) {
                Epic epicToUpdate = taskManager.findEpicById(epicId);
                if (epicToUpdate != null) {
                    updateEpicFromJson(epicToUpdate, receivedJson);
                    jsonResponse = "{\"message\":\"Epic updated\", \"id\":\"" + epicId + "\"}";
                    exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);
                } else {
                    jsonResponse = "{\"error\":\"Epic with this ID does not exist\"}";
                    exchange.sendResponseHeaders(404, jsonResponse.getBytes().length);
                }
            } else {
                jsonResponse = "{\"error\":\"Provided ID is empty\"}";
                exchange.sendResponseHeaders(400, jsonResponse.getBytes().length);
            }
        } else {
            Epic newEpic = createEpicFromJson(receivedJson);

            if (taskManager.getEpics().containsKey(newEpic.getId())) {
                jsonResponse = "{\"error\":\"A epic with the generated ID already exists\"}";
                exchange.sendResponseHeaders(406, jsonResponse.getBytes().length);
            } else {
                taskManager.addEpic(newEpic);
                jsonResponse = "{\"message\":\"Epic created\", \"id\":\"" + newEpic.getId() + "\"}";
                exchange.sendResponseHeaders(201, jsonResponse.getBytes().length);
            }
        }

        os.write(jsonResponse.getBytes());
        os.close();
    }

    private void handleDeleteRequest(HttpExchange exchange) throws IOException {
        InputStreamReader stream = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        BufferedReader reader = new BufferedReader(stream);
        JsonObject receivedJson = JsonParser.parseReader(reader).getAsJsonObject();
        reader.close();

        String jsonResponse;
        OutputStream os = exchange.getResponseBody();

        if (receivedJson.has("id") && !receivedJson.get("id").isJsonNull()) {
            String epicId = receivedJson.get("id").getAsString().trim();

            if (!epicId.isEmpty()) {
                if (taskManager.getEpics().containsKey(epicId)) {
                    taskManager.removeEpicById(epicId);
                    jsonResponse = "{\"message\":\"Epic with ID " + epicId + " has been deleted\"}";
                    exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);
                } else {
                    jsonResponse = "{\"error\":\"No epic found with ID " + epicId + "\"}";
                    exchange.sendResponseHeaders(404, jsonResponse.getBytes().length);
                }
            } else {
                jsonResponse = "{\"error\":\"Provided ID is empty\"}";
                exchange.sendResponseHeaders(406, jsonResponse.getBytes().length);
            }
        } else {
            jsonResponse = "{\"error\":\"Epic ID is required\"}";
            exchange.sendResponseHeaders(400, jsonResponse.getBytes().length);
        }

        os.write(jsonResponse.getBytes());
        os.close();
    }

    private void sendResponse(HttpExchange exchange, String response, int statusCode) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private JsonObject epicToJson(Epic epic) {
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

        return jsonEpic;
    }

    private Epic createEpicFromJson(JsonObject jsonObject) {
        try {
            String title = jsonObject.get("title").getAsString();
            JsonArray tasksJsonArray = jsonObject.getAsJsonArray("tasks");
            ArrayList<Task> tasks = new ArrayList<>();

            for (JsonElement taskElement : tasksJsonArray) {
                JsonObject taskJsonObject = taskElement.getAsJsonObject();
                Task task = createTaskFromJson(taskJsonObject);
                tasks.add(task);
            }

            Epic epic = new Epic(title, tasks.toArray(new Task[0]));

            if (jsonObject.has("startTime")) {
                LocalDateTime startTime = LocalDateTime.parse(jsonObject.get("startTime").getAsString());
                epic.setStartTime(startTime);
            }

            if (jsonObject.has("endTime")) {
                LocalDateTime endTime = LocalDateTime.parse(jsonObject.get("endTime").getAsString());
                epic.setEndTime(endTime);
            }

            if (jsonObject.has("duration")) {
                long minutes = jsonObject.get("duration").getAsLong();
                epic.setDuration(Duration.ofMinutes(minutes));
            }

            return epic;

        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private Task createTaskFromJson(JsonObject jsonObject) {
        try {
            String title = jsonObject.get("title").getAsString();
            String description = jsonObject.get("description").getAsString();
            LocalDateTime startTime = LocalDateTime.parse(jsonObject.get("startTime").getAsString());
            long duration = jsonObject.get("duration").getAsLong();
            return new Task(title, description, startTime, duration);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private void updateEpicFromJson(Epic epic, JsonObject jsonObject) {
        try {
            if (jsonObject.has("title")) {
                String title = jsonObject.get("title").getAsString();
                epic.setTitle(title);
            }

            if (jsonObject.has("startTime")) {
                LocalDateTime startTime = LocalDateTime.parse(jsonObject.get("startTime").getAsString());
                epic.setStartTime(startTime);
            }

            if (jsonObject.has("duration")) {
                long durationMinutes = jsonObject.get("duration").getAsLong();
                epic.setDuration(Duration.ofMinutes(durationMinutes));
            }

            if (jsonObject.has("endTime")) {
                LocalDateTime endTime = LocalDateTime.parse(jsonObject.get("endTime").getAsString());
                epic.setEndTime(endTime);
            }

            if (jsonObject.has("tasks")) {
                JsonArray tasksJsonArray = jsonObject.getAsJsonArray("tasks");
                epic.getTasks().clear();
                for (JsonElement taskElement : tasksJsonArray) {
                    JsonObject taskJsonObject = taskElement.getAsJsonObject();
                    Task task = createTaskFromJson(taskJsonObject);
                    if (task != null) {
                        epic.add(task);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


