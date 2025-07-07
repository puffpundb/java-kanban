package server.handlers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class TasksHandler extends BaseHttpHandler {
    private final TaskManager taskManager;

    public TasksHandler(TaskManager taskManager) {
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
                    List<Task> currentTaskList = taskManager.getAllTasks();
                    sendText(exchange, gson.toJson(currentTaskList));
                } else {
                    try {
                        idFromRequest = Integer.parseInt(path[2]);

                        if (taskManager.isContainsTask(idFromRequest)) {
                            Task currentTask = taskManager.getTaskById(idFromRequest);

                            sendText(exchange, gson.toJson(currentTask));
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

                if (!taskToCreate.has("title") ||
                        !taskToCreate.has("description") ||
                        !taskToCreate.has("duration") ||
                        !taskToCreate.has("startTime")) {
                    sendIncorrectData(exchange);
                    return;
                }

                String title = taskToCreate.get("title").getAsString();
                String description = taskToCreate.get("description").getAsString();
                Duration duration = Duration.parse(taskToCreate.get("duration").getAsString());
                LocalDateTime startTime = LocalDateTime.parse(taskToCreate.get("startTime").getAsString());

                Task newTask = new Task(title, description, duration, startTime);

                if (path.length == 2) {
                    if (!taskManager.isTaskIntersectedWithOther(newTask)) {
                        taskManager.createNewTask(newTask);

                        sendSuccess(exchange, "Задача успешно создана");
                    } else {
                        sendHasInteractions(exchange);
                    }
                } else {
                    try {
                        idFromRequest = Integer.parseInt(path[2]);

                        if (taskManager.isContainsTask(idFromRequest)) {
                            if (!taskManager.isTaskIntersectedWithOther(newTask)) {
                                taskManager.updateTask(newTask);

                                sendSuccess(exchange, "Задача успешно обновлена");
                            } else {
                                sendHasInteractions(exchange);
                            }
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
                    taskManager.clearAllTasks();

                    sendText(exchange, "Все задачи успешно удалены");
                } else {
                    try {
                        idFromRequest = Integer.parseInt(path[2]);

                        if (taskManager.isContainsTask(idFromRequest)) {
                            taskManager.deleteTask(idFromRequest);

                            sendText(exchange, "Задача успешно удалена");
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