package server.handlers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.util.List;

public class EpicsHandler extends BaseHttpHandler {
    private final TaskManager taskManager;

    public EpicsHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String[] path = exchange.getRequestURI().getPath().split("/");
        HttpMethods method = HttpMethods.valueOf(exchange.getRequestMethod());
        Integer idFromRequest;

        switch (method) {
            case HttpMethods.GET -> {
                if (path.length == 2) {
                    List<Task> currentEpicsList = taskManager.getAllEpics();

                    sendText(exchange, gson.toJson(currentEpicsList));
                } else if (path.length == 3) {
                    try {
                        idFromRequest = Integer.parseInt(path[2]);

                        if (taskManager.isContainsEpic(idFromRequest)) {
                            Epic currentEpic = taskManager.getEpicById(idFromRequest);

                            sendText(exchange, gson.toJson(currentEpic));
                        } else {
                            sendNotFound(exchange);
                        }
                    } catch (NumberFormatException e) {
                        sendNotFound(exchange);
                    }
                } else {
                    try {
                        idFromRequest = Integer.parseInt(path[2]);

                        if (taskManager.isContainsEpic(idFromRequest)) {
                            List<SubTask> epicSubsList = taskManager.getEpicSubs(idFromRequest);

                            sendText(exchange, gson.toJson(epicSubsList));
                        } else {
                            sendNotFound(exchange);
                        }
                    } catch (NumberFormatException e) {
                        sendNotFound(exchange);
                    }
                }
            }
            case HttpMethods.POST -> {
                JsonElement jsonElement = JsonParser.parseString(new String(exchange.getRequestBody().readAllBytes()));
                JsonObject taskToCreate;
                if (jsonElement.isJsonObject()) {
                    taskToCreate = jsonElement.getAsJsonObject();
                } else {
                    sendIncorrectData(exchange);
                    return;
                }

                if (!taskToCreate.has("title") || !taskToCreate.has("description")) {
                    sendIncorrectData(exchange);
                    return;
                }

                String title = taskToCreate.get("title").getAsString();
                String description = taskToCreate.get("description").getAsString();

                Epic newEpic = new Epic(title, description);

                if (path.length == 2) {
                    taskManager.createNewEpic(newEpic);

                    sendSuccess(exchange, "Эпик успешно создан");
                } else {
                    try {
                        idFromRequest = Integer.parseInt(path[2]);

                        if (taskManager.isContainsEpic(idFromRequest)) {
                            taskManager.updateEpic(newEpic);

                            sendSuccess(exchange, "Эпик успешно обновлен");
                        } else {
                            sendNotFound(exchange);
                        }
                    } catch (NumberFormatException e) {
                        sendNotFound(exchange);
                    }
                }
            }
            case HttpMethods.DELETE -> {
                if (path.length == 2) {
                    taskManager.clearAllEpics();

                    sendText(exchange, "Все эпики успешно удалены");
                } else {
                    try {
                        idFromRequest = Integer.parseInt(path[2]);

                        if (taskManager.isContainsEpic(idFromRequest)) {
                            taskManager.deleteEpic(idFromRequest);

                            sendText(exchange, "Эпик успешно удален");
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
