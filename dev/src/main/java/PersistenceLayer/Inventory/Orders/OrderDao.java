package PersistenceLayer.Inventory.Orders;

import PersistenceLayer.BranchProfile;
import PersistenceLayer.DatabaseManager;
import PersistenceLayer.Suppliers.SupplierContact;
import org.hibernate.criterion.Order;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.Date;
import java.util.List;

import static BusinessLayer.Enums.EnumTypeJob.shiftManager;

public class OrderDao {

    public Orders insert(Date date, SupplierContact supplierContact, List<OrderItem> orderItems, int needDelivery,BranchProfile branch) {
        Orders newOrder = null;
        OrderItemDao orderItemDao= new OrderItemDao();
        try {
            DatabaseManager db = DatabaseManager.getInstance();
            newOrder = new Orders(date, supplierContact, needDelivery,branch);
            db.getSession().save(newOrder);
            db.commitAndFLush();
            int weight= 0;
            for (OrderItem o : orderItems) {
                OrderItem o1= orderItemDao.insert(o.getpID(),o.getAmount());
                o1.setoID(newOrder);
                o1.setDetails(o);
                newOrder.addOrderItem(o1);
                db.getSession().update(o1);
                db.getSession().update(newOrder);
                db.commitAndFLush();
                weight+=o1.getpID().getpID().getWeight();
            }
            newOrder.setWeight(weight);
            db.getSession().update(newOrder);
            db.commitAndFLush();
            return newOrder;

        } catch (Exception e) {
            return newOrder;
        }

    }

    public List<Orders> findAll(BranchProfile branch) {
        DatabaseManager db = DatabaseManager.getInstance();
        try {
            CriteriaQuery<Orders> criteriaQuery = db.getCb().createQuery(Orders.class);
            Root<Orders> root = criteriaQuery.from(Orders.class);
            criteriaQuery.select(root);
            criteriaQuery.where(db.getCb().equal(root.get("branch"), branch));
            Query<Orders> query = db.getSession().createQuery(criteriaQuery);
            return query.getResultList();

        } catch (Exception e) {
            return null;
        }
    }

    public List<Orders> findAllForDelivery() {
        DatabaseManager db = DatabaseManager.getInstance();
        try {
            CriteriaQuery<Orders> criteriaQuery = db.getCb().createQuery(Orders.class);
            Root<Orders> root = criteriaQuery.from(Orders.class);
            criteriaQuery.select(root);
            criteriaQuery.where(db.getCb().and(db.getCb().equal(root.get("status"), "WAITING")),db.getCb().equal(root.get("needDelivery"),1));
            Query<Orders> query = db.getSession().createQuery(criteriaQuery);
            return query.getResultList();

        } catch (Exception e) {
            return null;
        }
    }

    public void update(Orders o) {
        DatabaseManager db = DatabaseManager.getInstance();
        db.getSession().update(o);
        db.commitAndFLush();
    }

    public Orders find(long oID) {
        DatabaseManager db = DatabaseManager.getInstance();
        try {
            Orders ins = db.getSession().get(Orders.class, oID);
            return ins;
        } catch (Exception e) {
            return null;
        }
    }

    public List<Orders> findAllPendings() {
        DatabaseManager db = DatabaseManager.getInstance();
        try {
            CriteriaQuery<Orders> criteriaQuery = db.getCb().createQuery(Orders.class);
            Root<Orders> root = criteriaQuery.from(Orders.class);
            criteriaQuery.select(root);
            criteriaQuery.where(db.getCb().equal(root.get("status"), "WAITING"));
            Query<Orders> query = db.getSession().createQuery(criteriaQuery);
            return query.getResultList();

        } catch (Exception e) {
            return null;
        }
    }

    public List<Orders> findNeedCancel(String employee) {
        DatabaseManager db = DatabaseManager.getInstance();
        try {
            CriteriaQuery<Orders> criteriaQuery = db.getCb().createQuery(Orders.class);
            Root<Orders> root = criteriaQuery.from(Orders.class);
            criteriaQuery.select(root);
            switch (employee){
                case "Shift Manager": {
                    criteriaQuery.where(db.getCb().and(db.getCb().equal(root.get("cancel"), 1)), db.getCb().and(db.getCb().equal(root.get("status"),"WAITING")),db.getCb().equal(root.get("HRCancel"), 0));
                    break;
                }
                case "Storekeeper": {
                    criteriaQuery.where(db.getCb().and(db.getCb().equal(root.get("cancel"), 1)), db.getCb().and(db.getCb().equal(root.get("status"),"WAITING")), db.getCb().equal(root.get("InvCancel"), 0));
                    break;
                }
                case "Logistics Manager": {
                    criteriaQuery.where(db.getCb().and(db.getCb().equal(root.get("cancel"), 1)), db.getCb().and(db.getCb().equal(root.get("status"),"WAITING")), db.getCb().equal(root.get("LogisticsCancel"), 0));
                    break;
                }
                default:
                    return null;
            }
            Query<Orders> query = db.getSession().createQuery(criteriaQuery);
            return query.getResultList();

        } catch (Exception e) {
            return null;
        }
    }
}
