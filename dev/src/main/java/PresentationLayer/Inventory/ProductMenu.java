package PresentationLayer.Inventory;

import BusinessLayer.Inventory.minimumAlert;
import BusinessLayer.Parsers.LogicParser;

import java.sql.Date;
import java.util.Scanner;

/**
 * For the operation of new user in the system
 */
public class ProductMenu{

    private Scanner sc;
    private BusinessLayer.Parsers.LogicParser logicParser;
    private String _prodMenu, _prodEdit;

    /**
     * constructor
     *
     * @param logicParser- for prevent addition instance of this
     */
    //region Ctor
    public ProductMenu(LogicParser logicParser) {
        this.logicParser = logicParser;
        this.sc = new Scanner(System.in);
        initMenu();
    }
    //endregion

    //region Init
    private void initMenu() {
        _prodMenu = "\n" +
                "1.\tAdd a Product to the current stock\n" +
                "2.\tDefine new Product in the Stock system \n" +
                "3.\tRemove a specific Product from the system \n" +
                "4.\tRemove all Product records from the system  \n" +
                "5.\tUpdate Product minimum amount  \n" +
                "6.\tEdit product details \n" +
                "7.\tBack\n";

        _prodEdit = "\n" +
                "1.\tUpdate Product name  \n" +
                "2.\tUpdate selling price \n" +
                "3.\tUpdate Product category \n" +
                "4.\tChange Product location \n" +
                "5.\tBack\n";
    }
    //endregion

    //region Sub-Menu

    public void showSubMenu() {
        String input;
        do {
            System.out.println(_prodMenu);
            input = sc.nextLine();
            switch (input) {
                case "1": {
                    addProduct();
                    break;
                }
                case "2": {
                    addProductToSystem();
                    break;
                }
                case "3": {
                    removeProductInstance();
                    break;
                }
                case "4": {
                    removeAllProductRecords();
                    break;
                }
                case "5": {
                    setMinimumValue();
                    break;
                }
                case "6": {
                    int pID;
                    System.out.print("Insert product id: ");
                    try {
                        pID = Integer.parseInt(sc.nextLine());
                        showEditMenu(pID);
                    } catch (Exception e) {
                        System.out.println("ProductMenu id is a number !");
                        break;
                    }
                    break;
                }
                case "7": {
                    break;
                }
                default: //invalid input
                    System.out.println("Invalid input");

            }
        } while (!input.equals("7"));
    }

    public void showEditMenu(int pID) {
        String input;
        do {
            System.out.println(_prodEdit);
            input = sc.nextLine();
            switch (input) {
                case "1": {
                    changeProductName(pID);
                    break;
                }
                case "2": {
                    changeSellingPrice(pID);
                    break;
                }
                case "3": { // Print report
                    changeProductCategory(pID);
                    break;
                }
                case "4": { // Print report
                    changeProductLocation(pID);
                    break;
                }
                case "5": { // Print report
                    break;
                }
                default: //invalid input
                    System.out.println("Invalid input");
            }
        } while (!input.equals("5"));
    }


    private void setMinimumValue() {
        int pID, minimum;

        System.out.print("Insert product id: ");
        pID = Integer.parseInt(sc.nextLine());
        System.out.print("Insert Minimum Value to store: ");
        minimum = Integer.parseInt(sc.nextLine());
        if (logicParser.get_invParser().setMinimum(pID, minimum)) System.out.println("Minimum Set.");
        else System.out.println("Check the pID");
    }


//endregion

    //region Edit menu
    private void changeProductName(int pID) {
        String name;
        System.out.print("Insert new name: ");
        name = sc.nextLine();
        if (logicParser.get_invParser().changeProductName(pID, name))
            System.out.println("ProductMenu Name Changed to: " + name);
        else System.out.println("Couldn't change the name, make sure you provide a valid ProductMenu ID");
    }

    private void changeSellingPrice(int pID) {
        double newPrice;
        System.out.print("Insert new price: ");
        newPrice = Double.parseDouble(sc.nextLine());
        if (logicParser.get_invParser().changeProductSellingPrice(pID, newPrice))
            System.out.println("ProductMenu price changed to: " + newPrice);
        else System.out.println("Couldn't change the price. make sure you input non-negative prices only");
    }

    private void changeProductLocation(int pID) {
        String newLocation;
        System.out.print("Insert new location at shop: ");
        newLocation = sc.nextLine();
        if (logicParser.get_invParser().changeProductLocation(pID, newLocation))
            System.out.println("ProductMenu lcation changed to: " + newLocation);
        else System.out.println("Couldn't change the location.");

    }

    private void changeProductCategory(int pID) {
        int newCategory;
        System.out.print("Insert new Category id: ");
        newCategory = Integer.parseInt(sc.nextLine());
        if (logicParser.get_invParser().changeProductCategory(pID, newCategory))
            System.out.println("ProductMenu price changed to: " + newCategory);
        else System.out.println("Couldn't change the category. ");

    }
    //endregion

    //region ProductMenu menu- user inputs


    private void removeProductInstance() {
        boolean successRemove;
        int pPID;
        System.out.print("Insert product id: ");
        try {
            pPID = Integer.parseInt(sc.nextLine());

        } catch (Exception e) {
            System.out.println("Serial Number must include digits 0-9 only.");
            return;
        }
        if (logicParser.get_invParser().removeProductInstance(pPID)) {
            System.out.println("Item Removed!");
        }
    }

    private void removeAllProductRecords() {
        boolean successRemove;
        int pID;
        System.out.print("Insert ProductMenu ID: ");
        try {
            pID = Integer.parseInt(sc.nextLine());

        } catch (Exception e) {
            System.out.println("Serial Number must include digits 0-9 only.");
            return;
        }
        if (logicParser.get_invParser().removeProdcutAndSubRecords(pID)) {
            System.out.println("Item Removed!");
        }
    }

