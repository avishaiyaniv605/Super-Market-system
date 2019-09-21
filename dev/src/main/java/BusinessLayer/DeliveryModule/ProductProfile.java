package BusinessLayer.DeliveryModule;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "ProductProfile")
public class ProductProfile {

    @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "product_profile")
    @SequenceGenerator(name = "product_profile", sequenceName = "product_seq", allocationSize = 1)
    private int ID;
    @Column(unique = true)
    private String name;
    private int weight;

    public ProductProfile(){

    }

    public ProductProfile(String name, int weight) {
        this.name = name;
        this.weight = weight;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductProfile that = (ProductProfile) o;
        return getWeight() == that.getWeight() &&
                Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getWeight());
    }

    @Override
    public String toString() {
        return ID + ") "  +
                name + '\n';
    }
}