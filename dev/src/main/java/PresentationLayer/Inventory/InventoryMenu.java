package PresentationLayer.Inventory;

import BusinessLayer.Interfaces.ShowMenuAction;
import BusinessLayer.Parsers.LogicParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InventoryMenu implements ShowMenuAction {

    private String _invMain, _invDamaged, _invReport, _invCategories, _invDiscount;
    private ProductMenu product;
    private OrdersMenu orders;
    private String input;
    private Scanner _sc;
    private LogicParser _logicParser;
    private String time;

    /**
     * constructor
     *
     * @param logicParser - accessing the data base. handling the logic of the system.
     */
    /*
    For the merge in future
     */
    public InventoryMenu(Scanner sc, LogicParser logicParser) {
        _sc = sc;
        _logicParser = logicParser;
        time = _logicParser.getTime();
        initMenus();
        product = new ProductMenu(_logicParser);
        orders = new OrdersMenu(_logicParser);

    }

    /**
     * Init the menus and submenus
     */
    private void initMenus() {

        _invMain = "Welcome!!! the date is:" + _logicParser.getTime() + "\n" +
                "0.\tNext day...\n"+
                "1.\tProduct Menu management system \n" +
                "2.\tReports system\n" +
                "3.\tDamaged | Expired Products management system \n" +
                "4.\tCategory management\n" +
                "5.\tDiscount management\n" +
                "6.\tPeriodical orders management\n" +
                "7.\tChoose branch\n" +
                "8.\tCancel Order\n"+
                "9.\tBack\n";

        _invDamaged = "\n" +
                "Welcome to Damaged Products Management menu!" + "\n"
                + "1.\tReport damaged product" + "\n" +
                "2.\tPrint damaged/expired products report \n" +
                "3.\tRefresh the expired list \n" +
                "4.\tBack\n";

        _invReport = "\n" +
                "Welcome to reports menu!" + "\n"
                + "1.\tGeneral stock report" + "\n"
                + "2.\tReport by category" + "\n" +
                "3.\tBack\n";

        _invDiscount = "\n" +
                "Welcome to discounts menu!" + "\n"
                + "1.\tEnter new product discount" + "\n"
                + "2.\tEnter new category discount" + "\n" +
                "3.\tCancel product discount\n" +
                "4.\tCancel category discount\n" +
                "5.\tBack\n";

        _invCategories = "\n" +
                "Welcome to Categories menu!" + "\n"
                + "1.\tDisplay all store categories" + "\n"
                + "2.\tAdd new category" + "\n"
                + "3.\tDelete category" + "\n" +
                "4.\tEdit category\n" +
                "5.\tBack\n";
    }
    private void chooseBranch() {
        boolean branchOK= false;
        do{
            System.out.println("Choose your branch name from the list:");
            System.out.println(_logicParser.getAllBranches());
            String bBname = _sc.nextLine();
            if (!_logicParser.setBranch(bBname)) {
                System.out.println("Enter valid branch!");
                continue;
            }
            branchOK=true;
        }
        while(!branchOK);
    }
    public void showMenu() {
        chooseBranch();
        System.out.println(_logicParser.get_invParser().getUpdatesFromAllBranches());
        do {
            System.out.println(_invMain);
            input = _sc.nextLine();
            switch (input) {
                case "0": {
                    time = _logicParser.nextDay();
                    System.out.println(_logicParser.get_invParser().getUpdatesFromAllBranches());
                    initMenus();
                    break;
                }
                case "1": {
                    product.showSubMenu();
                    break;
                }
                case "2": {
                    showReportMenu();
                    break;
                }
                case "3": {
                    showDamagedMenu();
                    break;
                }
                case "4": {
                    showCategoriesMenu();
                    break;
                }
                case "5": {
                    showDiscountMenu();
                    break;
                }
                case "6": {
                    orders.showSubMenu();
                    break;
                }
                case "7": {
                    chooseBranch();
                    break;
                }
                case "8": {
                    long oID;
                    String ordersID= _logicParser.get_invParser().getPendingOrders();
                    if(!ordersID.equals("")){
                        System.out.println("Choose order to cancel from the pendings orders:\n"+ordersID +"\n");
                        try {
                            oID = Long.parseLong(_sc.nextLine());
                            orders.cancelOrder(oID);
                        }
                        catch(Exception e){

                        }
                    }
                    else{
                        System.out.println("Not pending orders.");
                    }

                    break;
                }
                case "9": {

                    break;
                }

                default: //invalid input
                    System.out.println("Invalid input");

            }
        } while (!input.equals("9"));
    }

    private void showCategoriesMenu() {
        String input;
        do {
            System.out.println(_invCategories);
            input = _sc.nextLine();
            switch (input) {
                case "1": {
                    System.out.println(_logicParser.get_invParser().getAllCategories());
                    break;
                }
                case "2": {
                    addCategory();
                    break;
                }
                case "3": {
                    System.out.println("Enter category id");
                    try {
                        int cID = Integer.parseInt(_sc.nextLine());
                        if (!_logicParser.get_invParser().removeCategory(cID)) {
                            System.out.println("Failed to remove category");
                        }
                        System.out.println("The category removed successfully");

                    } catch (Exception e) {
                        System.out.println("Category not found");
                    }

                    break;
                }
                case "4": {
                    System.out.println("Enter category id");
                    try {
                        int categoryID = Integer.parseInt(_sc.nextLine());
                        editCategory(categoryID);
                    } catch (Exception e) {
                        System.out.println("Category not found");
                    }
                    break;
                }
                case "5": {
                    break;
                }
                default:
                    System.out.println("Invalid input");

            }
        } while (!input.equals("5"));
    }

    private void showDamagedMenu() {
        String input;
        do {
            System.out.println(_invDamaged);
            input = _sc.nextLine();
            switch (input) {
                case "1": { //Report damaged
                    product.reportDefective("damaged");
                    break;
                }
                case "2": { //Back
                    System.out.println(_logicParser.get_invParser().getDefective());
                    break;
                }
                case "3": {
                    _logicParser.get_invParser().refreshExpiredProducts();
                    System.out.println("Updated successfully");
                    break;
                }
                case "4": {
                    break;
                }
                default: //invalid input
                    System.out.println("Invalid input");
            }
        } while (!input.equals("4"));
    }

    private void showDiscountMenu() {
        String input;
        do {
            System.out.println(_invDiscount);
            input = _sc.nextLine();
            switch (input) {
                case "1": {
                    product.addDiscountByPID();
                    break;
                }
                case "2": {
                    product.addDiscountByCategory();
                    break;
                }
                case "3": {
                    try {
                        product.removeProductDiscount();
                    } catch (Exception e) {
                        System.out.println("invalid parameter");
                        break;
                    }
                    break;
                }
                case "4": {
                    product.removeCategoryDiscount();
                    break;
                }
                case "5": {
                    break;
                }
                default: //invalid input
                    System.out.println("Invalid input");
            }
        } while (!input.equals("5"));
    }

    public void showReportMenu() {
        String input;
        do {
            System.out.println(_invReport);
            input = _sc.nextLine();
            switch (input) {
                case "1": { //Report damaged
                    System.out.println(_logicParser.get_invParser().getStock());
                    break;
                }
                case "2": {
                    reportByCategory();
                    break;
                }
                case "3": {
                    break;
                }
                default: //invalid input
                    System.out.println("Invalid input");

            }
        } while (!input.equals("3"));
    }

    private void reportByCategory() {
        int numOfCategory;
        int cID;
        try {
            System.out.print("Enter number of categories you want to see in the report: ");
            numOfCategory = Integer.parseInt(_sc.nextLine());
            List<Integer> categoryList = new ArrayList<>();
            for (int i = 0; i < numOfCategory; i++) {
                System.out.print("Insert Category id: ");
                cID = Integer.parseInt(_sc.nextLine());
                categoryList.add(cID);
            }
            System.out.println(_logicParser.get_invParser().reportByCategories(categoryList));

        } catch (Exception e) {
        }
    }

    private void addCategory() {
        int categoryID, parentID;
        String categoryName;
        try {
            System.out.print("Insert Category id: ");
            categoryID = Integer.parseInt(_sc.nextLine());
            System.out.print("Insert Category name: ");
            categoryName = _sc.nextLine();
            System.out.print("Insert parent category id: (0 if none)");
            parentID = Integer.parseInt(_sc.nextLine());
            if (_logicParser.get_invParser().addNewCategory(categoryID, categoryName, parentID))
                System.out.println("Category " + categoryName + " added successfully");
            else
                System.out.println("Something was wrong. try again.");
        } catch (Exception e) {
            System.out.println("Not valid parameters.");
        }
    }

    private void editCategory(int categoryID) {
        String newName;
        int newParentID;
        String category = _logicParser.get_invParser().getCategoryDetails(categoryID);
        if (category.equals("")) {
            System.out.println("Category not found");
            return;

        }
        System.out.println("Category Details:\n" + category);

        System.out.println("Enter new name: ");
        newName = _sc.nextLine();
        System.out.println("Enter new parent id: (0 for declare as Primary Category)");
        try {
            newParentID = Integer.parseInt(_sc.nextLine());
        } catch (Exception e) {
            System.out.println("Invalid input !");
            return;
        }
        if (!_logicParser.get_invParser().editCategory(categoryID, newName, newParentID)) {
            System.out.println("One or more parameter are invalid");
        } else {
            System.out.println("Category " + categoryID + " successfully edited");
        }

    }

    public String toString(){
        return "Inventory Menu";
    }

    public OrdersMenu getOrders(){
        return this.orders;
    }

}

