package PersistenceLayer.Inventory.Orders;

import PersistenceLayer.BranchProfile;
import PersistenceLayer.DatabaseManager;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.Date;
import java.util.List;

public class PeriodicalOrderDao {

    public List<PeriodicalOrder> findAll(BranchProfile branch) {
        DatabaseManager db = DatabaseManager.getInstance();
        try {
            CriteriaQuery<PeriodicalOrder> criteriaQuery = db.getCb().createQuery(PeriodicalOrder.class);
            Root<PeriodicalOrder> root = criteriaQuery.from(PeriodicalOrder.class);
            criteriaQuery.select(root);
            criteriaQuery.where(db.getCb().equal(root.get("branch"), branch));
            Query<PeriodicalOrder> query = db.getSession().createQuery(criteriaQuery);
            return query.getResultList();

        } catch (Exception e) {
            return null;
        }
    }

    public PeriodicalOrder insert(Date startDate, int period, List<OrderItem> orderItems, BranchProfile branch) {
        PeriodicalOrder newOrder;
        try {
            DatabaseManager db = DatabaseManager.getInstance();
            newOrder = new PeriodicalOrder(startDate,period,branch);
            db.getSession().save(newOrder);
            db.commitAndFLush();
            for(OrderItem o : orderItems){
                o.setPoID(newOrder);
                newOrder.addOrder(o);
                db.getSession().update(o);
                db.getSession().update(newOrder);
                db.commitAndFLush();
            }

            return newOrder;
        } catch (Exception e) {
            return null;
        }
    }

    public PeriodicalOrder find(long oID) {
        DatabaseManager db = DatabaseManager.getInstance();
        try {
            PeriodicalOrder order = db.getSession().get(PeriodicalOrder.class, oID);
            return order;
        } catch (Exception e) {
            return null;
        }
    }

    public void update(PeriodicalOrder order) {
        DatabaseManager db = DatabaseManager.getInstance();
        db.getSession().update(order);
        db.commitAndFLush();
    }

    public boolean remove(PeriodicalOrder periodicalOrder) {
        DatabaseManager db = DatabaseManager.getInstance();
        try {
            db.getSession().delete(periodicalOrder);
            db.commitAndFLush();
            return true;
        } catch (Exception e) {
            return false;
        }

    }
}
