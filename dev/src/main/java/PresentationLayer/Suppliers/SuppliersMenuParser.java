package PresentationLayer.Suppliers;

import BusinessLayer.Parsers.LogicParser;
import PersistenceLayer.Suppliers.*;

import java.util.List;
import java.util.Scanner;

public class SuppliersMenuParser {

    private Scanner _sc;
    private LogicParser _logicParser;
    private final String ERROR = "An error occurred";
    private final String INSERT_COM_ID = "Insert private company id: ";
    private final String INSERT_NAME = "Insert name: ";
    private final String TYPE_Y_N = "type y or n : ";

    public SuppliersMenuParser(LogicParser logicParser , Scanner sc){
        _logicParser=logicParser;
        _sc = sc;
    }

    //region Supplier

    /***
     * Adds a supplier to the database according to a detailed wizard,
     * which includes company id, bank account and payment terms query.
     * Once a supplier is added, options to add contact and contracts are shown.
     */
    public void addSupplier() {
        boolean proccessCheck = false;
        int pComID;
        String bankAcc, payTerms, name, address;

        // add supplier
        System.out.print(INSERT_COM_ID);
        pComID = getIntFromUser();
        System.out.print("Insert bank account: ");
        bankAcc = _sc.nextLine();
        System.out.print("Insert payment terms: ");
        payTerms = _sc.nextLine();
        System.out.print(INSERT_NAME);
        name = _sc.nextLine();
        System.out.print("Insert address: ");
        address = _sc.nextLine();


        if(_logicParser.getSuppParser().getSupplier(pComID) == null){   // doesn't exists
            proccessCheck = _logicParser.getSuppParser().addSupplier(pComID,bankAcc,payTerms, name, address);
            if (proccessCheck)
                System.out.println("\nSupplier " + pComID + " has been successfully added.\n");
            else
                System.out.println('\n' + ERROR +'\n');
        }
        else
            System.out.println("\nSupplier " + pComID + " already exists.\n");

        if (!proccessCheck)
            return;

        // add contact
        addContact(pComID);

        // add contract
        addContract(pComID);
    }

    /**
     * Changes bank account details of the supplier
     */
    public void changeBankAccount() {
        int pComID;
        String newBankAccount;
        System.out.print(INSERT_COM_ID);
        pComID = getIntFromUser();
        System.out.print("Insert new bank account: ");
        newBankAccount = _sc.nextLine();

        if (_logicParser.getSuppParser().changeBankAccount(pComID,newBankAccount))
            System.out.println("\nBank account updated.\n");
        else
            System.out.println('\n' + ERROR +'\n');
    }

    /**
     * Changes payment terms details of the supplier
     */
    public void changePaymentTerms() {
        int pComID;
        String newPayTerms;
        System.out.print(INSERT_COM_ID);
        pComID = getIntFromUser();
        System.out.print("Insert new payment terms: ");
        newPayTerms = _sc.nextLine();

        if (_logicParser.getSuppParser().changePaymentTerms(pComID,newPayTerms))
            System.out.println("\nPayment Terms updated.\n");
        else
            System.out.println('\n' + ERROR +'\n');
    }

    /**
     * removes supplier contacts from database
     */
    public void removeSupplier() {
        System.out.println("Attention: \n" +
                "Removing supplier will only remove supplier contacts, it will not remove contracts and orders!");
        System.out.print(INSERT_COM_ID);
        int comID = getIntFromUser();
        if (_logicParser.getSuppParser().removeContacts(comID))
            System.out.println("\nDeleted successfully.\n");
        else
            System.out.println('\n' + ERROR +'\n');
    }

    /**
     * retrieves and prints all suppliers in database
     */
    public void showAllSuppliers() {

        List<Supplier> suppliers = _logicParser.getSuppParser().getAllSuppliers();
        if (suppliers == null){
            System.out.println(ERROR);
            return;
        }
        int i = 1, size = suppliers.size();
        for (Supplier supplier : suppliers) {
            System.out.println(supplier.toString());
            if (i != size)
                System.out.println("--------------------------");
            i++;
        }

    }

    //endregion

    //region Contact

    /**
     * adds new contact to the database by using @addContact(int pComID) method
     * this method has no assumption on company id
     */
    public void addContact() {
        int pComID;
        System.out.print(INSERT_COM_ID);
        pComID = getIntFromUser();
        addContact(pComID);
    }

    /**
     * Adds supplier contacts to the database according to a detailed wizard,
     * which includes name phone and email. this procedure can be done several times
     * When the addition is done, a contract query will be shown.
     * @param pComID private company id
     */
    private void addContact(int pComID) {
        String  name, phoneNum, email;
        System.out.println("Add at least one contact:");
        boolean proccessCheck = true;
        while (proccessCheck){
            System.out.print(INSERT_NAME);
            name = _sc.nextLine();
            System.out.print("Insert phone number: ");
            phoneNum = _sc.nextLine();
            System.out.print("Insert email: ");
            email = _sc.nextLine();
            if(_logicParser.getSuppParser().getContact(pComID,name,phoneNum,email) == null){   // doesn't exists
                proccessCheck = _logicParser.getSuppParser().addContact(pComID,name,phoneNum,email);
                if (proccessCheck)
                    System.out.println("\nContact " + name + " has been successfully added.\n");
                else
                    System.out.println('\n' + ERROR +'\n');
            }
            else
                System.out.println("\nContact " + name + " already exists.\n");
            System.out.print("Do you want to add another contact? ");
            System.out.print(TYPE_Y_N);

            proccessCheck =  _sc.nextLine().equals("y");
        }
    }

