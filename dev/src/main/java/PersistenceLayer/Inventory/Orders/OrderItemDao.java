package PersistenceLayer.Inventory.Orders;

import PersistenceLayer.DatabaseManager;
import PersistenceLayer.Inventory.StockProduct;
import PersistenceLayer.Product;

public class OrderItemDao {
    public OrderItem insert(StockProduct chosenProduct, int amount) {
        OrderItem newItem;
        try {
            DatabaseManager db = DatabaseManager.getInstance();
            newItem = new OrderItem(chosenProduct, amount);
            db.getSession().save(newItem);
            db.commitAndFLush();
            return newItem;
        } catch (Exception e) {
            return null;
        }

    }

    public OrderItem find(long oid) {
        DatabaseManager db = DatabaseManager.getInstance();
        try {
            OrderItem ins = db.getSession().get(OrderItem.class, oid);
            return ins;
        } catch (Exception e) {
            return null;
        }
    }

    public void update(OrderItem orderItem) {
        DatabaseManager db = DatabaseManager.getInstance();
        db.getSession().update(orderItem);
        db.commitAndFLush();
    }


    public boolean remove(OrderItem orderItem) {
        DatabaseManager db = DatabaseManager.getInstance();
        try {
            db.getSession().delete(orderItem);
            db.commitAndFLush();
            return true;
        } catch (Exception e) {
            return false;
        }

    }
}
