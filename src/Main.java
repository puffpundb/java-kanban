import manager.Status;
import manager.TaskManager;

import tasks.Task;
import tasks.SubTask;
import tasks.Epic;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        System.out.println("Поехали!");

        taskManager.createNewTask(new Task("Почистить рыбу", "жестко почистить")); //0
        taskManager.createNewTask(new Task("Порезать сыр", "жестко порезать")); //1

        taskManager.createNewEpic(new Epic("Помыть посуду", "перед гостями")); //2
        taskManager.createNewSubTask(new SubTask("Помыть вилки","опять же жестко"), 2); //3
        taskManager.createNewSubTask(new SubTask("Помыть ложки","нежнее чем вилки"), 2); //4

        taskManager.createNewEpic(new Epic("Помыть пол", "не везде")); //5
        taskManager.createNewSubTask(new SubTask("Помыть пол в гостиной", "и под диваном"), 5); //6

        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubTasks());

        Task currentTask = taskManager.getTaskById(0);
        currentTask.setStatus(Status.DONE);
        taskManager.updateTask(currentTask);

        SubTask currentSub = taskManager.getSubTaskById(6);
        currentSub.setStatus(Status.DONE);
        taskManager.updateSubTask(currentSub);
        currentSub = taskManager.getSubTaskById(4);
        currentSub.setStatus(Status.DONE);
        taskManager.updateSubTask(currentSub);

        System.out.println();
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubTasks());

        taskManager.deleteTask(0);
        taskManager.deleteEpic(2);

        System.out.println();
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubTasks());
    }
}
