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
    private static final Path saveDirectory = Paths.get("saves");
    private static final Path fileToSave = saveDirectory.resolve("saveFile" + ".csv");

    public HttpTaskServer(TaskManager taskManager) {
        this.taskManager = taskManager;
        try {
            server = HttpServer.create(new InetSocketAddress(8080), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        server.start();

        server.createContext("/tasks", new TasksHandler(taskManager));
        server.createContext("/subtasks" , new SubTasksHandler(taskManager));
        server.createContext("/epics", new EpicsHandler(taskManager));
        server.createContext("/history", new HistoryHandler(taskManager));
        server.createContext("/prioritized", new PrioritizedHandler(taskManager));
    }

    public void serverStop(int delay) {
        server.stop(delay);
    }

    private static boolean isFileExist() {
        return Files.exists(saveDirectory) && Files.exists(fileToSave);
    }

    private static Path createFile() {
        try {
            if (!Files.exists(saveDirectory)) Files.createDirectories(saveDirectory);
            if (!Files.exists(fileToSave)) Files.createFile(fileToSave);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return fileToSave;
    }

    public TaskManager getTaskManagerForTest(){
        return taskManager;
    }

    public static void main(String[] args) {
        HttpTaskServer taskServer;

        if (isFileExist()) taskServer = new HttpTaskServer(FileBackedTaskManager.loadFromFile(fileToSave));
        else taskServer = new HttpTaskServer(Managers.getFileBackedTaskManager(createFile()));

        Scanner scanner = new Scanner(System.in);
        System.out.println("e - for server stop");
        String input = scanner.nextLine();
        while (!input.equals("e")) input = scanner.nextLine();

        taskServer.serverStop(0);
    }
}
