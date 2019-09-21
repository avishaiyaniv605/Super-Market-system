package PersistenceLayer.Inventory;

import PersistenceLayer.BranchProfile;
import PersistenceLayer.DatabaseManager;
import PersistenceLayer.Product;
import PersistenceLayer.ProductDao;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class StockProductDao {

    public StockProduct insert(Product chosenProduct, BranchProfile branch, Category chooseCat, double sellingPrice, String location, Discount discount) {
        StockProduct newProduct;
        try {
            DatabaseManager db = DatabaseManager.getInstance();
            newProduct = new StockProduct(chosenProduct,branch, chooseCat, sellingPrice, location,discount);
            db.getSession().save(newProduct);
            db.commitAndFLush();
            return newProduct;
        } catch (Exception e) {
            return null;
        }

    }

    public List<StockProduct> findAll(BranchProfile branch) {
        DatabaseManager db = DatabaseManager.getInstance();
        try {
            CriteriaQuery<StockProduct> criteriaQuery = db.getCb().createQuery(StockProduct.class);
            Root<StockProduct> root = criteriaQuery.from(StockProduct.class);
            criteriaQuery.select(root);
            criteriaQuery.where(db.getCb().equal(root.get("branch"), branch));
            Query<StockProduct> query = db.getSession().createQuery(criteriaQuery);
            return query.getResultList();

        } catch (Exception e) {
            return null;
        }
    }


    public StockProduct find(Product product,BranchProfile branch) {
        DatabaseManager db = DatabaseManager.getInstance();
        try {
            StockProduct ans = db.getSession().get(StockProduct.class, new StockProductKey(product,branch));
            return ans;
        } catch (Exception e) {
            return null;
        }
    }

    public void update(StockProduct sProduct) {
        DatabaseManager db = DatabaseManager.getInstance();
        db.getSession().update(sProduct);
        db.commitAndFLush();
    }

    public List<StockProduct> findInCat(List<Integer> categoryList) {
        DatabaseManager db = DatabaseManager.getInstance();
        try {
            CriteriaQuery<StockProduct> criteriaQuery = db.getCb().createQuery(StockProduct.class);
            Root<StockProduct> root = criteriaQuery.from(StockProduct.class);
            criteriaQuery.select(root);
            criteriaQuery.where(root.get("category").in(categoryList));
            Query<StockProduct> query = db.getSession().createQuery(criteriaQuery);
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

}
