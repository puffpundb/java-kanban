package tasks;

public class SubTask extends Task {
    private final Integer epicId;

    public SubTask(String title, String description, Integer epicId) {
        super(title, description);
        this.epicId = epicId;
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
