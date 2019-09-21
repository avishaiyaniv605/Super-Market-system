package PresentationLayer.DeliveryModule;

import BusinessLayer.DeliveryModule.*;
import BusinessLayer.EmployeeModule.Shifts;

import BusinessLayer.Interfaces.ShowMenuAction;
import BusinessLayer.Parsers.LogicParser;
import PersistenceLayer.BranchProfile;
import PersistenceLayer.Inventory.Orders.OrderItem;
import PersistenceLayer.Inventory.Orders.Orders;
import PresentationLayer.Inventory.OrdersMenu;


import java.util.*;

public class DeliveryMenu implements ShowMenuAction {
    private String _startUpMenu, _chooseOption, _illegal, _chooseProductsAndDestinations, _errMenu;

    private LogicParser logicParser;
    private DeliveryMenuParser deliveryMenuParser;
    private Scanner sc;


    private int choice;
    private Integer choose;
    private List<BranchProfile> destinations;
    private int totalWeight = 0;
    private BranchProfile source = null;
    private List<Orders> orderList;
    private Shifts shift = null;

    public DeliveryMenu(Scanner sc, LogicParser logicParser) {
        this.logicParser = logicParser;
        this.sc = new Scanner(System.in);
        deliveryMenuParser = new DeliveryMenuParser(sc, this.logicParser);
        destinations = new LinkedList<>();
        initMenus();
        orderList = new LinkedList<>();
    }

    /**
     * Initialize menus
     */
    private void initMenus() {
        _startUpMenu = "\n" +
                "1) Create delivery \n"
                +"2) Cancel Order\n"
                +"3) Back\n";

        _chooseOption = "Please enter the option you want:\n";

        _illegal = "illegal input \n";

        _chooseProductsAndDestinations = "Would you like to:\n" +
                                        "1) add another order\n" +
                                        "2) there are no more products or destinations to add to this delivery";

        _errMenu = "Your delivery is over weight. Would you like to:\n" +
                    "1) remove an, thus removing all products to that destination\n" +
                    "2) remove items from one of the orders";
    }

    /**
     * The method shoes the first menu to the user
     */
    public void showMenu() {
        boolean back = false;
        while (!back) {
            System.out.println(_startUpMenu);
            System.out.println(_chooseOption);
            check();
            switch (choice) {
                case 1: {
                    getOrders();
                    break;
                }
                case 2: {
                    long oID;
                    String ordersID = logicParser.get_invParser().getPendingOrders();
                    if (!ordersID.equals("")) {
                        System.out.println("Choose order to cancel from the pendings orders:\n" + ordersID + "\n");
                        try {
                            Scanner sc2= new Scanner(System.in);
                            oID = Long.parseLong(sc2.nextLine());
                            OrdersMenu orders = new OrdersMenu(logicParser);
                            orders.cancelOrder(oID);
                        } catch (Exception e) {

                        }

                    }
                    break;
                }
                case 3:
                    back = true;
                    break;
            }
        }

    }

    private void getOrders(){
        //go over with tal how to display them, want them sorted by date created
        //add function to one of the parsers returning the string of all order options.
        String result = deliveryMenuParser.chooseOrders();

        if(result.equals("")){
            System.out.println("there are no orders to process");

        }else {
            System.out.println("Please choose the order you want to process");
            System.out.println(result);
            check();

            if (choice > deliveryMenuParser.getOrders()) {
                System.out.println(_illegal);
                getOrders();
            } else {
                Orders order = deliveryMenuParser.getOrder(choice - 1);
                orderList.add(order);
                List<Orders> list = new LinkedList<>();
                list.add(order);
                destinations.add(order.getBranch());
                source = order.getBranch();
                chooseShift(order.getBranch());
            }
        }
    }
    /**
     * After the user chose to make a new delivery, he will pick the source location
     *

    private void getSource(){
        System.out.println(deliveryMenuParser.chooseSource());
        check();
        BranchProfile branch = deliveryMenuParser.getSource(choice-1);
        if(branch == null){
            System.out.println(_illegal);
            getSource();
        }else{
            source = branch;
            //getDest(shifts);
            chooseShift(branch);
        }
    }
*/
    private void chooseShift(BranchProfile branchProfile){
            List<String> shiftString= this.logicParser.getEmployeeParser().convertBranchShiftsToString(this.logicParser.getEmployeeParser().getShiftFromThisDate(), branchProfile);
            if(shiftString == null || shiftString.isEmpty()){
                System.out.println("You can't create new delivery, because there are no shifts for this branch.");
                return;
            }
            String menuShift="Choose a number of the shift you want from the list below or type -1 in order to go back to the menu\n";

            for(int i=0; i<shiftString.size(); i++){
                menuShift=menuShift+(i+1)+")\t"+shiftString.get(i);
            }
            String number=null;
            do{
                if(number!=null){
                    System.out.println("Invalid input. Please try again.");
                }
                System.out.println(menuShift);
                //number = null;
                number=sc.next();
                System.out.print("\n");
            }while (!this.logicParser.getEmployeeParser().checkIsValidRange(shiftString.size(),number) && !number.equals("-1"));

            if(number.equals("-1")){
                return;
            }
            choose=Integer.parseInt(number)-1;
            Shifts shifts = this.logicParser.getEmployeeParser().getShiftInPlace(choose);

            //Check for available storekeeper to be on the branch
            if (!(logicParser.getDeliveryEmployeeParser().isThereStorekeeperInShift(choose))){
                System.out.println("There are no available storekeeper in this branch at that shift!");
                return;
            }
            shift = shifts;
            checkAddMoreOrders();
    }