    /**
     * Changes contact details
     */
    public void changeContactDetails() {
        int pComID;
        String oldPhone, newPhone, oldEmail, newEmail,name;
        System.out.print(INSERT_COM_ID);
        pComID = getIntFromUser();
        System.out.print(INSERT_NAME);
        name = _sc.nextLine();
        System.out.print("Insert old phone number: ");
        oldPhone = _sc.nextLine();
        System.out.print("Insert new phone number: ");
        newPhone = _sc.nextLine();
        System.out.print("Insert old email: ");
        oldEmail = _sc.nextLine();
        System.out.print("Insert new email: ");
        newEmail = _sc.nextLine();

        if (_logicParser.getSuppParser().changeContact(pComID,name,oldPhone,oldEmail,newEmail,newPhone))
            System.out.println("\nContact details updated.\n");
        else
            System.out.println('\n' + ERROR +'\n');
    }

    /**
     * removes a certain contact from database by user's input
     */
    public void removeContact() {
        int pComID;
        String phone, email ,name;
        System.out.print(INSERT_COM_ID);
        pComID = getIntFromUser();
        System.out.print(INSERT_NAME);
        name = _sc.nextLine();
        System.out.print("Insert phone number: ");
        phone = _sc.nextLine();
        System.out.print("Insert email: ");
        email = _sc.nextLine();

        int response = _logicParser.getSuppParser().removeContact(pComID, name, phone, email);
        if (response == 1)
            System.out.println("\n" + name + " removed.\n");
        else if (response == 0) // error in deletion
            System.out.println('\n' + ERROR +'\n');
        else {  //last contact
            System.out.println('\n' +name +" is the last contact of supplier " + pComID + ". Therefore, cannot be deleted." +'\n');
        }
    }

    public void showContacts() {
        System.out.print(INSERT_COM_ID);
        int comID = getIntFromUser();
        List<SupplierContact> contacts = _logicParser.getSuppParser().getContactsByCompany(comID);
        if (contacts == null) {
            System.out.println(ERROR);
            return;
        }
        int i = 1, size = contacts.size();
        for (SupplierContact contact : contacts) {
            System.out.println(contact.toString());
            if (i != size)
                System.out.println("--------------------------");
            i++;
        }
    }

    //endregion

    //region Contract

    /**
     * adds contracts to the database
     */
    public void addContract() {
        int pComID;
        System.out.print(INSERT_COM_ID);
        pComID = getIntFromUser();
        addContract(pComID);
    }

    /***
     * Adds supplier contracts to the database according to a detailed wizard,
     * which includes working days, items specification and discounts. this procedure can be done several times.
     * @param pComID private company id
     */
    private void addContract(int pComID) {
        System.out.println("Add a contract:");
        boolean proccessCheck = true;
        int contractNum = -1;
            System.out.print("The company has permanent delivery days? ");
            System.out.print(TYPE_Y_N);
            proccessCheck =  _sc.nextLine().equals("y");
            if (proccessCheck) {
                contractNum = addSupplyingDays(pComID);
            }
            else {
                System.out.print("The company does deliveries? ");
                System.out.print(TYPE_Y_N);
                contractNum = _logicParser.getSuppParser().addContract(pComID,false, _sc.nextLine().equals("y"));
            }
            if (contractNum != -1) {
                System.out.print("Do you want to add a catalog to this contract? ");
                System.out.print(TYPE_Y_N);
                proccessCheck =  _sc.nextLine().equals("y");
                if (proccessCheck)
                    addItemsToCatalog(contractNum, pComID);
            }
    }

    public void showContracts() {
        System.out.print(INSERT_COM_ID);
        int comID = getIntFromUser();
        SupplierContract contract = _logicParser.getSuppParser().getContractByCompany(comID);
        if (contract == null) {
            System.out.println(ERROR);
            return;
        }
            List<DeliveryDay> days = _logicParser.getSuppParser().getDeliveryDaysByContract(contract.getContract_number());
            StringBuilder tmpBuilder = new StringBuilder();
            if (days == null){
                System.out.println(ERROR);
                return;
            }
            tmpBuilder.append(contract.toString());
            // add delivery days
            for (DeliveryDay day : days)
                tmpBuilder.append(" " + day.getDelivery_day());
            // add quantity list flag
            if (_logicParser.getSuppParser().contractHasQuantityList(contract.getContract_number()))
                tmpBuilder.append("\nHas quantity list agreement");
            else
                tmpBuilder.append("\nHas no quantity list agreement");

            System.out.println(tmpBuilder.toString());
                System.out.println("--------------------------");
    }

