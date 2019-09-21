package PresentationLayer.EmployeeModule;
import BusinessLayer.Enums.License;
import BusinessLayer.Enums.EnumTypeJob;
import BusinessLayer.Enums.EnumValid;
import BusinessLayer.Interfaces.ShowMenuAction;
import BusinessLayer.Parsers.LogicParser;
import PresentationLayer.Inventory.OrdersMenu;

import java.util.Scanner;

@SuppressWarnings("Duplicates")
public class MenuEmployee implements ShowMenuAction {
    /**
     * if the user type succeed to login he passes to here
     */
    private String _loginMenuReg, _loginMenuManager,editMenuAnotherUser;

    private RegisterEmployee register;
    private ShiftMenu shiftMenu;
    private LogicParser logicParser;
    private boolean isManger;
    private String input;
    private Scanner sc;

////region <Initail>


    /**
     * constructor
     * @param logicParser - accessing the data base. handling the logic of the system.
     */
    public MenuEmployee(Scanner scanner, LogicParser logicParser){

        //for logic and input
        sc = scanner;
        this.logicParser = logicParser;

        //initial the string of menus
        initMenus();
        register = new RegisterEmployee(this.logicParser);
        shiftMenu = new ShiftMenu(this.logicParser, sc);
    }


    private void initMenus() {
        _loginMenuReg= "\n" +
                "1.\t Change personal details \n"+
                "2.\t Show personal details \n" +
                "3.\t Back";

        _loginMenuManager ="\n" +
                "1.\t Change personal details \n"
                + "2.\t Show personal details" + "\n"
                + "3.\t Change another employees's personal details" + "\n"
                + "4.\t Show another employees's personal details" + "\n"
                + "5.\t Remove an employee" + "\n"
                + "6.\t Register a new employee" +"\n"
                + "7.\t Create a shift" +"\n"
                + "8.\t Update a shift" +"\n"
                + "9.\t History Of Shifts" +"\n"
                + "10.\t View a shift" + "\n"
                 +"11.\tCancel Order\n"
                + "12.\t Back";
    }

    /**
     * check if the user is manager
     * @return
     */
    private boolean checkIsManager() {
        return this.logicParser.getEmployeeParser().checkIsManager(this.logicParser.getEmployeeParser().getLoggedInEmployee().getID());
    }

