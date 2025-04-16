import manager.TaskManager;
import tasks.Task;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        System.out.println("Поехали!");

        taskManager.createNewTask("Почистить рыбу", "жестко почистить"); //0
        taskManager.createNewTask("Порезать сыр", "жестко порезать"); //1

        taskManager.createNewEpic("Помыть посуду", "перед гостями"); //2
        taskManager.createNewSubTask("Помыть вилки","опять же жестко", 2); //3
        taskManager.createNewSubTask("Помыть ложки","нежнее чем вилки", 2); //4

        taskManager.createNewEpic("Помыть пол", "не везде"); //5
        taskManager.createNewSubTask("Помыть пол в гостиной", "и под диваном", 5); //6

        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubTasks());

        taskManager.updateTask(0);
        taskManager.updateSubTask(6);
        taskManager.updateSubTask(4);

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
