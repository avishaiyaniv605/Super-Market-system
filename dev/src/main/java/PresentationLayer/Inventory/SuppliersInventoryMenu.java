package PresentationLayer.Inventory;

import BusinessLayer.Parsers.LogicParser;
import PersistenceLayer.DatabaseManager;
import PersistenceLayer.Product;
import PersistenceLayer.Suppliers.Supplier;
import PresentationLayer.Inventory.InventoryMenu;
import PresentationLayer.Menu;
import PresentationLayer.Suppliers.SuppliersMenu;
import PresentationLayer.Suppliers.SuppliersMenuParser;

import java.util.Scanner;

public class SuppliersInventoryMenu {

    private final Scanner _sc;
    private String _menu, time;
    private LogicParser _logicParser;
    private SuppliersMenu _sMenu;
    private InventoryMenu _iMenu;


    public SuppliersInventoryMenu() {

        _sc = new Scanner(System.in);
        _logicParser = new LogicParser();
        time = _logicParser.getTime();
        _sMenu = new SuppliersMenu(_sc, _logicParser);
        _iMenu = new InventoryMenu(_sc, _logicParser);
        initMenus();

    }

    /**
     * initializes menus
     */
    private void initMenus() {
        _menu = "Welcome!!! the date is:" + time + "\n" +
                "1.\tSuppliers Menu\n" +
                "2.\tInventory Menu \n" +
                "3.\tNext day...\n" +
                "4.\tChoose branch \n" +
                "5.\tExit \n";
    }

    /**
     * prints the main suppliers menu and uses functions to parse user's input
     */
    public void showMenu() {
        chooseBranch();
        System.out.println(_logicParser.get_invParser().getUpdatesFromAllBranches());
        String input;
        do {
            initMenus();
            System.out.println(_menu);
            input = _sc.nextLine();
            switch (input) {
                case "1": {
                    _sMenu.showMenu();
                    break;
                }
                case "2": {

                    _iMenu.showMenu();
                    break;
                }
                case "3": {

                    time = _logicParser.nextDay();
                    System.out.println(_logicParser.get_invParser().getUpdatesFromAllBranches());
                    break;
                }
                case "4": {
                    chooseBranch();
                    break;
                }
                case "5": {
                    _logicParser.saveDate();
                    DatabaseManager.getInstance().closeSession();
                    break;
                }
                default: //invalid input
                    System.out.println("Invalid input");
            }
        } while (!input.equals("5"));
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

}
