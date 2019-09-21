package PersistenceLayer.Suppliers;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "SupplierContact")
public class SupplierContact implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int contact_id;

    @ManyToOne
    @JoinColumn(name = "supplier", referencedColumnName = "private_company_id",foreignKey = @ForeignKey(name= "private_company_id"))
    private Supplier supplier;

    private String name;

    private String phone_number;

    private String email;

    public SupplierContact(Supplier supplier, String name, String phone_number, String email) {
        this.supplier = supplier;
        this.name = name;
        this.phone_number = phone_number;
        this.email = email;
    }

    public SupplierContact() {
    }


    //region getters & setters

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public String getName() {
        return name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getEmail() {
        return email;
    }


    //endregion


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Name: " + name);
        stringBuilder.append(" | Phone number: " + phone_number);
        stringBuilder.append(" | Email: " + email);
        return stringBuilder.toString();
    }
}