    /**
     * Adds quantity lists to the database
     * @param pComID private company id
     * @param contractNum contract number of the certain supplier
     */
    private void addCatalogProduct(int contractNum, int pComID, double prodPrice, boolean ql, int suppCatalogNum) {
        int quantity = 0;
        double discount = 0;
        if (ql) {
            System.out.print("Insert quantity for discount: ");
            quantity = getIntFromUser();
            System.out.print("Insert discount in percent: ");
            discount = getDoubleFromUser();
        }
        if (_logicParser.getSuppParser().addCatalogList(contractNum, suppCatalogNum, quantity, discount, pComID, prodPrice, ql))
            System.out.println("\nQuantity list discount added.\n");
        else
            System.out.println('\n' + ERROR +'\n');
    }


    public void ShowQuantityListInContract() {
        System.out.print("Insert contract number: ");
        int contractNum = getIntFromUser();
        List<Catalog> list = _logicParser.getSuppParser().getCatalogListByContract(contractNum);
        if (list == null) {
            System.out.println(ERROR);
            return;
        }
        int i = 1, size = list.size();
        for (Catalog catalogList : list) {
            System.out.println(catalogList.toString());
            if (i != size)
                System.out.println("--------------------------");
            i++;
        }

    }

    //endregion

    //region items
    /***
     * Adds items to the database
     * @param pComID private company id
     * @param contractNum contract number of the certain supplier
     */
    private void addItemsToCatalog(int contractNum, int pComID) {
        System.out.print("\nInsert items one by one according to the following requests:\n");

        boolean process = true;
        while(process) {
            int suppCatalogNum;
            double price;
            String itemName, manufacturer;
            System.out.print("Insert product's catalog id: ");
            suppCatalogNum = getIntFromUser();
            System.out.print("Insert price: ");
            price = getDoubleFromUser();
            System.out.print(INSERT_NAME);
            itemName = _sc.nextLine();
            System.out.print("Insert item manufacturer: ");
            manufacturer = _sc.nextLine();
            boolean success = _logicParser.getSuppParser().addProduct(suppCatalogNum, itemName, manufacturer);
            if (success) {
                System.out.print("Do you want to approve discounts? ");
                System.out.print(TYPE_Y_N);
                addCatalogProduct(contractNum, pComID, price, _sc.nextLine().equals("y"), suppCatalogNum);
            }
            else {
                System.out.println('\n' + ERROR +'\n');
            }
            System.out.print("Do you want to add more items? ");
            System.out.print(TYPE_Y_N);
            process =  _sc.nextLine().equals("y");
        }
    }

    public void ShowItemsInContract() {
        System.out.print("Insert contract number: ");
        int contractNum = getIntFromUser();
        List<Item> items = _logicParser.getSuppParser().getItemsByContract(contractNum);
        if (items == null) {
            System.out.println(ERROR);
            return;
        }
        int i = 1, size = items.size();
        for (Item item : items) {
            System.out.println(item.toString());
            if (i != size)
                System.out.println("--------------------------");
            i++;
        }
    }
    //endregion

    //region Delivery Days

    /**
     * adds delivery days to the database
     * @param pComID - company id
     * @return
     */
    private int addSupplyingDays(int pComID) {
        int contractNum = -1;
        contractNum = _logicParser.getSuppParser().addContract(pComID, true, true);
        if (contractNum == -1) {
            System.out.println("Could not find supplier");
            return -1;
        }
        System.out.println("Insert days, one by one. After each day, press enter.\n" +
                "If you don't want to add any more days type stop. \n" +
                "Valid inputs: Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday");
        System.out.print("Insert day: ");
        String currDay = checkDayInput();

        while (!currDay.equals("stop")) {
            if (_logicParser.getSuppParser().addDeliveryDay(pComID, currDay,contractNum))
                System.out.println('\n'+currDay + " added.\n");
            else
                System.out.println('\n' + ERROR +'\n');
            System.out.print("Insert day: ");
            currDay = checkDayInput();
        }
        return contractNum;
    }

    private String checkDayInput() {
        while (true) {
            String currDay = _sc.nextLine();
            currDay = currDay.toLowerCase();
            if (currDay.equals("stop")) return "stop";
            if (currDay.equals("sunday")) return "Sunday";
            if (currDay.equals("monday")) return "Monday";
            if (currDay.equals("tuesday")) return "Tuesday";
            if (currDay.equals("wednesday")) return "Wednesday";
            if (currDay.equals("thursday")) return "Thursday";
            if (currDay.equals("friday")) return "Friday";
            if (currDay.equals("saturday")) return "Saturday";
            System.out.println("Invalid input, try again.");
        }

    }

    //endregion


    private int getIntFromUser() {
        int ans = -1;
        while (ans == -1) {
            try {
                ans = Integer.parseInt(_sc.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid input!");
            }
        }
        return ans;
    }

    private double getDoubleFromUser() {
        double ans = -1;
        while (ans == (double)-1) {
            try {
                ans = Double.parseDouble(_sc.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid input!");
            }
        }
        return ans;
    }

}

