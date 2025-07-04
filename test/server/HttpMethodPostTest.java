package server;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpMethodPostTest extends HttpTaskServerTest {
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
    public void shouldServerRightPostEpicAndSub() throws IOException, InterruptedException {
        Epic rightEpic = new Epic("1", "1");
        SubTask rightSub = new SubTask("2", "2", 1, Duration.parse("PT30M"), LocalDateTime.parse("2025-01-21T10:00"));

        rightEpic.setId(1);
        rightEpic.putSubsId(2);
        rightEpic.setStartTime(LocalDateTime.parse("2025-01-21T10:00"));
        rightEpic.setEndTime(LocalDateTime.parse("2025-01-21T10:30"));
        rightEpic.setDuration(Duration.parse("PT30M"));

        rightSub.setId(2);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/1"))
                .GET()
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        JsonObject epicFromServer = JsonParser.parseString(response.body()).getAsJsonObject();
        Epic epicFromJson = GSON.fromJson(epicFromServer, Epic.class);

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks/2"))
                .GET()
                .header("Accept", "application/json")
                .build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        JsonObject subFromServer = JsonParser.parseString(response.body()).getAsJsonObject();
        SubTask subFromJson = GSON.fromJson(subFromServer, SubTask.class);

        Assertions.assertEquals(rightSub, subFromJson);
        Assertions.assertEquals(rightEpic, epicFromJson);
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

        request = HttpRequest.newBuilder()
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
    public void shouldServerRightUpdateEpic() throws IOException, InterruptedException {
        Epic rightEpic = server.getTaskManagerForTest().getEpicById(1);
        rightEpic.setTitle("10");

        String taskRequest = GSON.toJson(rightEpic);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/1"))
                .POST(HttpRequest.BodyPublishers.ofString(taskRequest))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/1"))
                .GET()
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        JsonObject taskFromServer = JsonParser.parseString(response.body()).getAsJsonObject();
        Task taskFromJson = GSON.fromJson(taskFromServer, Epic.class);

        Assertions.assertEquals(rightEpic, taskFromJson);
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

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks/2"))
                .GET()
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        JsonObject taskFromServer = JsonParser.parseString(response.body()).getAsJsonObject();
        Task taskFromJson = GSON.fromJson(taskFromServer, SubTask.class);

        Assertions.assertEquals(rightSub, taskFromJson);
    }
}
