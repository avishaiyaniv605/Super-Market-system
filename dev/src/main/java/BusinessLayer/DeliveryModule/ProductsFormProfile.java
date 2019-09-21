package BusinessLayer.DeliveryModule;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity(name = "ProductsFormProfile")
public class ProductsFormProfile {
    @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "products_form")
    @SequenceGenerator(name = "products_form", sequenceName = "products_form_seq", allocationSize = 1)
    private int ID;
    @OneToMany(cascade = CascadeType.ALL)
    private List<ProductDelivery> products;
    @ManyToOne
    @JoinColumn(name = "delivery", referencedColumnName = "ID", foreignKey = @ForeignKey(name = "FK_ID"))
    private DeliveryProfile delivery;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "destination", referencedColumnName = "destId", foreignKey = @ForeignKey(name = "destFK"))
    private DestinationProfile destination;
    private int formWeight;

    //constructor
    public ProductsFormProfile(DestinationProfile destination) {
        this.destination = destination;
        products = new LinkedList<>();
    }

    public ProductsFormProfile() {
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public DeliveryProfile getDelivery() {
        return delivery;
    }

    public void setDelivery(DeliveryProfile delivery) {
        this.delivery = delivery;
    }

    public DestinationProfile getDestination() {
        return destination;
    }

    public void setDestination(DestinationProfile destination) {
        this.destination = destination;
    }

    public DeliveryProfile getDeliveryProfile() {
        return delivery;
    }

    public void setDeliveryProfile(DeliveryProfile deliveryProfile) {
        this.delivery = deliveryProfile;
    }

        public List<ProductDelivery> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDelivery> products) {
        this.products = products;
    }

    public int getFormWeight() {
        return formWeight;
    }

    public void setFormWeight(int formWeight) {
        this.formWeight = formWeight;
    }

    public void checkWeight() {
        int formWeight = 0;
        for(ProductDelivery prod : getProducts()){
            formWeight += prod.getAmount()*prod.getID().getWeight();
        }
        setFormWeight(formWeight);
    }
}
