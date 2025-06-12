package tasks;

import managers.Types;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subsId;

    public Epic(String title, String description) {
        super(title, description);
        this.subsId = new ArrayList<>();
    }

    private Epic(Epic epic) {
        super(epic.getTitle(), epic.getDescription());
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
                '}';
    }
}
