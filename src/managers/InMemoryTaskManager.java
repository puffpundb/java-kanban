package managers;

import exceptions.ManagerSaveException;
import tasks.Epic;
import tasks.Status;
import tasks.Task;
import tasks.SubTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public class InMemoryTaskManager implements TaskManager {
    protected int globalId = 0;
    protected final HashMap<Integer, Task> savedTasks = new HashMap<>();
    protected final HashMap<Integer, SubTask> savedSubTasks = new HashMap<>();
    protected final HashMap<Integer, Epic> savedEpics = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    protected final TreeSet<Task> prioritizedTasks = new TreeSet<>();

    private Integer generateId() {
        return globalId++;
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(savedTasks.values());
    }

    @Override
    public ArrayList<Task> getAllSubTasks() {
        return new ArrayList<>(savedSubTasks.values());
    }

    @Override
    public ArrayList<Task> getAllEpics() {
        return new ArrayList<>(savedEpics.values());
    }

    @Override
    public void clearAllTasks() {
        prioritizedTasks.removeIf(task -> task.getType() == Types.TASK);
        savedTasks.clear();
    }

    @Override
    public void clearAllSubTasks() {
        prioritizedTasks.removeIf(task -> task.getType() == Types.SUBTASK);
        savedEpics.values().forEach(epic -> epic.getSubsId().clear());

        savedSubTasks.clear();
    }

    @Override
    public void clearAllEpics() {
        prioritizedTasks.removeIf(task -> task.getType() == Types.SUBTASK);
        savedEpics.clear();
        savedSubTasks.clear();
    }

    @Override
    public Epic getEpicById(Integer id) {
        Epic currentEpic = savedEpics.get(id);
        historyManager.addToHistory(currentEpic);

        return currentEpic;

    }

    @Override
    public Task getTaskById(Integer id) {
        Task currentTask = savedTasks.get(id);
        historyManager.addToHistory(currentTask);

        return currentTask;
    }

    @Override
    public SubTask getSubTaskById(Integer id) {
        SubTask currentSubTask = savedSubTasks.get(id);
        historyManager.addToHistory(currentSubTask);

        return currentSubTask;
    }

    @Override
    public void createNewTask(Task newTask) {
        if (isTaskIntersectedWithOther(newTask)) throw new ManagerSaveException("Задача пересекается с другой");

        newTask.setId(generateId());
        if (newTask.getStartTime() != null) prioritizedTasks.add(newTask);
        savedTasks.put(newTask.getId(), newTask);
    }

    @Override
    public void createNewSubTask(SubTask newSubTask) {
        if (isTaskIntersectedWithOther(newSubTask)) throw new ManagerSaveException("Задача пересекается с другой");

        if (savedEpics.containsKey(newSubTask.getEpicId())) {
            newSubTask.setId(generateId());
            savedSubTasks.put(newSubTask.getId(), newSubTask);
            if (newSubTask.getStartTime() != null) prioritizedTasks.add(newSubTask);

            savedEpics.get(newSubTask.getEpicId()).putSubsId(newSubTask.getId());
            updateEpicStatus(newSubTask.getEpicId());
        } else {
            throw new ManagerSaveException("Невозможно создать подзадачу без эпика");
        }
    }

    @Override
    public void createNewEpic(Epic newEpic) {
        newEpic.setId(generateId());

        savedEpics.put(newEpic.getId(), newEpic);
    }

    @Override
    public void updateTask(Task task) {
        Task oldTask = getTaskById(task.getId());
        prioritizedTasks.remove(oldTask);

        if (isTaskIntersectedWithOther(task)) throw new ManagerSaveException("Задача пересекается с другой");

        savedTasks.put(task.getId(), task);
        if (task.getStartTime() != null) prioritizedTasks.add(task);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        SubTask oldSub = getSubTaskById(subTask.getId());
        prioritizedTasks.remove(oldSub);

        if (isTaskIntersectedWithOther(subTask)) throw new ManagerSaveException("Задача пересекается с другой");

        savedSubTasks.put(subTask.getId(), subTask);
        if (subTask.getStartTime() != null) prioritizedTasks.add(subTask);

        updateEpicStatus(subTask.getEpicId());
    }

    @Override
    public void updateEpic(Epic epic) {
        savedEpics.put(epic.getId(), epic);

        updateEpicStatus(epic.getId());
    }

    protected void updateEpicStatus(Integer id) {
        Epic epic = savedEpics.get(id);

        if (epic.getSubsId().isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        int doneCounter = 0;
        int newCounter = 0;
        int allSubsCount = epic.getSubsId().size();

        Duration epicDuration = Duration.ZERO;
        LocalDateTime epicStartTime = getSubTaskById(epic.getSubsId().getFirst()).getStartTime();

        for (Integer subId : epic.getSubsId()) {
            SubTask subTask = getSubTaskById(subId);

            epicDuration = epicDuration.plus(subTask.getDuration());
            if (subTask.getStartTime().isBefore(epicStartTime)) {
                epicStartTime = subTask.getStartTime();
            }

            if (subTask.getStatus() == Status.DONE) {
                doneCounter++;

            } else if (subTask.getStatus() == Status.NEW) {
                newCounter++;
            }
        }

        epic.setStartTime(epicStartTime);
        epic.setDuration(epicDuration);
        epic.setEndTime(epicStartTime.plus(epicDuration));

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
        Task taskToDelete = getTaskById(id);
        if (taskToDelete != null) prioritizedTasks.remove(taskToDelete);

        savedTasks.remove(id);
    }

    @Override
    public void deleteSubTask(Integer id) {
        SubTask subToDelete = getSubTaskById(id);
        if (subToDelete != null) prioritizedTasks.remove(subToDelete);

        SubTask currentSubTask = savedSubTasks.get(id);
        Epic currentEpic = savedEpics.get(currentSubTask.getEpicId());

        currentEpic.deleteSub(id);
        updateEpicStatus(currentSubTask.getEpicId());

        savedSubTasks.remove(id);
    }

    @Override
    public void deleteEpic(Integer id) {
        Epic currentEpic = savedEpics.get(id);

        prioritizedTasks.removeIf(task -> {
            if (task.getType() != Types.SUBTASK) return false;
            SubTask sub = (SubTask) task;
            return sub.getEpicId().equals(id);
        });

        currentEpic.getSubsId().forEach(savedSubTasks::remove);

        savedEpics.remove(id);
    }

    @Override
    public ArrayList<SubTask> getEpicSubs(Integer id) {
        ArrayList<SubTask> taskList = new ArrayList<>();

        savedEpics.get(id).getSubsId().forEach(subsId -> taskList.add(savedSubTasks.get(subsId)));

        return taskList;
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    private boolean isIntersected(Task t1, Task t2) {
        if (t1.getStartTime() == null || t1.getEndTime() == null ||
                t2.getStartTime() == null || t2.getEndTime() == null) {
            return false;
        }

//        boolean isTimePeriodsIntersected = t1.getStartTime().isBefore(t2.getEndTime()) && t2.getStartTime().isBefore(t1.getEndTime());
//        boolean isTimePeriodsIntersected = (t1.getStartTime().isBefore(t2.getStartTime()) && t1.getEndTime().isBefore(t2.getEndTime()))
//                || (t1.getStartTime().isAfter(t2.getStartTime()) && t1.getEndTime().isAfter(t2.getEndTime()))
//                || (t1.getStartTime().isBefore(t2.getStartTime()) && t1.getEndTime().isAfter(t2.getEndTime()))
//                || (t1.getStartTime().isAfter(t2.getStartTime()) && t1.getEndTime().isBefore(t2.getEndTime()))
//                || (t1.getStartTime().isEqual(t2.getStartTime()) && t1.getEndTime().isEqual(t2.getEndTime()));
        boolean isTimePeriodsIntersected = (t1.getEndTime().isAfter(t2.getStartTime()) && t1.getStartTime().isBefore(t2.getEndTime())) || (t1.getStartTime().isEqual(t2.getStartTime()) && t1.getEndTime().isEqual(t2.getEndTime())) || (t1.getStartTime().isBefore(t2.getStartTime()) && t1.getEndTime().isAfter(t2.getEndTime())) || (t1.getStartTime().isAfter(t2.getStartTime()) && t1.getEndTime().isBefore(t2.getEndTime())) || t1.getStartTime().isBefore(t2.getEndTime()) && t2.getStartTime().isBefore(t1.getEndTime());

        return isTimePeriodsIntersected;
    }

    private boolean isTaskIntersectedWithOther(Task newTask) {
        return getPrioritizedTasks().stream().anyMatch(task -> isIntersected(newTask, task));
    }
}
