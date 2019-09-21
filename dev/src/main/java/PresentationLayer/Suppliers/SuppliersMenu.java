package PresentationLayer.Suppliers;

import BusinessLayer.Interfaces.ShowMenuAction;
import BusinessLayer.Parsers.LogicParser;

import java.util.Scanner;

// Avishai Yaniv    307916023
// Alex Abramov     314129438


public class SuppliersMenu implements ShowMenuAction {

    private final Scanner _sc;
    private String _suppMainMenu, _modifySupp, _showInfo;
    private SuppliersMenuParser _supplier;

    public SuppliersMenu(Scanner sc,LogicParser logicParser) {
        _sc = sc;
        _supplier = new SuppliersMenuParser(logicParser,_sc);
        initMenus();
    }

    /**
     * initializes menus
     */
    private void initMenus() {
        _suppMainMenu ="\n"+
                "1.\tAdd supplier\n"+
                "2.\tShow information \n" +
                "3.\tChange supplier details \n" +
                "4.\tExit\n";

        _modifySupp ="\n"+
                "Update supplier:"+"\n"
                +"1.\tChange bank account"+"\n"
                +"2.\tChange payment terms"+"\n"
                +"3.\tChange contact details"+"\n"
                +"4.\tAdd contract"+"\n"
                +"5.\tAdd contact"+"\n"
                +"6.\tRemove contact"+"\n"
                +"7.\tBack\n";
        _showInfo ="\n"+
                "Choose info to show:"+"\n"
                 +"1.\tShow suppliers"+"\n"
                 +"2.\tShow contacts"+"\n"
                 +"3.\tShow contracts"+"\n"
                 +"4.\tShow items in contract"+"\n"
                 +"5.\tShow quantity list agreement"+"\n"
                 +"6.\tBack\n";
    }

    /**
     * prints the main suppliers menu and uses functions to parse user's input
     */
    public void showMenu() {
        String input;
        do {
            System.out.println(_suppMainMenu);
            input = _sc.nextLine();
            switch (input) {
                case "1": {
                    _supplier.addSupplier();
                    break;
                }
                case "2": {
                    showInfo();
                    break;
                }
                case "3": {
                    modifySupplier();
                    break;
                }
                case "4": { //back
                    break;
                }
                default: //invalid input
                    System.out.println("Invalid input");
            }
        } while (!input.equals("4"));

    }

    /**
     * prints a menu of data chooses
     */
    public void showInfo() {
        String input;
        do {
            System.out.println(_showInfo);
            input = _sc.nextLine();
            switch (input) {
                case "1": {
                    _supplier.showAllSuppliers();
                    break;
                }
                case "2": {
                    _supplier.showContacts();
                    break;
                }
                case "3": {
                    _supplier.showContracts();
                    break;
                }
                case "4": {
                    _supplier.ShowItemsInContract();
                    break;
                }
                case "5": {
                    _supplier.ShowQuantityListInContract();
                    break;
                }
                case "6": {
                    break;
                }
                default: //invalid input
                    System.out.println("Invalid input");

            }
        } while (!input.equals("6"));

    }

    /**
     * prints a menu of supplier's modifications and uses the appropriate functions
     */
    public void modifySupplier() {
        String input;
        do {
            System.out.println(_modifySupp);
            input = _sc.nextLine();
            switch (input) {
                case "1": {
                    _supplier.changeBankAccount();
                    break;
                }
                case "2": {
                    _supplier.changePaymentTerms();
                    break;
                }
                case "3": {
                    _supplier.changeContactDetails();
                    break;
                }
                case "4": {
                    _supplier.addContract();
                    break;
                }
                case "5": {
                    _supplier.addContact();
                    break;
                }
                case "6": {
                    _supplier.removeContact();
                    break;
                }
                case "7" :{
                    break;
                }
                default: //invalid input
                    System.out.println("Invalid input");

            }
        } while (!input.equals("7"));
    }
    public String toString(){
        return "Supplier Menu";
    }
}
