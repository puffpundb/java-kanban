package server;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class SubTasksHandlerTest extends HttpTaskServerTest{
    @Test
    public void shouldServerRightGetSubTask() throws IOException, InterruptedException {
        SubTask rightSub = server.getTaskManagerForTest().getSubTaskById(2);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks/2"))
                .GET()
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        JsonObject subFromServer = JsonParser.parseString(response.body()).getAsJsonObject();
        SubTask subFromJson = GSON.fromJson(subFromServer, SubTask.class);

        Assertions.assertEquals(rightSub, subFromJson);
    }

    @Test
    public void shouldServerRightGetSubsArray() throws IOException, InterruptedException {
        List<Task> rightSubList = server.getTaskManagerForTest().getAllSubTasks();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .GET()
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        JsonArray listFromServer = JsonParser.parseString(response.body()).getAsJsonArray();
        List<Task> listFromJson = GSON.fromJson(listFromServer, new TypeToken<List<SubTask>>() {
        }.getType());

        Assertions.assertEquals(rightSubList, listFromJson);
    }

    @Test
    public void shouldServerRightDeleteSub() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks/2"))
                .DELETE()
                .header("Accept", "application/json")
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        List<Task> tasksList = server.getTaskManagerForTest().getAllSubTasks();

        Assertions.assertEquals(0, tasksList.size());
    }

    @Test
    public void shouldServerRightPostSub() throws IOException, InterruptedException {
        SubTask rightSub = new SubTask("2", "2", 1, Duration.parse("PT30M"), LocalDateTime.parse("2025-01-21T10:00"));
        rightSub.setId(2);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks/2"))
                .GET()
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        JsonObject subFromServer = JsonParser.parseString(response.body()).getAsJsonObject();
        SubTask subFromJson = GSON.fromJson(subFromServer, SubTask.class);

        Assertions.assertEquals(rightSub, subFromJson);
    }

    @Test
    public void shouldServerRightUpdateSub() throws IOException, InterruptedException {
        SubTask rightSub = server.getTaskManagerForTest().getSubTaskById(2);
        rightSub.setTitle("123");

        String taskRequest = GSON.toJson(rightSub);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks/2"))
                .POST(HttpRequest.BodyPublishers.ofString(taskRequest))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        SubTask taskFromManager = server.getTaskManagerForTest().getSubTaskById(2);

        Assertions.assertEquals(rightSub, taskFromManager);
    }
}
