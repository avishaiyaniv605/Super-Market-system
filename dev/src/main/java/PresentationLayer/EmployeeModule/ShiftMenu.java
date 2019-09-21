package PresentationLayer.EmployeeModule;

import BusinessLayer.Enums.EnumShiftsTimes;
import BusinessLayer.Enums.EnumTypeJob;
import BusinessLayer.Enums.EnumValid;
import BusinessLayer.Parsers.LogicParser;

import java.util.List;
import java.util.Scanner;


public class ShiftMenu {

    private LogicParser logicParser;
    private Scanner sc;
    private String menuEditShift;
    private String menuRemoveAdd;

    //region <Initial>

    /**
     * constructor
     * @param logicParser
     * @param sc
     */
    public ShiftMenu(LogicParser logicParser, Scanner sc) {
        this.logicParser=logicParser;
        this.sc=sc;
        initMenu();
    }

    /**
     * initial the menu
     */
    private void initMenu(){
        menuRemoveAdd="Choose one from the options below or type -1 in order to go back to the menu:\n";
        menuEditShift="Choose a job type from the options below or type -1 in order to go back to the menu:";
        menuRemoveAdd=menuRemoveAdd+"1) add Employee to Shift\n"
                +"2) remove Employee from shift\n";

        EnumTypeJob e[]=EnumTypeJob.values();
        int numOfJobs=e.length;
        for(int i=0;i<numOfJobs;i++){
            menuEditShift=menuEditShift+"\n"+(i+1)+")"+e[i].toString();
        }
    }

    //endregion

    //region <create shift>

    public void createShift() {
        String year,month,day,timeShift, branch, shiftManager;
        if((year=getStringFromUser(EnumValid.year, ""))==null || (month=getValidMonthFromUser(year))==null ||
                (day=getValidDayFromUser(year,month))==null || (timeShift=getStringFromUser(EnumValid.timeShift, ""))==null
                || (branch=chooseBranch())==null){
            return;
        }

        //check if the shift exists
        if(this.logicParser.getEmployeeParser().checkIfShiftExist(branch, year, month, day, timeShift)){
            System.out.println("The shift already exists.");
            return;
        }

        //check if there is shift manager
        if((shiftManager = getShiftManger(EnumTypeJob.shiftManager,year,month,day,timeShift,branch))==null){
            System.out.println("\nShift can't be created since there isn't a shift manger who can work at these hours."
                    +"\n"+"Please add a new constraint to one of the shift managers and try again\n");
            return;
        }
        if(this.logicParser.getEmployeeParser().createShift(year,month,day,timeShift, branch, shiftManager)){
            System.out.println("\nThe shift was created successfully.\n");
        }
        else {
            System.out.println("\nThe shift can't be created.\n");
            return;
        }
        add(year,month,day,timeShift, branch);
    }

    private String getValidMonthFromUser(String year) {
        String s=null;
        do{
            if(s!=null){
                System.out.println("Invalid input. Please try again.");
            }
            System.out.println("Insert month or -1 in order to go back to the menu.");
            s = this.sc.nextLine();
        }while (! s.equals("-1") && !this.logicParser.getEmployeeParser().checkIsValidMonth( s,year) );

        if(s.equals("-1")) {
            return null;
        }
        return  s;
    }

    /**
     *
     * @param enumTypeJob
     * @param year
     * @param month
     * @param day
     * @param timeShift
     * @param branch
     * @return
     */
    private String getShiftManger(EnumTypeJob enumTypeJob, String year, String month, String day, String timeShift, String branch){
        return this.logicParser.getEmployeeParser().getIdOfMangerShiftCanWork( year, month, day, timeShift, branch, enumTypeJob.toString());
    }

    //endregion

    //region <General>
    private String getFirstValueOfString(String s){
        String [] arr= s.split(" ");
        String options = "";
        for (int i=0; i<arr.length; i++){
            options = options + (i+1) +")"+arr[i];
        }
        return options;
    }

    private String getStringFromUser(EnumValid e, String printStr){
        String s=null;
        do{
            if(s!=null){
                System.out.println("Invalid input. Please try again.");
            }
            System.out.println("Insert "+e.toString()+printStr+" or -1 in order to go back to the menu");
            s = this.sc.nextLine();
        }while (! s.equals("-1") && !this.logicParser.getEmployeeParser().checkValid(e, s) );

        if(s.equals("-1")) {
            return null;
        }
        if(e.equals(EnumValid.timeShift)){
            EnumShiftsTimes []arr=EnumShiftsTimes.values();
            s=arr[Integer.parseInt(s)-1].toString();
        }
        return  s;
    }

