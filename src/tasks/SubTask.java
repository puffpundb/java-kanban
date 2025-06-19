package tasks;

import managers.Types;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private final Integer epicId;

    public SubTask(String title, String description, Integer epicId, Duration duration, LocalDateTime startTime) {
        super(title, description, duration, startTime);
        this.epicId = epicId;
    }

    private SubTask(SubTask subTask) {
        super(subTask.getTitle(), subTask.getDescription(), subTask.getDuration(), subTask.getStartTime());
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
    public Types getType() {
        return Types.SUBTASK;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "epicId=" + epicId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", id=" + id +
                ", duration=" + duration +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
