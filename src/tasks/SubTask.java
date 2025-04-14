package tasks;

public class SubTask extends Task{
    private final Integer epicId;

    public SubTask(String title, String description, Integer id, Integer epicId) {
        super(title, description, id);
        this.epicId = epicId;
    }

    public SubTask(SubTask oldObject) {
        this(oldObject.title, oldObject.description, oldObject.getId(), oldObject.getEpicId());
        this.status = oldObject.getStatus();
    }

    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "epicId=" + epicId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", id=" + id +
                '}';
    }
}
