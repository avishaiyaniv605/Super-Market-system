package PresentationLayer.DeliveryModule;

import BusinessLayer.DeliveryModule.*;
import BusinessLayer.EmployeeModule.Shifts;
import BusinessLayer.Parsers.DeliveryParser;
import BusinessLayer.Parsers.LogicParser;
import PersistenceLayer.BranchProfile;
import PersistenceLayer.Inventory.Orders.Orders;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class DeliveryMenuParser {
    private Scanner sc;
    private DeliveryParser deliveryParser;

    public DeliveryMenuParser(Scanner sc, LogicParser logicParser) {
        this.sc = sc;
        deliveryParser = logicParser.getDeliveryParser();
    }

    /*public String chooseSource() {
        System.out.println("Please choose your delivery source");
        return deliveryParser.getAllLocations();
    }*/
    public String chooseOrders() {
        return deliveryParser.getAllOrders();
    }

    public int getOrders(){
        return deliveryParser.getOrders().size();
    }

    public void createDelivery(DeliveryProfile del) {
        deliveryParser.createDelivery(del);
      /*  for (ProductsFormProfile form : del.getProductsFormList()) {
            form.setDelivery(del);
            createProductsForm(form);
            for (ProductDelivery pd : form.getProducts()) {
                pd.setPrdctFrmId(form);
                createProductDelivery(pd);
            }

        }*/


    }

    /* public BranchProfile getSource(int choice) {
         BranchProfile branchProfile = deliveryParser.getBranchProfiles().get(choice);
         return branchProfile;
     }*/
    public Orders getOrder(int choice) {
        Orders order = deliveryParser.getOrders().get(choice);
        return order;
    }

    public TruckProfile chooseTruck(int totalWeight) {

        return deliveryParser.getAllTrucks(totalWeight);
    }

    public String chooseDest() {
        System.out.println("Please choose a delivery destination");
        return deliveryParser.getAllDestinations();
    }

    public DestinationProfile getDest(int choice) {
        DestinationProfile dest = deliveryParser.getDestinationProfile(choice);
        return dest;
    }

    public String chooseProd() {
        System.out.println("Please choose a product for the destination");
        return deliveryParser.getAllProducts();
    }

    public ProductProfile getProd(int choice) {
        ProductProfile prod = deliveryParser.getProductProfile(choice);
        return prod;
    }


    public DriverProfile chooseDriver(TruckProfile truck, List<DriverProfile> driversInShift, Shifts shifts) {
        return deliveryParser.getDriverByLicense(truck.getLicense(), driversInShift, shifts);
    }

    public void createProductsForm(ProductsFormProfile pf) {
        deliveryParser.createProductFormProfile(pf);
    }

    public void createProductDelivery(ProductDelivery pd) {
        deliveryParser.createProductDelivery(pd);
    }

    public void createDeliveryForm(DeliveryFormProfile deliveryForm) {
        deliveryParser.createDeliveryFormProfile(deliveryForm);
    }

    public void setDeliveryToForm(DeliveryProfile delivery, DeliveryFormProfile df) {
        deliveryParser.setDeliveryToForm(delivery, df);
    }

    public List<ProductsFormProfile> getPfList(Map<DestinationProfile, ProductsFormProfile> destOrders) {
        List<ProductsFormProfile> pfList = deliveryParser.getPfList(destOrders);
        return pfList;
    }

    public List<DestinationProfile> getDestList(Map<DestinationProfile, ProductsFormProfile> destOrders) {
        List<DestinationProfile> destList = deliveryParser.getDestList(destOrders);
        return destList;
    }

    public int getTotalWeight(List<Orders> orders) {
        int totalWeight;
        totalWeight = deliveryParser.getTotalWeight(orders);
        return totalWeight;
    }


}
