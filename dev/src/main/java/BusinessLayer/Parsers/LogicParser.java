package BusinessLayer.Parsers;

import BusinessLayer.EmployeeModule.EmployeeJobs;
import BusinessLayer.EmployeeModule.EmployeeProfile;
import BusinessLayer.Inventory.OrdersManager;
import PersistenceLayer.BranchProfile;
import PersistenceLayer.BranchProfileDao;
import PersistenceLayer.Inventory.Orders.PeriodicalOrder;
import PersistenceLayer.Product;
import org.hibernate.Session;
import PersistenceLayer.DatabaseManager;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;


public class LogicParser {
    private DatabaseManager databaseManager;
    private Session session;
    private OrdersManager ordersManager;
    private InventoryParser _invParser;
    private SuppliersParser _suppParser;

    EmployeeParser employeeParser;
    DeliveryParser deliveryParser;
    DeliveryEmployeeParser deliveryEmployeeParser;

    public LogicParser() {
        databaseManager = DatabaseManager.getInstance();
        session = databaseManager.getSession();
        ordersManager = new OrdersManager();
        _invParser = new InventoryParser(ordersManager);
        _suppParser = new SuppliersParser();
        deliveryParser = new DeliveryParser(databaseManager);
        employeeParser = new EmployeeParser(databaseManager);
        deliveryEmployeeParser = new DeliveryEmployeeParser(databaseManager, employeeParser, deliveryParser);

        ordersManager.setSupplierParser(_suppParser);
        ordersManager.setDatabaseManager(databaseManager);
        ordersManager.setInventoryParser(_invParser);
        _invParser.setAlert(ordersManager);


//        _invParser.clearDB();
        // _suppParser.clearDB();
        if (emptyDB())
            initDB();
        _invParser.setBranch(String.valueOf(1));
    }

    private boolean emptyDB() {
        return _suppParser.getAllSuppliers().isEmpty();
    }

    private void initDB() {

      //  _invParser.addBranch("1", "Beer Sheba", "Susu", 6, 050506070, "Rami");
      //  _invParser.addBranch("2", "Ashdod", "Kenyon", 7, 050777770, "Alex");

        _invParser.addNewCategory(0, "Default", 0);
        _invParser.addNewCategory(1, "Snacks", 0);
        _invParser.addNewCategory(2, "Milk Products", 0);
        _invParser.addNewCategory(3, "Milk", 2);
        _invParser.addNewCategory(4, "Small milk 3%", 3);
        _invParser.addNewCategory(5, "Bathroom", 0);
        _invParser.addNewCategory(6, "Shampoo", 5);
        _invParser.addNewCategory(7, "Shampoo 700 ML", 6);
        _invParser.setBranch("1");


        initProducts();

        _suppParser.addSupplier(1, "123456", "pay on 1st", "Supp1", "somewhere");
        _suppParser.addSupplier(2, "654321", "pay on 10th", "Supp2", "dunno");
        _suppParser.addSupplier(3, "111111", "pay on 10th", "Supp3", "Negev");

        _suppParser.addContact(1, "Yossi", "050505011", "yossi@gmail.com");
        _suppParser.addContact(1, "Yaacov", "05111111", "yaacov@gmail.com");
        _suppParser.addContact(2, "Moshe", "050123123", "moshe@gmail.com");
        _suppParser.addContact(2, "Isaac", "0501235050", "isaac@gmail.com");
        _suppParser.addContact(3, "Ron", "0501235050", "ron@gmail.com");

        int firstContract = _suppParser.addContract(1, true, true);           // has permanent delivery days
        int secondContract = _suppParser.addContract(2, true, true);          // has deliveries but not permanent days
        int thirdContract = _suppParser.addContract(3, false, false);         // no deliveries at all

        _suppParser.addDeliveryDay(1, "Sunday", firstContract);
        _suppParser.addDeliveryDay(1, "Monday", firstContract);
        _suppParser.addDeliveryDay(1, "Tuesday", firstContract);
        _suppParser.addDeliveryDay(1, "Wednesday", firstContract);
        _suppParser.addDeliveryDay(1, "Thursday", firstContract);
        _suppParser.addDeliveryDay(1, "Friday", firstContract);
        _suppParser.addDeliveryDay(1, "Saturday", firstContract);
        _suppParser.addDeliveryDay(2, "Sunday", secondContract);
        _suppParser.addDeliveryDay(2, "Monday", secondContract);
        _suppParser.addDeliveryDay(2, "Tuesday", secondContract);
        _suppParser.addDeliveryDay(2, "Wednesday", secondContract);
        _suppParser.addDeliveryDay(2, "Thursday", secondContract);
        _suppParser.addDeliveryDay(2, "Friday", secondContract);
        _suppParser.addDeliveryDay(2, "Saturday", secondContract);



        _suppParser.addCatalogList(thirdContract, 1, 2, 3.5, 1, 3, true);    // best seller
        _suppParser.addCatalogList(thirdContract, 2, 0, 0, 1, 4, true);
        _suppParser.addCatalogList(firstContract, 3, 5, 3.5, 1, 7, true);     // best seller
        _suppParser.addCatalogList(firstContract, 4, 10, 3.5, 1, 4, true);    // best seller
        _suppParser.addCatalogList(firstContract, 5, 0, 0, 1, 3, false);
        _suppParser.addCatalogList(firstContract, 6, 0, 0, 1, 3, false);
        _suppParser.addCatalogList(firstContract, 9, 0, 0, 1, 4, false);
        _suppParser.addCatalogList(firstContract, 10, 0, 0, 1, 4, false);

        _suppParser.addCatalogList(secondContract, 1, 0, 0, 2, 3, false);
        _suppParser.addCatalogList(thirdContract, 2, 0, 0, 2, 2.95, false);// best seller
        _suppParser.addCatalogList(secondContract, 5, 10, 3.5, 2, 3, true);    // best seller
        _suppParser.addCatalogList(secondContract, 6, 10, 3.5, 2, 3, true);    // best seller
        _suppParser.addCatalogList(secondContract, 7, 7, 3.5, 2, 2, true);     // best seller
        _suppParser.addCatalogList(secondContract, 8, 6, 3.5, 2, 4, true);     // best seller
        _suppParser.addCatalogList(secondContract, 9, 0, 0, 2, 3.98, false);
        _suppParser.addCatalogList(secondContract, 10, 0, 0, 2, 3.95, false);

        _suppParser.addCatalogList(firstContract, 1, 0, 0, 3, 3.1, false);
        _suppParser.addCatalogList(secondContract, 2, 0, 0, 3, 3, false);
        _suppParser.addCatalogList(thirdContract, 5, 0, 0, 3, 2.97, false);
        _suppParser.addCatalogList(thirdContract, 6, 0, 0, 3, 2.99, false);
        _suppParser.addCatalogList(thirdContract, 9, 10, 3.5, 3, 4, true);     // best seller
        _suppParser.addCatalogList(thirdContract, 10, 10, 3.5, 3, 4, true);    // best seller
        _suppParser.addCatalogList(thirdContract, 11, 10, 3.5, 3, 15, true);   // best seller
        _suppParser.addCatalogList(thirdContract, 12, 10, 3.5, 3, 15, true);   // best seller

        _suppParser.addCatalogList(1, 1, 100, 5, 2, 6, true);
        _suppParser.addCatalogList(2, 1, 0, 0, 3, 3.5, false);
        _suppParser.addCatalogList(3, 2, 0, 0, 2, 2.1, false);
        _suppParser.addCatalogList(4, 2, 150, 3, 3, 5.5, true);
        _suppParser.addCatalogList(5, 3, 0, 0, 1, 2.1, false);
    }

