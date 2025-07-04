package server.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
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
            case HttpMethods.DELETE -> {
                if (path.length == 2) {
                    taskManager.clearHistory();

                    sendText(exchange, "История успешно очищена");
                } else {
                    try {
                        idFromRequest = Integer.parseInt(path[2]);

                        if (taskManager.isContainsInHistory(idFromRequest)) {
                            taskManager.removeFromHistory(idFromRequest);

                            sendText(exchange, "Задача успешно удалена из истории");
                        } else {
                            sendNotFound(exchange);
                        }
                    } catch (NumberFormatException e) {
                        sendNotFound(exchange);
                    }
                }
            }
            default -> sendIncorrectMethod(exchange);
        }
    }
}
