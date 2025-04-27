package managers;

import tasks.Epic;
import tasks.Task;
import tasks.SubTask;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private int globalId = 0;
    private final HashMap<Integer, Task> savedTasks = new HashMap<>();
    private final HashMap<Integer, SubTask> savedSubTasks = new HashMap<>();
    private final HashMap<Integer, Epic> savedEpics = new HashMap<>();
    private final HistoryManager<Task> historyManager = Managers.getDefaultHistory();

    private Integer generateId() {
        return globalId++;
    }

    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        for (Task currentTask : savedTasks.values()) {
            historyManager.addToHistory(currentTask);
        }

        return new ArrayList<>(savedTasks.values());
    }

    @Override
    public ArrayList<Task> getAllSubTasks() {
        for (Task currentSubTask : savedSubTasks.values()) {
            historyManager.addToHistory(currentSubTask);

        }

        return new ArrayList<>(savedSubTasks.values());
    }

    @Override
    public ArrayList<Task> getAllEpics() {
        for (Task currentEpic : savedEpics.values()) {
            historyManager.addToHistory(currentEpic);
        }

        return new ArrayList<>(savedEpics.values());
    }

    @Override
    public void clearAllTasks() {
        savedTasks.clear();
    }

    @Override
    public void clearAllSubTasks() {
        for (Epic currnetEpic : savedEpics.values()) {
            currnetEpic.getSubsId().clear();
        }

        savedSubTasks.clear();
    }

    @Override
    public void clearAllEpics() {
        savedEpics.clear();
        savedSubTasks.clear();
    }

    @Override
    public Epic getEpicById(Integer id) {
        historyManager.addToHistory(savedEpics.get(id));

        return savedEpics.get(id);
    }

    @Override
    public Task getTaskById(Integer id) {
        historyManager.addToHistory(savedTasks.get(id));

        return savedTasks.get(id);
    }

    @Override
    public SubTask getSubTaskById(Integer id) {
        historyManager.addToHistory(savedSubTasks.get(id));

        return savedSubTasks.get(id);
    }

    @Override
    public void createNewTask(Task newTask) {
        newTask.setId(generateId());
        savedTasks.put(newTask.getId(), newTask);
    }

    @Override
    public void createNewSubTask(SubTask newSubTask) {
        newSubTask.setId(generateId());

        if (savedEpics.containsKey(newSubTask.getEpicId())) {
            savedSubTasks.put(newSubTask.getId(), newSubTask);
            savedEpics.get(newSubTask.getEpicId()).putSubsId(newSubTask.getId());
            updateEpicStatus(newSubTask.getEpicId());
        }
    }

    @Override
    public void createNewEpic(Epic newEpic) {
        newEpic.setId(generateId());

        savedEpics.put(newEpic.getId(), newEpic);
    }

    @Override
    public void updateTask(Task task) {
        savedTasks.put(task.getId(), task);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        savedSubTasks.put(subTask.getId(), subTask);

        updateEpicStatus(subTask.getEpicId());
    }

    @Override
    public void updateEpic(Epic epic) {
        savedEpics.put(epic.getId(), epic);

        updateEpicStatus(epic.getId());
    }

    @Override
    public void updateEpicStatus(Integer id) {
        Epic epic = savedEpics.get(id);

        if (epic.getSubsId().isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        int doneCounter = 0;
        int newCounter = 0;
        int allSubsCount = epic.getSubsId().size();

        for (Integer subId : epic.getSubsId()) {
            SubTask subTask = getSubTaskById(subId);

            if (subTask.getStatus() == Status.DONE) {
                doneCounter++;

            } else if (subTask.getStatus() == Status.NEW) {
                newCounter++;
            }
        }

        if (doneCounter == allSubsCount) {
            epic.setStatus(Status.DONE);

        } else if (newCounter == allSubsCount) {
            epic.setStatus(Status.NEW);

        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public void deleteTask(Integer id) {
        savedTasks.remove(id);
    }

    @Override
    public void deleteSubTask(Integer id) {
        SubTask currentSubTask = savedSubTasks.get(id);
        Epic currentEpic = savedEpics.get(currentSubTask.getEpicId());

        currentEpic.deleteSub(id);
        updateEpicStatus(currentSubTask.getEpicId());

        savedSubTasks.remove(id);
    }

    @Override
    public void deleteEpic(Integer id) {
        Epic currentEpic = savedEpics.get(id);

        for (Integer currentEpicSubsId : currentEpic.getSubsId()) {
            savedSubTasks.remove(currentEpicSubsId);
        }

        savedEpics.remove(id);
    }

    @Override
    public ArrayList<SubTask> getEpicSubs(Integer id) {
        Epic epic = savedEpics.get(id);
        ArrayList<SubTask> taskList = new ArrayList<>();

        for (Integer subId : epic.getSubsId()) {
            taskList.add(savedSubTasks.get(subId));
        }

        return taskList;
    }

}