package Database.CSP.Status;

import Const.Const;
import Database.CSP.CSPDAO;
import Database.DAO;
import Database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StatusDAO extends CSPDAO {

    private static StatusDAO statusDAO = new StatusDAO();

    public static StatusDAO getInstance() {
        if(statusDAO==null){
            statusDAO = new StatusDAO();
        }
        return statusDAO;
    }

    private StatusDAO() {
        super(Const.TABLE_STATUS);
    }
}
