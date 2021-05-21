package models;

import Database.Project.Project;
import Database.Project.ProjectDAO;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;

public class ProjectModel {

    ProjectDAO projectDAO;

    public ProjectModel() {
        this.projectDAO = ProjectDAO.getInstance();
    }
}
