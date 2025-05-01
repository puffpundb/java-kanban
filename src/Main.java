import tasks.Status;
import managers.InMemoryTaskManager;

import tasks.Task;
import tasks.SubTask;
import tasks.Epic;

public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

        System.out.println("Поехали!");

        inMemoryTaskManager.createNewTask(new Task("Почистить рыбу", "жестко почистить")); //0
        inMemoryTaskManager.createNewTask(new Task("Порезать сыр", "жестко порезать")); //1

        inMemoryTaskManager.createNewEpic(new Epic("Помыть посуду", "перед гостями")); //2
        inMemoryTaskManager.createNewSubTask(new SubTask("Помыть вилки","опять же жестко", 2)); //3
        inMemoryTaskManager.createNewSubTask(new SubTask("Помыть ложки","нежнее чем вилки",2 )); //4

        inMemoryTaskManager.createNewEpic(new Epic("Помыть пол", "не везде")); //5
        inMemoryTaskManager.createNewSubTask(new SubTask("Помыть пол в гостиной", "и под диваном", 5)); //6

        System.out.println(inMemoryTaskManager.getAllTasks());
        System.out.println(inMemoryTaskManager.getAllEpics());
        System.out.println(inMemoryTaskManager.getAllSubTasks());

        Task currentTask = inMemoryTaskManager.getTaskById(0);
        currentTask.setStatus(Status.DONE);
        inMemoryTaskManager.updateTask(currentTask);

        SubTask currentSub = inMemoryTaskManager.getSubTaskById(6);
        currentSub.setStatus(Status.DONE);
        inMemoryTaskManager.updateSubTask(currentSub);
        currentSub = inMemoryTaskManager.getSubTaskById(4);
        currentSub.setStatus(Status.DONE);
        inMemoryTaskManager.updateSubTask(currentSub);

        System.out.println();
        System.out.println(inMemoryTaskManager.getAllTasks());
        System.out.println(inMemoryTaskManager.getAllEpics());
        System.out.println(inMemoryTaskManager.getAllSubTasks());

        inMemoryTaskManager.deleteTask(0);
        inMemoryTaskManager.deleteEpic(2);

        System.out.println();
        System.out.println(inMemoryTaskManager.getAllTasks());
        System.out.println(inMemoryTaskManager.getAllEpics());
        System.out.println(inMemoryTaskManager.getAllSubTasks());

        inMemoryTaskManager.clearAllSubTasks();

        System.out.println();
        System.out.println(inMemoryTaskManager.getAllTasks());
        System.out.println(inMemoryTaskManager.getAllEpics());
        System.out.println(inMemoryTaskManager.getAllSubTasks());

        System.out.println(inMemoryTaskManager.getTaskById(0));
        System.out.println(inMemoryTaskManager.getHistory());
        inMemoryTaskManager.getTaskById(0).setTitle("Новый тайтл");
        System.out.println(inMemoryTaskManager.getHistory());
        System.out.println(inMemoryTaskManager.getTaskById(0));
    }
}
