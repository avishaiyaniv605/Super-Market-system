package PersistenceLayer.Inventory;

import PersistenceLayer.BranchProfile;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import java.sql.Date;
import javax.persistence.*;
import java.io.Serializable;

@Entity
public class ProductInstance implements Serializable {
    @Id
    private double sn;
    @ManyToOne(fetch = FetchType.LAZY)
    private StockProduct pID;
    private Date expDate;
    private double buyingPrice;
    private int in_shop;
    private int is_damaged=0;
    private int is_expired=0;

    //region constructors
    public ProductInstance(StockProduct pID, double sn, int buyingPrice, String place, int in_shop, int is_damaged,Date date) {
        this.pID = pID;
        this.buyingPrice = buyingPrice;
        this.in_shop = in_shop;
        this.is_damaged = is_damaged;
        this.sn = sn;
        this.expDate= date;
    }

    public ProductInstance(StockProduct pID, double sn, double buyingPrice,Date date) {
        this.pID = pID;
        this.buyingPrice = buyingPrice;
        this.in_shop = 0;
        this.is_damaged = 0;
        this.sn = sn;
        this.expDate= date;
    }

    public ProductInstance() {
    }

    //endregion
    //region getters setters
    public Date getExpDate() {
        return expDate;
    }

    public void setExpDate(Date expDate) {
        this.expDate = expDate;
    }


    public int getIs_damaged() {
        return is_damaged;
    }

    public int getIs_expired() {
        return is_expired;
    }

    public double getSn() {
        return sn;
    }

    public StockProduct getpID() {
        return pID;
    }

    public int getIn_shop() {
        return in_shop;
    }



    public double getBuyingPrice() {
        return buyingPrice;
    }
    public void setIs_damaged(int is_damaged) {
        this.is_damaged = is_damaged;
    }

    public void setIs_expired(int is_expired) {
        this.is_expired = is_expired;
    }

    public void setSn(Long sn) {
        this.sn = sn;
    }

    public void setpID(StockProduct pID) {
        this.pID = pID;
    }

    public void setIn_shop(int in_shop) {
        this.in_shop = in_shop;
    }

    public void setBuyingPrice(int buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    //endregion
    @Override
    public String toString() {
        String result= "Product id: " + pID.getpID().getID() + " SN: " + getSn() + " Name: " + pID.getpID().getName() + " Buying price: " + getBuyingPrice() +
                " Expiration date: "+getExpDate().toString()+ "\n";
        if (is_damaged == 1) {
            result+= " Damaged!!!\n";
        } else if (is_expired == 1) {
            result+= " Expired!!!\n";
        }
        return result;

    }
}
