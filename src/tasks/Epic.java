package tasks;

import manager.Status;

import java.util.ArrayList;

public class Epic{
    protected String title;
    protected String description;
    protected Status status;
    protected final Integer id;
    private final ArrayList<Integer> subsId;

    public Epic(String title, String description, Integer id) {
        this.title = title;
        this.description = description;
        this.status = Status.NEW;
        this.id = id;
        this.subsId = new ArrayList<>();
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getId() {
        return this.id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ArrayList<Integer> getSubsId() {
        return subsId;
    }

    public void putSubs(SubTask subTask) {
        this.subsId.add(subTask.getId());
    }

    public void deleteSub(int index) {
        subsId.remove(index);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", id=" + id +
                ", subs=" + subsId +
                '}';
    }
}
