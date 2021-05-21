package Database.CSP.Category;

import Const.Const;
import Database.CSP.CSPDAO;

public class CategoryDAO extends CSPDAO {

    private static CategoryDAO categoryDAO;

    public static CategoryDAO getInstance() {
        if (categoryDAO == null) {
            categoryDAO = new CategoryDAO();
        }
        return categoryDAO;
    }

    private CategoryDAO() {
        super(Const.TABLE_CATEGORY);
    }
}
