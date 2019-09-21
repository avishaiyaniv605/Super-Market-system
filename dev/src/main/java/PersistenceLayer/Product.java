package PersistenceLayer;



import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Product implements Serializable {
    @Id
    private int ID;
    private String name;
    private String manufacturer;
    private int weight;

    //region Ctors
    public Product(int ID, String name, String manufacturer) {
        this.ID = ID;
        this.name = name;
        this.manufacturer = manufacturer;
    }

    public Product() {
    }
    //endregion

    //region getters & setters
    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
    public int getID() {
        return ID;
    }
    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
    //endregion
}
