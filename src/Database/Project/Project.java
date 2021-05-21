package Database.Project;

import java.sql.Date;
/**
 * This class represent project record in database
 * @author Ali Dali
 */
public class Project {

    private int id;
    private String title;
    private String description;
    private int status;
    private int category;
    private int priority;
    private Date startDate;
    private Date dueDate;
    private byte open;

    public Project(String title, String description, int status, int category, int priority, Date startDate, Date dueDate, byte open) {
        this.title = title.trim();
        this.description = description.trim();
        this.status = status;
        this.category = category;
        this.priority = priority;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.open = open;
    }

    public Project(int id, String title, String description, int status, int category, int priority, Date startDate, Date dueDate, byte open) {
        this.id = id;
        this.title = title.trim();
        this.description = description.trim();
        this.status = status;
        this.category = category;
        this.priority = priority;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.open = open;
    }

    public int getId() {
        return id;
    }

    public Project setId(int id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Project setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Project setDescription(String description) {
        this.description = description;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public Project setStatus(int status) {
        this.status = status;
        return this;
    }

    public int getCategory() {
        return category;
    }

    public Project setCategory(int category) {
        this.category = category;
        return this;
    }

    public int getPriority() {
        return priority;
    }

    public Project setPriority(int priority) {
        this.priority = priority;
        return this;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Project setStartDate(Date startDate) {
        this.startDate = startDate;
        return this;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public Project setDueDate(Date dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public byte getOpen() {
        return open;
    }

    public Project setOpen(byte open) {
        this.open = open;
        return this;
    }

    @Override
    public String toString() {
        return this.getTitle();
    }
}
