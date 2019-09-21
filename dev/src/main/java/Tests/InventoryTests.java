package Tests;

//import BusinessLayer.Inventory.ProductInstance;
//import BusinessLayer.Inventory.StockProduct;

import BusinessLayer.Parsers.LogicParser;
import PersistenceLayer.Inventory.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Date;

import static junit.framework.TestCase.*;

public class InventoryTests {
/*
    //region Tests Data
    private static BusinessLayer.Parsers.LogicParser _logicParser;
    private static final int TODELETE_PID = 5551;
    private static ProductInstanceDao _productInstanceDao;
    private static StockProductDao _stockProductDao;
    private static CategoryDao _catDao;
    private static final int TOADD_PID = 5552;
    private static final int TOADD_SerialNum = 5552;
    private static final int CHANGEPRICE_PID = 5553;
    private static final int REPORT_DAMAGE_PID = 5554;
    private static final int REPORT_DAMAGE_SN = 5554;
    private static final int DISCTOUNTED_PID = 5556;
    private static final int CHANGENAME_PID = 5555;
    private static final int REMOVEINSTANCE_SN = 5557;
    private static final int ADD_CAT = 55512;
    private static final int ADD_CAT_PID = 55513;
    private static Date expDate;
    //endregion
//int pID, String name, int categoryID, String manufacturer, double sellingPrice, String location
    //region BeforeClass
    @BeforeClass
    public static void beforeTests() {
        _logicParser = new LogicParser();
        expDate = Date.valueOf("2222-10-22");
        try {
            _productInstanceDao = new ProductInstanceDao();
            _stockProductDao = new StockProductDao();
            _catDao = new CategoryDao();
            _catDao.remove(ADD_CAT);
            _logicParser.get_invParser().addNewCategory(0, "DefaultCat", 0);
            _logicParser.get_invParser().addProduct(ADD_CAT_PID,"NEED CAT",0,"No man",120,"1d");

            _logicParser.get_invParser().addProduct(TODELETE_PID, "Delete", 0, "no man", 10,"1d");
            _logicParser.get_invParser().removeProductInstance(TOADD_SerialNum);
            _logicParser.get_invParser().addProduct(DISCTOUNTED_PID, "Discount", 0, "no man", 100.0,"1d");
            _logicParser.get_invParser().removeProdcutAndSubRecords(TOADD_PID);
            _logicParser.get_invParser().addProduct(CHANGEPRICE_PID, "ChangeThisProductPrice", 0, "no man", 20,"1d");
            _logicParser.get_invParser().addProduct(REPORT_DAMAGE_PID, "ReportOnDamageInstance", 0, "no man", 20,"1d");
        //    _logicParser.get_invParser().addProductInstance(REPORT_DAMAGE_PID, REPORT_DAMAGE_SN, 20, expDate,);
            _logicParser.get_invParser().addProduct(CHANGENAME_PID, "OldName", 0, "none", 10,"1d");
           // _logicParser.get_invParser().addProductInstance(DISCTOUNTED_PID,REMOVEINSTANCE_SN,120,Date.valueOf("2020-10-10"));
        } catch (Exception e) {
            System.out.println("something went wrong when trying to init tests, tests may run without any problem\n might be due to db setup\n make sure all the tables are empty");
        }
    }
//endregion
/*
    //region Tests
    @Test
    public void addProductToInv() {
        _logicParser.get_invParser().addProduct(TOADD_PID, "Bamba", 0, "Osem", 2,"1d");
        assertNotNull(_stockProductDao.find(TOADD_PID));
    }

    @Test
    public void removeProduct() {
        _logicParser.get_invParser().removeProdcutAndSubRecords(TODELETE_PID);
        assertNull(_stockProductDao.find(TODELETE_PID));
    }

    @Test
    public void changePrice() {
        _logicParser.get_invParser().changeProductSellingPrice(CHANGEPRICE_PID, 100);
        StockProduct changedPricedSp = _stockProductDao.find(CHANGEPRICE_PID);
        assertEquals(100.0, changedPricedSp.getSellingPrice());
    }

    @Test
    public void addProductInstance() {
        _logicParser.get_invParser().addProductInstance(TOADD_PID, TOADD_SerialNum, 10, expDate);
        ProductInstance tBamba = _productInstanceDao.find(TOADD_SerialNum);
        assertNotNull(tBamba);
    }

    @Test
    public void reportDamagedProduct() {
        _logicParser.get_invParser().reportDefective(REPORT_DAMAGE_SN);
        ProductInstance damagedProductInstance = _productInstanceDao.find(REPORT_DAMAGE_SN);
        assertEquals(1, damagedProductInstance.getIs_damaged());
    }

    @Test
    public void changeProductName() {
        StockProduct sP = _stockProductDao.find(CHANGENAME_PID);
        _logicParser.get_invParser().changeProductName(CHANGENAME_PID, "newName");
        assertEquals("newName", sP.getpID().getName());
    }

    @Test
    public void addDiscountOnStockProduct() {
        double oldPrice = _stockProductDao.find(DISCTOUNTED_PID).getSellingPrice();
        _logicParser.get_invParser().addDiscountByPID(DISCTOUNTED_PID, 85, Date.valueOf("1990-10-10"), Date.valueOf("2222-10-10"));
        assertEquals(_stockProductDao.find((DISCTOUNTED_PID)).getPrice(), oldPrice * 0.15);

    }

    @Test
    public void removeProductInstance() {
        _productInstanceDao.remove(REMOVEINSTANCE_SN);
        ProductInstance toRemove = _productInstanceDao.find(REMOVEINSTANCE_SN);
        assertNull(toRemove);
    }

    @Test
    public void addNewCategory(){
        _logicParser.get_invParser().addNewCategory(ADD_CAT,"NO NAME CAT",0);
        Category cat = _catDao.find(ADD_CAT);
        assertNotNull(cat);

    }

    @Test
    public void addCategoryToProduct(){
        _logicParser.get_invParser().changeProductCategory(ADD_CAT_PID,0);
        Category c = _stockProductDao.find(ADD_CAT_PID).getCategory();
        assertEquals(0,c.getCategoryid());
    }
//endregion
*/

}
