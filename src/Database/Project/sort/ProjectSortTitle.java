package Database.Project.sort;

import Database.Project.Project;

import java.util.Comparator;

public class ProjectSortTitle implements Comparator<Project> {

    @Override
    public int compare(Project o1, Project o2) {
        return o1.getTitle().compareTo(o2.getTitle());
    }
}
