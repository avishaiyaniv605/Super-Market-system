package PersistenceLayer.Suppliers;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "DeliveryDay")
public class DeliveryDay implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "supplier", referencedColumnName = "private_company_id",foreignKey = @ForeignKey(name= "private_company_id"))
    private Supplier supplier;

    @Id
    @ManyToOne
    @JoinColumn(name = "contract", referencedColumnName = "contract_number",foreignKey = @ForeignKey(name= "contract_number"))
    private SupplierContract contract;

    @Id
    private String delivery_day;

    public DeliveryDay(Supplier supplier, SupplierContract contract, String delivery_day) {
        this.supplier = supplier;
        this.contract = contract;
        this.delivery_day = delivery_day;
    }

    public DeliveryDay() {
    }
    //region getters & setters

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public void setContract(SupplierContract contract) {
        this.contract = contract;
    }

    public void setDelivery_day(String delivery_day) {
        this.delivery_day = delivery_day;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public SupplierContract getContract() {
        return contract;
    }

    public String getDelivery_day() {
        return delivery_day;
    }

    //endregion
}