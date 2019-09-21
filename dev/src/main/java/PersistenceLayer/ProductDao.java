package PersistenceLayer;

public class ProductDao {

    public Product find(int pID) {
        DatabaseManager db= DatabaseManager.getInstance();
        try{
            Product ins = db.getSession().get(Product.class,pID);
            return ins;
        }
        catch (Exception e){return null;}
    }

    public Product insert(int pID, String name, String manufacturer) {
        Product newProduct;
        try{
            DatabaseManager db= DatabaseManager.getInstance();
            newProduct = new Product(pID,name,manufacturer);
            db.getSession().save(newProduct);
            db.commitAndFLush();
            return newProduct;
        }
        catch (Exception e){return null;}
    }

    public void update(Product product) {
        DatabaseManager db= DatabaseManager.getInstance();
        db.getSession().update(product);
        db.commitAndFLush();
    }
}
