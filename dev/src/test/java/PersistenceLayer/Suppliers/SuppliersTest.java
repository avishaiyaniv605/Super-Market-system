package PersistenceLayer.Suppliers;

import BusinessLayer.Parsers.SuppliersParser;
import PersistenceLayer.DatabaseManager;
import PersistenceLayer.Product;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

public class SuppliersTest {
    /*
    private SuppliersParser suppliersParser;
    private Supplier supplier;
    private SupplierContract supplierContract;
    private DeliveryDay deliveryDay;
    private Catalog catalogList;
    private Product product;

    @Before
    public void testSetUp(){
        suppliersParser = new SuppliersParser();
        supplier = new Supplier(100,"bank","pay fast", "yossi", "hana");
        supplierContract = new SupplierContract(supplier,true, true);
        deliveryDay = new DeliveryDay(supplier,supplierContract,"Monday");
        product = new Product(1,"Bamba","Osem");
    }

    @Test
    public void testSuppliersParser() {
        assertNotNull(suppliersParser);
    }

    @Test
    public void testSuppliers() {
        assertNotNull(supplier);
    }

    @Test
    public void testContract() {
        assertNotNull(supplierContract);
    }

    @Test
    public void testDeliveryDay() {
        assertNotNull(deliveryDay);
    }

    @Test
    public void testSupplierInjection(){
        suppliersParser.addSupplier(100,"bank","pay now","ab", "ba");
        assertNotNull(suppliersParser.getSupplier(100));
    }

    @Test
    public void testContractInjection(){
        suppliersParser.addSupplier(100,"bank","pay now", "ilana dayan", "ovda");
        suppliersParser.addContract(100,true,true);
        assertNotNull(suppliersParser.getSupplier(100));
    }

    @Test
    public void testContractIdGeneration(){
        int id1 = suppliersParser.addContract(100,true,true);
        int id2 = suppliersParser.addContract(100,true,true);
        assertTrue(id2 > id1);
    }

    @Test
    public void testProductInjection(){
        suppliersParser.addProduct(15,"Galaxy S10","Samsung");
        assertNotNull(suppliersParser.getProduct(15));
    }

    @Test
    public void testContactInjection(){
        suppliersParser.addContact(100,"contact1","050","con@mail.com");
        assertNotNull(suppliersParser.getContact(100,"contact1","050","con@mail.com"));
    }

    @Test
    public void testContactModificationNotNull(){
        String oldPhone = "05050", newPhone = "99999", oldEmail = "aa", newEmail = "bb";
        suppliersParser.addContact(100,"c2",oldPhone,oldEmail);
        suppliersParser.changeContact(100,"c2",oldPhone,oldEmail,newEmail, newPhone);
        assertNotNull(suppliersParser.getContact(100,"c2",newPhone,newEmail));
    }

    @Test
    public void testContactModificationValue(){
        String oldPhone = "05050", newPhone = "99999", oldEmail = "aa", newEmail = "bb";
        suppliersParser.addContact(100,"c2",oldPhone,oldEmail);
        suppliersParser.changeContact(100,"c2",oldPhone,oldEmail,newEmail, newPhone);
        SupplierContact contact = suppliersParser.getContact(100,"c2",newPhone,newEmail);
        assertEquals(contact.getPhone_number(), newPhone);
    }

*/


}
