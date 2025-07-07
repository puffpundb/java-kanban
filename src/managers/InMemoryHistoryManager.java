package managers;

import tasks.Task;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node> viewHistory = new LinkedHashMap<>();
    private Node head;
    private Node tail;

    private void linkLast(Node newNode) {
        if (tail == null) {
            head = newNode;

        } else {
            tail.next = newNode;
            newNode.prev = tail;

        }

        tail = newNode;
    }

    @Override
    public void addToHistory(Task task) {
        if (task == null) return;

        Integer currentTaskId = task.getId();

        remove(currentTaskId);

        Node newNode = new Node(task.copy());
        linkLast(newNode);

        viewHistory.put(currentTaskId, newNode);
    }

    @Override
    public ArrayList<Task> getHistory() {
        ArrayList<Task> history = new ArrayList<>();

        Node currentNode = head;
        while (currentNode != null) {
            history.add(currentNode.task);
            currentNode = currentNode.next;

        }

        return history;
    }

    @Override
    public void clearHistory() {
        viewHistory.clear();
        head = null;
        tail = null;
    }

    @Override
    public void remove(Integer id) {
        if (!viewHistory.containsKey(id)) return;

        if (head == tail) {
            head = null;
            tail = null;

        } else if (viewHistory.get(id) == head) {
            Node aheadNode = head.next;
            aheadNode.prev = null;
            head = aheadNode;

        } else if (viewHistory.get(id) == tail) {
            Node prevNode = tail.prev;
            prevNode.next = null;
            tail = prevNode;

        } else {
            Node prevTask = viewHistory.get(id).prev;
            Node nextTask = viewHistory.get(id).next;

            prevTask.next = nextTask;
            nextTask.prev = prevTask;
        }

        viewHistory.remove(id);
    }

    @Override
    public boolean isContainsInHistory(Integer id) {
        return viewHistory.containsKey(id);
    }

    private static class Node {
        private final Task task;
        private Node prev;
        private Node next;

        public Node(Task task) {
            this.task = task;
        }
    }
}

