package PersistenceLayer.Inventory;

import PersistenceLayer.DatabaseManager;

import java.sql.Date;

public class DiscountDao {

    public Discount insert(int percentage, Date startTime, Date endTime) {
        DatabaseManager db = DatabaseManager.getInstance();
        try {
            Discount d = new Discount(percentage, startTime, endTime);
            db.getSession().save(d);
            db.commitAndFLush();
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    public Discount find(int dID) {
        DatabaseManager db = DatabaseManager.getInstance();
        try {
            Discount ins = db.getSession().get(Discount.class, dID);
            return ins;
        } catch (Exception e) {
            return null;
        }
    }
}
