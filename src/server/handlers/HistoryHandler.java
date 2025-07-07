package server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler {
    private final TaskManager taskManager;

    public HistoryHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String[] path = exchange.getRequestURI().getPath().split("/");
        HttpMethods method = HttpMethods.valueOf(exchange.getRequestMethod());
        Integer idFromRequest;

        switch (method) {
            case HttpMethods.GET -> {
                List<Task> currentHistory = taskManager.getHistory();

                sendText(exchange, gson.toJson(currentHistory));
            }
            default -> sendIncorrectMethod(exchange);
        }
    }
}
