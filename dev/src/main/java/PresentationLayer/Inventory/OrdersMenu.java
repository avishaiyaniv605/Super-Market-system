package PresentationLayer.Inventory;

import BusinessLayer.EmployeeModule.EmployeeJobs;
import BusinessLayer.EmployeeModule.EmployeeProfile;
import BusinessLayer.Inventory.minimumAlert;
import BusinessLayer.Parsers.LogicParser;
import PersistenceLayer.BranchProfile;
import PersistenceLayer.Inventory.Orders.OrderItem;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class OrdersMenu implements minimumAlert {
    private Scanner sc;
    private LogicParser logicParser;
    private String _invPeriodical, _orderEdit;

    /**
     * constructor
     *
     * @param logicParser- for prevent addition instance of this
     */
    //region Ctor
    public OrdersMenu(LogicParser logicParser) {
        this.logicParser = logicParser;
        this.sc = new Scanner(System.in);

        initMenu();
        logicParser.get_invParser().setAlert(this);
    }

    //endregion
    //region Init
    private void initMenu() {
        _invPeriodical = "\n" +
                "Welcome to Periodical orders menu" + "\n"
                + "1.\tDisplay all periodical orders" + "\n"
                + "2.\tAdd new periodical order" + "\n"
                + "3.\tEdit periodical order" + "\n"
                + "4.\tRemove periodical order" + "\n" +
                "5.\tDisplay orders history\n" +
                "6.\tBack\n";

        _orderEdit = "\n" +
                "1.\tUpdate order date  \n" +
                "2.\tChange order period \n" +
                "3.\tUpdate product amount\n" +
                "4.\tRemove product from order \n" +
                "5.\tCancel order\n" +
                "6.\tBack\n";
    }

    public void showSubMenu() {
        String input;
        do {
            System.out.println(_invPeriodical);
            input = sc.nextLine();
            switch (input) {
                case "1": {
                    showAllTheProductsPeriodical();
                    break;
                }
                case "2": {
                    addOrder();
                    break;
                }
                case "3": {
                    long oID;
                    System.out.print("Insert order id: ");
                    try {
                        oID = Long.parseLong(sc.nextLine());
                        boolean exists = logicParser.get_invParser().getPerdOrder(oID);
                        if (!exists) {
                            System.out.println("The order id not exists");
                            break;
                        }
                        if (logicParser.get_invParser().canEditPOrder(oID))
                            showEditMenu(oID);
                        else {
                            System.out.println("You can't edit the order in the last 24  hours!");
                        }
                    } catch (Exception e) {
                        System.out.println("order id is a number !");
                        break;
                    }
                    break;
                }
                case "4": {
                    long oID;
                    System.out.print("Insert order id: ");
                    try {
                        oID = Long.parseLong(sc.nextLine());
                        boolean exists = logicParser.get_invParser().getPerdOrder(oID);
                        if (!exists) {
                            System.out.println("The order id not exists");
                            break;
                        }
                        if (logicParser.get_invParser().removePeriodical(oID))
                            System.out.println("Deleted succsesfully");
                        else
                            System.out.println("Enter valid order id");
                        break;
                    } catch (Exception e) {
                        System.out.println("order id is a number !");
                        break;
                    }
                }
                case "5": {
                        displayOrderHistory();
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


    private void removeOrder() {

    }

    public void showAllTheProductsPeriodical(){
        System.out.println(logicParser.get_invParser().displayAllPeriodical());
    }
    public void displayOrderHistory(){
        System.out.println(logicParser.get_invParser().displayOrderHistory());
    }
    public void showEditMenu(long oID) {
        String input;
        do {
            System.out.println(_orderEdit);
            input = sc.nextLine();
            switch (input) {
                case "1": {
                    changeorderDate(oID);
                    break;
                }
                case "2": {
                    changePeriod(oID);
                    break;
                }
                case "3": {
                    updateAmount(oID);
                    break;
                }
                case "4": {
                    removeProdFromOrder(oID);
                    break;
                }
                case "5": {
                    cancelOrder(oID);
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


    private void removeProdFromOrder(long oID) {
        try {
            int pid;
            System.out.print("Insert product ID: ");
            pid = Integer.parseInt(sc.nextLine());
            if (logicParser.get_invParser().removeProductPerdOrder(oID, pid))
                System.out.println("Product " + pid + " removed from order " + oID);
            else System.out.println("make sure you provide a valid parameters!");

        } catch (Exception e) {
            System.out.println("Not a valid input!");
        }
    }


    public void cancelOrder(long oID) {
        try {
            List<EmployeeJobs> employeeJobsList = this.logicParser.getEmployeeParser().getEmployeeJobs
                    (this.logicParser.getEmployeeParser().getLoggedInEmployee().getID());
            EmployeeProfile employeeProfile= this.logicParser.getEmployeeParser().getEmployeeProfile(this.logicParser.getEmployeeParser().getLoggedInEmployee().getID());
            logicParser.get_invParser().cancelOrder(oID,employeeJobsList,employeeProfile);
            System.out.println(logicParser.get_invParser().getOrderCancelStatus(oID));

        } catch (Exception e) {
            System.out.println("Not a valid input!");
        }
    }

    private void updateAmount(long oID) {
        try {
            int pid, amount;
            System.out.print("Insert product ID: ");
            pid = Integer.parseInt(sc.nextLine());
            System.out.print("Insert new amount: ");
            amount = Integer.parseInt(sc.nextLine());
            if (logicParser.get_invParser().changePAmount(oID, pid, amount))
                System.out.println("Product " + pid + " amount changed to " + amount);
            else System.out.println("Couldn't change the amount, make sure you provide a valid parameters!");

        } catch (Exception e) {
            System.out.println("Not a valid input!");
        }
    }

    private void changePeriod(long oID) {
        try {
            int period;
            System.out.print("Insert new period: ");
            period = Integer.parseInt(sc.nextLine());
            if (logicParser.get_invParser().changeOrderPeriod(oID, period))
                System.out.println("Orders period Changed to: " + period);
            else System.out.println("Couldn't change the period, make sure you provide a valid period");

        } catch (Exception e) {
            System.out.println("Not a valid input! period is numbers represenet days.");
        }
    }

    private void changeorderDate(long oID) {
        String date;
        Date startDate = null;
        System.out.print("Insert new order date: YYYY-MM-DD");
        date = sc.nextLine();
        if (!date.isEmpty())
            startDate = Date.valueOf(date);
        if (logicParser.get_invParser().changeOrderDate(oID, startDate))
            System.out.println("Orders date Changed to: " + startDate.toString());
        else System.out.println("Couldn't change the date, make sure you provide a valid date");
    }

    private void addOrder() {
        try {
            boolean successAdd;
            int period, pid = 0, amount;
            Date startDate = null;
            String date;
            HashMap<Integer, Integer> orders = new HashMap<>();
            System.out.print("Insert first order date: YYYY-MM-DD");
            date = sc.nextLine();
            if (!date.isEmpty())
                startDate = Date.valueOf(date);
            System.out.print("Insert period: (days) ");
            period = Integer.parseInt(sc.nextLine());
            System.out.print("Insert products to the order! enter -1 when you done ");
            while (pid != -1) {
                System.out.print("Insert Product id: ");
                pid = Integer.parseInt(sc.nextLine());
                if (pid == -1)
                    continue;
                System.out.print("Insert amount: ");
                amount = Integer.parseInt(sc.nextLine());
                orders.put(pid, amount);
            }

            successAdd = logicParser.get_invParser().addPeriodicalOrder(startDate, period, orders);
            if (successAdd) {
                System.out.println("new periodical order at " + startDate.toString() + " has been successfully added.");
            } else {
                System.out.println("adding new order attempt failed.");
            }
        } catch (Exception e) {
            System.out.println("Enter valid parameters!");
        }
    }

    @Override
    public boolean placeOrder(List<OrderItem> items, BranchProfile branch) {
        if (items.size() > 0)
            System.out.println("Minimum has reached to product: " + items.get(0).getpID().getpID().getName());

        return true;
    }
}
