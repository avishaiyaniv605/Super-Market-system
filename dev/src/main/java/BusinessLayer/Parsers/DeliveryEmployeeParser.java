package BusinessLayer.Parsers;

import BusinessLayer.DeliveryModule.DriverProfile;
import BusinessLayer.EmployeeModule.Shifts;
import BusinessLayer.EmployeeModule.ShiftsEmployees;
import BusinessLayer.Enums.Area;
import BusinessLayer.Enums.EnumShiftsTimes;
import BusinessLayer.Enums.License;
import BusinessLayer.EmployeeModule.EmployeeProfile;
import BusinessLayer.Enums.EnumTypeJob;
import PersistenceLayer.BranchProfile;
import PersistenceLayer.DatabaseManager;

import java.util.LinkedList;
import java.util.List;


public class DeliveryEmployeeParser {

    DatabaseManager databaseManager;
    EmployeeParser employeeParser;
    DeliveryParser deliveryParser;

    public DeliveryEmployeeParser(DatabaseManager databaseManager , EmployeeParser employeeParser, DeliveryParser deliveryParser) {
        this.databaseManager = databaseManager;
        this.deliveryParser = deliveryParser;
        this.employeeParser = employeeParser;
        if (emptyDB()){
            initDeliveryTables();
            initEmployeeTables();
        }

    }

    private boolean emptyDB() {
        SuppliersParser _suppParser= new SuppliersParser();
        return _suppParser.getAllSuppliers().isEmpty();
    }

    public boolean addJob(String job, String eId , License license){
        EmployeeProfile employeeProfile = this.employeeParser.addJob(job,eId);
        if( employeeProfile !=null && job!=null &&  job.equals(EnumTypeJob.driver.toString())){
           return this.deliveryParser.createDriverProfile(license, employeeProfile);
        }
        return  employeeProfile !=null;
    }

    public List<BranchProfile> getBranchProfiles(){
        return this.deliveryParser.getBranchProfiles();
    }

    private void initDeliveryTables() {
        //Destinations initializations
        this.deliveryParser.createDestinationProfile("Susu&Sons", "Tel Aviv", "Arlozorov", 49, 66787, "YOLO", Area.South);
        this.deliveryParser.createDestinationProfile("Moses", "Ranana", "Shlomo Hamelech", 32, 3333, "BOBO", Area.South);
        this.deliveryParser.createDestinationProfile("Agadir", "Haifa", "Ben Zaken", 1, 44434, "AUAU", Area.North);

            this.deliveryParser.createBranchProfile("1", "Beer Sheva", "Ben-Yehuda", 30, 0506661117, "MOKO");
            this.deliveryParser.createBranchProfile("2", "Ashdod", "Ben-Yehuda", 30, 0506671117, "MOKO");
            this.deliveryParser.createBranchProfile("3", "Natanya", "Ben-Yehuda", 30, 333, "MOKO");
            this.deliveryParser.createBranchProfile("4", "Dimona", "Ben-Yehuda", 30, 1111, "MOKO");
            this.deliveryParser.createBranchProfile("5", "Givatayim", "Ben-Yehuda", 30, 1111, "MOKO");
            this.deliveryParser.createBranchProfile("6", "Ramat Gan", "Ben-Yehuda", 30, 1111, "MOKO");
            this.deliveryParser.createBranchProfile("7", "Bat Yam", "Ben-Yehuda", 30, 1111, "MOKO");
            this.deliveryParser.createBranchProfile("8", "Holon", "Ben-Yehuda", 30, 1111, "MOKO");
            this.deliveryParser.createBranchProfile("9", "Lod", "Ben-Yehuda", 30, 1111, "MOKO");
            this.deliveryParser.createBranchProfile("10", "Haifa", "Ben-Yehuda", 30, 1111, "MOKO");
        //Trucks initializations
        this.deliveryParser.createTruckProfile("2005", 1000, 10000, License.A, Area.North);
        this.deliveryParser.createTruckProfile("2006", 1000, 10000, License.A, Area.South);
        this.deliveryParser.createTruckProfile("2007", 2000, 13000, License.B, Area.North);
        this.deliveryParser.createTruckProfile("2008", 2000, 13000, License.B, Area.South);
        this.deliveryParser.createTruckProfile("2009", 3000, 17000, License.C, Area.North);
        this.deliveryParser.createTruckProfile("2010", 3000, 17000, License.C, Area.South);
        this.deliveryParser.createTruckProfile("2011", 4000, 20000, License.D, Area.North);
        this.deliveryParser.createTruckProfile("2012", 4000, 20000, License.D, Area.South);


        //Prooducts initializations
        this.deliveryParser.createProductProfile("Banana", 5);
        this.deliveryParser.createProductProfile("Milk", 10);
        this.deliveryParser.createProductProfile("Mushrooms", 20);
        this.deliveryParser.createProductProfile("Water Mellon", 40);
        this.deliveryParser.createProductProfile("Macbook Pro", 800);
        this.deliveryParser.createProductProfile("Windows-Kaki", 800);
        this.deliveryParser.createProductProfile("Freedom", 1000);
        this.deliveryParser.createProductProfile("Bread", 10);
        this.deliveryParser.createProductProfile("Wine", 100);

    }

