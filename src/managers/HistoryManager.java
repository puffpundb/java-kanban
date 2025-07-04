package managers;

import tasks.Task;

import java.util.ArrayList;

public interface HistoryManager {
    void addToHistory(Task task);

    ArrayList<Task> getHistory();

    void remove(Integer id);

    void clearHistory();

    boolean isContainsInHistory(Integer id);
}
