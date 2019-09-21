package BusinessLayer.DeliveryModule;
import PersistenceLayer.BranchProfile;
import PersistenceLayer.Inventory.Orders.Orders;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import static javax.persistence.TemporalType.DATE;

@Entity(name="DeliveryProfile")
public class DeliveryProfile implements Serializable {
    @GeneratedValue
    @Id
    private int ID;


    @OneToOne
    @JoinColumn(name = "truck", referencedColumnName = "ID", foreignKey = @ForeignKey(name = "IDFK"))
    private TruckProfile truck;  //FK
    @Temporal(DATE)
    private Date date;
    @Temporal(DATE)
    private Date leavingTime;
    private int deliveryWeight;
    private String driverName;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Orders> OrdersList;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "source", referencedColumnName = "branchName", foreignKey = @ForeignKey(name = "branchNameFK"))
    private BranchProfile source;
    @OneToMany(cascade = CascadeType.ALL)
    private List<BranchProfile> destinations;




    public DeliveryProfile() {

    }
    public DeliveryProfile(TruckProfile truck, String driverName, int deliveryWeight, List<Orders> orderlist, List<BranchProfile> destList, BranchProfile loc, Date date, Date leavingTime) {

        this.truck = truck;
        this.driverName = driverName;
        this.date = date;
        this.leavingTime = leavingTime;
        this.deliveryWeight = deliveryWeight;
        OrdersList = orderlist;
        destinations = destList;
        source = loc;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public TruckProfile getTruck() {
        return truck;
    }

    public void setTruck(TruckProfile truck) {
        this.truck = truck;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getLeavingTime() {
        return leavingTime;
    }

    public void setLeavingTime(Date leavingTime) {
        this.leavingTime = leavingTime;
    }

    public int getDeliveryWeight() {
        return deliveryWeight;
    }

    public void setDeliveryWeight(int deliveryWeight) {
        this.deliveryWeight = deliveryWeight;
    }

    public List<Orders> getOrdersList() {
        return OrdersList;
    }

    public void setProductsFormList(List<Orders> OrdersList) {
        this.OrdersList = OrdersList;
    }

    public List<BranchProfile> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<BranchProfile> destinations) {
        this.destinations = destinations;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public void setOrdersList(List<Orders> ordersList) {
        OrdersList = ordersList;
    }

    public BranchProfile getSource() {
        return source;
    }

    public void setSource(BranchProfile source) {
        this.source = source;
    }

}