    private void initEmployeeTables() {
        addManagserIfDoesntExist();
        addEmployeeWithConstraintsAndJobIfDontExist();
    }

    /**
     * Initial the data base with basic tuples of Employee ,jobs ,constraints
     */
    private void addEmployeeWithConstraintsAndJobIfDontExist() {
        String namesAndTerms[][] = {
                {"Dana", "Danny", "Dan", "Yossi", "Avi", "Nir", "Adele", "Amy", "Eve", "Ava",
                        "Kate", "Lacy", "Adam", "Ben", "Carl", "Cole", "Eli", "Gary", "Ian", "Liam"},
                {"Israeli", "Israeli", "Cohen", "Levi", "Smith", "Brown", "White", "Miller", "Moore", "Clark",
                        "Lopez", "Adams", " Reed", "Bailey", "Ross", "Perry", "Wood", "Diaz", "Ford", "Wells"},
                {"a mother", "a father", null, null, null, "a father", null, null, null, null,
                        "a mother", null, null, null, null, "a student", "a father", "a student", null, "a father"}
        };


        int numOfEmployee = namesAndTerms[0].length;
        int numOfDays = 6; // number of work days

        String[] eId = new String[20];
        for (int i = 0; i < numOfEmployee; i++) {
            eId[i] = (i+1)*100+"";
        }
        this.employeeParser.register("Logistics", "log", "8", "Logistics", 9999, "Logistics Manager", 0);
        this.employeeParser.register("driver 1 ", "log", "3454", "Logistics", 9999, "Logistics Manager", 0);
        this.employeeParser.register("driver 1 ", "log", "34554", "Logistics", 9999, "Logistics Manager", 0);

        this.employeeParser.register("storekeeper 1 ", "log", "77777", "Logistics", 9999, "Logistics Manager", 0);
        this.employeeParser.register("storekeeper 2 ", "log", "88888", "Logistics", 9999, "Logistics Manager", 0);
        this.employeeParser.register("storekeeper 3 ", "log", "88889", "Logistics", 9999, "Logistics Manager", 0);
        this.employeeParser.register("storekeeper 3 ", "log", "88899", "Logistics", 9999, "Logistics Manager", 0);

        this.deliveryParser.createDriverProfile(License.D,this.employeeParser.getEmployeeProfile("3454"));
        this.deliveryParser.createDriverProfile(License.D,this.employeeParser.getEmployeeProfile("34554"));
        this.deliveryParser.createDriverProfile(License.D,this.employeeParser.getEmployeeProfile("88899"));



        addJob(EnumTypeJob.logisticsManager.toString(), "8", null);
        addJob(EnumTypeJob.driver.toString(), "3454", License.D);//
        addJob(EnumTypeJob.driver.toString(), "34554", License.D);//
        addJob(EnumTypeJob.driver.toString(), "88899", License.D);//

        addJob(EnumTypeJob.storekeeper.toString(), "77777", null);
        addJob(EnumTypeJob.storekeeper.toString(), "88888", null);
        addJob(EnumTypeJob.storekeeper.toString(), "88889", null);


        for (int i = 0; i < numOfEmployee; i++) {
            this.employeeParser.register(namesAndTerms[0][i], namesAndTerms[1][i], eId[i],  eId[i], (i + 1) * 1000, namesAndTerms[2][i], 0);

            if (namesAndTerms[2][i]!= null && (namesAndTerms[2][i].compareTo("a father") == 0 || namesAndTerms[2][i].compareTo("a mother") == 0))
                for(int j = 1; j < numOfDays; j++)
                    this.employeeParser.addConstraints(j, 8, 15, eId[i]);

            else if (namesAndTerms[2][i] == null)
                for(int j = 1; j <= numOfDays; j++)
                    this.employeeParser.addConstraints(j, 8, 21, eId[i]);

            else if (namesAndTerms[2][i] != null && namesAndTerms[2][i].compareTo("a student") == 0)
                for(int j = 1; j <= numOfDays; j++)
                    this.employeeParser.addConstraints(j, 15, 21, eId[i]);

            if (i==0) {
                addJob(EnumTypeJob.shiftManager.toString(), eId[i], null);
            }
            else if (i==1) {
                addJob(EnumTypeJob.storekeeper.toString(), eId[i], null);
                addJob(EnumTypeJob.driver.toString(), eId[i], License.A);
                addJob(EnumTypeJob.cashier.toString(), eId[i], null);
            }
            else if (i==2 || i==3) {
                addJob(EnumTypeJob.shiftManager.toString(), eId[i], null);
                addJob(EnumTypeJob.storekeeper.toString(), eId[i], null);
                addJob(EnumTypeJob.cashier.toString(), eId[i], null);
                addJob(EnumTypeJob.driver.toString(), eId[i], License.C);
            }
            else if (i<10){
                addJob(EnumTypeJob.storekeeper.toString(), eId[i], null);
                addJob(EnumTypeJob.driver.toString(), eId[i], License.B);
            }
            else if (i<16){
                addJob(EnumTypeJob.shiftManager.toString(), eId[i], null);
                addJob(EnumTypeJob.cashier.toString(), eId[i], null);
            }
            else {
                addJob(EnumTypeJob.shiftManager.toString(), eId[i], null);
            }


        }

        List<BranchProfile> branchProfiles = this.deliveryParser.getBranchProfiles();

        this.employeeParser.createShift("2019", "9", "1", EnumShiftsTimes.Morning.toString(), branchProfiles.get(0).getBranchName(), eId[0]);
        this.employeeParser.addEmployeeToShift(eId[1], "2019", "9", "1", EnumShiftsTimes.Morning.toString(), branchProfiles.get(0).getBranchName(), EnumTypeJob.storekeeper.toString());
        this.employeeParser.addEmployeeToShift(eId[2], "2019", "9", "1", EnumShiftsTimes.Morning.toString(), branchProfiles.get(0).getBranchName(), EnumTypeJob.cashier.toString());
        this.employeeParser.addEmployeeToShift(eId[3], "2019", "9", "1", EnumShiftsTimes.Morning.toString(), branchProfiles.get(0).getBranchName(), EnumTypeJob.cashier.toString());
        this.employeeParser.addEmployeeToShift(eId[18], "2019", "9", "1", EnumShiftsTimes.Morning.toString(), branchProfiles.get(0).getBranchName(), EnumTypeJob.driver.toString());
        this.employeeParser.addEmployeeToShift("8", "2019", "9", "1", EnumShiftsTimes.Morning.toString(), branchProfiles.get(0).getBranchName(), EnumTypeJob.logisticsManager.toString());
        this.employeeParser.addEmployeeToShift("3454", "2019", "9", "1", EnumShiftsTimes.Morning.toString(), branchProfiles.get(0).getBranchName(), EnumTypeJob.driver.toString());
        this.employeeParser.addEmployeeToShift("7777", "2019", "9", "1", EnumShiftsTimes.Morning.toString(), branchProfiles.get(0).getBranchName(), EnumTypeJob.driver.toString());

        this.employeeParser.createShift("2019", "5", "2", EnumShiftsTimes.Morning.toString(), branchProfiles.get(0).getBranchName(), eId[2]);
        this.employeeParser.addEmployeeToShift(eId[1], "2019", "5", "2", EnumShiftsTimes.Morning.toString(), branchProfiles.get(0).getBranchName(), EnumTypeJob.storekeeper.toString());
        this.employeeParser.addEmployeeToShift(eId[3], "2019", "5", "2", EnumShiftsTimes.Morning.toString(), branchProfiles.get(0).getBranchName(), EnumTypeJob.cashier.toString());
        this.employeeParser.addEmployeeToShift(eId[18], "2019", "5", "2", EnumShiftsTimes.Morning.toString(), branchProfiles.get(0).getBranchName(), EnumTypeJob.driver.toString());
        this.employeeParser.addEmployeeToShift("8", "2019", "5", "2", EnumShiftsTimes.Morning.toString(), branchProfiles.get(0).getBranchName(), EnumTypeJob.logisticsManager.toString());
        this.employeeParser.addEmployeeToShift("1000", "2019", "5", "2", EnumShiftsTimes.Morning.toString(), branchProfiles.get(0).getBranchName(), EnumTypeJob.storekeeper.toString());


        this.employeeParser.createShift("2019", "9", "2", EnumShiftsTimes.Evening.toString(), branchProfiles.get(0).getBranchName(), eId[3]);
        this.employeeParser.addEmployeeToShift(eId[2], "2019", "9", "2", EnumShiftsTimes.Evening.toString(), branchProfiles.get(0).getBranchName(), EnumTypeJob.cashier.toString());
        this.employeeParser.addEmployeeToShift(eId[18], "2019", "9", "2", EnumShiftsTimes.Evening.toString(), branchProfiles.get(0).getBranchName(), EnumTypeJob.driver.toString());
        this.employeeParser.addEmployeeToShift("8", "2019", "9", "2", EnumShiftsTimes.Evening.toString(), branchProfiles.get(0).getBranchName(), EnumTypeJob.logisticsManager.toString());
        this.employeeParser.addEmployeeToShift("1000", "2019", "9", "2", EnumShiftsTimes.Evening.toString(), branchProfiles.get(0).getBranchName(), EnumTypeJob.driver.toString());
        this.employeeParser.addEmployeeToShift("88888", "2019", "9", "2", EnumShiftsTimes.Evening.toString(), branchProfiles.get(0).getBranchName(), EnumTypeJob.storekeeper.toString());

        this.employeeParser.createShift("2019", "4", "15", EnumShiftsTimes.Evening.toString(), branchProfiles.get(1).getBranchName(), eId[3]);
        this.employeeParser.addEmployeeToShift(eId[18], "2019", "4", "15", EnumShiftsTimes.Evening.toString(), branchProfiles.get(1).getBranchName(), EnumTypeJob.driver.toString());
        this.employeeParser.addEmployeeToShift("88899", "2019", "4", "15", EnumShiftsTimes.Evening.toString(), branchProfiles.get(1).getBranchName(), EnumTypeJob.storekeeper.toString());

        this.employeeParser.addEmployeeToShift("8", "2019", "9", "15", EnumShiftsTimes.Evening.toString(), branchProfiles.get(1).getBranchName(), EnumTypeJob.logisticsManager.toString());
}

