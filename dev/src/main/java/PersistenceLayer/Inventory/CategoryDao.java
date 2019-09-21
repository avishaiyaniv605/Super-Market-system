package PersistenceLayer.Inventory;

import PersistenceLayer.DatabaseManager;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.*;

public class CategoryDao {


    public Category find(int categoryID) {
        DatabaseManager db = DatabaseManager.getInstance();
        try {
            Category productCat = db.getSession().get(Category.class, categoryID);
            return productCat;
        } catch (Exception e) {
            return null;
        }
    }

    public List<Category> findAll() {
        DatabaseManager db = DatabaseManager.getInstance();
        try {
            CriteriaQuery<Category> criteriaQuery = db.getCb().createQuery(Category.class);
            Root<Category> root = criteriaQuery.from(Category.class);
            criteriaQuery.select(root);
            criteriaQuery.orderBy(db.getCb().asc(root.get("categoryid")));
            Query<Category> query = db.getSession().createQuery(criteriaQuery);
            return query.getResultList();

        } catch (Exception e) {
            return null;
        }
    }

    public Category insert(int categoryID, String categoryName, Category parent) {
        DatabaseManager db = DatabaseManager.getInstance();
        try {
            Category cat = new Category(categoryID, categoryName, parent);
            db.getSession().save(cat);
            db.commitAndFLush();
            return cat;
        } catch (Exception e) {
            return null;
        }

    }

    public void update(Category current) {
        DatabaseManager db = DatabaseManager.getInstance();
        db.getSession().update(current);
        db.commitAndFLush();
    }

    public void remove(int cID) {
        DatabaseManager db = DatabaseManager.getInstance();
        try {
            Category cat = db.getSession().get(Category.class, cID);
            db.getSession().delete(cat);
            db.commitAndFLush();

        } catch (Exception e) {
        }
    }

    public Set<Category> findWithsub(int cID) {
        DatabaseManager db = DatabaseManager.getInstance();
        Set<Category> childs = new HashSet<>();
        Queue<Category> queue = new LinkedList<>();

        try {
            Category parent = find(cID);
            childs.add(parent);
            ((LinkedList<Category>) queue).push(parent);
            while (!queue.isEmpty()) {
                Category next = ((LinkedList<Category>) queue).pop();
                Set<Category> cList = next.getSubCategories();
                if (cList != null)
                    for (Category c : cList) {
                        childs.add(c);
                        ((LinkedList<Category>) queue).push(c);
                    }
            }
            return childs;
        } catch (Exception e) {
            return childs;
        }

    }
}