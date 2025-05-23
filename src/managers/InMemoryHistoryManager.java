package managers;

import tasks.Task;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node> viewHistory = new LinkedHashMap<>();
    private Node head;
    private Node tail;

    private void linkLast(Node newNode) {
        if (head == null) {
            head = newNode;
            return;
        }

        if (tail == null) {
            tail = newNode;
            tail.setPrev(head);

        } else {
            newNode.setPrev(tail);
            tail.setNext(newNode);
            tail = newNode;

        }
    }

    @Override
    public void addToHistory(Task task) {
        if (task == null) return;

        Integer currentTaskId = task.getId();

        if (viewHistory.containsKey(currentTaskId)) {
            remove(currentTaskId);
        }

        Node newNode = new Node(task.copy());
        linkLast(newNode);

        viewHistory.put(currentTaskId, newNode);
    }

    @Override
    public ArrayList<Task> getHistory() {
        ArrayList<Task> history = new ArrayList<>();

        if (viewHistory.isEmpty()) return history;

        Node currentNode = head;
        while (currentNode != null) {
            history.add(currentNode.getTask());
            currentNode = currentNode.getNext();

        }

        return history;
    }

    @Override
    public void remove(Integer id) {
        if (viewHistory.get(id).equals(head)) {
            Node aheadNode = head.getNext();
            aheadNode.setPrev(null);
            head = aheadNode;
            return;
        }

        if (viewHistory.get(id).equals(tail)) {
            Node prevNode = tail.getPrev();
            prevNode.setNext(null);
            tail = prevNode;
            return;
        }

        Node prevTask = viewHistory.get(id).getPrev();
        Node nextTask = viewHistory.get(id).getNext();

        prevTask.setNext(nextTask);
        nextTask.setPrev(prevTask);

        viewHistory.remove(id);
    }
}

class Node {
    private Task task;
    private Node prev;
    private Node next;

    public Node (Task task) {
        this.task = task;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Node getPrev() {
        return prev;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(task, node.task) && Objects.equals(prev, node.prev) && Objects.equals(next, node.next);
    }
}