package BusinessLayer.Inventory;

import BusinessLayer.EmployeeModule.EmployeeJobs;
import BusinessLayer.EmployeeModule.EmployeeProfile;
import BusinessLayer.Parsers.InventoryParser;
import BusinessLayer.Parsers.SuppliersParser;
import PersistenceLayer.BranchProfile;
import PersistenceLayer.DatabaseManager;
import PersistenceLayer.Inventory.Orders.Orders;
import PersistenceLayer.Inventory.Orders.OrderDao;
import PersistenceLayer.Inventory.Orders.OrderItem;
import PersistenceLayer.Inventory.Orders.PeriodicalOrder;
import PersistenceLayer.Inventory.StockProduct;
import PersistenceLayer.Suppliers.SupplierContact;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

public class OrdersManager implements minimumAlert {
    private SuppliersParser suppliersParser;
    private DatabaseManager databaseManager;
    private OrderDao orderDao;
    private InventoryParser inventoryParser;

    public OrdersManager() {
        orderDao= new OrderDao();
    }

    public boolean placeOrder(List<OrderItem> items, BranchProfile branch) {
        try {
            HashMap<SupplierContact, List<OrderItem>> orders = new HashMap<>();
            for (OrderItem o : items) {
                StockProduct product = o.getpID();
                int amount = o.getAmount();
                SupplierContact bestSup = suppliersParser.findBestSupp(product.getpID(), amount);
                if (orders.containsKey(bestSup)) {
                    orders.get(bestSup).add(o);
                } else {
                    List<OrderItem> newList = new ArrayList<>();
                    newList.add(o);
                    orders.put(bestSup, newList);
                }
            }
            for (Map.Entry<SupplierContact, List<OrderItem>> entry : orders.entrySet()) {
                List<OrderItem> orderItems = entry.getValue();
                SupplierContact orderSupplier = entry.getKey();
                suppliersParser.fillPrices(orderSupplier, orderItems);
                boolean need_delivery= !suppliersParser.doesDeliveries(orderSupplier);
                Orders newOrder = orderDao.insert(Date.valueOf(inventoryParser.getTime()),orderSupplier,orderItems,need_delivery? 1:0,branch);

            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public void setSupplierParser(SuppliersParser suppParser) {
        this.suppliersParser = suppParser;
    }

    public void setDatabaseManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    public void setInventoryParser(InventoryParser inventoryParser) {
        this.inventoryParser = inventoryParser;
    }

    public String updateOrders(BranchProfile branch) {
        String ans = "";
        List<Orders> orders = orderDao.findAll(branch);
        boolean arrived;
        List<OrderItem> canceledOrders= new ArrayList<>();
        for (Orders o : orders) {
            if ((o.getStatus().equals("WAITING") && o.getNeedDelivery()==0 && (arrived = checkDate(o))) || (o.getStatus().equals("OTW") && o.getNeedDelivery()==1)) {
                o.setStatus("DONE");
                orderDao.update(o);
                ans+= "Order arrived!!!\n" + o.getOrderDetails();
                List<OrderItem> new_items= o.getOrderItems();
                for(OrderItem order:new_items){
                    if(order.getCancel()==0){
                        inventoryParser.addFromSupplier(order,o.getBranch());
                    }
                    else
                        canceledOrders.add(order);
                }
            }
            }
            placeOrder(canceledOrders,branch);

        return ans;
    }


    private boolean checkDate(Orders o) {
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE", Locale.ENGLISH); // the day of the week spelled out completely
        String today= simpleDateformat.format(inventoryParser.getDate());
        List<String> dayOfSupp= suppliersParser.getSupplyDay(o.getSupplierContact());
        for(String s: dayOfSupp){
            if(s.equals(today))
                return true;

        }
        return false;

    }

    public List<Orders> findAll(BranchProfile branch) {
        List<Orders> orderList = orderDao.findAll(branch);
        return orderList;

    }

    public boolean cancelOrder(long oID, List<EmployeeJobs> employeeJobsList, EmployeeProfile employeeProfile) {
        Orders toCancel= orderDao.find(oID);
        if(toCancel == null) return false;
        toCancel.setCancel(1);
        List<String> jobs= new ArrayList<>();
        for(EmployeeJobs e:employeeJobsList){
            jobs.add(e.getJob());
        }
        if(jobs.contains("Storekeeper")){
            toCancel.setInvCancel(1);
        }
        else if(jobs.contains("Logistics Manager"))
            toCancel.setLogisticsCancel(1);
        else if(jobs.contains("Shift Manager"))
            toCancel.setHRCancel(1);

        if(toCancel.getHRCancel() == 1 && toCancel.getInvCancel()==1 && toCancel.getLogisticsCancel()==1)
            toCancel.setStatus("CANCELED");
        orderDao.update(toCancel);
        return true;

    }
}
