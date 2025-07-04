package server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.List;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public PrioritizedHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        HttpMethods method = HttpMethods.valueOf(exchange.getRequestMethod());

        switch (method) {
            case HttpMethods.GET -> {
                List<Task> currentPrioritized = taskManager.getPrioritizedTasks();

                sendText(exchange, gson.toJson(currentPrioritized));
            }
            default -> sendIncorrectMethod(exchange);
        }
    }
}
