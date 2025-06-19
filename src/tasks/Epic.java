package tasks;

import managers.Types;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subsId;

    public Epic(String title, String description) {
        super(title, description, Duration.ZERO, null);
        this.subsId = new ArrayList<>();
    }

    private Epic(Epic epic) {
        super(epic.getTitle(), epic.getDescription(), epic.getDuration(), epic.getStartTime());
        this.status = epic.getStatus();
        this.id = epic.getId();
        this.subsId = new ArrayList<>(epic.subsId);

    }

    @Override
    public Epic copy() {
        return new Epic(this);
    }

    public ArrayList<Integer> getSubsId() {
        return subsId;
    }

    public void putSubsId(Integer id) {
        subsId.add(id);
    }

    public void deleteSub(Integer id) {
        subsId.remove(id);
    }

    @Override
    public Types getType() {
        return Types.EPIC;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subsId=" + subsId +
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
