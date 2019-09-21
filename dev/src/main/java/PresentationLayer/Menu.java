package PresentationLayer;

import BusinessLayer.EmployeeModule.EmployeeJobs;
import BusinessLayer.Enums.EnumTypeJob;
import BusinessLayer.Interfaces.ShowMenuAction;
import BusinessLayer.Parsers.LogicParser;
import PersistenceLayer.Inventory.Orders.Orders;
import PresentationLayer.DeliveryModule.DeliveryMenu;
import PresentationLayer.EmployeeModule.MenuEmployee;
import PresentationLayer.Inventory.InventoryMenu;
import PresentationLayer.Suppliers.SuppliersMenu;

import java.util.*;

/**
 * The main menu of the program.
 * From here the program start
 */
public class Menu {

    //scanner
    private Scanner sc = new Scanner(System.in);
    private LogicParser logicParser;
    private MenuEmployee menuEmployee;
    private DeliveryMenu deliveryMenu;
    private SuppliersMenu suppliersMenu;
    private InventoryMenu inventoryMenu;
    private ReportsMenu reportsMenu;
    private String input;
    private String startMenu;
    private String menuLogin;
    private Map <Integer,ShowMenuAction> menuAllow;
    private Map<ShowMenuAction, Boolean> arrayAllMenus;
    /**
     * Constructor
     */
    public Menu() {

        this.logicParser = new LogicParser();
        this.sc= new Scanner(System.in);

        this.menuEmployee=new MenuEmployee(getSc(),getLogicParser());
        this.deliveryMenu = new DeliveryMenu(getSc(), getLogicParser());
        this.suppliersMenu = new SuppliersMenu(getSc(), getLogicParser());
        this.inventoryMenu = new InventoryMenu(getSc(),getLogicParser());
        this.reportsMenu = new ReportsMenu(getSc(),suppliersMenu, inventoryMenu,menuEmployee);

        initMenu();

    }

    private void initMenu(){
        arrayAllMenus = new HashMap<>();
        restArrayAllMenus();

        this.menuLogin = "1. \tLogin\n" +
                "2. \tExit\n";

    }
    /**
     * start the menu login;
     */
    public void start(){
        do {
            System.out.println(menuLogin);
            input = sc.nextLine();
            switch (input) {
                case "1": {
                    login(); //login
                    input = "1";
                    break;
                }
                case "2": {
                    break;
                }
                default:
                    System.out.println("Invalid input");

            }
        } while (!input.equals("2"));
    }

    //region <Login>
    /**
     * login the user to the system
     */
    private void login() {
        boolean successLogin;
        String id;

        //ID input
        System.out.println("\nEnter ID: ");
        id = sc.nextLine();

        //Checks the validity of the input
        successLogin = this.logicParser.getEmployeeParser().login(id);

        if (successLogin) {  // If the input is valid, shows the menu

            userSucLogin();
        }
        else {
            System.out.println("Invalid ID.");
        }
    }

    public void userSucLogin(){
        resetMenu();
        int counter = 1 ;
        menuAllow.put(counter, this.menuEmployee);

        // check permissions
        List<EmployeeJobs> employeeJobsList = this.logicParser.getEmployeeParser().getEmployeeJobs
                (this.logicParser.getEmployeeParser().getLoggedInEmployee().getID());

        for (EmployeeJobs e : employeeJobsList) {
            String s =e.getJob();
            EnumTypeJob curJob = EnumTypeJob.getEnum(e.getJob());
            counter = checkPermissions(curJob,counter);
        }

        //add logout
        counter++;
        menuAllow.put(counter,new ShowMenuAction() {
            @Override
            public void showMenu() {
                logOut();
            }
            public String toString(){
                return "logout";
            }
        } );

        //init the menu
        initStartMenu(counter);

        //show the menus to user
        showMenus(counter);
    }

    /**
     * reset menuAllow and arrayAllMenus for new user login
     */
    private void resetMenu() {
        this.menuAllow = new Hashtable<>();
        restArrayAllMenus();
    }

    /**
     * reset the arrayAllMenus
     */
    private void restArrayAllMenus(){
        arrayAllMenus.put(this.menuEmployee,false);
        arrayAllMenus.put(this.deliveryMenu,false);
        arrayAllMenus.put(this.suppliersMenu,false);
        arrayAllMenus.put(this.inventoryMenu,false);
        arrayAllMenus.put(this.reportsMenu,false);
    }

    /**
     * initial the start menu with all the menus that decide the user can see
     * @param counter
     */
    private void initStartMenu(int counter) {
        this.startMenu = "";
        for(int i=1;i<=counter;i++){
            this.startMenu =this.startMenu+ i+")\t"+this.menuAllow.get(i).toString()+"\n";
        }
    }

