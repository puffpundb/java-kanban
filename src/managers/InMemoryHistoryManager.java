package managers;

import tasks.Epic;
import tasks.SubTask;
import tasks.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager<Task> {
    private final ArrayList<Task> viewHistory = new ArrayList<>();


    @Override
    public void addToHistory(Task task) {
        if (viewHistory.size() > 9) {
            viewHistory.removeFirst();

        }

        viewHistory.add(task.copy());
    }

    @Override
    public ArrayList<Task> getHistory() {
        return viewHistory;
    }

    @Override
    public void clearHistory() {
        viewHistory.clear();
    }
}
