package PersistenceLayer.Inventory.Orders;

import PersistenceLayer.Inventory.StockProduct;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class OrderItem implements Serializable {

    @GeneratedValue
    @Id
    private long id;

    @ManyToOne
    private StockProduct pID;
    @ManyToOne
    @JoinColumn(name = "oID", referencedColumnName = "orderID", foreignKey = @ForeignKey(name = "ORDERITEM_ORDER_FK"))
    private Orders oID;
    @ManyToOne
    @JoinColumn(name = "poID", referencedColumnName = "pID")
    private PeriodicalOrder poID;

    private int amount;

    @ColumnDefault("0")
    private double buyingPrice;
    @ColumnDefault("0")
    private double discountPer;
    @ColumnDefault("0")
    private double finalPrice;


    @ColumnDefault("0")
    private int cancel;


    public OrderItem() {
    }
    public OrderItem(StockProduct pID, int amount,PeriodicalOrder periodicalOrder) {
        this.pID= pID;
        this.amount=amount;
        this.poID= periodicalOrder;
    }
    public OrderItem(StockProduct pID, int amount) {
        this.pID= pID;
        this.amount=amount;

    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public StockProduct getpID() {
        return pID;
    }

    public void setpID(StockProduct pID) {
        this.pID = pID;
    }

    public Orders getoID() {
        return oID;
    }

    public void setoID(Orders oID) {
        this.oID = oID;
    }

    public PeriodicalOrder getPoID() {
        return poID;
    }

    public void setPoID(PeriodicalOrder poID) {
        this.poID = poID;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(double buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public double getDiscountPer() {
        return discountPer;
    }

    public void setDiscountPer(double discountPer) {
        this.discountPer = discountPer;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public int getCancel() {
        return cancel;
    }

    public void setCancel(int cancel) {
        this.cancel = cancel;
    }

    @Override
    public String toString() {
        return "Order " +
                "\t id:" + getId() +
                "\t Product id:" +  getpID().getpID().getID() +
                "\t Product name: " + getpID().getpID().getName()+
                "\t Amount: "+getAmount();
    }

    public void setDetails(OrderItem o) {
        this.setBuyingPrice(o.getBuyingPrice());
        this.setDiscountPer(o.getDiscountPer());
        this.setFinalPrice(o.getFinalPrice());
    }
}