    private String getValidDayFromUser(String year, String month){
        String s=null;
        do{
            if(s!=null){
                System.out.println("Invalid input. Please try again.");
            }
            System.out.println("Insert day that exists in the month and the year you choose or -1 in order to go back to the menu");
            s = this.sc.nextLine();
        }while (! s.equals("-1") && !this.logicParser.getEmployeeParser().checkIsValidDay( s,year,month) );

        if(s.equals("-1")) {
            return null;
        }
        return  s;
    }

    private String chooseBranch(){
        List<String> list=this.logicParser.getEmployeeParser().createListStringBranch(this.logicParser.getDeliveryEmployeeParser().getBranchProfiles());
        return getUserChoiceFromLIst(list,"BranchProfile");
    }


    private String getUserChoiceFromLIst(List<String> list, String listString){
        String s = null;
        int counter = 1;
        listString = listString+"\n"+
                "Choose a number from the list below or type -1 in order to go back to the menu\n";

        for(String employee: list) {
            listString = listString +counter+ ") "+employee+"\n";
            counter++;
        }

        do{
            if(s!=null){
                System.out.println("Invalid input. Please try again.");
            }
            if (list.size()==0)
                return null;
            System.out.print(listString);
            s = this.sc.nextLine();
        } while (!s.equals("-1") && !this.logicParser.getEmployeeParser().checkIsValidRange(list.size(), s) );

        if(s.equals("-1")) {
            return null;
        }

        return list.get(Integer.parseInt(s)-1);
    }

    //endregion

    //region <update shift >

    /**
     * update the shift check if exist if yes , enable edit shift
     */
    public void updateShift() {

        List<String> shiftString= this.logicParser.getEmployeeParser().convertListShiftToString(this.logicParser.getEmployeeParser().getShiftFromThisDate());
        if(shiftString == null || shiftString.isEmpty()){
            System.out.println("There are no shifts that can be updated, since there are no shifts that are scheduled to take place.");
            return;
        }
        String menuShift="Choose a number that you want update from the list below or type -1 in order to go back to the menu\n";

        for(int i=0;i<shiftString.size();i++){
            menuShift=menuShift+(i+1)+")\t"+shiftString.get(i);
        }
        String number=null;
        do{
            if(number!=null){
                System.out.println("Invalid input. Please try again.");
            }
            System.out.println(menuShift);

            number=sc.nextLine();
        }while (!this.logicParser.getEmployeeParser().checkIsValidRange(shiftString.size(),number) && !number.equals("-1"));

        if(number.equals("-1")){
            return;
        }
        Integer choose=Integer.parseInt(number)-1;
        editTheShift( choose);
    }

    private void editTheShift( Integer choose){
        String s=null;
        int numOfOptionMenuRemove = 2;
        do{
            if(s!=null){
                System.out.println("Invalid input. Please try again.");
            }
            System.out.println(this.menuRemoveAdd);
            s = this.sc.nextLine();
        }while (! s.equals("-1") && !this.logicParser.getEmployeeParser().checkIsValidRange(numOfOptionMenuRemove, s));

        if(s.equals("-1")) {
            return ;
        }
        if(s.equals("1")){
            String []arrOfShift=this.logicParser.getEmployeeParser().getArrOfShift(choose);
             add(arrOfShift[0],arrOfShift[1],arrOfShift[2],arrOfShift[3],arrOfShift[4]);
        }
        if(s.equals("2")){
            removeEmployeeFromShift(choose);
        }

    }
    //endregion

    //region <remove employee>
    private void removeEmployeeFromShift(Integer choose) {
        EnumTypeJob []arr=EnumTypeJob.values();
        String s=null;
        do {
            if (s != null) {
                System.out.println("Invalid input. Please try again.");
            }
            System.out.println(menuEditShift);
            s = this.sc.nextLine();
            if (this.logicParser.getEmployeeParser().checkIsValidRange(arr.length, s)) {
                Integer chooseJob=Integer.parseInt(s) - 1;
                if (arr[chooseJob].toString().equals(EnumTypeJob.shiftManager)) {
                    System.out.println("The shift manager can't be removed from the shift.");
                    return;
                }
                removeEmployeeFromShift(choose,arr[chooseJob].toString());
                return;
            } else {
                if (!s.equals("-1"))
                    s = null;
            }
        } while (!s.equals("-1"));
    }

