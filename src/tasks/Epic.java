package tasks;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subsId;

    public Epic(String title, String description) {
        super(title, description);
        this.subsId = new ArrayList<>();
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
