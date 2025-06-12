package managers;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;

public interface TaskManager {

    ArrayList<Task> getAllTasks();

    ArrayList<Task> getAllSubTasks();

    ArrayList<Task> getAllEpics();

    void clearAllTasks();

    void clearAllSubTasks();

    void clearAllEpics();

    Epic getEpicById(Integer id);

    Task getTaskById(Integer id);

    SubTask getSubTaskById(Integer id);

    void createNewTask(Task newTask);

    void createNewSubTask(SubTask newSubTask);

    void createNewEpic(Epic newEpic);

    void updateTask(Task task);

    void updateSubTask(SubTask subTask);

    void updateEpic(Epic epic);

    void deleteTask(Integer id);

    void deleteSubTask(Integer id);

    void deleteEpic(Integer id);

    ArrayList<SubTask> getEpicSubs(Integer id);

    public ArrayList<Task> getHistory();

    public void putTaskToSave(Task task);
}
