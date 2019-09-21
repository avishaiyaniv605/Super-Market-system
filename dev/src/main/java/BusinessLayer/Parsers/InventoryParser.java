package BusinessLayer.Parsers;

import BusinessLayer.EmployeeModule.EmployeeJobs;
import BusinessLayer.EmployeeModule.EmployeeProfile;
import BusinessLayer.Inventory.OrdersManager;
import BusinessLayer.Inventory.minimumAlert;
import PersistenceLayer.*;
import PersistenceLayer.Inventory.*;
import PersistenceLayer.Inventory.Orders.*;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.Date;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class InventoryParser {
    private DatabaseManager databaseManager;
    private List<minimumAlert> _alert;
    private ProductDao productDao;
    private StockProductDao spDao;
    private ProductInstanceDao piDao;
    private CategoryDao categoryDao;
    private DiscountDao discountDao;
    private BranchProfileDao branchProfileDao;
    private BranchProfile branch;
    private OrdersManager ordersManager;
    private PeriodicalOrderDao poDao;
    private OrderItemDao oiDao;
    private LastDateDao ldDao;
    private OrderDao orderDao;
    private Date today;
    private Date lastUpdate;

    public InventoryParser(OrdersManager ordersManager) {
        this.databaseManager = DatabaseManager.getInstance();
        _alert = new ArrayList<minimumAlert>();
        productDao = new ProductDao();
        spDao = new StockProductDao();
        piDao = new ProductInstanceDao();
        categoryDao = new CategoryDao();
        discountDao = new DiscountDao();
        branchProfileDao = new BranchProfileDao();
        this.ordersManager = ordersManager;
        ldDao = new LastDateDao();
        poDao = new PeriodicalOrderDao();
        oiDao = new OrderItemDao();
        orderDao= new OrderDao();
        retrieveDate();
    }

    private void retrieveDate() {
        LastDate lastDate = ldDao.findAll();
        if (lastDate == null)   // empty db
            today = new Date(System.currentTimeMillis());
        else
            today = lastDate.getDate();
    }

    public void saveDate() {
        ldDao.removeAllAndInsert(today);
    }

    //region create
    public boolean addProduct(int pID, String name, int categoryID, String manufacturer, double sellingPrice, String location) {
        if (pID < 0) return false;
        boolean prodExists = productDao.find(pID) != null;
        if (prodExists)
            return addProduct(pID, categoryID, sellingPrice, location);
        else {
            Product newProduct = productDao.insert(pID, name, manufacturer);
            if (newProduct == null) return false;
            Category productCat = categoryDao.find(categoryID);
            if (productCat == null) {
                productCat = categoryDao.find(0);
            }
            Discount defaultD = discountDao.find(0);
            StockProduct s = spDao.insert(newProduct, branch, productCat, sellingPrice, location, defaultD);
            if (s == null) return false;
            return true;
        }
    }

    public boolean addProduct(int pID, int categoryID, double sellingPrice, String location) {
        if (pID < 0) return false;
        Product newProduct = productDao.find(pID);
        if (newProduct == null) return false;
        Category productCat = categoryDao.find(categoryID);
        if (productCat == null) {
            productCat = categoryDao.find(0);
        }
        Discount defaultD = discountDao.find(0);
        StockProduct s = spDao.insert(newProduct, branch, productCat, sellingPrice, location, defaultD);
        if (s == null) return false;
        return true;
    }

    public boolean addProductInstance(int pID, double sn, double bPrice, Date date,BranchProfile branch) {
        if (pID < 0 || sn < 0 || bPrice < 0) return false;
        Product product_temp = productDao.find(pID);
        StockProduct choosenProduct = spDao.find(product_temp, branch);

        if (choosenProduct == null) return false;
        ProductInstance newProduct = piDao.insert(choosenProduct, sn, bPrice, date);
        if (newProduct == null) return false;
        choosenProduct.setStorageQuantity(choosenProduct.getStorageQuantity() + 1);
        spDao.update(choosenProduct);
        refreshExpiredProducts();
        return true;
    }

    public boolean addDiscountByPID(int pID, int percentage, Date startTime, Date endTime) {
        if (endTime != null && startTime != null && startTime.after(endTime)) return false;
        if (pID < 0 || percentage < 0 || percentage > 100) return false;
        Product product_temp = productDao.find(pID);
        StockProduct sp = spDao.find(product_temp, branch);
        if (sp == null) return false;
        Discount d = discountDao.insert(percentage, startTime, endTime);
        if (d == null) return false;
        sp.setDiscount(d);
        spDao.update(sp);
        return true;
    }

    public boolean addDiscountByCategory(int cID, int percentage, Date startTime, Date endTime) {
        if (endTime != null && startTime != null && startTime.after(endTime)) return false;
        if (cID < 0 || percentage < 0 || percentage > 100) return false;
        Discount d = discountDao.insert(percentage, startTime, endTime);
        if (d == null) return false;

        List<Integer> catList = new ArrayList<>();
        Set<Category> childs = categoryDao.findWithsub(cID);
        for (Category cat : childs) {
            catList.add(cat.getCategoryid());
        }
        Set<Integer> set = new HashSet<>(catList);
        catList.clear();
        catList.addAll(set);

        List<StockProduct> stockList = spDao.findInCat(catList);
        if (stockList == null) return false;
        for (StockProduct sp : stockList) {
            sp.setDiscount(d);
            spDao.update(sp);
        }
        return true;
    }

    public boolean addNewCategory(int categoryID, String categoryName, int parentID) {
        Category parent = null;
        if (parentID < 0 || categoryID < 0) return false;
        if (parentID != 0)
            parent = categoryDao.find(parentID);
        Category newCat = categoryDao.insert(categoryID, categoryName, parent);
        if (newCat == null) return false;
        return true;
    }

    public boolean addBranch(String branch_name, String city, String street, int number, int phone, String contact) {
        BranchProfile newB = branchProfileDao.insert(branch_name, city, street, number, phone, contact);
        if (newB == null) return false;
        return true;
    }

    public boolean addPeriodicalOrder(Date startDate, int period, HashMap<Integer, Integer> orders) {
        if (period < 0 || startDate == null) return false;
        List<OrderItem> orderItems = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : orders.entrySet()) {
            StockProduct sp = spDao.find(productDao.find(entry.getKey()), branch);
            if (sp != null) {
                OrderItem o = oiDao.insert(sp, entry.getValue());
                orderItems.add(o);
            }
        }

        PeriodicalOrder pOrder = poDao.insert(startDate, period, orderItems, branch);
        if (pOrder == null) return false;
        return true;
    }
    //endregion

    //region remove
    public boolean removeProductInstance(int pID) {

        StockProduct s = spDao.find(productDao.find(pID), branch);
        if (s == null) return false;
        double sn = piDao.findLast(s,branch);
        if (sn == 0)
            return false;
        ProductInstance toDelete = databaseManager.getSession().get(ProductInstance.class, sn);
        if (toDelete == null) return false;
        if (piDao.remove(sn)) {
            checkMinValue(s);
            return true;
        }
        return false;
    }

    public boolean removeCategory(int cID) {
        if (cID == 0) return false;
        Category category = categoryDao.find(cID);
        Category defaultCategory = categoryDao.find(0);
        if (category == null) return false;
        List<Integer> list = new ArrayList<>();
        list.add(cID);
        List<StockProduct> products = spDao.findInCat(list);
        Category toUpdate = category.getParentId() == null ? defaultCategory : category.getParentId();
        for (StockProduct s : products) {
            s.setCategory(toUpdate);
        }
        categoryDao.remove(cID);
        return true;
    }

    public boolean removeProductDiscount(int pID) {
        Product product_temp = productDao.find(pID);
        StockProduct choosenProduct = spDao.find(product_temp, branch);
        if (choosenProduct == null) return false;
        Discount defaultDiscount = discountDao.find(0);
        choosenProduct.setDiscount(defaultDiscount);
        spDao.update(choosenProduct);
        return true;
    }

    public boolean removeCategoryDiscount(int cID) {
        if (cID < 0) return false;
        Discount defaultDiscount = discountDao.find(0);
        List<Integer> catList = new ArrayList<>();
        Set<Category> childs = categoryDao.findWithsub(cID);
        for (Category cat : childs) {
            catList.add(cat.getCategoryid());
        }
        Set<Integer> set = new HashSet<>(catList);
        catList.clear();
        catList.addAll(set);

        List<StockProduct> stockList = spDao.findInCat(catList);
        if (stockList == null) return false;
        for (StockProduct sp : stockList) {
            sp.setDiscount(defaultDiscount);
            spDao.update(sp);
        }
        return true;
    }

    public boolean removeProdcutAndSubRecords(int pID) {
        try {
            Product toDelete = databaseManager.getSession().get(Product.class, pID);
            StockProduct spDelete = databaseManager.getSession().get(StockProduct.class, pID);
            if (toDelete == null) return false;
            Transaction tx = databaseManager.getSession().beginTransaction();
            databaseManager.getSession().createQuery(
                    "delete ProductInstance " +
                            "where pID = :pID")
                    .setParameter("pID", spDelete)
                    .executeUpdate();
            tx.commit();
            databaseManager.getSession().remove(spDelete);
            databaseManager.getSession().remove(toDelete);
            databaseManager.commitAndFLush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    //endregion

    //region update
    public boolean changeProductName(int pID, String name) {
        Product product = productDao.find(pID);
        if (product == null) return false;
        product.setName(name);
        productDao.update(product);
        return true;
    }

    public boolean setMinimum(int pID, int minimum) {
        Product product_temp = productDao.find(pID);
        StockProduct sProduct = spDao.find(product_temp, branch);
        if (sProduct == null) return false;
        sProduct.setMinimumAmount(minimum);
        spDao.update(sProduct);
        checkMinValue(sProduct);
        return true;
    }

    public boolean reportDefective(long sn) {
        try {
            ProductInstance chosenProduct = piDao.find(sn);
            if (chosenProduct == null) return false;
            if (chosenProduct.getIs_damaged() == 1) return true;
            chosenProduct.setIs_damaged(1);
            StockProduct stockP = chosenProduct.getpID();
            if (!(chosenProduct.getIs_expired() == 1))
                stockP.setStorageQuantity(stockP.getStorageQuantity() - 1);
            checkMinValue(stockP);
            piDao.update(chosenProduct);
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public boolean changeProductSellingPrice(int pID, double newPrice) {
        Product product_temp = productDao.find(pID);
        StockProduct product = spDao.find(product_temp, branch);
        if (product == null || newPrice < 0) return false;
        product.setSellingPrice(newPrice);
        spDao.update(product);
        return true;
    }

    public boolean changeProductLocation(int pID, String newLocation) {
        Product product_temp = productDao.find(pID);
        StockProduct product = spDao.find(product_temp, branch);
        if (product == null || newLocation.equals("")) return false;
        product.setLocation(newLocation);
        spDao.update(product);
        return true;
    }

    public boolean changeProductCategory(int pID, int newCategory) {
        Product product_temp = productDao.find(pID);
        StockProduct product = spDao.find(product_temp, branch);
        Category category = categoryDao.find(newCategory);
        if (product == null || category == null) return false;
        product.setCategory(category);
        spDao.update(product);
        return true;
    }

    public boolean editCategory(int categoryID, String newName, int newParentID) {
        Category current = categoryDao.find(categoryID);
        if (current == null || newParentID < 0) return false;
        Category newParent = categoryDao.find(categoryID);
        if (newParentID != 0 && newParent == null) return false;
        current.setTitle(newName);
        current.setParentId(newParent);
        categoryDao.update(current);
        return true;
    }

    public String reportByCategories(List<Integer> categoryList) {

        List<Integer> temp = new ArrayList<>(categoryList);
        for (Integer i : temp) {
            Set<Category> childs = categoryDao.findWithsub(i);
            for (Category cat : childs) {
                categoryList.add(cat.getCategoryid());
            }
        }
        Set<Integer> set = new HashSet<>(categoryList);
        categoryList.clear();
        categoryList.addAll(set);
        List<StockProduct> products = spDao.findInCat(categoryList);
        if (products == null) return "";
        String categoryReport = "\n";
        for (StockProduct i : products) {
            categoryReport += i.toString() + "----------------------------------------\n";

        }
        return categoryReport;
    }

    public boolean changeOrderDate(long oID, Date startDate) {
        PeriodicalOrder order = poDao.find(oID);
        if (order == null) return false;
        order.setOrderDate(startDate);
        poDao.update(order);
        return true;

    }

    public boolean changeOrderPeriod(long oID, int period) {
        PeriodicalOrder order = poDao.find(oID);
        if (order == null) return false;
        order.setPeriod(period);
        poDao.update(order);
        return true;

    }

    public boolean changePAmount(long oID, int pid, int amount) {
        PeriodicalOrder order = poDao.find(oID);
        long oid = 0;
        if (order == null) return false;
        List<OrderItem> items = order.getOrderItems();
        for (OrderItem i : items) {
            if (i != null && i.getpID() != null && i.getpID().getpID().getID() == pid)
                oid = i.getId();
        }
        OrderItem item = oiDao.find(oid);
        item.setAmount(amount);
        oiDao.update(item);
        return true;
    }

    //endregion

    //region getters
    public String getStock() {

        List<StockProduct> stockList = spDao.findAll(branch);
        String stockReport = "\n";
        for (StockProduct i : stockList) {
            stockReport += i.toString();
            stockReport += "----------------------------------------\n";
        }
        return stockReport;
    }

    public String getDefective() {
        List<ProductInstance> stockList = piDao.findAllDefective();
        String stockReport = "\n";
        for (ProductInstance i : stockList) {
            stockReport += i.toString();
            stockReport += "----------------------------------------\n";
        }
        return stockReport;
    }

    public String getAllCategories() {
        List<Category> categories = categoryDao.findAll();
        String categoryReport = "\n";
        for (Category i : categories) {
            categoryReport += i.toString();
        }
        return categoryReport;
    }

    public String getCategoryDetails(int categoryID) {
        Category cat = categoryDao.find(categoryID);
        if (cat == null)
            return "";
        else
            return cat.toString() + "\n";
    }

    public String displayAllPeriodical() {
        List<PeriodicalOrder> orderList = poDao.findAll(branch);
        String ordersReport = "\n";
        for (PeriodicalOrder p : orderList) {
            ordersReport += p.toString();
            ordersReport += "----------------------------------------\n";
        }
        return ordersReport;
    }

    public boolean getPerdOrder(long oID) {
        PeriodicalOrder periodicalOrder = poDao.find(oID);
        return periodicalOrder == null ? false : true;
    }

    public String displayOrderHistory() {
        List<Orders> orderList = ordersManager.findAll(branch);
        String ordersReport = "\n";
        for (Orders p : orderList) {
            ordersReport += p.getOrderDetails();
            ordersReport += "----------------------------------------\n";
        }
        return ordersReport;
    }
    //endregion

    //region alerts
    public void setAlert(minimumAlert m) {
        _alert.add(m);
    }

    private void checkMinValue(StockProduct p) {
        if (p.getMinimumAmount() > p.getQuantity()) {
            try {

                alert(p);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    private void alert(StockProduct sp) {
        List<OrderItem> toOrder = new ArrayList<>();
        int amount = sp.getMinimumAmount() * 2;
        OrderItem o = oiDao.insert(sp, amount);
        toOrder.add(o);
        for (minimumAlert m : _alert) {
            m.placeOrder(toOrder, branch);
        }
    }

    //endregion

    public void refreshExpiredProducts() {
        try {
            Date now = new Date(System.currentTimeMillis());
            CriteriaQuery<ProductInstance> criteriaQuery = databaseManager.getCb().createQuery(ProductInstance.class);
            Root<ProductInstance> root = criteriaQuery.from(ProductInstance.class);
            criteriaQuery.select(root);
            criteriaQuery.where(databaseManager.getCb().lessThan(root.get("expDate"), now));
            criteriaQuery.orderBy(databaseManager.getCb().asc(root.get("sn")));
            Query<ProductInstance> query = databaseManager.getSession().createQuery(criteriaQuery);
            List<ProductInstance> stockList = query.getResultList();
            for (ProductInstance i : stockList) {
                if (i.getIs_expired() != 1) {
                    StockProduct stockP = i.getpID();
                    if (stockP == null) continue;
                    int inStore = i.getIn_shop();
                    if (inStore == 1) {
                        stockP.setStoreQuantity(stockP.getStoreQuantity() - 1);

                        i.setIn_shop(0);
                        i.setIs_expired(1);
                    } else {
                        stockP.setStorageQuantity(stockP.getStorageQuantity() - 1);
                        i.setIs_expired(1);
                    }
                    checkMinValue(stockP);
                }
            }
            databaseManager.commitAndFLush();
        } catch (Exception e) {
        }
    }

    public BranchProfile getBranch() {
        return branch;
    }

    public boolean setBranch(String bName) {
        BranchProfile b = branchProfileDao.find(bName);
        if (b == null) return false;
        else {
            branch = b;
            return true;
        }
    }


    public boolean removeProductPerdOrder(long oID, int pid) {
        try {

            OrderItem to_delete = null;
            if (oID < 0 || pid < 0) return false;
            PeriodicalOrder period_temp = poDao.find(oID);
            if (period_temp == null) return false;
            List<OrderItem> orderItems = period_temp.getOrderItems();
            for (OrderItem o : orderItems) {
                if (o != null & o.getpID() != null && o.getpID().getpID().getID() == pid) {
                    to_delete = o;
                }
            }
            if (to_delete != null) {
                // orderItems.remove(to_delete);
                // period_temp.setOrderItems(orderItems);
                period_temp.removeOrder(to_delete);
                poDao.update(period_temp);

            }

            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public boolean removePeriodical(long oID) {
        PeriodicalOrder periodicalOrder = poDao.find(oID);
        if (periodicalOrder == null) return false;
        poDao.remove(periodicalOrder);
        return true;
    }

    public String getTime() {
        return today.toString();
    }

    public String nextDay() {
        Date tomorrow = new java.sql.Date(today.getTime() + 24 * 60 * 60 * 1000);
        today.setTime(tomorrow.getTime());
        return today.toString();
    }

    public String getUpdates(BranchProfile branch) {
        String ans = "";
        String temp = "";
        if (lastUpdate == null || lastUpdate.before(today)) {
            ans += "\nOrders arrived from suppliers:";
            temp = new StringBuilder().append(ans).toString();
            ans += ordersManager.updateOrders(branch);
            if (ans.equals(temp)) ans="";
            List<PeriodicalOrder> periodicalOrders = poDao.findAll(branch);
            boolean placeOrder;
            for (PeriodicalOrder po : periodicalOrders) {
                if ((placeOrder = checkDate(po))) {
                    if (ordersManager.placeOrder(po.getOrderItems(), branch)) {
                        Date nextDay = new java.sql.Date(today.getTime() + po.getPeriod() * 24 * 60 * 60 * 1000);
                        po.setOrderDate(nextDay);
                        poDao.update(po);
                        ans += "New order placed: " + po;
                    }
                }
            }
        }
        if (ans.equals(temp))
            return "";
        return ans;
    }

    private boolean checkDate(PeriodicalOrder po) {
        Date date = po.getOrderDate();

        if (date.toString().equals(today.toString())) {

            return true;
        }
        return false;
    }

    public Object getDate() {
        return today;
    }

    public void addFromSupplier(OrderItem o,BranchProfile orderBranch) {
        StockProduct s = spDao.find(o.getpID().getpID(), orderBranch);
        double newsn;
        for (int i = 0; i < o.getAmount(); i++) {
            double last_in = piDao.findLast(s);
            if (last_in == 0 || last_in==s.getpID().getID())
                newsn = s.getpID().getID() + 0.00000001 * 1;
            else
                newsn = last_in + 0.00000001 * 1;
            addProductInstance(s.getpID().getID(), newsn, o.getFinalPrice(), expdate(),orderBranch);
        }
    }

    private Date expdate() {
        return new Date(today.getTime() + 2 * 24 * 60 * 60 * 1000);
    }


    public boolean canEditPOrder(long oID) {
        PeriodicalOrder po = poDao.find(oID);
        long diff = TimeUnit.DAYS.convert(Math.abs(today.getTime() - po.getOrderDate().getTime()), TimeUnit.MILLISECONDS);
        System.out.println(diff);
        return diff > 1;

    }

    public String getUpdatesFromAllBranches() {
        String ans = "";
        List<BranchProfile> branches = branchProfileDao.findAll();
        for (BranchProfile b : branches) {
            if (b == branch) {
                ans += getUpdates(b);
            } else {
                getUpdates(b);
            }
        }
        lastUpdate = new Date(today.getTime());
        return ans;


    }


    public void cancelOrder(long oID, List<EmployeeJobs> employeeJobsList, EmployeeProfile employeeProfile) {
        ordersManager.cancelOrder(oID,employeeJobsList,employeeProfile);
    }

    public String getOrderCancelStatus(long oID) {
        Orders o= orderDao.find(oID);
        if (o ==null) return "";
        return o.getCancelStatus();

    }

    public String getPendingOrders() {
        List<Orders> orders= orderDao.findAllPendings();
        String ans="";
        for(Orders o:orders){
            ans+=o.getOrderID()+" ";
        }

        return ans;
    }

    public List<Orders> getWaitingToCancel(String employee) {
        try{
            List<Orders> waiting= orderDao.findNeedCancel(employee);
            return waiting;

        }

        catch(Exception e){
    return null;
        }
    }
}
