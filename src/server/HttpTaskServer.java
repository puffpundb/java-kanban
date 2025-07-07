package server;

import com.sun.net.httpserver.HttpServer;
import managers.FileBackedTaskManager;
import managers.Managers;
import managers.TaskManager;
import server.handlers.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class HttpTaskServer {
    private final HttpServer server;
    private final TaskManager taskManager;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.start();

        server.createContext("/tasks", new TasksHandler(taskManager));
        server.createContext("/subtasks", new SubTasksHandler(taskManager));
        server.createContext("/epics", new EpicsHandler(taskManager));
        server.createContext("/history", new HistoryHandler(taskManager));
        server.createContext("/prioritized", new PrioritizedHandler(taskManager));
    }

    public void serverStop(int delay) {
        server.stop(delay);
    }

    public TaskManager getTaskManagerForTest() {
        return taskManager;
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer taskServer = new HttpTaskServer(Managers.getDefault());

        Scanner scanner = new Scanner(System.in);
        System.out.println("e - for server stop");
        String input = scanner.nextLine();
        while (!input.equals("e")) input = scanner.nextLine();

        taskServer.serverStop(0);
    }
}
