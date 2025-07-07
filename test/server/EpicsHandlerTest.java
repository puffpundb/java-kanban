package server;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
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
import java.util.List;

class EpicsHandlerTest extends HttpTaskServerTest {
    @Test
    public void shouldServerRightDeleteEpic() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/1"))
                .DELETE()
                .header("Accept", "application/json")
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        List<Task> tasksList = server.getTaskManagerForTest().getAllEpics();

        Assertions.assertEquals(0, tasksList.size());
    }

    @Test
    public void shouldServerRightGetEpic() throws IOException, InterruptedException {
        Epic rightEpic = server.getTaskManagerForTest().getEpicById(1);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/1"))
                .GET()
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        JsonObject epicFromServer = JsonParser.parseString(response.body()).getAsJsonObject();
        Epic epicFromJson = GSON.fromJson(epicFromServer, Epic.class);

        Assertions.assertEquals(rightEpic, epicFromJson);
    }

    @Test
    public void shouldServerRightGetEpicsArray() throws IOException, InterruptedException {
        List<Task> rightEpicList = server.getTaskManagerForTest().getAllEpics();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
                .GET()
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        JsonArray listFromServer = JsonParser.parseString(response.body()).getAsJsonArray();
        List<Task> listFromJson = GSON.fromJson(listFromServer, new TypeToken<List<Epic>>() {}.getType());

        Assertions.assertEquals(rightEpicList, listFromJson);
    }

    @Test
    public void shouldServerRightGetEpicSubs() throws IOException, InterruptedException {
        List<SubTask> rightList = server.getTaskManagerForTest().getEpicSubs(1);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/1/subtasks"))
                .GET()
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        JsonArray listFromServer = JsonParser.parseString(response.body()).getAsJsonArray();
        List<Task> listFromJson = GSON.fromJson(listFromServer, new TypeToken<List<SubTask>>() {
        }.getType());

        Assertions.assertEquals(rightList, listFromJson);
    }

    @Test
    public void shouldServerRightPostEpic() throws IOException, InterruptedException {
        Epic rightEpic = new Epic("1", "1");
        rightEpic.setId(1);
        rightEpic.putSubsId(2);
        rightEpic.setStartTime(LocalDateTime.parse("2025-01-21T10:00"));
        rightEpic.setEndTime(LocalDateTime.parse("2025-01-21T10:30"));
        rightEpic.setDuration(Duration.parse("PT30M"));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/1"))
                .GET()
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        JsonObject epicFromServer = JsonParser.parseString(response.body()).getAsJsonObject();
        Epic epicFromJson = GSON.fromJson(epicFromServer, Epic.class);

        Assertions.assertEquals(rightEpic, epicFromJson);
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

        Epic taskFromManager = server.getTaskManagerForTest().getEpicById(1);

        Assertions.assertEquals(rightEpic, taskFromManager);
    }
}