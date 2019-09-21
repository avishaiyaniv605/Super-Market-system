package PersistenceLayer.Suppliers;

import PersistenceLayer.Product;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "Item")
public class Item implements Serializable {

    @Id
    private double serial_number;

    @ManyToOne
    @JoinColumn(name = "contract", referencedColumnName = "contract_number",foreignKey = @ForeignKey(name= "contract_number"))
    private SupplierContract contract;

    private double price;

    @ManyToOne
    @JoinColumn(name = "product", referencedColumnName = "ID",foreignKey = @ForeignKey(name= "ID"))
    private Product product;

    public Item(double serial_number, SupplierContract contract, double price, Product product) {
        this.serial_number = serial_number;
        this.contract = contract;
        this.price = price;
        this.product = product;
    }

    public Item() {
    }

    //region getters & setters

    public void setSerial_number(double serial_number) {
        this.serial_number = serial_number;
    }

    public void setContract(SupplierContract contract) {
        this.contract = contract;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getSerial_number() {
        return serial_number;
    }

    public SupplierContract getContract() {
        return contract;
    }

    public double getPrice() {
        return price;
    }

    public Product getProduct() {
        return product;
    }

    //endregion


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Catalog number: " + product.getID());
        stringBuilder.append(" | Serial number: " + serial_number);
        stringBuilder.append("\nName: " + product.getName());
        stringBuilder.append("\nManufacturer: " + product.getManufacturer());

        return stringBuilder.toString();
    }
}