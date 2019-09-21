package BusinessLayer.Parsers;

import BusinessLayer.DeliveryModule.*;
import BusinessLayer.EmployeeModule.EmployeeProfile;
import BusinessLayer.EmployeeModule.Shifts;
import BusinessLayer.Enums.Area;
import BusinessLayer.Enums.License;
import PersistenceLayer.BranchProfile;
import PersistenceLayer.DatabaseManager;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import PersistenceLayer.Inventory.Orders.OrderDao;
import PersistenceLayer.Inventory.Orders.Orders;
import org.hibernate.query.Query;

import java.util.*;

public class DeliveryParser {
    public final int NUM_OF_BRANCHES = 10;
    DatabaseManager databaseManager;
    private OrderDao orderDao;


    public DeliveryParser(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        orderDao = new OrderDao();
    }


    /**
     * This method will create a new delivery record in the database using the databaseManager
     * @param newDelivery - The delivery to be added to the database
     * @return - True if the delivery was added to the database, false otherwise
     */
    public boolean createDelivery(DeliveryProfile newDelivery) {

        try {
            OrderDao orderDao = new OrderDao();

            for(Orders order : newDelivery.getOrdersList()){
                order.setStatus("OTW");
                orderDao.update(order);
            }


            databaseManager.getSession().save(newDelivery);
            databaseManager.commitAndFLush();

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * This method will create a new Product Form record in the database using the databaseManager
     * @param pf -  The product form to be added to the database
     * @return - True if the Product Form was added to the database, false otherwise
     */
    public boolean createProductFormProfile(ProductsFormProfile pf) {
        try {

            databaseManager.getSession().save(pf);
            databaseManager.commitAndFLush();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     *Set to the @deliveryProfile to the delivery field of @deliveryFormProfile
     * @param deliveryProfile - Delivery to be set to a field
     * @param deliveryFormProfile - Form to update the delivery field
     */
    public void setDeliveryToForm(DeliveryProfile deliveryProfile, DeliveryFormProfile deliveryFormProfile){
        deliveryFormProfile.setDelivery(deliveryProfile);
        databaseManager.getSession().save(deliveryFormProfile);
        databaseManager.commitAndFLush();
    }

    /**
     * This method will create a new Delivery Form record in the database using the databaseManager
     * @param pf - The delivery form to be added to the database
     * @return - True if the delivery Form was added to the database, false otherwise
     */
    public boolean createDeliveryFormProfile(DeliveryFormProfile pf) {
        try {

            databaseManager.getSession().save(pf);
            databaseManager.commitAndFLush();
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    /**
     * This method will create a new Product Delivery record in the database using the databaseManager
     * @param pf - The product delivery to be added to the database
     * @return True if the delivery Form was added to the database, false otherwise
     */
    public boolean createProductDelivery(ProductDelivery pf) {
        try {

            databaseManager.getSession().save(pf);
            databaseManager.commitAndFLush();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * This method will create a new Destination record in the database using the databaseManager
     * @param name - Destination's name
     * @param city - Destination's city
     * @param street - Destination's street
     * @param number - Destinations's number
     * @param phoneNuber - Destinations phone number
     * @param contactName - Destination's contact name
     * @param area - Destination's area
     * @return - True if the destination was added to the database, false otherwise
     */
    public boolean createDestinationProfile(String name, String city, String street, int number, int phoneNuber, String contactName, Area area) {
        try {
            DestinationProfile destinationProfile = new DestinationProfile(name, city, street, number, phoneNuber, contactName, area);
            List<DestinationProfile> destinationProfiles = getAllDestinationsObjects();
            for (DestinationProfile dest: destinationProfiles) {
                if (destinationProfile.equals(dest))
                    return false;
            }
            databaseManager.getSession().save(destinationProfile);
            databaseManager.commitAndFLush();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * This method will create a new Location record in the database using the databaseManager
     * @param locationName - Location's name
     * @param city - Location's city
     * @param street - Location's street
     * @param number - Location's number
     * @param phoneNuber - Location's phone number
     * @param contactName - Location's contact name
     * @return - True if the destination was added to the database, false otherwise
     */
    public boolean createBranchProfile(String locationName, String city, String street, int number, int phoneNuber, String contactName) {
        try {
            BranchProfile branchProfile = new BranchProfile(locationName, city, street, number, phoneNuber, contactName);
            List<BranchProfile> branchProfiles = getBranchProfiles();
            for (BranchProfile loc: branchProfiles) {
                if(branchProfile.equals(loc))
                    return false;
            }
            databaseManager.getSession().save(branchProfile);
            databaseManager.commitAndFLush();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * This method will create a new Product profile record in the database using the databaseManager
     * @param name - Product's name
     * @param weight - Product's weight
     * @return - True if the location was added to the database, false otherwise
     */
    public boolean createProductProfile(String name, int weight) {
        try {
            ProductProfile productProfile = new ProductProfile(name, weight);
            List<ProductProfile> productProfiles = getAllProductProfileObjects();
            for (ProductProfile productProfile1: productProfiles) {
                if (productProfile.equals(productProfile1))
                    return false;
            }
            databaseManager.getSession().save(productProfile);
            databaseManager.commitAndFLush();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * This method will create a new Truck record in the database using the databaseManager
     * @param model - Truck's model
     * @param truckWeight - Truck's weight
     * @param maxWeight - Truck's max weight to carry
     * @param license - Truck's license
     * @param area - Truck's area
     * @return -  True if the Truck was added to the database, false otherwise
     */
    public boolean createTruckProfile(String model, int truckWeight, int maxWeight, License license, Area area) {
        try {
            TruckProfile truckProfile = new TruckProfile(model, truckWeight, maxWeight, license, area);
            List<TruckProfile> truckProfiles = getAllTruckPrfileObjects();
            for (TruckProfile truck: truckProfiles) {
                if(truckProfile.equals(truck))
                    return false;
            }
            databaseManager.getSession().save(truckProfile);
            databaseManager.commitAndFLush();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * This method will create a new Truck record in the database using the databaseManager
     * @param license
     * @param employeeProfile
     * @return
     */
    public boolean createDriverProfile( License license,EmployeeProfile employeeProfile) {
        try {
            DriverProfile driverProfile = new DriverProfile(license, employeeProfile);
            List<DriverProfile> driverProfiles = getAllDriverProfileObjects();
            for (DriverProfile driver: driverProfiles) {
                if (driverProfile.equals(driver))
                    return false;
            }
            databaseManager.getSession().save(driverProfile);
            databaseManager.commitAndFLush();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public DeliveryProfile getDelivery(int ID){
        try{
            return databaseManager.getSession().get(DeliveryProfile.class, ID);
        }catch (Exception e){
            return null;
        }
    }

    public DeliveryFormProfile getDeliveryForm(int formID){
        try{
            return databaseManager.getSession().get(DeliveryFormProfile.class, formID);
        }catch (Exception e){
            return null;
        }
    }

    public DestinationProfile getDestinationProfile(int deliveryId){
        try{
            return databaseManager.getSession().get(DestinationProfile.class, deliveryId);
        }catch (Exception e){
            return null;
        }
    }

    public ProductProfile getProductProfile(int Id){
        try{
            return databaseManager.getSession().get(ProductProfile.class, Id);
        }catch (Exception e){
            return null;
        }
    }

    public DriverProfile getDriveProfile(String ID){
        try{
            return databaseManager.getSession().get(DriverProfile.class, ID);
        }catch (Exception e){
            return null;
        }
    }

    public BranchProfile getLocationProfile(int locationID){
        try{
            return databaseManager.getSession().get(BranchProfile.class, locationID);
        }catch (Exception e){
            return null;
        }
    }
    public List<BranchProfile> getBranchProfiles(){
        try {
            CriteriaQuery<BranchProfile> criteriaQuery = databaseManager.getCb().createQuery(BranchProfile.class);
            Root<BranchProfile> root = criteriaQuery.from(BranchProfile.class);
            criteriaQuery.select(root);

            Query<BranchProfile> query = databaseManager.getSession().createQuery(criteriaQuery);
            List<BranchProfile> branchProfiles = query.getResultList();
            return branchProfiles;
        }catch (Exception e){
            return new ArrayList<>();
        }
    }

    public String getAllLocations(){
        List<BranchProfile> locationsList = getBranchProfiles();
        String locsList = "";
        int counter = 1;
        for (BranchProfile loc: locationsList) {
            locsList += counter + ")\t" + loc.toString() + "\n";
            counter++;
        }
        return locsList;
    }

    public List<DestinationProfile> getAllDestinationsObjects(){
        CriteriaQuery<DestinationProfile> criteriaQuery = databaseManager.getCb().createQuery(DestinationProfile.class);
        Root<DestinationProfile> root = criteriaQuery.from(DestinationProfile.class);
        criteriaQuery.select(root);

        Query<DestinationProfile> query = databaseManager.getSession().createQuery(criteriaQuery);
        List<DestinationProfile> destinationProfiles = query.getResultList();
        return destinationProfiles;
    }

    public TruckProfile getTruckProfile(int ID){
        try{
            return databaseManager.getSession().get(TruckProfile.class, ID);
        }catch (Exception e){
            return null;
        }
    }

    public List<TruckProfile> getAllTruckPrfileObjects(){
        CriteriaQuery<TruckProfile> criteriaQuery = databaseManager.getCb().createQuery(TruckProfile.class);
        Root<TruckProfile> root = criteriaQuery.from(TruckProfile.class);
        criteriaQuery.select(root);

        Query<TruckProfile> query = databaseManager.getSession().createQuery(criteriaQuery);
        List<TruckProfile> truckProfiles = query.getResultList();
        return truckProfiles;
    }

    public List<ProductProfile> getAllProductProfileObjects(){
        CriteriaQuery<ProductProfile> criteriaQuery = databaseManager.getCb().createQuery(ProductProfile.class);
        Root<ProductProfile> root = criteriaQuery.from(ProductProfile.class);
        criteriaQuery.select(root);

        Query<ProductProfile> query = databaseManager.getSession().createQuery(criteriaQuery);
        List<ProductProfile> productProfiles = query.getResultList();
        return productProfiles;
    }

    public List<DriverProfile> getAllDriverProfileObjects(){
        CriteriaQuery<DriverProfile> criteriaQuery = databaseManager.getCb().createQuery(DriverProfile.class);
        Root<DriverProfile> root = criteriaQuery.from(DriverProfile.class);
        criteriaQuery.select(root);

        Query<DriverProfile> query = databaseManager.getSession().createQuery(criteriaQuery);
        List<DriverProfile> driverProfiles = query.getResultList();
        return driverProfiles;
    }

    public String getAllDestinations() {
        CriteriaQuery<DestinationProfile> criteriaQuery = databaseManager.getCb().createQuery(DestinationProfile.class);
        Root<DestinationProfile> root = criteriaQuery.from(DestinationProfile.class);
        criteriaQuery.select(root);

        Query<DestinationProfile> query = databaseManager.getSession().createQuery(criteriaQuery);
        List<DestinationProfile> destList = query.getResultList();
        String destsList = "";
        for (DestinationProfile dest: destList) {
            destsList += dest.toString() + "\n";
        }
        return destsList;
    }

    public String getAllProducts() {
        CriteriaQuery<ProductProfile> criteriaQuery = databaseManager.getCb().createQuery(ProductProfile.class);
        Root<ProductProfile> root = criteriaQuery.from(ProductProfile.class);
        criteriaQuery.select(root);

        Query<ProductProfile> query = databaseManager.getSession().createQuery(criteriaQuery);
        List<ProductProfile> prodList = query.getResultList();
        String prodsList = "";
        for (ProductProfile prod: prodList) {
            prodsList += prod.toString();
        }
        return prodsList;
    }

    public TruckProfile getAllTrucks(int totalWeight) {
        CriteriaQuery<TruckProfile> criteriaQuery = databaseManager.getCb().createQuery(TruckProfile.class);
        Root<TruckProfile> root = criteriaQuery.from(TruckProfile.class);
        criteriaQuery.select(root);

        Query<TruckProfile> query = databaseManager.getSession().createQuery(criteriaQuery);
        List<TruckProfile> truckList = query.getResultList();
        Collections.sort(truckList, new TruckComparor());
        for(TruckProfile truck : truckList){
            int capacity = truck.getMaxWeight() - truck.getTruckWeight();
            if(capacity > totalWeight){
                return truck;
            }
        }
        return null;
    }

    public DriverProfile getDriverByLicense(License license, List<DriverProfile> driverProfiles, Shifts shifts) {

        for(DriverProfile driver : driverProfiles){
            if(driver.getLicense().ordinal() == license.ordinal() && !isOccThisShift(shifts, driver)){
                //addShiftToDriverList(shifts, driver);
                return driver;
            }
        }

        for(DriverProfile driver : driverProfiles){
            if(driver.getLicense().ordinal() >= license.ordinal() && !isOccThisShift(shifts, driver)){
               // addShiftToDriverList(shifts, driver);
                return driver;
            }
        }
        return null;
    }

    private void addShiftToDriverList(Shifts shifts, DriverProfile driver) {
        driver.addOccShift(shifts);
        databaseManager.getSession().save(driver);
        databaseManager.commitAndFLush();
    }

    private boolean isOccThisShift(Shifts shifts, DriverProfile driver) {
        boolean occ = driver.getShifts().contains(shifts);
        return occ;
    }

    public List<ProductsFormProfile> getPfList(Map<DestinationProfile, ProductsFormProfile> destOrders) {
        List<ProductsFormProfile> pfList = new LinkedList<>();
        for(Map.Entry<DestinationProfile, ProductsFormProfile> entry: destOrders.entrySet()){
            pfList.add(entry.getValue());
        }
        return pfList;
    }

    public List<DestinationProfile> getDestList(Map<DestinationProfile, ProductsFormProfile> destOrders) {
        List<DestinationProfile> destList = new LinkedList<>();
        for(Map.Entry<DestinationProfile, ProductsFormProfile> entry: destOrders.entrySet()){
            destList.add(entry.getKey());
        }
        return destList;
    }

    public int getTotalWeight(List<Orders> orders) {
        int totalWeight = 0;
        for (Orders order : orders) {
            totalWeight += order.getWeight();
        }
        return totalWeight;
    }

    public boolean removeDriverProfile(String id) {
        try{
            DriverProfile driverProfile = this.databaseManager.getSession().get(DriverProfile.class, id);
            if (driverProfile != null) {
                this.databaseManager.getSession().delete(driverProfile);
                this.databaseManager.commitAndFLush();
                return true;
            }
            else{
                return true;

            }

        }catch (Exception e){return  false;}
    }
    //NEED TO CHANGE THIS WITH TAL
    //NEED TO ADD TOSTRING IN ORDERS
    public List<Orders> getOrders(){
        List<Orders> orders = orderDao.findAllForDelivery();
        return orders;
    }

    public String getAllOrders(){
        //get orders from DB
        List<Orders> ordersList = getOrders();
        System.out.println(ordersList.size());
        String ordList = "";
        int counter = 1;
        for (Orders ord: ordersList) {
            ordList += counter + ")\t" + "ID: "+ord.getOrderID() +" Weight: "+ord.getWeight()+ " Branch: "+ord.getBranch()+ "\n";
            counter++;
        }
        return ordList;

    }
/*
    public List<Orders> getAllOrdersFromBranch(BranchProfile branch){
        List<Orders> ordersList = getOrders();
        List<Orders> orderBranch = new LinkedList<>();
        for (Orders ord: ordersList) {
            if(ord.getBranch().getBranchName().equals(branch.getBranchName())) {
               orderBranch.add(ord);
            }
        }
        return orderBranch;
    }


    public String getOrdersFromBranch(BranchProfile branch){
        List<Orders> ordersList = getAllOrdersFromBranch(branch);
        String ordList = "";
        int counter = 1;
        for (Orders ord: ordersList) {
            if(ord.getBranch().getBranchName().equals(branch.getBranchName())) {
                ordList += counter + ")\t" + ord.toString() + "\n";
                counter++;
            }
        }
        return ordList;
    }*/
}
