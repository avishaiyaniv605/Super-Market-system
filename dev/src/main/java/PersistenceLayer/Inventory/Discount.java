package PersistenceLayer.Inventory;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Discount implements Serializable {

    @Id
    @GeneratedValue
    private int dID;
    private int percentage;
    private Date startTime;
    private Date endTime;



    @OneToMany(orphanRemoval = true)
    private List<StockProduct> products = new ArrayList<>();

    public Discount(int discount, Date startTime, Date endTime) {
        this.percentage= discount;
        this.startTime= startTime;
        this.endTime= endTime;
    }

    public Discount() {
    }


    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    public int getdID() {
        return dID;
    }

    public void setdID(int dID) {
        this.dID = dID;
    }

    public List<StockProduct> getProducts() {
        return products;
    }

    public void setProducts(List<StockProduct> products) {
        this.products = products;
    }
}
