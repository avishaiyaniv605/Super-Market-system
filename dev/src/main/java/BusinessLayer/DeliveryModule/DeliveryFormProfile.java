package BusinessLayer.DeliveryModule;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
@Entity(name = "DeliveryFormProfile")
public class DeliveryFormProfile implements Serializable {
    @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "delivery_form")
    @SequenceGenerator(name = "delivery_form", sequenceName = "delivery_form_seq", allocationSize = 1)
    private int formID; //PK
    @OneToOne
    @JoinColumn(name = "deliveryID", referencedColumnName = "ID", foreignKey = @ForeignKey(name = "deliverId_FK"))
    private DeliveryProfile delivery;
    private int truckWeight;
    private String issues;


    public DeliveryFormProfile() {
        this.issues = "";
    }

    public int getFormID() {
        return formID;
    }

    public void setFormID(int formID) {
        this.formID = formID;
    }

    public DeliveryProfile getDelivery() {
        return delivery;
    }

    public void setDelivery(DeliveryProfile delivery) {
        this.delivery = delivery;
    }

    public int getTruckWeight() {
        return truckWeight;
    }

    public void setTruckWeight(int truckWeight) {
        this.truckWeight = truckWeight;
    }

    public String getIssues() {
        return issues;
    }

    public void setIssues(String issues) {
        this.issues += " " + issues;
    }

}
