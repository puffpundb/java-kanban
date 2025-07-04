package server.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import server.handlers.adapters.DurationAdapter;
import server.handlers.adapters.LocalDateTimeAdapter;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public abstract class BaseHttpHandler {
    protected Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    public void sendText(HttpExchange exchange, String text) throws IOException {
        byte[] response = text.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);
        }
    }

    public void sendNotFound(HttpExchange exchange) throws IOException {
        byte[] response = "Объект не найден или был указан некорректный id".getBytes(StandardCharsets.UTF_8);

        exchange.sendResponseHeaders(404, response.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);
        }
    }

    public void sendHasInteractions(HttpExchange exchange) throws IOException {
        byte[] response = "Задача пересекается с уже существующей".getBytes(StandardCharsets.UTF_8);

        exchange.sendResponseHeaders(406, response.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);
        }
    }

    public void sendIncorrectData(HttpExchange exchange) throws IOException {
        byte[] response = "Некорректный формат задачи".getBytes(StandardCharsets.UTF_8);

        exchange.sendResponseHeaders(500, response.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);
        }
    }

    public void sendSuccess(HttpExchange exchange, String text) throws IOException {
        byte[] response = text.getBytes(StandardCharsets.UTF_8);

        exchange.sendResponseHeaders(201, response.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);
        }
    }

    public void sendIncorrectMethod(HttpExchange exchange) throws IOException {
        byte[] response = "Некорректный http-метод".getBytes(StandardCharsets.UTF_8);

        exchange.sendResponseHeaders(500, response.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);
        }
    }
}
