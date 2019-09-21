package PersistenceLayer.Inventory.Orders;

import PersistenceLayer.BranchProfile;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
public class PeriodicalOrder implements Serializable {

    @Id
    @GeneratedValue
    private long pID;

    private Date orderDate;

    @ColumnDefault("7")
    private int period;

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<OrderItem> orderItems=new ArrayList<>();

    @ManyToOne()
    @JoinColumn(name = "branch", referencedColumnName = "branchName", foreignKey = @ForeignKey(name = "PERIODICAL_BRANCH_FK"))
    private BranchProfile branch;



    public PeriodicalOrder() {
    }
    public PeriodicalOrder(long id,BranchProfile branch) {
        this.pID= id;
        this.orderDate= Date.valueOf("9999-09-09");
        this.period=0;
        this.branch=branch;
    }

    public PeriodicalOrder(Date orderDate, int period,BranchProfile branch) {
        this.orderDate = orderDate;
        this.period = period;
        this.branch= branch;
    }

    public void setpID(long pID) {
        this.pID = pID;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public long getpID() {
        return pID;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }


    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }
    public BranchProfile getBranch() {
        return branch;
    }

    public void setBranch(BranchProfile branch) {
        this.branch = branch;
    }
    public String printItems(){
        String ans= "";
        for(OrderItem o : orderItems){
            ans+= o.toString()+ "\n";
        }
        return ans;
    }
    @Override
    public String toString() {
        return "Periodical order id: " +pID+
                "\tNext order date: " +orderDate.toString()+
                " \tPeriod: " +period+"\n" + printItems();
    }

    public void addOrder(OrderItem o) {
        this.orderItems.add(o);
    }

    public void removeOrder(OrderItem to_delete) {
        this.orderItems.remove(to_delete);
    }
}
