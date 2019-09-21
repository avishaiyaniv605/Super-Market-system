package PersistenceLayer.Suppliers;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "SupplierContract")
public class SupplierContract implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int contract_number;

    @ManyToOne
    @JoinColumn(name = "supplier", referencedColumnName = "private_company_id",foreignKey = @ForeignKey(name= "private_company_id"))
    private Supplier supplier;

    private boolean permanent_days;
    private boolean delivering;

    public SupplierContract(Supplier supplier, boolean permanent_days, boolean delivering) {
        this.supplier = supplier;
        this.permanent_days = permanent_days;
        this.delivering = delivering;
    }

    public SupplierContract(int contract_number) {
        this.contract_number = contract_number;
    }

    public SupplierContract() {
    }

    //region getters & setters

    public void setContract_number(int contract_number) {
        this.contract_number = contract_number;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public void setPermanent_days(boolean permanent_days) {
        this.permanent_days = permanent_days;
    }

    public int getContract_number() {
        return contract_number;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public boolean isPermanent_days() {
        return permanent_days;
    }

    public boolean isDelivering() {
        return delivering;
    }

//endregion


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Contract number: " + contract_number);
        String hasDays = "";
        if (!permanent_days)
            hasDays = "no ";
        stringBuilder.append("\nHas " + hasDays + "permanent delivery days");
        if (permanent_days)
            stringBuilder.append(":");
        else if (delivering)
            stringBuilder.append(", but has deliveries.");
        return stringBuilder.toString();
    }
}