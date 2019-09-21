package PersistenceLayer.Inventory;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity(name = "Category")
public class Category implements Serializable {
    @Id
    @ColumnDefault("0")
    private int categoryid;
    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "categoryid", foreignKey = @ForeignKey(name = "FK_cat_parent"))
    private Category ParentId;
    private String title;


    @OneToMany(mappedBy = "ParentId")
    private Set<Category> subCategories;
    @OneToMany(cascade = CascadeType.REFRESH, orphanRemoval = true)
    private List<StockProduct> products = new ArrayList<>();
    public List<StockProduct> getProducts() {
        return products;
    }

    public Category(int categoryID, String categoryName, Category parentid) {
        this.categoryid=categoryID;
        this.title= categoryName;
        this.setParentId(parentid);
    }

    public Category() {
    }

    //region getters & setters
    public int getCategoryid() {
        return categoryid;
    }
    public Category getParentId() {
        return ParentId;
    }
    public String getTitle() {
        return title;
    }
    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
    }
    public void setParentId(Category parentId) {
        ParentId = parentId;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public Set<Category> getSubCategories() {
        return subCategories;
    }
    //endregion

    @Override
    public String toString() {
        if(getParentId()==null){
            return "ID: " + getCategoryid() +
                    "\tName: " + getTitle() +
                    "\tParent ID: none\n";
        }
        return "ID: " + getCategoryid() +
                "\tName: " + getTitle() +
                "\tParent ID: " + getParentId().categoryid+ "\n";
    }

}
