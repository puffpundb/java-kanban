package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import managers.Managers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import server.handlers.adapters.DurationAdapter;
import server.handlers.adapters.LocalDateTimeAdapter;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;


abstract class HttpTaskServerTest {
    public final Gson GSON = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter()).
            create();
    public HttpTaskServer server;
    public HttpClient client;

    @BeforeEach
    public void beforeEach() throws IOException, InterruptedException {
        server = new HttpTaskServer(Managers.getDefault());
        client = HttpClient.newHttpClient();

        String taskRequest = GSON.toJson(new Task("0", "0", Duration.parse("PT30M"), LocalDateTime.parse("2026-01-21T10:00")));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(taskRequest))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        taskRequest = GSON.toJson(new Epic("1", "1"));
        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
                .POST(HttpRequest.BodyPublishers.ofString(taskRequest))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        taskRequest = GSON.toJson(new SubTask("2", "2", 1, Duration.parse("PT30M"), LocalDateTime.parse("2025-01-21T10:00")));
        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .POST(HttpRequest.BodyPublishers.ofString(taskRequest))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    @AfterEach
    public void afterEach() {
        server.serverStop(0);
    }
}