    private void checkAddMoreOrders(){
        System.out.println(_chooseProductsAndDestinations);
        check();
        switch (choice) {
            case 1:
                addMoreOrders();
                break;
            case 2:
                getTotalWeight();
                TruckProfile truck = deliveryMenuParser.chooseTruck(totalWeight);
                issueSorting(truck);
                break;
        }

    }


    //driver is connected to branch so can only add  other orders from same branch
    private void addMoreOrders(){
        String result = deliveryMenuParser.chooseOrders();
        if(result.equals("")){
            System.out.println("There are no more orders to deliver");
            getTotalWeight();
            TruckProfile truck = deliveryMenuParser.chooseTruck(totalWeight);
            issueSorting(truck);
        }else{
            System.out.println("Please choose the order you want to add to delivery");
            System.out.println(result);
            check();

            if(choice > deliveryMenuParser.getOrders()){
                System.out.println(_illegal);
                addMoreOrders();
            }else{ Orders order = deliveryMenuParser.getOrder(choice-1);
                if(checkChoiceOrder(order)) {
                    System.out.println("Destination has already been chosen for this delivery");
                    System.out.println("Please choose different destination\n");
                    addMoreOrders();
                }else {
                    orderList.add(order);
                    destinations.add(order.getBranch());
                    //MAYBE CHECK WEIGHT
                    checkAddMoreOrders();
                }

            }
        }

    }



    /**
     * Gets destination from the user
     * @param shifts
     */
  /*  private void getDest(Shifts shifts){
        System.out.println(deliveryMenuParser.chooseDest());
        check();
        DestinationProfile dest = deliveryMenuParser.getDest(choice);
        if(dest == null){
            System.out.println(_illegal);
            getDest(shifts);
        }else{
            if(checkChoiceDest(dest)){
                System.out.println("Destination has already been chosen for this delivery");
                System.out.println("Please choose different destination\n");
                getDest(shifts);
            }else{
            ProductsFormProfile pf = new ProductsFormProfile(dest);
            //deliveryMenuParser.createProductsForm(pf);
            destOrders.put(dest,pf);
            addProducts(pf, shifts);}
        }
    }

*/
    /**
     * Gets the products per each destination
     * @param pf - The products will be added to the specific destination's products form
     * @param shifts
     */
   /* private void addProducts(ProductsFormProfile pf, Shifts shifts) {
        System.out.println(deliveryMenuParser.chooseProd());
        check();
        ProductProfile prod = deliveryMenuParser.getProd(choice);
        if(prod == null){
            System.out.println(_illegal);
            addProducts(pf, shifts);
        }else{
            if(checkChoiceProd(pf,prod)){
                System.out.println("Product has already been chosen for this destination");
                System.out.println("Please choose different product\n");
                addProducts(pf, shifts);
            }else{
                System.out.println("please enter amount wanted \n");
                check();
                ProductDelivery pd = new ProductDelivery(prod, choice, pf);

                pf.getProducts().add(pd);
                System.out.println(_chooseProductsAndDestinations);
                check();
                switch (choice) {
                    case 1:
                        addProducts(pf, shifts);
                        break;
                    case 2:
                        pf.checkWeight();
                        getDest(shifts);
                        break;
                    case 3:
                        pf.checkWeight();
                        getTotalWeight();
                        TruckProfile truck = deliveryMenuParser.chooseTruck(totalWeight);
                        issueSorting(truck, shifts);
                        break;
                }
            }
        }
    }*/
//ADD TO ISSUES TO PF


