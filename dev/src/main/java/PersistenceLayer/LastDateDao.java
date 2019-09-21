package PersistenceLayer;

import PersistenceLayer.Inventory.Orders.OrderItem;
import PersistenceLayer.Inventory.Orders.PeriodicalOrder;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.Date;
import java.util.List;

public class LastDateDao {

    public LastDate findAll() {
        DatabaseManager db = DatabaseManager.getInstance();
        try {
            CriteriaQuery<LastDate> criteriaQuery = db.getCb().createQuery(LastDate.class);
            Root<LastDate> root = criteriaQuery.from(LastDate.class);
            criteriaQuery.select(root);
            Query<LastDate> query = db.getSession().createQuery(criteriaQuery);
            List<LastDate> lastDates = query.getResultList();
            if (lastDates.isEmpty())
                return null;
            else
                return lastDates.get(0);

        } catch (Exception e) {
            return null;
        }
    }

    public void removeAllAndInsert(Date date) {
        remove(findAll());
        insert(date);
    }

    public LastDate insert(Date date) {
        LastDate newDate;
        try {
            DatabaseManager db = DatabaseManager.getInstance();
            newDate = new LastDate(date);
            db.getSession().save(newDate);
            db.commitAndFLush();

            return newDate;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean remove(LastDate date) {
        DatabaseManager db = DatabaseManager.getInstance();
        if (date == null) return true;
        try {
            db.getSession().delete(date);
            db.commitAndFLush();
            return true;
        } catch (Exception e) {
            return false;
        }

    }

}
