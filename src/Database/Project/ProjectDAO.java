package Database.Project;

import Const.Const;
import Database.CSP.CSP;
import Database.CSP.Category.Category;
import Database.CSP.Priority.Priority;
import Database.CSP.Status.Status;
import Database.DAO;
import Database.DatabaseConnection;

import javax.xml.crypto.Data;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This represent Projects Table in database
 * @author Ali Dali
 */
public class ProjectDAO implements DAO<Project> {

    Connection connection;
    PreparedStatement preparedStatement;
    ResultSet resultSet;

    private static ArrayList<Project> projects;

    private static ProjectDAO projectDAO = new ProjectDAO();

    public static ProjectDAO getInstance() {
        return projectDAO;
    }

    private ProjectDAO() {
        connection = DatabaseConnection.getConnection();
        // get all projects from database and store them in 'projects' list
        updateList();
    }

    /**
     * get project by id
     * @param projectID
     * @return project object or null if no project found with given id
     * @author Ali Dali
     */
    @Override
    public Optional<? extends Project> get(int projectID) {
        Project project = null;
        try {
            String queryString = "SELECT * FROM `" + Const.TABLE_PROJECT + "` WHERE " + Const.PROJECT_COLUMN_ID + " = ? LIMIT 1";
            preparedStatement = connection.prepareStatement(queryString);
            preparedStatement.setInt(1, projectID);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt(Const.PROJECT_COLUMN_ID);
                String title = resultSet.getString(Const.PROJECT_COLUMN_TITLE);
                String description = resultSet.getString(Const.PROJECT_COLUMN_DESCRIPTION);
                int status = resultSet.getInt(Const.PROJECT_COLUMN_STATUS);
                int category = resultSet.getInt(Const.PROJECT_COLUMN_CATEGORY);
                int priority = resultSet.getInt(Const.PROJECT_COLUMN_PRIORITY);
                Date startDate = resultSet.getDate(Const.PROJECT_COLUMN_START_DATE);
                Date dueDate = resultSet.getDate(Const.PROJECT_COLUMN_DUE_DATE);
                byte open = resultSet.getByte(Const.PROJECT_COLUMN_OPEN);

                project = new Project(id, title, description, status, category, priority, startDate, dueDate, open);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
                if (preparedStatement != null)
                    preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Optional.ofNullable(project);
    }

    /**
     * get project by title
     * @param projectTitle
     * @return project object or null if no project found with given title
     * @author Ali Dali
     */
    @Override
    public Optional<? extends Project> get(String projectTitle) {
        Project project = null;
        try {
            String queryString = "SELECT * FROM `" + Const.TABLE_PROJECT + "` WHERE " + Const.PROJECT_COLUMN_TITLE + " = ? LIMIT 1";
            preparedStatement = connection.prepareStatement(queryString);
            preparedStatement.setString(1, projectTitle);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt(Const.PROJECT_COLUMN_ID);
                String title = resultSet.getString(Const.PROJECT_COLUMN_TITLE);
                String description = resultSet.getString(Const.PROJECT_COLUMN_DESCRIPTION);
                int status = resultSet.getInt(Const.PROJECT_COLUMN_STATUS);
                int category = resultSet.getInt(Const.PROJECT_COLUMN_CATEGORY);
                int priority = resultSet.getInt(Const.PROJECT_COLUMN_PRIORITY);
                Date startDate = resultSet.getDate(Const.PROJECT_COLUMN_START_DATE);
                Date dueDate = resultSet.getDate(Const.PROJECT_COLUMN_DUE_DATE);
                byte open = resultSet.getByte(Const.PROJECT_COLUMN_OPEN);

                project = new Project(id, title, description, status, category, priority, startDate, dueDate, open);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
                if (preparedStatement != null)
                    preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Optional.ofNullable(project);
    }

    /**
     *
     * @return all projects
     */
    @Override
    public List<Project> getAll() {
        return projects;
    }

    /**
     * add project to database
     * @param project
     * @return an int that will determine if project was created successfully or failed
     */
    @Override
    public int create(Project project) {
        int result;
        try {
            String queryString = "INSERT INTO `" + Const.TABLE_PROJECT + "` VALUES(0,?,?,?,?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, project.getTitle());
            preparedStatement.setString(2, project.getDescription());
            preparedStatement.setInt(3, project.getStatus());
            preparedStatement.setInt(4, project.getCategory());
            preparedStatement.setInt(5, project.getPriority());
            preparedStatement.setDate(6, project.getStartDate());
            preparedStatement.setDate(7, project.getDueDate());
            preparedStatement.setByte(8, project.getOpen());

            preparedStatement.executeUpdate();

            // Get last inserted ID and set it to the project object to add to the projects list
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                project.setId(id);
                projects.add(project);
            }
            result = Const.SUCCESS;
        } catch (SQLException e) {
            e.printStackTrace();
            result = Const.FAILED;
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
                if (preparedStatement != null)
                    preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * update project in database
     * @param project
     * @return an int that will determine if project was updated successfully or failed
     */
    @Override
    public int update(Project project) {
        int result;
        try {
            String queryString = String.format("UPDATE `%s` SET %s=?, %s=?, %s=?, %s=?, %s=?, %s=?, %s=?, %s=? WHERE %s=?", Const.TABLE_PROJECT, Const.PROJECT_COLUMN_TITLE, Const.PROJECT_COLUMN_DESCRIPTION, Const.PROJECT_COLUMN_STATUS, Const.PROJECT_COLUMN_CATEGORY, Const.PROJECT_COLUMN_PRIORITY, Const.PROJECT_COLUMN_START_DATE, Const.PROJECT_COLUMN_DUE_DATE, Const.PROJECT_COLUMN_OPEN, Const.PROJECT_COLUMN_ID);
            preparedStatement = connection.prepareStatement(queryString);
            preparedStatement.setString(1, project.getTitle());
            preparedStatement.setString(2, project.getDescription());
            preparedStatement.setInt(3, project.getStatus());
            preparedStatement.setInt(4, project.getCategory());
            preparedStatement.setInt(5, project.getPriority());
            preparedStatement.setDate(6, project.getStartDate());
            preparedStatement.setDate(7, project.getDueDate());
            preparedStatement.setByte(8, project.getOpen());
            preparedStatement.setInt(9, project.getId());

            preparedStatement.executeUpdate();

            // Update project in projects list :)
            for (Project p : projects) {
                if (p.getId() == project.getId()) {
                    p.setTitle(project.getTitle());
                    p.setDescription(project.getDescription());
                    p.setStatus(project.getStatus());
                    p.setCategory(project.getCategory());
                    p.setPriority(project.getPriority());
                    p.setStartDate(project.getStartDate());
                    p.setDueDate(project.getDueDate());
                    p.setOpen(project.getOpen());
                    break;
                }
            }
            result = Const.SUCCESS;
        } catch (SQLException e) {
            e.printStackTrace();
            result = Const.FAILED;
        } finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * delete project from database
     * @param project
     * @return if project was successfully deleted
     */
    @Override
    public int delete(Project project) {
        int result;
        if (get(project.getId()).isPresent()) {
            try {
                String queryString = "DELETE FROM `" + Const.TABLE_PROJECT + "` WHERE " + Const.PROJECT_COLUMN_ID + " = ?";
                preparedStatement = connection.prepareStatement(queryString);
                preparedStatement.setInt(1, project.getId());
                preparedStatement.executeUpdate();

                // Delete project if it exist in projects list :)
                for (Project p : projects) {
                    if (p.getId() == project.getId()) {
                        projects.remove(p);
                        break;
                    }
                }
                result = Const.SUCCESS;
            } catch (SQLException e) {
                e.printStackTrace();
                result = Const.FAILED;
            } finally {
                try {
                    if (preparedStatement != null)
                        preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            result = Const.NOT_FOUND;
        }
        return result;
    }

    /**
     * populate projects list with records from database
     */
    @Override
    public void updateList() {
        projects = new ArrayList<>();
        Project project;
        try {
            String queryString = "SELECT * FROM `" + Const.TABLE_PROJECT + "`";

            preparedStatement = connection.prepareStatement(queryString);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt(Const.PROJECT_COLUMN_ID);
                String title = resultSet.getString(Const.PROJECT_COLUMN_TITLE);
                String description = resultSet.getString(Const.PROJECT_COLUMN_DESCRIPTION);
                int status = resultSet.getInt(Const.PROJECT_COLUMN_STATUS);
                int category = resultSet.getInt(Const.PROJECT_COLUMN_CATEGORY);
                int priority = resultSet.getInt(Const.PROJECT_COLUMN_PRIORITY);
                Date startDate = resultSet.getDate(Const.PROJECT_COLUMN_START_DATE);
                Date dueDate = resultSet.getDate(Const.PROJECT_COLUMN_DUE_DATE);
                byte open = resultSet.getByte(Const.PROJECT_COLUMN_OPEN);

                project = new Project(id, title, description, status, category, priority, startDate, dueDate, open);

                projects.add(project);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null)
                    resultSet.close();
                if (preparedStatement != null)
                    preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @return id of last inserted project
     * @author Ali Dali
     */
    @Override
    public int getLastInsertedId() {
        return !projects.isEmpty() ? projects.get(projects.size() - 1).getId() : 0;
    }

    /**
     * @return id of last inserted project
     * @author Ali Dali
     */
    public int getProjectsCountByCSP(ArrayList<Project> projects, CSP csp) {
        int count = 0;
        for (Project project : projects) {
            if (csp instanceof Category)
                if (project.getCategory() == csp.getId()) count++;
            if (csp instanceof Status)
                if (project.getStatus() == csp.getId()) count++;
            if (csp instanceof Priority)
                if (project.getPriority() == csp.getId()) count++;
        }
        return count;
    }
    /**
     * @return count of projects started in each month
     * @author Ali Dali
     */
    public int getProjectsCountByDate(ArrayList<Project> projects, Date date) {
        int count = 0;
        for(Project project: projects) {
            if (project.getStartDate().getMonth() == date.getMonth()) count++;
        }
        return count;
    }

    /**
     * @return list of projects with specific date range
     * @author Ali Dali
     */
    public ArrayList<Project> getProjectsByDate(List<Project> projects, LocalDate fromDate, LocalDate toDate) {
        if(fromDate == null)
            fromDate = LocalDate.of(2000,1,1);
        if(toDate == null)
            toDate = LocalDate.now();

        ArrayList<Project> list = new ArrayList<>();
        for (Project project : projects) {
            if(project.getStartDate().after(Date.valueOf(fromDate)) && project.getStartDate().before(Date.valueOf(toDate)))
                list.add(project);
        }
        return list;
    }

    /**
     *
     * @param filter close or open
     * @return list of projects (closed / opened)
     * @author Ali Dali
     */
    public ArrayList<Project> getFilteredProjects(int filter) {
        ArrayList<Project> list = new ArrayList<>();
        for (Project project : projects) {
            if (project.getOpen() == filter)
                list.add(project);
        }
        return list;
    }
}