    /**
     * need to add function that calculates weight of order!!!!!!!!!!
     *
     */

    /**
     * In case of overweight of the delivery, the user will choose a destination and
     * products to remove from the specific destination products
     * @param truck
     *
     */
    private void issueSorting(TruckProfile truck) {

        DeliveryFormProfile deliveryForm = new DeliveryFormProfile();
        //deliveryMenuParser.createDeliveryForm(deliveryForm);
        if(truck != null){
            getDriver(truck, deliveryForm, shift);
        }else{
            System.out.println(_errMenu);
            check();
            switch (choice){
                case 1:
                    System.out.println("Please choose what order you would like to remove");
                    printDest();
                    check();
                    if(choice > orderList.size()){
                        System.out.println(_illegal);
                        issueSorting(null);
                    }else {
                        removeOrder(choice, deliveryForm);
                    }
                    break;
                case 2:
                    System.out.println("Please choose what order you would like to remove products from");
                    printDest();
                    check();
                    if(choice > orderList.size()){
                        System.out.println(_illegal);
                        issueSorting(null);
                    }else{
                        int i = 1;

                        for(Orders order : orderList){
                            if(i == choice){
                                removeProducts(order, deliveryForm, shift);
                                break;
                            }
                            i++;
                        }
                    }


                    break;
            }
        }
    }

    /**
     * Prints the destinations to the screen so the user can choose the one he want
     * to remove
     */
    private void printDest(){
        int i = 1;
        for(Orders order : orderList){
            System.out.println(i +") " + order.toString());
            i++;
        }
    }

    /**
     *  @param order - The order to remove the products from
     * @param df - //:TODO should be products form???
     * @param shifts
     */
    private void removeProducts(Orders order, DeliveryFormProfile df, Shifts shifts) {
        System.out.println("Please choose what product you would like to remove:");
        printProducts(order);
        check();
        if(choice > order.getOrderItems().size()){
            System.out.println("illegal input \n");
            removeProducts(order, df, shifts);
        }else{
            int i = 1;
            for(OrderItem orderItem : order.getOrderItems()){
                if(choice == i){
                    System.out.println("Over Weight : product "+ orderItem.getpID().getpID().getName() + " was removed");
                    df.setIssues("Over Weight : product "+ orderItem.getpID().getpID().getName() + " was removed");
                    orderItem.setCancel(1);
                    //ORDER ITEM SET CANCELED TO 1
                    /*if(pf.getProducts().size() == 0){
                        System.out.println("There are no more products for this destination, removing destination from delivery");
                        destOrders.remove(dest);
                        if(empty()){
                            break;
                        }
                    }*/ //ASK TAL!

                    getTotalWeight();
                    TruckProfile truck = deliveryMenuParser.chooseTruck(totalWeight);
                    issueSorting(truck);
                    break;
                }
                i++;
            }
        }


    }

    /**
     * Prints the products that belongs to the @pf so the user can decide which one to remove
     * @param order - The products form to show the user to decide which and how many to remove
     */
    private void printProducts(Orders order) {
        int i = 1;
        for(OrderItem orderItem : order.getOrderItems()){
            System.out.println(i +") " +orderItem.getpID().getpID().getName());
            i++;
        }
    }

    /**
     * Removes the destination, and all of the product's that was in his products form, from the delivery
     * @param choice
     * @param df - //TODO: should be a product form???
     *
     */
    private void removeOrder(int choice, DeliveryFormProfile df) {
        int i = 1;
        for(Orders order : orderList){
            if(i == choice){
                System.out.println("Over Weight : order "+ order.getOrderID() + " was removed");
                df.setIssues("Over Weight : order "+ order.getOrderID() + " was removed");
                orderList.remove(order);
                if(!empty()){
                    getTotalWeight();
                    TruckProfile truck = deliveryMenuParser.chooseTruck(totalWeight);
                    issueSorting(truck);
                }
                break;
            }
            i++;
        }

    }