    public void addManagserIfDoesntExist() {
        if (!this.employeeParser.checkIfUserExist("9")) {
            String eId = "9";
            this.employeeParser.register("Manager", "Manager", eId, "Manager", 9999, "Manager", 1);
            addJob(EnumTypeJob.shiftManager.toString(), eId, null);
            addJob(EnumTypeJob.storekeeper.toString(),eId, null);
            addJob(EnumTypeJob.cashier.toString(), eId, null);
            addJob(EnumTypeJob.logisticsManager.toString(), eId, null);
            addJob(EnumTypeJob.storeManger.toString(), eId, null);
            addJob(EnumTypeJob.driver.toString(), eId, License.C);
        }
    }

    public boolean removeEmployee(String id){
        if(this.deliveryParser.removeDriverProfile(id)){
           return this.employeeParser.removeEmployee(id);
        }
        else{
            return false;
        }
    }

    /**
     * Returns a list of all the drivers in the @chooseShift shift
     * @param chooseShift - Shift number to get the drivers from
     * @return - List of shift's drivers, null if there are none
     */
    public List<DriverProfile> getDriversInShift(Integer chooseShift){
        List<ShiftsEmployees> shiftsEmployees = employeeParser.getListEmployeeInJobAccordingByOrder(chooseShift, "Driver" );
        List<DriverProfile> driverProfiles = new LinkedList<>();
        if(shiftsEmployees != null) {
            for (ShiftsEmployees emp : shiftsEmployees) {
                if(emp.getJob().equals("Driver")) {
                    DriverProfile driver = deliveryParser.getDriveProfile(emp.getEmployeeProfile().getID());
                    if(driver!=null)
                        driverProfiles.add(driver);
                }
            }
            return driverProfiles;
        }
        return null;
    }



    public boolean isThereStorekeeperInShift(Integer chooseShift){
        List<ShiftsEmployees> shiftsEmployees = employeeParser.getListEmployeeInJobAccordingByOrder(chooseShift, "Storekeeper" );
        boolean isExist = !shiftsEmployees.isEmpty();
        return isExist;
    }

    public boolean isDriverFreeThisShift(){
        return false;
    }



}
