package PersistenceLayer.Suppliers;

import PersistenceLayer.Product;

import javax.persistence.*;
import java.io.Serializable;


@Entity(name = "Catalog")
public class Catalog implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "contract", referencedColumnName = "contract_number",foreignKey = @ForeignKey(name= "contract_number"))
    private SupplierContract contract;

    @Id
    @ManyToOne
    @JoinColumn(name = "product", referencedColumnName = "ID",foreignKey = @ForeignKey(name= "catalog_number"))
    private Product product;

    private int quantity;
    private double discount;
    private double price;
    private boolean hasQL;

    public Catalog(SupplierContract contract, Product product, int quantity, double discount, double price, boolean hasQL) {
        this.contract = contract;
        this.product = product;
        this.quantity = quantity;
        this.discount = discount;
        this.price = price;
        this.hasQL = hasQL;
    }

    public Catalog() {
    }


    // region getters and setters

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean hasQL() {
        return hasQL;
    }

    public void setContract(SupplierContract contract) {
        this.contract = contract;
    }

    public void setItem(Product product) {
        this.product = product;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public SupplierContract getContract() {
        return contract;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getDiscount() {
        return discount;
    }


    //endregion


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Product catalog id: " + product.getID());
        stringBuilder.append("\nProduct Name: " + product.getName());
        stringBuilder.append("\nProduct Manufacturer: " + product.getManufacturer());
        stringBuilder.append("\nProduct Price: " + price);
        if (hasQL)
            stringBuilder.append("\nFor each " + quantity + " units, you get a discount of " + discount + "%");
        return stringBuilder.toString();
    }
}