    /**
     * check Permission for specific job. if to job have permission see specific menu,
     * the menu will be add, the counter grow in 1.
     * this function prevent add of the the same menu than one time.
     * @param curJob
     * @param counter
     * @return
     */
    private int checkPermissions(EnumTypeJob curJob,int counter) {
        switch (curJob){

            case shiftManager:{

                return counter;
            }
            case storekeeper:{

                if(!this.arrayAllMenus.get(this.suppliersMenu) ){
                    counter++;
                    this.menuAllow.put( counter, this.suppliersMenu);
                    this.arrayAllMenus.put(this.suppliersMenu,true);
                }
                if(!this.arrayAllMenus.get(this.inventoryMenu) ){
                    counter++;
                    this.menuAllow.put( counter, this.inventoryMenu);
                    this.arrayAllMenus.put(this.inventoryMenu,true);
                }
                return  counter;

            }
            case cashier:{

                return counter;

            } case driver:{

                return counter;

            } case logisticsManager:{

                if(!this.arrayAllMenus.get(this.deliveryMenu) ){
                    counter++;
                    this.menuAllow.put( counter, this.deliveryMenu);
                    this.arrayAllMenus.put(this.deliveryMenu,true);
                }
                return counter;

            } case storeManger:{

                if(!this.arrayAllMenus.get(this.suppliersMenu) ){
                    counter++;
                    this.menuAllow.put( counter, this.suppliersMenu);
                    this.arrayAllMenus.put(this.suppliersMenu,true);
                }
                if(!this.arrayAllMenus.get(this.reportsMenu) ){
                    counter++;
                    this.menuAllow.put( counter, this.reportsMenu);
                    this.arrayAllMenus.put(this.reportsMenu,true);
                }
                return counter;

            } default:{

                return counter;

            }
        }
    }

    //endregion

    /**
     * the function show all the menu the user login can see
     * @param size
     */
    public void showMenus(int size){
        confirmCancelOrder();
        int choose = 0;
        do {

            System.out.println(startMenu);
            if(checkIsInteger(input = sc.nextLine()) && ( choose  = Integer.parseInt(input)) <= size && choose>=1) {
                ShowMenuAction menuChoose = this.menuAllow.get(choose);
                if(menuChoose != null){
                    menuChoose.showMenu();
                }
            }
            else{
                System.out.println("Invalid input");
            }

        } while (!(choose == size));
    }

    private void confirmCancelOrder() {
        List<EmployeeJobs> employeeJobsList = this.logicParser.getEmployeeParser().getEmployeeJobs
                (this.logicParser.getEmployeeParser().getLoggedInEmployee().getID());
        List<String> jobs= new ArrayList<>();
        for(EmployeeJobs e:employeeJobsList){
            jobs.add(e.getJob());
        }
        List<Orders> waiting= null;
        if(jobs.contains("Storekeeper")){
            waiting= logicParser.get_invParser().getWaitingToCancel("Storekeeper");
            if(waiting!=null&&waiting.size()!=0) System.out.println("Orders cancel waiting for your confirm. do it in inventory menu.");
        }
        else if(jobs.contains("Logistics Manager")) {
            waiting = logicParser.get_invParser().getWaitingToCancel("Logistics Manager");
            if (waiting != null &&waiting.size()!=0) System.out.println("Orders cancel waiting for your confirm. do it in delivery menu.");
        }
        else if(jobs.contains("Shift Manager")) {
            waiting = logicParser.get_invParser().getWaitingToCancel("Shift Manager");
            if(waiting!=null &&waiting.size()!=0) System.out.println("Orders cancel waiting for your confirm. do it in employee menu.");
        }


        if(waiting==null) return;

        for(Orders o:waiting){
            System.out.println("Order id: "+ o.getOrderID() + " waiting to cancel from you.\n");
        }

    }

    /**
     * check is Integer
     * @param str
     * @return
     */
    public boolean checkIsInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
            // is an integer!
        } catch (NumberFormatException e) {
            // not an integer!
            return false;
        }
    }

    public void close() {
        logicParser.close();
        sc.close();
    }

    public Scanner getSc() {
        return sc;
    }

    public LogicParser getLogicParser() {
        return logicParser;
    }

    /**
     * we call this function from the last option in the allowMenu map.
     */
    private  void logOut(){
        if(this.logicParser.getEmployeeParser().logout()){
            System.out.println("You are disconnected");
        }
        else{
            System.out.println("You can not log out");
        }
    }

    /**
     * The program start from here
     * @param args
     */
    public static void main(String[] args){

        //the menu for all module
        Menu menu = new Menu();

        //The program is start
        menu.start();

        //close all
        menu.close();
    }

}