    public void reportDefective(String validity) {
        boolean successAdd;
        long sn;
        System.out.print("Insert product SN: ");
        try {
            sn = Long.parseLong(sc.nextLine());
        } catch (Exception e) {
            System.out.println("valid sn is a number only");
            return;
        }
        if (!logicParser.get_invParser().reportDefective(sn)) {
            System.out.println("Wrong sn");
        } else {
            System.out.println("Thank you for the report!");
        }
    }

    public void addProduct() {
        try {
            boolean successAdd;
            int pID;
            double sn;
            double bPrice;
            Date expDate=null;
            String date;
            System.out.print("Insert product id: ");
            pID = Integer.parseInt(sc.nextLine());
            System.out.print("Insert serial number: ");
            sn = Double.parseDouble(sc.nextLine());
            System.out.print("Insert buying price: ");
            bPrice = Double.parseDouble(sc.nextLine());
            System.out.print("Insert Expiration date: YYYY-MM-DD (Enter if none)");
            date = sc.nextLine();
            if(!date.isEmpty())
                expDate= Date.valueOf(date);

            successAdd = logicParser.get_invParser().addProductInstance(pID, sn, bPrice, expDate,logicParser.get_invParser().getBranch());
            if (successAdd) {
                System.out.println("item id " + pID + " has been successfully added.");
            } else {
                System.out.println("adding product attempt failed.");
            }
        } catch (Exception e) {
            System.out.println("Enter valid parameters!");
        }

    }

    public void addProductToSystem() {
        boolean successAdd;
        int pID, pCatID;
        double sellingPrice;
        String location;
        String pName, manufacturer;
        try {
            System.out.print("Insert product id: ");
            pID = Integer.parseInt(sc.nextLine());
            System.out.print("Insert product name: ");
            pName = sc.nextLine();
            System.out.print("Insert Category id: ");
            pCatID = Integer.parseInt(sc.nextLine());
            System.out.print("Insert manufacturer name: ");
            manufacturer = sc.nextLine();
            System.out.print("Insert product selling price: ");
            sellingPrice = Double.parseDouble(sc.nextLine());
            System.out.print("Insert location: ");
            location = sc.nextLine();
        } catch (Exception e) {
            System.out.println("Enter valid data!");
            return;
        }
        if (logicParser.getProduct(pID) != null) {
            System.out.println("There is already a ProductMenu registered with the same ProductMenu ID, please use another one. ");
            return;
        }
        successAdd = logicParser.get_invParser().addProduct(pID, pName, pCatID, manufacturer, sellingPrice,location);
        if (successAdd) {
            System.out.println("Pruduct:  " + pName + " ID:" + pID + " has been successfully added.");
        } else {
            System.out.println("adding product attempt failed.");
        }
    }


    public void belowMinimum(String name, int current, int minimum) {
        System.out.println("\n***WARNING***");
        System.out.println("Prodect : " + name + " is Below the Minumum amount to store\n" +
                "Current Amount: " + current + "\nMinimum Amount: " + minimum);
        System.out.println("***WARNING***\n");
    }

    public void addDiscountByCategory() {
        try {
            int cID, discount;
            Date startTime, endTime;
            System.out.print("Insert category id: ");
            cID = Integer.parseInt(sc.nextLine());
            System.out.print("Insert Discount in values from 0-100%: ");
            discount = Integer.parseInt(sc.nextLine());
            System.out.print("Enter start date: In format YYYY-MM-DD ONLY ");
            startTime = Date.valueOf(sc.nextLine());
            System.out.print("Enter end date: In format YYYY-MM-DD ONLY ");
            endTime = Date.valueOf(sc.nextLine());
            if (logicParser.get_invParser().addDiscountByCategory(cID, discount, startTime, endTime))
                System.out.println("Discount Added");
            else System.out.println("An Error giving a discount, check the ProductMenu ID.");
        } catch (Exception e) {
            System.out.println("One or more parameters are invalid.");
        }
    }

    public void addDiscountByPID() {
        try {
            int pID, discount;
            Date startTime, endTime;
            System.out.print("Insert product id: ");
            pID = Integer.parseInt(sc.nextLine());
            System.out.print("Insert Discount in values from 0-100%: ");
            discount = Integer.parseInt(sc.nextLine());
            System.out.print("Enter start date: In format YYYY-MM-DD ONLY ");
            startTime = Date.valueOf(sc.nextLine());
            System.out.print("Enter end date: In format YYYY-MM-DD ONLY ");
            endTime = Date.valueOf(sc.nextLine());
            if (logicParser.get_invParser().addDiscountByPID(pID, discount, startTime, endTime))
                System.out.println("Discount Added");
            else System.out.println("An Error giving a discount, check the ProductMenu ID.");
        } catch (Exception e) {
            System.out.println("One or more parameters are invalid.");
        }
    }

    public void removeProductDiscount() {
        try {
            System.out.print("Insert product id: ");
            int pID = Integer.parseInt(sc.nextLine());
            if (logicParser.get_invParser().removeProductDiscount(pID)) System.out.println("Discount removed");
            else System.out.println("An Error removing a discount, check the product ID.");
        } catch (Exception e) {
            System.out.println("Invalid parameters");
        }

    }

    public void removeCategoryDiscount() {
        try {
            System.out.print("Insert category id: ");
            int cID = Integer.parseInt(sc.nextLine());
            if (logicParser.get_invParser().removeCategoryDiscount(cID)) System.out.println("Discount removed");
            else System.out.println("An Error removing a discount, check the product ID.");
        } catch (Exception e) {
            System.out.println("Invalid parameters");
        }
    }

    //endregion

}
