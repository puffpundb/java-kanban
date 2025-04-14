package tasks;

import manager.Status;

import java.util.ArrayList;

public class Task extends Epic {
    public Task(String title, String description, Integer id) {
        super(title, description, id);
    }

    @Override
    public ArrayList<Integer> getSubsId() {
        return new ArrayList<>();
    }

    @Override
    public void putSubs(SubTask subTask) {}

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", id=" + id +
                '}';
    }
}