    private void initProducts() {
        HashMap<Integer, Integer> orders = new HashMap<>();
        orders.put(1, 3);
        orders.put(2, 4);
        _suppParser.addProduct(1, "Bamba", "Osem");
        _suppParser.addProduct(2, "Bisli", "Osem");
        _suppParser.addProduct(3, "Shampoo Hawaii", "Hawaii");
        _suppParser.addProduct(4, "Milk", "Tnuva");
        _suppParser.addProduct(5, "Milk", "Yotvata");
        _suppParser.addProduct(6, "Milk", "Tara");
        _suppParser.addProduct(7, "Yogurt", "Tnuva");
        _suppParser.addProduct(8, "Orange Juice", "Tapuzina");
        _suppParser.addProduct(9, "Apple Juice", "Tapuzina");
        _suppParser.addProduct(10, "Mango Juice", "Tapuzina");
        _suppParser.addProduct(11, "Chicken", "Tnuva");
        _suppParser.addProduct(12, "Cheese", "Emek");
        double g = 0.00000001;
        for (int i = 1; i <= 2; i++) {
            _invParser.setBranch(String.valueOf(i));
            _invParser.addProduct(1, 1, 4.5, "1D");
            _invParser.addProduct(2, 1, 6, "1C");
            _invParser.addProduct(3, 7, 19, "2A");
            _invParser.addProduct(4, 4, 5.5, "3A");
            _invParser.addProductInstance(1, 1 + (0.00000001 * i), 5, Date.valueOf("2021-05-05"),get_invParser().getBranch());
            _invParser.addProductInstance(4, 4 + (g + 0.00000001 * i), 5, Date.valueOf("2021-07-05"),get_invParser().getBranch());
            _invParser.addProductInstance(4, 4 + (g + 0.00000002 * i), 5, Date.valueOf("2021-05-07"),get_invParser().getBranch());
            g = 0.00000002;
            _invParser.setMinimum(4, 2);
            _invParser.setMinimum(1, 1);
            if (i == 1)
                _invParser.addPeriodicalOrder(new Date(System.currentTimeMillis()), 5, orders);

        }
    }

    public InventoryParser get_invParser() {
        return _invParser;
    }

    public void close() {
        databaseManager.closeSession();
    }

    public Product getProduct(int pid) {
        return session.get(Product.class, pid);
    }

    //suppliers
    public SuppliersParser getSuppParser() {
        return _suppParser;
    }

    public String getAllBranches() {
        BranchProfileDao branchDao = new BranchProfileDao();
        List<BranchProfile> branches = branchDao.findAll();
        String branchesS = "";
        for (BranchProfile b : branches) {
            branchesS += b.toString() + "\n";

        }
        return branchesS;
    }
    public DeliveryParser getDeliveryParser() {
        return deliveryParser;
    }

    public boolean setBranch(String bBname) {
        return _invParser.setBranch(bBname);
    }

    public String getTime() {
        return _invParser.getTime();
    }

    public String nextDay() {
        return _invParser.nextDay();
    }

    public void saveDate() {
        _invParser.saveDate();

    }
    public EmployeeParser getEmployeeParser() {
        return employeeParser;
    }

    public DeliveryEmployeeParser getDeliveryEmployeeParser() {
        return deliveryEmployeeParser;
    }



}
