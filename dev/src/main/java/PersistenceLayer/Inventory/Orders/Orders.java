package PersistenceLayer.Inventory.Orders;

import PersistenceLayer.BranchProfile;
import PersistenceLayer.Suppliers.SupplierContact;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Orders implements Serializable {
    @GeneratedValue
    @Id
    private long orderID;

    private Date orderDate;
    @ManyToOne
    private SupplierContact supplierContact;
    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<OrderItem> orderItems = new ArrayList<>();

    private int weight;
    private int cancel;
    private int InvCancel;
    private int LogisticsCancel;
    private int HRCancel;


    private int needDelivery;
    private String status = "WAITING";
    @ManyToOne()
    @JoinColumn(name = "branch", referencedColumnName = "branchName", foreignKey = @ForeignKey(name = "PERIODICAL_BRANCH_FK"))
    private BranchProfile branch;


    public Orders() {
    }

    public Orders(Date orderDate, SupplierContact supplierContact, int needDelivery, BranchProfile branch) {
        this.orderDate = orderDate;
        this.supplierContact = supplierContact;
        this.needDelivery = needDelivery;
        this.branch = branch;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public SupplierContact getSupplierContact() {
        return supplierContact;
    }

    public void setSupplierContact(SupplierContact supplierContact) {
        this.supplierContact = supplierContact;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public int getNeedDelivery() {
        return needDelivery;
    }

    public void setNeedDelivery(int needDelivery) {
        this.needDelivery = needDelivery;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getOrderID() {
        return orderID;
    }

    public void setOrderID(long orderID) {
        this.orderID = orderID;
    }

    public BranchProfile getBranch() {
        return branch;
    }

    public void setBranch(BranchProfile branch) {
        this.branch = branch;
    }

    public void addOrderItem(OrderItem o) {
        if (o == null) return;
        orderItems.add(o);
    }

    public String getOrderDetails() {
        String ans = "";
        for (OrderItem o : orderItems) {
            ans += "Product " + o.getpID().getpID().getName() + "\tAmount: " + o.getAmount() + "\tPrice: " + o.getBuyingPrice() +
                    "\nDiscount: " + o.getDiscountPer() + "% " + "Final Price: " + o.getFinalPrice();
            if(o.getCancel()==1){
                ans+=" Canceled!! order again automatically.\n";
            }
            else{
                ans+="\n";
            }
        }
        return ans;
    }

    public int getCancel() {
        return cancel;
    }

    public void setCancel(int cancel) {
        this.cancel = cancel;
    }

    public int getInvCancel() {
        return InvCancel;
    }

    public void setInvCancel(int invCancel) {
        InvCancel = invCancel;
    }

    public int getLogisticsCancel() {
        return LogisticsCancel;
    }

    public void setLogisticsCancel(int logisticsCancel) {
        LogisticsCancel = logisticsCancel;
    }

    public int getHRCancel() {
        return HRCancel;
    }

    public void setHRCancel(int HRCancel) {
        this.HRCancel = HRCancel;
    }

    public String getCancelStatus() {
        String ans = "Cancellation status: Store keeper: " + getInvCancel() + "\tLogistics manager: " + getLogisticsCancel() + "\tShifts manager: " + getHRCancel()+"\n";

        return ans;
    }
}
