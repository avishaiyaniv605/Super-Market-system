/**
package BusinessLayer.EmployeeModule.test;

import BusinessLayer.DeliveryModule.*;
import BusinessLayer.Enums.Area;
import BusinessLayer.Enums.License;
import BusinessLayer.Parsers.DeliveryParser;
import PersistenceLayer.DatabaseManager;
import org.junit.Assert;

public class DeliveryParserTest {

    DatabaseManager databaseManager = new DatabaseManager("");
    DeliveryParser deliveryParser = new DeliveryParser(databaseManager);

    @org.junit.Before
    public void setUp() throws Exception {
    }

    @org.junit.Test
    public void createDestinationProfile() {
        // On first run true, later false
        Assert.assertTrue(deliveryParser.createDestinationProfile("KOAKOA", "Bariloche", "28 de Hulio", 20, 66689, "Horhe", Area.South));
    }

    @org.junit.Test
    public void createDestinationProfile1() {
        Assert.assertFalse(deliveryParser.createDestinationProfile("Susu&Sons", "Tel Aviv", "Arlozorov", 49, 66787, "YOLO", Area.South));
    }

    @org.junit.Test
    public void createLocationProfile() {
        Assert.assertFalse(deliveryParser.createBranchProfile("Ben-Gurion", "Beer Sheva", "Ben-Yehuda", 30, 1111, "MOKO", Area.South));
    }

    @org.junit.Test
    public void createLocationProfile1() {
        //On first run true, late false
        Assert.assertTrue(deliveryParser.createBranchProfile("SHOSHI", "Beer Sheva", "Ben-Yehuda", 30, 1111, "MOKO", Area.South));
    }

    @org.junit.Test
    public void equalsTruckProfile() {
        TruckProfile truck1 = deliveryParser.getTruckProfile(1);
        TruckProfile truck2 = deliveryParser.getTruckProfile(2);
        Assert.assertFalse(truck1.equals(truck2));
    }

    @org.junit.Test
    public void createProductProfile() {
        Assert.assertFalse(deliveryParser.createProductProfile("Banana", 5));
    }

    @org.junit.Test
    public void createProductProfile1() {
        Assert.assertTrue(deliveryParser.createProductProfile("Bread", 5));
    }

    @org.junit.Test
    public void equalsProductProfile() {
        ProductProfile productProfile1 = deliveryParser.getProductProfile(1);
        ProductProfile productProfile2 = deliveryParser.getProductProfile(2);
        Assert.assertFalse(productProfile1.equals(productProfile2));
    }

    @org.junit.Test
    public void createTruckProfile() {
        Assert.assertTrue(deliveryParser.createTruckProfile("2007", 4500, 5900, License.A, Area.South));
    }

    @org.junit.Test
    public void createTruckProfile1() {
        Assert.assertFalse(deliveryParser.createTruckProfile("2005", 1000, 10000, License.A, Area.North));
    }

}

*/