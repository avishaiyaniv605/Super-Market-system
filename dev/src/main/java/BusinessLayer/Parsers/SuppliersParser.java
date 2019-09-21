package BusinessLayer.Parsers;

import PersistenceLayer.Inventory.Orders.OrderItem;
import PersistenceLayer.Product;
import PersistenceLayer.Suppliers.*;
import PersistenceLayer.DatabaseManager;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class SuppliersParser {
    DatabaseManager databaseManager;

    public SuppliersParser( ) {
        this.databaseManager = DatabaseManager.getInstance();
    }


    //region supplier

    /**
     * adds supplier to the database
     * @param companyID private company id number
     * @param bankAccount   bank account details
     * @param paymentTerms  payment terms
     * @return false if failed, true otherwise
     */
    public boolean addSupplier(int companyID, String bankAccount, String paymentTerms, String name, String address) {
        try {
            Supplier newSupplier = new Supplier(companyID, bankAccount, paymentTerms, name, address);
            databaseManager.getSession().save(newSupplier);
            databaseManager.commitAndFLush();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * retrieves supplier instance from database by given company id
     * @param companyID supplier's company id
     * @return null if doesn't exist, otherwise the supplier is returned
     */
    public Supplier getSupplier(int companyID) {
        try {
            return databaseManager.getSession().get(Supplier.class, companyID);
        }
        catch (Exception e){return null;}
    }

    /**
     * Changes bank account details
     * @param pComID the company to change
     * @param newBankAccount the new change
     * @return  false if failed, otherwise returns true
     */
    public boolean changeBankAccount(int pComID, String newBankAccount) {
        try{
            Supplier supplier = getSupplier(pComID);
            supplier.setBank_account(newBankAccount);
            databaseManager.commitAndFLush();
            return true;
        }catch (Exception e){}
        return false;
    }

    /**
     * Changes bank account details
     * @param pComID the company to change
     * @param newPayTerms the new change
     * @return  false if failed, otherwise returns true
     */
    public boolean changePaymentTerms(int pComID, String newPayTerms) {
        try{
            Supplier supplier = getSupplier(pComID);
            supplier.setPayments_terms(newPayTerms);
            databaseManager.commitAndFLush();
            return true;
        }catch (Exception e){}
        return false;
    }

    public List<Supplier> getAllSuppliers() {
        try{
            CriteriaQuery<Supplier> criteriaQuery = databaseManager.getCb().createQuery(Supplier.class);
            Root<Supplier> root = criteriaQuery.from(Supplier.class);
            criteriaQuery.select(root);
            Query<Supplier> query = databaseManager.getSession().createQuery(criteriaQuery);
            return query.getResultList();
        }   catch (Exception e){}
        return null;
    }

    private void removeAllSuppliers(){
        try{
            CriteriaQuery<Supplier> criteriaQuery = databaseManager.getCb().createQuery(Supplier.class);
            Root<Supplier> root = criteriaQuery.from(Supplier.class);
            criteriaQuery.select(root);
            Query<Supplier> query = databaseManager.getSession().createQuery(criteriaQuery);
            List<Supplier> suppliers = query.getResultList();
            for (Supplier supplier: suppliers) {
                databaseManager.getSession().delete(supplier);
                databaseManager.commitAndFLush();
            }
        }   catch (Exception e){}
    }



    //endregion

    //region contact

    /**
     * adds new contact to the database
     * @param pComID contact's company id
     * @param name  contact's name
     * @param phoneNum contact's phone number
     * @param email contact's email
     * @return  false if failed, otherwise returns true
     */
    public boolean addContact(int pComID,String name,String phoneNum,String email){
        try{
            Supplier relevantSupplier = getSupplier(pComID);
            if (relevantSupplier != null) {
                SupplierContact newContact = new SupplierContact(relevantSupplier,name, phoneNum, email);
                databaseManager.getSession().save(newContact);
                databaseManager.commitAndFLush();
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * retrieves contact instance from database
     * @param pComID contact's company id
     * @param name  contact's name
     * @param phoneNum  contact's phone number
     * @param email contact's email
     * @return null if doesn't exist, otherwise the supplier is returned
     */
    public SupplierContact getContact(int pComID, String name, String phoneNum, String email) {
        try {
            Supplier relevantSupplier = getSupplier(pComID);
            if (relevantSupplier != null)
                return databaseManager.getSession().get(SupplierContact.class, new SupplierContact(relevantSupplier, name, phoneNum, email));
            return null;
        }
        catch (Exception e)
        {return null;}
    }

    public List<SupplierContact> getContactsByCompany(int comID) {
        try{
            CriteriaQuery<SupplierContact> criteriaQuery = databaseManager.getCb().createQuery(SupplierContact.class);
            Root<SupplierContact> root = criteriaQuery.from(SupplierContact.class);
            criteriaQuery.select(root);
            criteriaQuery.where(databaseManager.getCb().equal(root.get("supplier"), comID));
            Query<SupplierContact> query = databaseManager.getSession().createQuery(criteriaQuery);
            return query.getResultList();
        }   catch (Exception e){}
        return null;
    }

    /**
     * retrieves contacts connected to @PARAM {pComID}
     * @param pComID a company to retrieve from
     * @return list of contacts or empty list if failed or doesn't exist
     */
    public SupplierContact getRandomContact(int pComID) {
        try {
            CriteriaQuery<SupplierContact> criteriaQuery = databaseManager.getCb().createQuery(SupplierContact.class);
            Root<SupplierContact> root = criteriaQuery.from(SupplierContact.class);
            criteriaQuery.select(root);
            criteriaQuery.where(databaseManager.getCb().equal(root.get("supplier"), pComID));
            Query<SupplierContact> query = databaseManager.getSession().createQuery(criteriaQuery);
            List<SupplierContact> contacts = query.getResultList();
            if (contacts.isEmpty())
                return null;
            return contacts.get(0);
        }
        catch (Exception e)
        {return null;}
    }

    /**
     * chnages contact's details
     * @param pComID contact's company id
     * @param name contact's name
     * @param oldPhone  contact's old phone
     * @param oldEmail  contact's old email
     * @param newEmail  contact's new email
     * @param newPhone  contact's new phone
     * @return  false is failed, otherwise true
     */
    public boolean changeContact(int pComID,String name, String oldPhone, String oldEmail, String newEmail, String newPhone) {
        try{
            SupplierContact contact = getContact(pComID,name,oldPhone,oldEmail);
            if (contact == null)
                return false;
            removeContact(pComID, name, oldPhone, oldEmail);
            addContact(pComID, name, newPhone, newEmail);
            databaseManager.commitAndFLush();
            return true;
        }catch (Exception e){}
        return false;
    }

    /**
     * removes all contacts connected to a certain company
     * @param comID the company id to remove from
     * @return false if failed, otherwise true
     */
    public boolean removeContacts(int comID) {
        try {
            List<SupplierContact> contacts = getContactsByCompany(comID);
            for (SupplierContact contact : contacts){
                databaseManager.getSession().delete(contact);
            }
            databaseManager.commitAndFLush();
            return true;
        } catch (Exception e){}
        return false;
    }

    /**
     * removes a specific contact form database
     * @param pComID company the contact associated to
     * @param name contact's name
     * @param phone contact's phone number
     * @param email contact's email address
     * @return false if failed, otherwise true
     */
    public int removeContact(int pComID, String name, String phone, String email) {
        try {
            List<SupplierContact> contacts = getContactsByCompany(pComID);
            if (contacts.size() == 1)
                return -1;
            for (SupplierContact contact : contacts) {
                if (contact.getPhone_number().equals(phone) && contact.getEmail().equals(email) && contact.getName().equals(name)){
                    databaseManager.getSession().delete(contact);
                    databaseManager.commitAndFLush();
                    return 1;
                }
            }
            return 0;
        } catch (Exception e){
            return 0;
        }
    }

    private void removeAllContacts(){
        try{
            CriteriaQuery<SupplierContact> criteriaQuery = databaseManager.getCb().createQuery(SupplierContact.class);
            Root<SupplierContact> root = criteriaQuery.from(SupplierContact.class);
            criteriaQuery.select(root);
            Query<SupplierContact> query = databaseManager.getSession().createQuery(criteriaQuery);
            List<SupplierContact> contacts = query.getResultList();
            for (SupplierContact contact: contacts) {
                databaseManager.getSession().delete(contact);
                databaseManager.commitAndFLush();
            }
        }   catch (Exception e){}
    }


    //endregion

    //region contract

    /**
     * adds new contract to the database
     * @param pComID id of the company that signed the contract
     * @param permDays determines whether the company has permanent delivery days
     * @return the contract number, -1 if failed
     */
    public int addContract(int pComID, boolean permDays, boolean delivering){
        int contract = -1;
        try{
            Supplier relevantSupplier = getSupplier(pComID);
            if (relevantSupplier != null) {
                SupplierContract newContract = new SupplierContract(relevantSupplier,permDays, delivering);
                databaseManager.getSession().save(newContract);
                databaseManager.commitAndFLush();
                return newContract.getContract_number();
            }
        } catch (Exception e) {
            return contract;
        }
        return contract;
    }

    /**
     * retrieves a contract instance from the database
     * @param pComID the company's id
     * @param contractNum the contract's number
     * @return null if failed, otherwise a SupplierContract instance will be returned
     */
    public SupplierContract getContract(int pComID, int contractNum) {
        try {
            Supplier relevantSupplier = getSupplier(pComID);
            if (relevantSupplier != null) {
                return databaseManager.getSession().get(SupplierContract.class, contractNum);
            }
        }
        catch (Exception e) {}
        return null;
    }

    public SupplierContract getContractByCompany(int comID) {
        try{
            CriteriaQuery<SupplierContract> criteriaQuery = databaseManager.getCb().createQuery(SupplierContract.class);
            Root<SupplierContract> root = criteriaQuery.from(SupplierContract.class);
            criteriaQuery.select(root);
            criteriaQuery.where(databaseManager.getCb().equal(root.get("supplier"), comID));
            Query<SupplierContract> query = databaseManager.getSession().createQuery(criteriaQuery);
            List<SupplierContract> contracts = query.getResultList();
            if (!contracts.isEmpty())
                return contracts.get(0);
        }   catch (Exception e){}
        return null;
    }

    private void removeAllContracts(){
        try{
            CriteriaQuery<SupplierContract> criteriaQuery = databaseManager.getCb().createQuery(SupplierContract.class);
            Root<SupplierContract> root = criteriaQuery.from(SupplierContract.class);
            criteriaQuery.select(root);
            Query<SupplierContract> query = databaseManager.getSession().createQuery(criteriaQuery);
            List<SupplierContract> contracts = query.getResultList();
            for (SupplierContract contract: contracts) {
                databaseManager.getSession().delete(contract);
                databaseManager.commitAndFLush();
            }
        }   catch (Exception e){}
    }

    /**
     * checks whether a contract contains quantity list
     * @param contract_number
     * @return
     */
    public boolean contractHasQuantityList(int contract_number) {
        try{
            CriteriaQuery<Catalog> criteriaQuery = databaseManager.getCb().createQuery(Catalog.class);
            Root<Catalog> root = criteriaQuery.from(Catalog.class);
            criteriaQuery.select(root);
            criteriaQuery.where(databaseManager.getCb().equal(root.get("contract"), contract_number));
            Query<Catalog> query = databaseManager.getSession().createQuery(criteriaQuery);
            if (!query.getResultList().isEmpty())
                return query.getResultList().get(0).hasQL();
            return false;
        }   catch (Exception e){}
        return false;
    }


    //endregion

    //region delivery days

    /**
     * adds a delivery day to the database
     * @param pComID company's id
     * @param day   day to delivery
     * @param contractNumber contract number signed with company
     * @return false if failed, otherwise true
     */
    public boolean addDeliveryDay(int pComID, String day, int contractNumber) {
        try{
            Supplier supplier = getSupplier(pComID);
            if (supplier == null)
                return false;
            SupplierContract contract = getContract(pComID,contractNumber);
            if (contract == null)
                return false;
            DeliveryDay deliveryDay = new DeliveryDay(supplier, contract, day);
            databaseManager.getSession().save(deliveryDay);
            databaseManager.commitAndFLush();
            return true;
        } catch (Exception e) { }
        return false;
    }

    /**
     * retrieves delivery days of a certain contract
     * @param contract_number
     * @return
     */
    public List<DeliveryDay> getDeliveryDaysByContract(int contract_number) {
        try{
            CriteriaQuery<DeliveryDay> criteriaQuery = databaseManager.getCb().createQuery(DeliveryDay.class);
            Root<DeliveryDay> root = criteriaQuery.from(DeliveryDay.class);
            criteriaQuery.select(root);
            criteriaQuery.where(databaseManager.getCb().equal(root.get("contract"), contract_number));
            Query<DeliveryDay> query = databaseManager.getSession().createQuery(criteriaQuery);
            return query.getResultList();
        }   catch (Exception e){}
        return null;
    }

    private void removeAllDeliveryDays(){
        try{
            CriteriaQuery<DeliveryDay> criteriaQuery = databaseManager.getCb().createQuery(DeliveryDay.class);
            Root<DeliveryDay> root = criteriaQuery.from(DeliveryDay.class);
            criteriaQuery.select(root);
            Query<DeliveryDay> query = databaseManager.getSession().createQuery(criteriaQuery);
            List<DeliveryDay> days = query.getResultList();
            for (DeliveryDay day: days) {
                databaseManager.getSession().delete(day);
                databaseManager.commitAndFLush();
            }
        }   catch (Exception e){}
    }

    public boolean doesDeliveries(SupplierContact supplierContact){
        int pid = supplierContact.getSupplier().getPrivate_company_id();
        SupplierContract contract = getContractByCompany(pid);
        return contract.isDelivering();
    }

    public List<String> getSupplyDay(SupplierContact supplierContact){
        SupplierContract contract = getContractByCompany(supplierContact.getSupplier().getPrivate_company_id());
        List<DeliveryDay> deliveryDays = getDeliveryDaysByContract(contract.getContract_number());
        List<String> days = new ArrayList<>();
        for (DeliveryDay deliveryDay : deliveryDays)
            days.add(deliveryDay.getDelivery_day());
        return days;
    }
    //endregion

    //region items

//    /**
//     * adds items to the database
//     * @param pid catalog id
//     * @param price price
//     * @param contractNum   contract number
//     * @param itemName  name of item
//     * @param manufacturer  item's manufacturer
//     * @param pComID    company's id
//     * @return false if failed, otherwise true
//     */
//    public boolean addItems(int pid, double price, int contractNum, String itemName, String manufacturer,int pComID) {
//        try {
//            boolean prodExists = getProduct(pid) != null;
//            List<Item> items;
//            if (!prodExists) {
//                if (!addProduct(pid, itemName, manufacturer))
//                    return false;
//                items = generateItems(pid, price, contractNum,0, pComID);
//            }
//            else {
//                double lastItem = getLastSerialNumber(pid);
//                items = generateItems(pid, price, contractNum, lastItem, pComID);
//            }
//            for (Item item : items){
//                databaseManager.getSession().save(item);
//            }
//            databaseManager.commitAndFLush();
//            return true;
//        } catch (Exception e){}
//        return false;
//    }

    /**
     * creates items
     * @param pid item catalog id
     * @param price
     * @param contractNum
     * @param quantity
     * @param lastItem  last item serial number
     * @param pComID
     * @return list of items created
     */
    private List<Item> generateItems(int pid, double price, int contractNum, int quantity,double lastItem, int pComID) {
        LinkedList<Item> list = new LinkedList<>();
        double currSN = pid + lastItem;
        Product product = getProduct(pid);
        SupplierContract contract = getContract(pComID,contractNum);
        for (int i = 1 ; i <= quantity; i++) {
            double sn = currSN + (0.00000001*i);
            list.add(new Item(sn, contract,price, product));
        }
        return list;
    }

    /**
     * retrieves the last serial number of a certain product in the database
     * @param pid product catalog id
     * @return the largest number exists on serial numbers of a certain product
     */
    private double getLastSerialNumber(int pid) {
        try{
            CriteriaQuery<Item> criteriaQuery = databaseManager.getCb().createQuery(Item.class);
            Root<Item> root = criteriaQuery.from(Item.class);
            criteriaQuery.select(root);
            criteriaQuery.where(databaseManager.getCb().equal(root.get("catalog_number"), pid));
            criteriaQuery.orderBy(databaseManager.getCb().desc(root.get("serial_number")));
            Query<Item> query = databaseManager.getSession().createQuery(criteriaQuery);
            List<Item> items = query.getResultList();
            return items.get(0).getSerial_number();
        }   catch (Exception e){}
        return -1;
    }

    public List<Item> getItemsByContract(int contractNum) {
        try{
            CriteriaQuery<Item> criteriaQuery = databaseManager.getCb().createQuery(Item.class);
            Root<Item> root = criteriaQuery.from(Item.class);
            criteriaQuery.select(root);
            criteriaQuery.where(databaseManager.getCb().equal(root.get("contract"), contractNum));
            Query<Item> query = databaseManager.getSession().createQuery(criteriaQuery);
            return query.getResultList();
        }   catch (Exception e){}
        return null;
    }


    private void removeAllItems(){
        try{
            CriteriaQuery<Item> criteriaQuery = databaseManager.getCb().createQuery(Item.class);
            Root<Item> root = criteriaQuery.from(Item.class);
            criteriaQuery.select(root);
            Query<Item> query = databaseManager.getSession().createQuery(criteriaQuery);
            List<Item> items = query.getResultList();
            for (Item item: items) {
                databaseManager.getSession().delete(item);
                databaseManager.commitAndFLush();
            }
        }   catch (Exception e){}
    }

    /**
     * retrieves items by catalog id
     * @param pid catalog id
     * @return  null if failed, empty list if doesn't exist, list if found
     */
    public List<Item> getItemsByCatalogID(int pid){
        try{
            CriteriaQuery<Item> criteriaQuery = databaseManager.getCb().createQuery(Item.class);
            Root<Item> root = criteriaQuery.from(Item.class);
            criteriaQuery.select(root);
            criteriaQuery.where(databaseManager.getCb().equal(root.get("product"), pid));
            Query<Item> query = databaseManager.getSession().createQuery(criteriaQuery);
            return query.getResultList();
        }   catch (Exception e){}
        return null;
    }

    //endregion

    //region products

    /**
     * retrieves product from database
     * @param pid the product's catalog id
     * @return null if failed, otherwise a product instance is returned
     */
    public Product getProduct(int pid) {
        Product product = databaseManager.getSession().get(Product.class, pid);
        return product;
    }

    /**
     * adds a new product to the database
     * @param pid product's catalog id
     * @param name  product's name
     * @param manufacturer product's manufacturer
     * @return false if failed, otherwise true
     */
    public boolean addProduct(int pid, String name, String manufacturer) {
        try {
            boolean prodExists = getProduct(pid) != null;
            if (prodExists) return true;
            Product product = new Product(pid,name,manufacturer);
            databaseManager.getSession().save(product);
            databaseManager.commitAndFLush();
            return true;
        } catch (Exception e){}

        return false;
    }


    /**
     * removes all contacts connected to a certain company
     * @param comID the company id to remove from
     * @return false if failed, otherwise true
     */
    public boolean removeProductsByCompanyID(int comID) {
        try {
            SupplierContract contract = getContractByCompany(comID);
            databaseManager.getSession().delete(contract);
            databaseManager.commitAndFLush();
            return true;
        } catch (Exception e){}
        return false;
    }

    private void removeAllProducts(){
        try{
            CriteriaQuery<Product> criteriaQuery = databaseManager.getCb().createQuery(Product.class);
            Root<Product> root = criteriaQuery.from(Product.class);
            criteriaQuery.select(root);
            Query<Product> query = databaseManager.getSession().createQuery(criteriaQuery);
            List<Product> products = query.getResultList();
            for (Product product: products)
                databaseManager.getSession().delete(product);
        }   catch (Exception e){}
    }

    //endregion

    //region catalogs

    /**
     * adds a new quantity list into the database
     * @param contractNum contract number
     * @param pid product catalog id
     * @param quantity
     * @param quantityDis discount
     * @param pComID company id
     * @return false if failed, otherwise true
     */
    public boolean addCatalogList(int contractNum, int pid, int quantity, double quantityDis, int pComID, double price, boolean hasQL) {
        try{
            SupplierContract contract = getContract(pComID,contractNum);
            Product product = getProduct(pid);
            Catalog catalogList = new Catalog(contract,product,quantity, quantityDis, price, hasQL);
            databaseManager.getSession().save(catalogList);
            databaseManager.commitAndFLush();
            return true;
        }
        catch (Exception e){}
        return false;
    }

    public List<Catalog> getCatalogListByContract(int contractNum) {
        try{
            CriteriaQuery<Catalog> criteriaQuery = databaseManager.getCb().createQuery(Catalog.class);
            Root<Catalog> root = criteriaQuery.from(Catalog.class);
            criteriaQuery.select(root);
            criteriaQuery.where(databaseManager.getCb().equal(root.get("contract"), contractNum));
            Query<Catalog> query = databaseManager.getSession().createQuery(criteriaQuery);
            return query.getResultList();
        }   catch (Exception e){}
        return null;
    }

    private void removeAllCatalog(){
        try{
            CriteriaQuery<Catalog> criteriaQuery = databaseManager.getCb().createQuery(Catalog.class);
            Root<Catalog> root = criteriaQuery.from(Catalog.class);
            criteriaQuery.select(root);
            Query<Catalog> query = databaseManager.getSession().createQuery(criteriaQuery);
            List<Catalog> catalogs = query.getResultList();
            for (Catalog catalog: catalogs) {
                databaseManager.getSession().delete(catalog);
                databaseManager.commitAndFLush();
            }
        }   catch (Exception e){}
    }

    //endregion

    //region best deals
    /**
     * Finds the best supplier to buy from the given product with the given ammount
     * @param product
     * @param amount
     * @return
     */
    public SupplierContact findBestSupp(Product product, int amount){
        Catalog bestQL = null;
        double minPrice = Double.MAX_VALUE;
        try{
            CriteriaQuery<Catalog> criteriaQuery = databaseManager.getCb().createQuery(Catalog.class);
            Root<Catalog> root = criteriaQuery.from(Catalog.class);
            criteriaQuery.select(root);
            criteriaQuery.where(databaseManager.getCb().equal(root.get("product"), product));
            Query<Catalog> query = databaseManager.getSession().createQuery(criteriaQuery);
            List<Catalog> specificQL = query.getResultList();


            for(Catalog q : specificQL){
                if (q.getPrice() < minPrice) {
                    minPrice = q.getPrice();
                    bestQL = q;
                }
                if(!q.hasQL()) continue;
                double disPrice = getDiscount(q);
                if (amount >= q.getQuantity() && disPrice < minPrice){
                    minPrice = disPrice;
                    bestQL = q;
                }
            }
            if(bestQL != null){
                Supplier supplier = bestQL.getContract().getSupplier();
                return getRandomContact(supplier.getPrivate_company_id());
            }
        }   catch (Exception e){}
        return null;
    }

    private double getDiscount(Catalog q) {
        return q.getPrice() - q.getPrice()*q.getDiscount()/100;
    }

    /**
     * Fills order items in their best deals according the given supplier
     * @param supplierContact   a supplier to find the best deal with
     * @param orderItems        a list of items to update prices and discounts
     */
    public void fillPrices(SupplierContact supplierContact , List<OrderItem> orderItems){
        Supplier supplier = supplierContact.getSupplier();
        for (OrderItem orderItem: orderItems){
            double minPrice = Double.MAX_VALUE;             // min price, in case of discount it is after discount,
            double discount = 0;                            // otherwise equals to price
            double price = 0;

            int currProductID = orderItem.getpID().getpID().getID();
            SupplierContract contract = getContractByCompany(supplier.getPrivate_company_id());
            if (contract != null) {
                List<Catalog> catalog = getCatalogListByContract(contract.getContract_number());
                for (Catalog catalogItem: catalog){
                    if (currProductID == catalogItem.getProduct().getID()){ // found prod in supplier's catalog

                        if (catalogItem.getPrice() < minPrice) {        // before discount
                            minPrice = catalogItem.getPrice();
                            price = minPrice;
                            discount = 0;
                        }

                        if (catalogItem.hasQL()){                       // if there's discount
                            double priceAfterDiscount = getDiscount(catalogItem);
                            if (catalogItem.getQuantity() <= orderItem.getAmount() && priceAfterDiscount < minPrice) {
                                minPrice = priceAfterDiscount;      // after discount
                                price = catalogItem.getPrice();
                                discount= catalogItem.getDiscount();
                            }
                        }
                    }
                }
                // updating best deals
                orderItem.setBuyingPrice(price);
                orderItem.setDiscountPer(discount);
                orderItem.setFinalPrice(minPrice);
            }

        }
    }

    //endregion

    public void clearDB() {
        removeAllCatalog();
        removeAllDeliveryDays();
        removeAllItems();
        removeAllContacts();
        removeAllContracts();
        removeAllSuppliers();
    }







}

