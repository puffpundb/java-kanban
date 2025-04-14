import manager.TaskManager;
import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

public class Main {
    public static void printTest(TaskManager taskManager) {
        for (Epic task : taskManager.getAllTasks()) {
            if (task.getClass() == Task.class) {
                System.out.println(task);
                System.out.println();

            } else if (task.getClass() == SubTask.class) {
                System.out.println("\t > " + task);

            } else {
                System.out.println(task);

            }
        }
    }

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        taskManager.createNewTask(new Task("Помыть посуду", "Собрать посуду и помыть ее", TaskManager.generateId())); //0
        taskManager.createNewTask(new Task("Помыть полы", "Вымыть пол во всех комнатах", TaskManager.generateId())); //1

        taskManager.createNewTask(new Epic("Купить продукты", "Продукты для салата", TaskManager.generateId())); //2
        taskManager.createNewTask(new SubTask("Сыр", "Гауда", TaskManager.generateId(), 2), 2); //3
        taskManager.createNewTask(new SubTask("Молоко", "Свежее", TaskManager.generateId(), 2), 2); //4

        taskManager.createNewTask(new Epic("Разморозка", "Разморозить продукты", TaskManager.generateId())); //5
        taskManager.createNewTask(new SubTask("Разморозить рыбу", "Положить рыбу в раковину", TaskManager.generateId(), 5), 5); //6

        System.out.println("=====Создали таски:");
        System.out.println();
        printTest(taskManager);

        taskManager.updateTask(1);
        taskManager.updateTask(3);
        taskManager.updateTask(4);
        taskManager.updateTask(6);

        System.out.println();
        System.out.println("=====Апдейтнули таски:");

        printTest(taskManager);

        taskManager.deleteTask(0);
        taskManager.deleteTask(6);
        taskManager.deleteTask(3);

        System.out.println();
        System.out.println("=====Удалили таски:");

        printTest(taskManager);
    }
}
