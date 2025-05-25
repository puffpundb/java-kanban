package managers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;

class HistoryManagerTest {
    public static HistoryManager historyManager;
    public static Task currentTask;
    public static Epic currentEpic;
    public static SubTask currentSubTask;

    @BeforeAll
    public static void beforeAll() {
        historyManager = Managers.getDefaultHistory();
        currentTask = new Task("0", "0");
        currentEpic = new Epic("1", "1");
        currentSubTask = new SubTask("2", "2", 1);

        currentTask.setId(0);
        currentEpic.setId(1);
        currentSubTask.setId(2);

        historyManager.addToHistory(currentTask);
        historyManager.addToHistory(currentEpic);
        historyManager.addToHistory(currentSubTask);
    }

    @Test
    public void shouldHistorySaveTaskCopy() {
        currentTask.setTitle("1");
        currentTask.setDescription("1");
        currentTask.setStatus(Status.DONE);

        Assertions.assertNotEquals(currentTask, historyManager.getHistory().getFirst());
    }

    @Test
    public void shouldHistorySaveEpicCopy() {
        currentEpic.setTitle("2");
        currentEpic.setDescription("2");
        currentEpic.setStatus(Status.DONE);
        currentEpic.putSubsId(1);

        Assertions.assertNotEquals(currentEpic, historyManager.getHistory().get(1));
        Assertions.assertNotEquals(currentEpic.getSubsId(), ((Epic) historyManager.getHistory().get(1)).getSubsId());
    }

    @Test
    public void shouldHistorySaveSubCopy() {
        currentSubTask.setTitle("3");
        currentSubTask.setDescription("3");
        currentSubTask.setStatus(Status.DONE);

        Assertions.assertNotEquals(currentSubTask, historyManager.getHistory().get(2));
    }

    @Test
    public void shouldHistoryManagerReturnRightHistoryList() {
        ArrayList<Task> rightList = new ArrayList<>();
        rightList.add(currentTask);
        rightList.add(currentEpic);
        rightList.add(currentSubTask);

        Assertions.assertEquals(rightList, historyManager.getHistory());
    }

    @Test
    public void shouldHistoryManagerDeleteDuplicates() {
        ArrayList<Task> rightList = new ArrayList<>();
        rightList.add(currentEpic);
        rightList.add(currentSubTask);
        rightList.add(currentTask);

        HistoryManager newHistoryManager = Managers.getDefaultHistory();
        newHistoryManager.addToHistory(currentTask);
        newHistoryManager.addToHistory(currentEpic);
        newHistoryManager.addToHistory(currentSubTask);
        newHistoryManager.addToHistory(currentTask);

        Assertions.assertEquals(rightList, newHistoryManager.getHistory());
    }

    @Test
    public void shouldHistoryManagerRightRemoveHead() {
        ArrayList<Task> rightList = new ArrayList<>();
        rightList.add(currentEpic);
        rightList.add(currentSubTask);

        HistoryManager newHistoryManager = Managers.getDefaultHistory();
        newHistoryManager.addToHistory(currentTask);
        newHistoryManager.addToHistory(currentEpic);
        newHistoryManager.addToHistory(currentSubTask);
        newHistoryManager.remove(0);

        Assertions.assertEquals(rightList, newHistoryManager.getHistory());
    }

    @Test
    public void shouldHistoryManagerRightRemoveTail() {
        ArrayList<Task> rightList = new ArrayList<>();
        rightList.add(currentTask);
        rightList.add(currentEpic);

        HistoryManager newHistoryManager = Managers.getDefaultHistory();
        newHistoryManager.addToHistory(currentTask);
        newHistoryManager.addToHistory(currentEpic);
        newHistoryManager.addToHistory(currentSubTask);
        newHistoryManager.remove(2);

        Assertions.assertEquals(rightList, newHistoryManager.getHistory());
    }

    @Test
    public void shouldHistoryManagerRightRemoveMiddle() {
        ArrayList<Task> rightList = new ArrayList<>();
        rightList.add(currentTask);
        rightList.add(currentSubTask);

        HistoryManager newHistoryManager = Managers.getDefaultHistory();
        newHistoryManager.addToHistory(currentTask);
        newHistoryManager.addToHistory(currentEpic);
        newHistoryManager.addToHistory(currentSubTask);
        newHistoryManager.remove(1);

        Assertions.assertEquals(rightList, newHistoryManager.getHistory());
    }

    @Test
    public void shouldHistoryManagerRightRemoveFromEmptyHistory() {
        ArrayList<Task> rightList = new ArrayList<>();

        HistoryManager newHistoryManager = Managers.getDefaultHistory();
        newHistoryManager.remove(0);

        Assertions.assertEquals(rightList, newHistoryManager.getHistory());
    }
}