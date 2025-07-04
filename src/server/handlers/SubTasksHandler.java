package server.handlers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import managers.TaskManager;
import tasks.SubTask;
import tasks.Task;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class SubTasksHandler extends BaseHttpHandler implements HttpHandler {
    private final TaskManager taskManager;

    public SubTasksHandler(TaskManager taskManager) {
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
                    List<Task> currentSubTaskList = taskManager.getAllSubTasks();

                    sendText(exchange, gson.toJson(currentSubTaskList));
                } else {
                    try {
                        idFromRequest = Integer.parseInt(path[2]);

                        if (taskManager.isContainsSub(idFromRequest)) {
                            Task currentSubTask = taskManager.getSubTaskById(idFromRequest);

                            sendText(exchange, gson.toJson(currentSubTask));
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
                        !taskToCreate.has("startTime") ||
                        !taskToCreate.has("epicId")) {
                    sendIncorrectData(exchange);
                    return;
                }

                String title = taskToCreate.get("title").getAsString();
                String description = taskToCreate.get("description").getAsString();
                Duration duration = Duration.parse(taskToCreate.get("duration").getAsString());
                LocalDateTime startTime = LocalDateTime.parse(taskToCreate.get("startTime").getAsString());
                Integer epicId = taskToCreate.get("epicId").getAsInt();

                SubTask newSubTask = new SubTask(title, description, epicId, duration, startTime);

                if (path.length == 2) {
                    if (!taskManager.isTaskIntersectedWithOther(newSubTask)) {
                        taskManager.createNewSubTask(newSubTask);

                        sendSuccess(exchange, "Подзадача успешно создана");
                    } else {
                        sendHasInteractions(exchange);
                    }
                } else {
                    try {
                        idFromRequest = Integer.parseInt(path[2]);

                        if (taskManager.isContainsSub(idFromRequest)) {
                            if (!taskManager.isTaskIntersectedWithOther(newSubTask)) {
                                taskManager.updateSubTask(newSubTask);

                                sendSuccess(exchange, "Подзадача успешно обновлена");
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
                    taskManager.clearAllSubTasks();

                    sendText(exchange, "Все подзадачи успешно удалены");
                } else {
                    try {
                        idFromRequest = Integer.parseInt(path[2]);

                        if (taskManager.isContainsSub(idFromRequest)) {
                            taskManager.deleteSubTask(idFromRequest);

                            sendText(exchange, "Подзадача успешно удалена");
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
