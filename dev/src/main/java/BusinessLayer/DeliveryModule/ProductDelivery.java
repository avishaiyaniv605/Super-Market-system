package BusinessLayer.DeliveryModule;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity(name = "ProductDelivery")
public class ProductDelivery implements Serializable {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prod", referencedColumnName = "ID", foreignKey = @ForeignKey(name = "ID"))
    private ProductProfile prod;

    private int amount;
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prdctFrmId", referencedColumnName = "ID", foreignKey = @ForeignKey(name = "ID"))
    private ProductsFormProfile prdctFrmId; //fk


    public ProductDelivery(ProductProfile pp, int amount, ProductsFormProfile prdctFrmId) {
        this.prod = pp;
        this.amount = amount;
        this.prdctFrmId = prdctFrmId;
    }

    public ProductDelivery() {}
    public ProductProfile getID() {
        return prod;
    }

    public void setID(ProductProfile ID) {
        this.prod = ID;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public ProductsFormProfile getPrdctFrmId() {
        return prdctFrmId;
    }

    public void setPrdctFrmId(ProductsFormProfile prdctFrmId) {
        this.prdctFrmId = prdctFrmId;
    }


}
