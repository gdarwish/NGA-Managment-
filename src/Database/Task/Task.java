package Database.Task;

/**
 * This class represent task record in database
 * @author Ali Dali
 */
public class Task {

    private int id;
    private String name;
    private int project;
    private byte open;


    public Task(String name, int project, byte open) {
        this.name = name.trim();
        this.project = project;
        this.open = open;
    }

    public Task(int id, String name, int project, byte open) {
        this.id = id;
        this.name = name.trim();
        this.project = project;
        this.open = open;
    }

    public int getId() {
        return id;
    }

    public Task setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Task setName(String name) {
        this.name = name;
        return this;
    }

    public int getProject() {
        return project;
    }

    public Task setProject(int project) {
        this.project = project;
        return this;
    }

    public byte getOpen() {
        return open;
    }

    public Task setOpen(byte open) {
        this.open = open;
        return this;
    }

    @Override
    public String toString() {
        return String.format("ID: %d Name: %s Project %d Open: %d", id, name, project, open);
    }
}