    private boolean empty() {
        if(orderList.size() == 0){
            System.out.println("There are no more orders for this delivery, returning to main menu");
            source = null;
            destinations = new LinkedList<>();
            choice = 0;
            shift = null;
            showMenu();
            return true;
        }
        return false;
    }

    /**
     * Get a driver form that can drive on the chosen truck that will carry the delivery
     * @param truck - The truck that was chosen to carry the delivery
     * @param df - The delivery form
     * @param shifts
     */
    private void getDriver(TruckProfile truck, DeliveryFormProfile df, Shifts shifts) {
        List<DriverProfile> driverProfiles =logicParser.getDeliveryEmployeeParser().getDriversInShift(choose);;

        DriverProfile driver = deliveryMenuParser.chooseDriver(truck, driverProfiles, shifts);
        if(driver != null){
            createDelivery(truck, driver, df, shifts);
            System.out.println("New delivery on the way !!!");
        }
        else{
            System.out.println("There are no available driver for this delivery at that shift!");
            System.out.println("Please choose new shift");
            chooseNewShift(truck, df, source);
        }
    }
    private void chooseNewShift(TruckProfile truck, DeliveryFormProfile df ,BranchProfile branchProfile){
        List<String> shiftString= this.logicParser.getEmployeeParser().convertBranchShiftsToString(this.logicParser.getEmployeeParser().getShiftFromThisDate(), branchProfile);
        if(shiftString == null || shiftString.isEmpty()){
            System.out.println("You can't create new delivery, because there are no shifts for this branch.");
            return;
        }
        String menuShift="Choose a number of the shift you want from the list below or type -1 in order to go back to the menu\n";

        for(int i=0; i<shiftString.size(); i++){
            menuShift=menuShift+(i+1)+")\t"+shiftString.get(i);
        }
        String number=null;
        do{
            if(number!=null){
                System.out.println("Invalid input. Please try again.");
            }
            System.out.println(menuShift);
            //number = null;
            number=sc.next();
            System.out.print("\n");
        }while (!this.logicParser.getEmployeeParser().checkIsValidRange(shiftString.size(),number) && !number.equals("-1"));
        if(number.equals("-1")){
            return;
        }
        choose=Integer.parseInt(number)-1;
        Shifts shifts = this.logicParser.getEmployeeParser().getShiftInPlace(choose);
        //Check for available storekeeper to be on the branch
        if (!(logicParser.getDeliveryEmployeeParser().isThereStorekeeperInShift(choose))){
            System.out.println("There are no available storekeeper in this branch at that shift!");

        }
        getDriver(truck, df, shifts);
    }

    /**
     * After gathering all the necessary details for the delivery, this method will create a delivery object
     * and will start the process of insert this delivery to the database
     * @param truck
     * @param driver
     * @param df
     * @param shifts
     */
    private void createDelivery(TruckProfile truck, DriverProfile driver, DeliveryFormProfile df, Shifts shifts) {
        List<Orders> orders;
        List<BranchProfile> destList;
        orders = new LinkedList<>(orderList);
        destList = new LinkedList<>(destinations);

        DeliveryProfile delivery = new DeliveryProfile(truck, driver.getID().getFirstName(), totalWeight, orders, destList, source, shifts.getDate(), shifts.getDate());
        deliveryMenuParser.createDelivery(delivery);
        df.setDelivery(delivery);
        deliveryMenuParser.createDeliveryForm(df);
        choice = 0;
        source = null;
        shift = null;
        destinations = new LinkedList<>();
        orderList = new LinkedList<>();
        totalWeight = 0;
    }

    /**
     * Gets the total weight of the delivery
     */
    private void getTotalWeight() {
        totalWeight = deliveryMenuParser.getTotalWeight(orderList);
    }

    private boolean checkChoiceOrder(Orders order){
        if(orderList.contains(order)){
            return true;
        }
        return false;
    }

    private boolean checkChoiceProd(ProductsFormProfile pf, ProductProfile prod){
        for(ProductDelivery pd : pf.getProducts()){
            if(pd.getID().getID() == prod.getID()){
                return true;
            }
        }

        return false;
    }

    private void check(){
        boolean leg = false;
        while (!leg) {
            choice=0;
            try {
                choice = sc.nextInt();

                leg = true;

            } catch (Exception e) {
                System.out.println("illegal input, please insert again \n");
                sc.next();
            } finally {

            }
        }

    }

    public String toString(){
        return "Delivery Menu";
    }
}
