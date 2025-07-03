package server;

import com.sun.net.httpserver.HttpServer;
import managers.Managers;
import managers.TaskManager;
import server.handlers.TasksHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class HttpTaskServer {
    private final HttpServer server;
    private TaskManager taskManager;

    public HttpTaskServer(TaskManager taskManager) {
        this.taskManager = taskManager;
        try {
            server = HttpServer.create(new InetSocketAddress(8080), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        server.start();

        server.createContext("/tasks", new TasksHandler(taskManager));
    }

    public void serverStop() {
        server.stop(0);
    }

    private static Path createFile(String fileName) {
        Path saveDirectory = Paths.get("outs");
        Path fileToSave = saveDirectory.resolve(fileName + ".csv");

        try {
            if (!Files.exists(saveDirectory)) Files.createDirectories(saveDirectory);
            if (!Files.exists(fileToSave)) Files.createFile(fileToSave);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return fileToSave;
    }

    public static void main(String[] args) {
        HttpTaskServer taskServer = new HttpTaskServer(Managers.getFileBackedTaskManager(createFile("saveFile")));

        Scanner scanner = new Scanner(System.in);
        System.out.println("e - for server stop");
        String input = scanner.nextLine();
        while (!input.equals("e")) input = scanner.nextLine();

        taskServer.serverStop();
    }
}
