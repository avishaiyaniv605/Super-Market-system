package PresentationLayer.EmployeeModule;

import BusinessLayer.Enums.EnumTypeJob;
import BusinessLayer.Enums.EnumValid;
import BusinessLayer.Enums.License;
import BusinessLayer.Parsers.LogicParser;

import java.util.Scanner;



@SuppressWarnings("Duplicates")
public class RegisterEmployee {
    private Scanner sc;
    private LogicParser logicParser;

    //region <initial>
    /**
     * constructor
     * @param logicParser- for prevent addition instance of this
     */
    public RegisterEmployee(LogicParser logicParser){
        this.logicParser = logicParser;
        this.sc = new Scanner(System.in);
    }

    //endregion

    //region <Do register>
    /**
     * Do the operation of register
     */
    @SuppressWarnings("JoinDeclarationAndAssignmentJava")
    public void doRegister() {
        boolean successRegister ;
        String firstName, lastName, ID, bankAccount, salary, termsOfEmployment, job;
        Integer isManager = 0; // false

        //first name
        System.out.println("Insert first name: ");
        firstName = sc.nextLine();

        //last name
        System.out.println("Insert last name: ");
        lastName = sc.nextLine();

        //ID
        if ((ID = getStringFromUser(EnumValid.ID, "in order to go back to the menu")) == null)
            return;


        //Bank account
        System.out.println("Insert Bank Account: ");
        bankAccount = sc.nextLine();

        //Salary
        if ((salary = getStringFromUser(EnumValid.salary, "in order to go back to the menu")) == null)
            return;

        //Terms Of Employment
        termsOfEmployment = getStringFromUser(EnumValid.termsOfEmployment, "in order to not add any Terms Of Employment");

        //Do the self Register
        successRegister = logicParser.getEmployeeParser().register(firstName, lastName, ID, bankAccount, Integer.parseInt(salary), termsOfEmployment, isManager);

        //print the status of register
        if(successRegister) { // the register was successful
            System.out.println("\nThe employee '" + firstName +" "+ lastName+ "' has been successfully registered.\n");
        }
        else { // One of the details is wrong
            System.out.println("\nRegistration attempt failed.\n");
            return;
        }

        ConstraintsMenu constraintsMenu = new ConstraintsMenu(this.logicParser, this.sc);
        //constraints
        System.out.println("Insert a constraint:");
        constraintsMenu.addConstraints(ID);

        //jobs

        int counterOfMaxJobCanBe=0;
        do {
            System.out.println("Insert a job:");
            job = getJobFromUser();
            License license =null;
            if ( job !=null && job.equals(EnumTypeJob.driver.toString())){
                license = chooseLicense();
            }
            if(this.logicParser.getDeliveryEmployeeParser().addJob(job, ID, license)) {
                System.out.println("\nJob was added successfully\n");
                counterOfMaxJobCanBe++;
            }
            else if (job!=null)
                System.out.println("\nJob can't be added\n");
        } while (job != null && counterOfMaxJobCanBe< EnumTypeJob.values().length);

        //say to user that he can do all the jobs
        if(counterOfMaxJobCanBe==EnumTypeJob.values().length){
            System.out.println("\nThis Employee can do all the jobs exists !!! \n");
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

    //endregion


    //region <general functions>
    /**
     * function to return valid value from user according  by EnumValid e
     * @param e
     * @param outputString
     * @return
     */
    private String getStringFromUser(EnumValid e, String outputString){
        String s = null;
        do {
            if(s != null){
                System.out.println("\nInvalid input. Please try again\n");
                if(e.equals(EnumValid.ID)){
                    System.out.println("Or The ID exist please try again\n");

                }
            }
            System.out.println("Insert " + e.toString() + " or -1 " + outputString);
            s = this.sc.nextLine();
        } while (!s.equals("-1") && !this.logicParser.getEmployeeParser().checkValid(e, s));

        if(s.equals("-1")) {
            return null;
        }
        return  s;
    }

    //endregion

    //region <Job of Employee>
    private String getJobFromUser(){
        String jobsOptions = "";
        int numOfJob = 1;
        for (EnumTypeJob e:EnumTypeJob.values()) {
            jobsOptions = jobsOptions+numOfJob+") "+e.toString()+"\n";
            numOfJob++;
        }

        String s = null;
        do {
            if(s != null){
                System.out.println("\nInvalid input. Please try again\n");
            }
            System.out.println("Choose a number from the list below or -1 if you don't want to add a job");
            System.out.print(jobsOptions);
            s = this.sc.nextLine();
        } while (!s.equals("-1") && !this.logicParser.getEmployeeParser().checkValid(EnumValid.jobs, s));

        if(s.equals("-1")) {
            return null;
        }
        return EnumTypeJob.values()[Integer.parseInt(s)-1].toString();
    }
//endregion
}
