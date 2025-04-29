package managers;

import tasks.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private final ArrayList<Task> viewHistory = new ArrayList<>();
    private final int MAX_HISTORY_SIZE = 10;


    @Override
    public void addToHistory(Task task) {
        if (task == null) return;

        if (viewHistory.size() == MAX_HISTORY_SIZE) {
            viewHistory.removeFirst();

        }

        viewHistory.add(task.copy());
    }

    @Override
    public ArrayList<Task> getHistory() {
        return viewHistory;
    }
}
