package PresentationLayer.EmployeeModule;

import BusinessLayer.EmployeeModule.*;
import BusinessLayer.Parsers.LogicParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class ConstraintsMenu {

    private LogicParser logicParser;
    private Scanner sc;

    //region <Initial>
    /**
     * constructor
     * @param logicParser
     * @param sc
     */

    public ConstraintsMenu(LogicParser logicParser, Scanner sc){
        this.logicParser = logicParser;
        this.sc = sc;
    }
    //endregion

    //region <add constraints>
    /**
     * add constraints
     * @param ID
     */

    public void addConstraints(String ID) {
        String constriants = null;
        while (constriants==null || !constriants.equals("-1")) {
             constriants = null;
            int[] arrConstriants;
            do {
                if (constriants != null) {
                    System.out.println("\nConstraint can't be added (input is not valid or there is another constraint in the this time)\n");
                }
                System.out.println("Write the available work day and hours:[day_number-start_hour-end_hour] (Example: 2-8-12) or type -1 for back");
                constriants = this.sc.nextLine();
            } while (((arrConstriants = checkInputConstrinats(constriants, ID)) == null || this.logicParser.getEmployeeParser().checkIfExistConstraintsAtThisDay(ID,arrConstriants[0])) && !constriants.equals("-1"));

            if (constriants.equals("-1")) {
                return;
            }

            if (arrConstriants != null && this.logicParser.getEmployeeParser().addConstraints(arrConstriants[0], arrConstriants[1], arrConstriants[2], ID))
                System.out.println("\nConstraint was added successfully\n");
            else
                System.out.println("\nConstraint can't be added in these hour's\n");

        }

    }
//endregion

    //region <Remove constraints>

    public void removeConstraints(String id) {
        List<EmployeeConstraints> list = this.logicParser.getEmployeeParser().getListConstraintsEmployee(id);

        List<String> employeeConstraintsString = new ArrayList<>();
        if (list != null) {
            if (list != null) {
                for (EmployeeConstraints e : list) {
                    employeeConstraintsString.add(e.getDay()+ "-" +e.getStartConstriants() + "-" + e.getEndConstriants());
                }
            }

            if (list == null || list.isEmpty()) {
                System.out.println("The employee " + id + " has no constraints");
                return;
            }

            String choiceFromList = getUserChoiceFromLIst(employeeConstraintsString);

            int[] arr = checkInputConstrinats(choiceFromList,id);

            if (arr !=null && this.logicParser.getEmployeeParser().removeConstraint(id, arr[0]) ) {
                System.out.println("The constraint '" + choiceFromList + "' was removed from " + id + "'s constraints list");
            } else {
                System.out.println("Can't remove the constraint '" + choiceFromList + "' from " + id + "'s constraints list");
            }
        }
    }



    private String getUserChoiceFromLIst(List<String> list){
        String s = null;
        int counter = 1;
        String listString ="\n"+
                "Choose a number from the list below or type -1 in order to go back to the menu\n";

        for(String employee: list) {
            listString = listString +counter+ ") "+employee+"\n";
            counter++;
        }

        do{
            if(s!=null){
                System.out.println("Invalid input. Please try again");
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

    //region <General>
    /**
     * check if the input is valid
     * @param constriants
     * @param ID
     * @return
     */
    private int[] checkInputConstrinats(String constriants,String ID){
        String []dayHours=constriants.split("-");
        int[] result=null;
        if(dayHours!=null && dayHours.length==3 && this.logicParser.getEmployeeParser().checkIsInteger(dayHours[1])
                && this.logicParser.getEmployeeParser().checkIsInteger(dayHours[2]) && this.logicParser.getEmployeeParser().checkIsInteger(dayHours[2])){
            result=new int[3];
            result[0]=Integer.parseInt(dayHours[0]);
            result[1]=Integer.parseInt(dayHours[1]);
            result[2]=Integer.parseInt(dayHours[2]);

            if(!this.logicParser.getEmployeeParser().checkIsValidConstraints(ID,result[0],result[1],result[2])){
                return null;
            }
        }
        return result;
    }
//endregion
}
