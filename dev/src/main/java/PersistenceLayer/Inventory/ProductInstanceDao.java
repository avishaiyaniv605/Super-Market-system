package PersistenceLayer.Inventory;

import PersistenceLayer.BranchProfile;
import PersistenceLayer.DatabaseManager;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.Date;
import java.util.List;

public class ProductInstanceDao {

    public ProductInstance find(double sn) {
        DatabaseManager db = DatabaseManager.getInstance();
        try {
            ProductInstance ins = db.getSession().get(ProductInstance.class, sn);
            return ins;
        } catch (Exception e) {
            return null;
        }
    }

    public ProductInstance insert(StockProduct chooseProduct, double sn, double bPrice, Date date) {
        ProductInstance newProduct;
        try {
            DatabaseManager db = DatabaseManager.getInstance();
            newProduct = new ProductInstance(chooseProduct, sn, bPrice, date);
            db.getSession().save(newProduct);
            db.commitAndFLush();
            return newProduct;
        } catch (Exception e) {
            return null;
        }
    }


    public void update(ProductInstance chosenProduct) {
        DatabaseManager db = DatabaseManager.getInstance();
        db.getSession().update(chosenProduct);
        db.commitAndFLush();
    }

    public boolean remove(double sn) {
        DatabaseManager db = DatabaseManager.getInstance();
        try {
            ProductInstance toDelete = find(sn);
            StockProduct toDeleteStockProduct = toDelete.getpID();
            int inStore = toDelete.getIn_shop();
            if (!(toDelete.getIs_expired() == 1) && !(toDelete.getIs_damaged() == 1)) {
                if (inStore == 1) {
                    toDeleteStockProduct.setStoreQuantity(toDeleteStockProduct.getStoreQuantity() - 1);
                } else {
                    toDeleteStockProduct.setStorageQuantity(toDeleteStockProduct.getStorageQuantity() - 1);
                }
            }
            db.getSession().delete(toDelete);
            db.commitAndFLush();
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public List<ProductInstance> findAllDefective() {
        DatabaseManager db = DatabaseManager.getInstance();
        try {
            CriteriaQuery<ProductInstance> criteriaQuery = db.getCb().createQuery(ProductInstance.class);
            Root<ProductInstance> root = criteriaQuery.from(ProductInstance.class);
            criteriaQuery.select(root);
            criteriaQuery.where(db.getCb().or(db.getCb().equal(root.get("is_damaged"), 1), db.getCb().equal(root.get("is_expired"), 1)));
            criteriaQuery.orderBy(db.getCb().asc(root.get("sn")));
            Query<ProductInstance> query = db.getSession().createQuery(criteriaQuery);
            return query.getResultList();

        } catch (Exception e) {
            return null;
        }
    }

    public double findLast(StockProduct s) {
        DatabaseManager db = DatabaseManager.getInstance();
        try {
            CriteriaQuery<ProductInstance> criteriaQuery = db.getCb().createQuery(ProductInstance.class);
            Root<ProductInstance> root = criteriaQuery.from(ProductInstance.class);
            criteriaQuery.select(root);
            criteriaQuery.where(db.getCb().between(root.get("sn"), s.getpID().getID(), s.getpID().getID() + 1));
            criteriaQuery.orderBy(db.getCb().asc(root.get("sn")));
            Query<ProductInstance> query = db.getSession().createQuery(criteriaQuery);
            List<ProductInstance> list = query.getResultList();
            if (list.isEmpty())
                return s.getpID().getID();
            else
                return list.get(list.size() - 1).getSn();


        } catch (Exception e) {
            return 0;
        }
    }
    public double findLast(StockProduct s,BranchProfile branch) {
        DatabaseManager db = DatabaseManager.getInstance();
        try {
            CriteriaQuery<ProductInstance> criteriaQuery = db.getCb().createQuery(ProductInstance.class);
            Root<ProductInstance> root = criteriaQuery.from(ProductInstance.class);
            criteriaQuery.select(root);
            criteriaQuery.where(db.getCb().equal(root.get("pID"),s));
            Query<ProductInstance> query = db.getSession().createQuery(criteriaQuery);
            List<ProductInstance> list = query.getResultList();
            if (list.isEmpty())
                return s.getpID().getID();
            else
                return list.get(list.size() - 1).getSn();


        } catch (Exception e) {
            return 0;
        }
    }
}
