package Database.CSP.Priority;

import Database.CSP.CSP;

/**
 * This class represent priority record in database
 * @author Ali Dali
 */
public class Priority extends CSP {

    public Priority(String name, String color) {
        super(name, color);
    }

    public Priority(int id, String name, String color) {
        super(id, name, color);
    }
    public String toString(){
        return  this.getName();
    }
}
