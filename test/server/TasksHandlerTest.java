package server;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class TasksHandlerTest extends HttpTaskServerTest {
    @Test
    public void shouldServerRightGetTasks() throws IOException, InterruptedException {
        Task rightTask = server.getTaskManagerForTest().getTaskById(0);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/0"))
                .GET()
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        JsonObject taskFromServer = JsonParser.parseString(response.body()).getAsJsonObject();
        Task taskFromJson = GSON.fromJson(taskFromServer, Task.class);

        Assertions.assertEquals(rightTask, taskFromJson);
    }

    @Test
    public void shouldServerRightGetTasksArray() throws IOException, InterruptedException {
        List<Task> rightTaskList = server.getTaskManagerForTest().getAllTasks();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .GET()
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        JsonArray listFromServer = JsonParser.parseString(response.body()).getAsJsonArray();
        List<Task> listFromJson = GSON.fromJson(listFromServer, new TypeToken<List<Task>>() {}.getType());

        Assertions.assertEquals(rightTaskList, listFromJson);
    }

    @Test
    public void shouldServerRightDeleteTask() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/0"))
                .DELETE()
                .header("Accept", "application/json")
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        List<Task> tasksList = server.getTaskManagerForTest().getAllTasks();

        Assertions.assertEquals(0, tasksList.size());
    }

    @Test
    public void shouldServerRightPostTask() throws IOException, InterruptedException {
        Task rightTask = new Task("0", "0", Duration.parse("PT30M"), LocalDateTime.parse("2026-01-21T10:00"));
        rightTask.setId(0);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/0"))
                .GET()
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        JsonObject taskFromServer = JsonParser.parseString(response.body()).getAsJsonObject();
        Task taskFromJson = GSON.fromJson(taskFromServer, Task.class);

        Assertions.assertEquals(rightTask, taskFromJson);
    }

    @Test
    public void shouldServerRightUpdateTask() throws IOException, InterruptedException {
        Task rightTask = server.getTaskManagerForTest().getTaskById(0);
        rightTask.setTitle("01");

        String taskRequest = GSON.toJson(rightTask);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/0"))
                .POST(HttpRequest.BodyPublishers.ofString(taskRequest))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        Task taskFromManager = server.getTaskManagerForTest().getTaskById(0);

        Assertions.assertEquals(rightTask, taskFromManager);
    }
}
