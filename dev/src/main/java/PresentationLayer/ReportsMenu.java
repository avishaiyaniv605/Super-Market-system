package PresentationLayer;

import BusinessLayer.Interfaces.ShowMenuAction;
import BusinessLayer.Parsers.LogicParser;
import PresentationLayer.EmployeeModule.MenuEmployee;
import PresentationLayer.Inventory.InventoryMenu;
import PresentationLayer.Suppliers.SuppliersMenu;

import java.util.Scanner;

public class ReportsMenu implements ShowMenuAction {

    private SuppliersMenu suppliersMenu;
    private String input;
    private Scanner sc;
    private String menu;
    private InventoryMenu inventoryMenu;
    private MenuEmployee menuEmployee;

    public ReportsMenu(Scanner sc, SuppliersMenu suppliersMenu, InventoryMenu inventoryMenu,
                       MenuEmployee menuEmployee){
            this.sc = sc;
            this.suppliersMenu = suppliersMenu;
            this.inventoryMenu = inventoryMenu;
            this.menuEmployee = menuEmployee;
            initMenu();
    }

    public void initMenu(){
        menu = "1.\t Show suppliers Info\n"+
                "2.\t Show inventory reports\n"+
                "3.\t Display all periodical orders\n"+
                "4.\t Display order history\n"+
                "5.\t History of shifts\n"+
                "6.\t View specific shift\n"+
                "7.\t back\n";
    }

    @Override
    public void showMenu() {
        do {
            System.out.println(this.menu);
            input = sc.nextLine();
            switch (input) {
                case "1": {
                    this.suppliersMenu.showInfo();
                    break;
                }
                case "2": {
                    this.inventoryMenu.showReportMenu();
                    break;
                }
                case "3": {
                    this.inventoryMenu.getOrders().showAllTheProductsPeriodical();
                    break;
                }
                case "4": {
                   this.inventoryMenu.getOrders().displayOrderHistory();
                    break;
                }
                case "5": {
                    this.menuEmployee.printHistoreyShifts();
                    break;
                }
                case "6": {
                    this.menuEmployee.viewShift();
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
    public String toString(){
        return "Reports Menu";
    }
}