    private void removeEmployeeFromShift(Integer chooseShift, String chooseJob) {
        List<String> employeelistString = this.logicParser.getEmployeeParser().createListStringEmployeesWorking(this.logicParser.getEmployeeParser().getListEmployeeInJobAccordingByOrder(chooseShift, chooseJob));

        if (employeelistString == null || employeelistString.isEmpty()) {
            System.out.println("There are no available employees for this job");
            return;
        }
        if (employeelistString != null) {
            String menuRemove = "\nChoose a number from the list below that you want to remove or type -1 in order to go back to the menu\n";
            String s = null;
            for (int i = 0; i < employeelistString.size(); i++) {
                menuRemove = menuRemove + (i + 1) + ")\t" + employeelistString.get(i)+"\n";
            }
            do {
                if (s != null) {
                    System.out.println("Invalid input. Please try again.");
                }
                System.out.println(menuRemove);
                s = sc.nextLine();
            } while (!s.equals("-1") && !this.logicParser.getEmployeeParser().checkIsValidRange(employeelistString.size(), s));
            if (s.equals(-1)) {
                return;
            }
            Integer chooseEmployeeInt = Integer.parseInt(s);
            String choiceFromList = employeelistString.get(chooseEmployeeInt);
            if (this.logicParser.getEmployeeParser().removeEmployeeFromShift(chooseShift, chooseJob, chooseEmployeeInt)) {
                System.out.println("The employee " + choiceFromList + " was removed from the shift");
            } else {
                System.out.println("The employee " + choiceFromList + " can't be removed from the shift");
            }
        }
    }

    //endregion

    //region <add Employee to shift>

    /**
     * add employee to job select the specif job
     * @param year
     * @param month
     * @param day
     * @param timeShift
     * @param branch
     */
    private void add(String year,String month,String day,String timeShift,String branch){
        // String add="Choose a number from the list below that you want to add or type -1 in order to go back to the menu\n";
        String add=this.menuEditShift;
        String s=null;

        do{
            if(s != null){
                System.out.println("Invalid input. Please try again.");
            }
            System.out.println(add);
            s=this.sc.nextLine();
            if(this.logicParser.getEmployeeParser().checkIsValidRange(EnumTypeJob.values().length, s)){
                EnumTypeJob[] arr=EnumTypeJob.values();
                if(arr[Integer.parseInt(s)-1].equals(EnumTypeJob.shiftManager)){
                    System.out.println("There can be only one shift manager in a shift.\n");
                    s=null;
                    continue;
                }
                addEmployeeForJobToSUM(arr[Integer.parseInt(s)-1],year,month,day,timeShift, branch);
                s=null;
            }
            else{
                if(!s.equals("-1"))
                    s=null;
            }
        }while (s== null || !s.equals("-1"));
    }

    /**
     * add employee according by the job and to number
     * @param enumTypeJob
     * @param year
     * @param month
     * @param day
     * @param timeShift
     * @param branch
     * @return
     */
    private int addEmployeeForJobToSUM(EnumTypeJob enumTypeJob, String year, String month, String day, String timeShift, String branch){
        String input;
        if((input=getStringFromUser(EnumValid.numberPositvie, " that represents the amount of employees you'd like to have doing the chosen job"))==null){
            return 0;
        }
        int numOfEmployees=Integer.parseInt(input);
        int numberOfUseradded;
        if ((numberOfUseradded=this.logicParser.getEmployeeParser().addNumOfEmployeeOnJob(numOfEmployees, year, month, day, timeShift, branch, enumTypeJob))!=0){
            System.out.println("\n"+numberOfUseradded+" employees out of the "+numOfEmployees+ " that were requested to work as "+enumTypeJob.toString()+"s, were added to the shift.\n");
            return numberOfUseradded;
        }
        else{
            System.out.println("There are no available employees that can work as "+enumTypeJob.toString()+"s at this time\n" );
            return 0;
        }

    }

    //endregion


    /**
     * update the shift check if exist if yes , enable edit shift
     */
    public void viewShift() {

        List<String> shiftString= this.logicParser.getEmployeeParser().convertListShiftToString(this.logicParser.getEmployeeParser().getShifts());
        if(shiftString == null || shiftString.isEmpty()){
            System.out.println("There are no shifts that you can view that have yet to take place.");
            return;
        }
        String menuShift="Choose a number from the list below that you want to view details or type -1 in order to go back to the menu\n";

        for(int i=0;i<shiftString.size();i++){
            menuShift=menuShift+(i+1)+")\t"+shiftString.get(i);
        }
        String number=null;
        do{
            if(number!=null){
                System.out.println("Invalid input. Please try again");
            }
            System.out.println(menuShift);

            number=sc.nextLine();
        }while (!this.logicParser.getEmployeeParser().checkIsValidRange(shiftString.size(),number) && !number.equals("-1"));

        if(number.equals("-1")){
            return;
        }
        Integer choose=Integer.parseInt(number)-1;
        System.out.println(this.logicParser.getEmployeeParser().getDetailsOfShift(choose));
    }

}
