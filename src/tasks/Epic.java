package tasks;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private final ArrayList<Integer> subsId;

    public Epic(String title, String description, Integer id) {
        super(title, description, id);
        this.subsId = new ArrayList<>();
    }

    public ArrayList<Integer> getSubsId() {
        return subsId;
    }

    public void putSubsId(Integer id) {
        this.subsId.add(id);
    }

    public void deleteSub(Integer id) {
        int toDelete = 0;

        for (Integer subId : subsId) {
            if (Objects.equals(subId, id)) {
                subsId.remove(toDelete);
                return;
            }

            toDelete++;
        }
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