    /**
     * starts the login menu
     */
    public void showMenu(){
        isManger = checkIsManager();
        if(isManger)
            managerMenu();
        else
            regularEmployeeMenu();
    }
//endregion


//region <Menu manger and regular>
    /**
     *  manager's menu
     */
    private void managerMenu() {
        do {
            System.out.println(_loginMenuManager);
            input = sc.nextLine();
            switch (input) {
                case "1": { // change personal details
                    changeDetails(this.logicParser.getEmployeeParser().getLoggedInEmployee().getID(),"your");
                    input="1";
                    break;
                }
                case "2": { // show personal details
                    showPersonalDetails(this.logicParser.getEmployeeParser().getLoggedInEmployee().getID());
                    break;
                }
                case "3": { // change another's personal details
                    changeOtherPersonalDetails();
                    break;
                }
                case "4": { // show another's personal details
                    showOtherPersonalDetails();
                    break;
                }
                case "5": { // remove an employee from DB
                    removeEmployee();
                    break;
                }
                case "6": { // register a new employee
                    registerNewEmployee();
                    break;
                }
                case "7": { // Create a shift
                    createShift();
                    break;
                }
                case "8": { // Update a shift
                    updateShift();
                    break;
                }
                case "9": { // history of shifts
                    printHistoreyShifts();
                    break;
                }
                case "10": { // view a shift
                    viewShift();
                    break;
                }
                case "11": { // cancel order
                    long oID;
                    String ordersID= logicParser.get_invParser().getPendingOrders();
                    if(!ordersID.equals("")){
                        System.out.println("Choose order to cancel from the pendings orders:\n"+ordersID +"\n");
                        try {
                            oID = Long.parseLong(sc.nextLine());
                            OrdersMenu orders= new OrdersMenu(logicParser);
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
                case "12": { // logout
                    break;
                }
                default:
                    System.out.println("Invalid input");

            }
        } while (!input.equals("12")); // while the user doesn't choose to logout
    }

    public void viewShift() {
        this.shiftMenu.viewShift();
    }

    public void printHistoreyShifts() {
        System.out.println(this.logicParser.getEmployeeParser().getShifts());
    }

    /**
     * Regular employee menu
     */
    private void regularEmployeeMenu() {
        do {
            System.out.println(_loginMenuReg);
            input = sc.nextLine();
            switch (input) {
                case "1": { // change details
                    changeDetails(this.logicParser.getEmployeeParser().getLoggedInEmployee().getID(),"your");
                    break;
                }
                case "2": { // show personal details
                    showPersonalDetails(this.logicParser.getEmployeeParser().getLoggedInEmployee().getID());
                    break;
                }
                case "3": { // logout
                    break;
                }
                default: //invalid input
                    System.out.println("Invalid input");

            }
        } while (!input.equals("3"));
    }
//endregion


//region <Methods of Manger only>

    private void updateShift() {
        this.shiftMenu.updateShift();
    }

    private void createShift() {
        this.shiftMenu.createShift();
    }

    /**
     * Remove an employee (for manager use only)
     */
    private void removeEmployee() {
        System.out.println("Insert ID to remove: ");
        String id=sc.nextLine();
        if(this.logicParser.getDeliveryEmployeeParser().removeEmployee(id)){
            System.out.println("ID '"+id+"' has been successfully removed.");
        }
        else{
            System.out.println("Cannot remove ID '"+id+"'.");
        }
    }

    /**
     * new user registration
     */
    private void registerNewEmployee() {
        register.doRegister();

    }

    /**
     *show personal details of other (use for master)
     */
    private void showOtherPersonalDetails() {
        System.out.print("Choose ID: ");
        String details;
        if((details = this.logicParser.getEmployeeParser().getEmployeeDetails(sc.nextLine()))==null || details.equals("")){
            System.out.println("Employee doest exist."+"\n");
        }
        else{
            System.out.println(details+"\n");
        }
    }

    /**
     * Change other personal details
     */
    private void changeOtherPersonalDetails() {
        System.out.println("Choose ID: ");
        String employeeToChange = sc.nextLine();
        changeDetails(employeeToChange,employeeToChange + "'s");
    }


    //endregion

//region <Methods of manger and regular user>
    /**
     *  printing personal details
     */
    private void showPersonalDetails(String ID) {
        System.out.println(this.logicParser.getEmployeeParser().getEmployeeDetails(ID));
    }
    /**
     * change details of user
     * @param id- id of user you want change
     * @param pronoun - other or your
     */
    private void changeDetails(String id, String pronoun){
        //check if user exists
        if(!this.logicParser.getEmployeeParser().checkIfUserExist(id)){
            System.out.println("The employee '"+id+"' does not exist.");
            return;
        }

        //the menu of change details
        String menuChangeDetails ="\n"
                + "1.\t Change "+pronoun+" first name\n"
                + "2.\t Change "+pronoun+" last name\n"
                + "3.\t Change "+pronoun+" bank account\n"
                + "4.\t Change "+pronoun+" salary\n"
                + "5.\t Change "+pronoun+" terms of employment\n"
                + "6.\t add a new constraint\n"
                + "7.\t remove a constraint\n"
                + "8.\t add a job\n"
                + "9.\t Back";

        //to print this menu and do operation
        do {
            System.out.println(menuChangeDetails);
            input = sc.nextLine();

            switch (input) {
                case "1": { // change first name
                    changeFirstName(id);
                    break;
                }
                case "2": { // change last name
                    changeLastName(id);
                    break;
                }
                case "3": { // change bankAccount
                    changeBankAccount(id);
                    break;
                }
                case "4": { // change salary
                    changeSalary(id);
                    break;
                }
                case "5": { // change termsOfEmployment
                    changeTermsOfEmployment(id);
                    break;
                }
                case "6": { // add constraints
                    changeConstraints(id, true);
                    break;
                }
                case "7": { // remove constraints
                    changeConstraints(id, false);
                    break;
                }
                case "8": { // add a job
                    addJob(id);
                    break;
                }
                case "9": { // back
                    break;
                }
                default: // invalid input
                    System.out.println("Invalid input");
            }
        } while (!input.equals("9"));
    }

    /**
     * Change first name
     * @param id- the id of user you want change the details
     */
    private void changeFirstName(String id) {
        System.out.println("Insert a new first name");
        if(this.logicParser.getEmployeeParser().changeFirstName(sc.nextLine(),id)){
            System.out.println("First name has been successfully changed.");
        }
        else{
            System.out.println("First name change failed.");
        }
    }

    /**
     * Change last name
     * @param id- the id of user you want change the details
     */
    private void changeLastName(String id) {
        System.out.print("Insert a new last name");
        if(this.logicParser.getEmployeeParser().changeLastName(sc.nextLine(),id)){
            System.out.println("Last name has been successfully changed.");
        }
        else{
            System.out.println("Last name change failed.");
        }
    }

    /**
     * change bank Account
     * @param id
     */
    private void changeBankAccount(String id) {
        System.out.print("Insert a new bank account");
        if(this.logicParser.getEmployeeParser().changeBankAccount(sc.nextLine(),id)){
            System.out.println("Bank account has been successfully changed.");
        }
        else{
            System.out.println("Bank account change failed.");
        }
    }

    /**
     * change salary
     * @param id
     */
    private void changeSalary(String id) {
        System.out.print("Insert a new salary");
        if(this.logicParser.getEmployeeParser().changeSalary(sc.nextLine(),id)){
            System.out.println("Salary has been successfully changed.");
        }
        else{
            System.out.println("Salary change failed.");
        }
    }

    private void changeTermsOfEmployment(String id) {
        System.out.print("Insert new terms of employment");
        if(this.logicParser.getEmployeeParser().changeTermsOfEmployment(sc.nextLine(),id)){
            System.out.println("Terms of employment has been successfully changed.");
        }
        else{
            System.out.println("Terms of employment change failed.");
        }
    }

    private void changeConstraints(String id, boolean add) {
        ConstraintsMenu constraintsMenu = new ConstraintsMenu(this.logicParser, this.sc);
        if (add)
            constraintsMenu.addConstraints(id);
        else
            constraintsMenu.removeConstraints(id);
    }

    private void addJob(String id) {
        String newJob = getJobFromUser();
        License license = null;
        if(newJob.equals(EnumTypeJob.driver.toString())){
            license = chooseLicense();
        }
        if (newJob != null){
            if(this.logicParser.getDeliveryEmployeeParser().addJob(newJob, id, license)){
                System.out.println("The job has been successfully added.");
            }
            else{
                System.out.println("The job couldn't be added.");
            }
        }
    }

    private License chooseLicense() {
        String licenseJob = "";
        int numOfLicense = 1;
        for (License e:License.values()) {
            licenseJob = licenseJob + "\n"+numOfLicense+") "+e.toString();
            numOfLicense++;
        }

        String s = null;
        do {
            if(s != null){
                System.out.println("Invalid input. Please try again");
            }
            System.out.println("Choose a number from the list below or -1 if you don't want to back");
            System.out.println(licenseJob);
            s = this.sc.nextLine();
        } while (!s.equals("-1") && !this.logicParser.getEmployeeParser().checkValid(EnumValid.license, s));

        if(s.equals("-1")) {
            return null;
        }
        License []arr=License.values();
        return arr[Integer.parseInt(s)];
    }

    private String getJobFromUser(){
        String jobsOptions = "";
        int numOfJob = 1;
        for (EnumTypeJob e:EnumTypeJob.values()) {
            jobsOptions = jobsOptions + "\n"+numOfJob+") "+e.toString();
            numOfJob++;
        }

        String s = null;
        do {
            if(s != null){
                System.out.println("Invalid input. Please try again");
            }
            System.out.println("Choose a number from the list below or -1 if you don't want to add a job");
            System.out.println(jobsOptions);
            s = this.sc.nextLine();
        } while (!s.equals("-1") && !this.logicParser.getEmployeeParser().checkValid(EnumValid.jobs, s));

        if(s.equals("-1")) {
            return null;
        }
        EnumTypeJob []arr=EnumTypeJob.values();
        return arr[Integer.parseInt(s)-1].toString();
    }

    public String toString(){
        return  "Employee Menu";
    }


    //endregion
}



