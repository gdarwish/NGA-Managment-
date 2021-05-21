package Database.CSP.Status;

import Const.Const;
import Database.CSP.CSP;

/**
 * This class represent status record in database
 * @author Ali Dali
 */
public class Status extends CSP {



    public Status(String name, String color) {
        super(name, color);
    }

    public Status(int id, String name, String color) {
        super(id, name, color);
    }

    public String toString(){
        return  this.getName();
    }
}
