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
import java.util.List;

public class HttpMethodGetTest extends HttpTaskServerTest {
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
    public void shouldServerRightGetHistory() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/0"))
                .GET()
                .header("Accept", "application/json")
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        List<Task> rightList = server.getTaskManagerForTest().getHistory();

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/history"))
                .GET()
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        JsonArray listFromServer = JsonParser.parseString(response.body()).getAsJsonArray();
        List<Task> listFromJson = GSON.fromJson(listFromServer, new TypeToken<List<Task>>() {
        }.getType());

        Assertions.assertEquals(rightList, listFromJson);
    }

    @Test
    public void shouldServerRightGetPrioritized() throws IOException, InterruptedException {
        List<Task> rightList = server.getTaskManagerForTest().getPrioritizedTasks();
        JsonArray rightJsonArray = JsonParser.parseString(GSON.toJson(rightList)).getAsJsonArray();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/prioritized"))
                .GET()
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        JsonArray listFromServer = JsonParser.parseString(response.body()).getAsJsonArray();

        Assertions.assertEquals(rightJsonArray, listFromServer);
    }
}
