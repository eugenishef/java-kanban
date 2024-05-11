package utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.time.Duration;
import java.time.LocalDateTime;

public class JsonHelper {
    public static String getTitle(JsonObject jsonObject) {
        return jsonObject.get("title").getAsString();
    }

    public static String getDescription(JsonObject jsonObject) {
        return jsonObject.get("description").getAsString();
    }

    public static LocalDateTime getStartTime(JsonObject jsonObject) {
        return LocalDateTime.parse(jsonObject.get("startTime").getAsString());
    }

    public static LocalDateTime getEndTime(JsonObject jsonObject) {
        return LocalDateTime.parse(jsonObject.get("endTime").getAsString());
    }

    public static long getDuration(JsonObject jsonObject) {
        return jsonObject.get("duration").getAsLong();
    }

    public static Task.TaskStatus getStatus(JsonObject jsonObject) {
        String statusString = jsonObject.get("status").getAsString().toUpperCase();
        return Task.TaskStatus.valueOf(statusString);
    }

    public static JsonArray getAsArray(JsonObject jsonObject, String key) {
        return jsonObject.getAsJsonArray(key);
    }
}
