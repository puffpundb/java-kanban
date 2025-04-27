package tasks;

public class SubTask extends Task {
    private final Integer epicId;

    public SubTask(String title, String description, Integer epicId) {
        super(title, description);
        this.epicId = epicId;
    }

    private SubTask(SubTask subTask) {
        super(subTask.getTitle(), subTask.getDescription());
        this.status = subTask.getStatus();
        this.id = subTask.getId();
        this.epicId = subTask.getEpicId();
    }

    @Override
    public SubTask copy() {
        return new SubTask(this);
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
