package PersistenceLayer;

import PersistenceLayer.Inventory.Category;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class BranchProfileDao {
    public BranchProfile find(String branch) {
        DatabaseManager db= DatabaseManager.getInstance();
        try{
            BranchProfile ans = db.getSession().get(BranchProfile.class,branch);
            return ans;
        }
        catch (Exception e){return null;}
    }

    public BranchProfile insert(String branch_name, String city, String street, int number, int phone, String contact) {
        BranchProfile newB;
        try{
            DatabaseManager db= DatabaseManager.getInstance();
            newB = new BranchProfile(branch_name,city,street,number,phone,contact);
            db.getSession().save(newB);
            db.commitAndFLush();
            return newB;
        }
        catch (Exception e){return null;}
    }

    public void update(BranchProfile branch) {
        DatabaseManager db= DatabaseManager.getInstance();
        db.getSession().update(branch);
        db.commitAndFLush();
    }

    public List<BranchProfile> findAll() {
        DatabaseManager db = DatabaseManager.getInstance();
        try {
            CriteriaQuery<BranchProfile> criteriaQuery = db.getCb().createQuery(BranchProfile.class);
            Root<BranchProfile> root = criteriaQuery.from(BranchProfile.class);
            criteriaQuery.select(root);
            Query<BranchProfile> query = db.getSession().createQuery(criteriaQuery);
            return query.getResultList();

        } catch (Exception e) {
            return null;
        }
    }
}
