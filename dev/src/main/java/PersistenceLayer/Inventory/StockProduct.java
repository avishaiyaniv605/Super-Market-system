package PersistenceLayer.Inventory;

import PersistenceLayer.BranchProfile;
import PersistenceLayer.Product;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
@IdClass(StockProductKey.class)
@Entity
public class StockProduct implements Serializable {
    @Id
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "pID", referencedColumnName = "ID", foreignKey = @ForeignKey(name = "PRODUCT_ID_FK"))
    private Product pID;


    @Id
    @ManyToOne()
    @JoinColumn(name = "branch", referencedColumnName = "branchName", foreignKey = @ForeignKey(name = "STOCK_PRODUCT_BRANCH_FK"))
    private BranchProfile branch;
    @ColumnDefault("0")
    private int quantity;
    @ManyToOne
    @ColumnDefault("0")
    @JoinColumn(name = "discount", referencedColumnName = "dID", foreignKey = @ForeignKey(name = "PRODUCT_dID_FK"))
    private Discount discount;
    @ColumnDefault("0")
    private int storeQuantity;
    @ColumnDefault("0")
    private double sellingPrice;
    @ColumnDefault("0")
    private int minimumAmount = 0;
    @ColumnDefault("0")
    private int storageQuantity;
    private String location;
    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ProductInstance> products = new ArrayList<>();
    @ManyToOne
    @ColumnDefault("0")
    @JoinColumn(name = "category", referencedColumnName = "categoryid", foreignKey = @ForeignKey(name = "FK_cat"))
    private Category category;


    public List<ProductInstance> getProducts() {
        return products;
    }

    //region constructors
    public StockProduct(Product pID, BranchProfile branch, Category cat, double sellingPrice, String location, Discount discount) {
        this.pID = pID;
        this.quantity = 0;
        this.storeQuantity = 0;
        this.storageQuantity = 0;
        this.location = location;
        this.category = cat;
        this.sellingPrice = sellingPrice;
        this.discount = discount;
        this.branch = branch;

    }

    public StockProduct(Product pID, Category cat, double sellingPrice) {
        this.pID = pID;
        this.quantity = 0;
        this.storeQuantity = 0;
        this.storageQuantity = 0;
        this.category = cat;
        this.sellingPrice = sellingPrice;

    }

    public StockProduct(Product pID, BranchProfile branch, int quantity, int storeQuantity, int storageQuantity, List<ProductInstance> products) {
        this.pID = pID;
        this.quantity = quantity;
        this.storeQuantity = storeQuantity;
        this.storageQuantity = storageQuantity;
        this.products = products;
        this.branch = branch;
    }

    public StockProduct() {

    }
    //endregion

    //region getters & setters
    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public int getMinimumAmount() {
        return minimumAmount;
    }

    public void setMinimumAmount(int minimumAmount) {
        this.minimumAmount = minimumAmount;
    }

    public Product getpID() {
        return pID;
    }

    public void setpID(Product pID) {
        this.pID = pID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getStoreQuantity() {
        return storeQuantity;
    }

    public void setStoreQuantity(int storeQuantity) {
        this.storeQuantity = storeQuantity;
        updateTotal();
    }

    public int getStorageQuantity() {
        return storageQuantity;
    }

    public void setStorageQuantity(int storageQuantity) {
        this.storageQuantity = storageQuantity;
        updateTotal();
    }

    public void setProducts(List<ProductInstance> products) {
        this.products = products;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public Category getCategory() {
        return category;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public BranchProfile getBranch() {
        return branch;
    }

    public void setBranch(BranchProfile branch) {
        this.branch = branch;
    }

    //endregion
    public double getPrice() {
        Date today = new Date(System.currentTimeMillis());
        int theDiscount = 0;
        double res = getSellingPrice();
        if (discount != null) {
            if (discount.getStartTime() != null && discount.getEndTime() != null)

                if ((today.after(discount.getStartTime()) && today.before(discount.getEndTime())) || (today.equals(discount.getStartTime()) && (discount.getStartTime().equals(discount.getEndTime())))) {
                    theDiscount = getDiscount().getPercentage();
                    res = res - (getSellingPrice() * discount.getPercentage()) / 100;
                }
        }

        return res;
    }

    private int get_correct_discount() {
        if (getDiscount() != null)
            if (getPrice() == getSellingPrice())
                return 0;
            else
                return discount.getPercentage();

        return 0;
    }

    @Override
    public String toString() {
        return "Product ID: " + pID.getID() + "\tName: " + pID.getName() +
                "\tCategory id: " + getCategory().getCategoryid() +
                "\tCategory: " + getCategory().getTitle() +
                "\nTotal Quantity: " + getQuantity() + "\tStorage: " + getStorageQuantity() + "\tStore: " + getStoreQuantity() + "\tMinimum amount: " + getMinimumAmount() +
                "\nSelling price: " + getPrice() + "\tdiscount: " + get_correct_discount() + "%\n";
    }

    private void updateTotal() {
        if ((getStorageQuantity() + getStoreQuantity()) >= 0)
            setQuantity(getStorageQuantity() + getStoreQuantity());
    }

}
