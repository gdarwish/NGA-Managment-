package Database.CSP;

import Const.Const;
import Database.CSP.Category.Category;
import Database.CSP.Priority.Priority;
import Database.CSP.Status.Status;
import Database.DAO;
import Database.DatabaseConnection;
import Database.Project.Project;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This represent (Category, Status, Priority) Tables in database
 * @author Ali Dali
 */
public class CSPDAO implements DAO<CSP> {

    Connection connection;
    PreparedStatement preparedStatement;
    ResultSet resultSet;

    private  List<CSP> cspList = null;

    private String table;

    public CSPDAO(String table) {
        this.table = table;
        connection = DatabaseConnection.getConnection();
        updateList();
    }

    /**
     * get csp by id
     * @param cspID
     * @return csp object or null if no csp found with given id
     * @author Ali Dali
     */
    @Override
    public Optional<? extends CSP> get(int cspID) {
        CSP csp = null;
        try {
            String queryString = "SELECT * FROM `" + table + "` WHERE id=? LIMIT 1";
            preparedStatement = connection.prepareStatement(queryString);
            preparedStatement.setInt(1, cspID);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt(Const.CSP_COLUMN_ID);
                String name = resultSet.getString(Const.CSP_COLUMN_NAME);
                String color = resultSet.getString(Const.CSP_COLUMN_COLOR);

                switch (table) {
                    case Const.TABLE_STATUS:
                        csp = new Status(id, name, color);
                        break;
                    case Const.TABLE_CATEGORY:
                        csp = new Category(id, name, color);
                        break;
                    case Const.TABLE_PRIORITY:
                        csp = new Priority(id, name, color);
                        break;
                }
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
        return Optional.ofNullable(csp);
    }

    /**
     * get csp by name
     * @param CSPName
     * @return project object or null if no csp found with given name
     * @author Ali Dali
     */
    @Override
    public Optional<? extends CSP> get(String CSPName) {
        CSP csp = null;
        try {
            String queryString = "SELECT * FROM `" + table + "` WHERE  " + Const.CSP_COLUMN_NAME + " = ? LIMIT 1";
            preparedStatement = connection.prepareStatement(queryString);
            preparedStatement.setString(1, CSPName);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt(Const.CSP_COLUMN_ID);
                String name = resultSet.getString(Const.CSP_COLUMN_NAME);
                String color = resultSet.getString(Const.CSP_COLUMN_COLOR);

                switch (table) {
                    case Const.TABLE_STATUS:
                        csp = new Status(id, name, color);
                        break;
                    case Const.TABLE_CATEGORY:
                        csp = new Category(id, name, color);
                        break;
                    case Const.TABLE_PRIORITY:
                        csp = new Priority(id, name, color);
                        break;
                }
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
        return Optional.ofNullable(csp);
    }

    /**
     *
     * @return all csp
     */
    @Override
    public List<? extends CSP> getAll() {
        return cspList;
    }

    /**
     * add csp to database
     * @param csp
     * @return an int that will determine if csp was created successfully or failed
     */
    @Override
    public int create(CSP csp) {
        int result;
        if (!get(csp.getName()).isPresent()) {
            try {
                String queryString = "INSERT INTO `" + table + "`(" + Const.CSP_COLUMN_ID + ", " + Const.CSP_COLUMN_NAME + ", " + Const.CSP_COLUMN_COLOR + ") VALUES(0,?,?)";
                preparedStatement = connection.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, csp.getName());
                preparedStatement.setString(2, csp.getColor());
                preparedStatement.executeUpdate();

                // Get last inserted ID and set it to the CSP object to add to the CSPes list
                resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    csp.setId(id);
                    cspList.add(csp);
                }

                result = Const.SUCCESS;
            } catch (SQLException e) {
                e.printStackTrace();
                result = Const.FAILED;
            } finally {
                try {
                    if (preparedStatement != null)
                        preparedStatement.close();
                    if (resultSet != null)
                        resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            result = Const.EXIST;
        }
        return result;
    }

    /**
     * update csp in database
     * @param csp
     * @return an int that will determine if csp was updated successfully or failed
     */
    @Override
    public int update(CSP csp) {
        int result;
        try {
            // Check if name already exist do not update it (prevent duplicate names)
            if (!get(csp.getName()).isPresent()) {
                String queryString = "UPDATE `" + table + "` SET " + Const.CSP_COLUMN_NAME + " = ? ,  " + Const.CSP_COLUMN_COLOR + " = ? WHERE " + Const.CSP_COLUMN_ID + " = ?";
                preparedStatement = connection.prepareStatement(queryString);
                preparedStatement.setString(1, csp.getName());
                preparedStatement.setString(2, csp.getColor());
                preparedStatement.setInt(3, csp.getId());
            } else {
                String queryString = "UPDATE `" + table + "` SET " + Const.CSP_COLUMN_COLOR + " = ? WHERE " + Const.CSP_COLUMN_ID + " = ?";
                preparedStatement = connection.prepareStatement(queryString);
                preparedStatement.setString(1, csp.getColor());
                preparedStatement.setInt(2, csp.getId());
            }

            preparedStatement.executeUpdate();

            // Update CSP name and color in CSPes list :)
            for (CSP oldCSP : cspList) {
                if (csp.getId() == oldCSP.getId()) {
                    oldCSP.setName(csp.getName());
                    oldCSP.setColor(csp.getColor());
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
     * delete csp from database
     * @param csp
     * @return if csp was successfully deleted
     */
    @Override
    public int delete(CSP csp) {
        int result;
        if (get(csp.getId()).isPresent()) {
            try {
                String queryString = "DELETE FROM `" + table + "` WHERE id=?";
                preparedStatement = connection.prepareStatement(queryString);
                preparedStatement.setInt(1, csp.getId());
                preparedStatement.executeUpdate();

                // Delete CSP if it exist in CSPes list :)
                for (CSP oldCSP : cspList) {
                    if (csp.getId() == oldCSP.getId()) {
                        cspList.remove(csp);
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
     * Populating CSPes list from database
     */
    @Override
    public void updateList() {
        cspList = new ArrayList<>();
        CSP csp = null;
        try {
            String queryString = "SELECT * FROM `" + table + "`";
            preparedStatement = connection.prepareStatement(queryString);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                // Table columns (id, name, color)
                int id = resultSet.getInt(Const.CSP_COLUMN_ID);
                String name = resultSet.getString(Const.CSP_COLUMN_NAME);
                String color = resultSet.getString(Const.CSP_COLUMN_COLOR);

                switch (table) {
                    case Const.TABLE_STATUS:
                        csp = new Status(id, name,color);
                        break;
                    case Const.TABLE_CATEGORY:
                        csp = new Category(id, name,color);
                        break;
                    case Const.TABLE_PRIORITY:
                        csp = new Priority(id, name, color);
                        break;
                }
                cspList.add(csp);
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
     * @return id of last inserted csp
     * @author Ali Dali
     */
    @Override
    public int getLastInsertedId() {
        return !cspList.isEmpty() ? cspList.get(cspList.size() - 1).getId() : 0;
    }

    /**
     *
     * @param id
     * @return csp object with matched id
     * @author Ali Dali
     */
    public CSP getItemById(int id) {
        for(CSP csp : cspList) {
            if(csp.getId() == id)
                return csp;
        }
        return null;
    }
}
