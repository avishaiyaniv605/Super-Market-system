/**
package BusinessLayer.EmployeeModule.test;

import BusinessLayer.EmployeeModule.*;

import BusinessLayer.Enums.EnumTypeJob;
import BusinessLayer.Parsers.EmployeeParser;
import PersistenceLayer.DatabaseManager;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;


public class EmployeeParserTest {
    private EmployeeParser employeeParser;
    private String id="1234568";


    @org.junit.Before
    public void setUp()  {
       employeeParser= new EmployeeParser(new DatabaseManager(""));

    }

    @org.junit.After
    public void tearDown()  {
    }

    @org.junit.Test
    public void register() {
        //register new Employee
        removeEmployee(id);
        employeeParser.register("testEmployeeParser", "testEmployeeParser",id
                ,"testEmployeeParser",200,"testEmployeeParser",0,
                "123456");
        assertTrue(this.employeeParser.login(id,"123456"));
    }

    @org.junit.Test
    public void checkIsManager() {
        register();
        assertTrue(!this.employeeParser.checkIsManager(id));
    }

    @org.junit.Test
    public void getEmployeeDetails() {
        register();
        assertNotNull(this.employeeParser.getEmployeeDetails(id));
    }
    @org.junit.Test
    public void addJob() {
        this.employeeParser.removeEmployeeJobCanDo(id, EnumTypeJob.shiftManager.toString());
        register();
        assertNotNull(this.employeeParser.addJob(EnumTypeJob.shiftManager.toString(),id));

    }


    @org.junit.Test
    public void getEmployeeProfile() {
        register();
        assertNotNull(this.employeeParser.getEmployeeProfile(id));
    }


    @org.junit.Test
    public void changeFirstName() {
        register();
        EmployeeProfile employeeProfile=this.employeeParser.getEmployeeProfile(id);
        String nameBefore=employeeProfile.getFirstName();
        this.employeeParser.changeFirstName(nameBefore +"1",id);
         employeeProfile=this.employeeParser.getEmployeeProfile(id);
        String nameAfter=employeeProfile.getFirstName();
        assertTrue(!nameAfter.equals(nameBefore));
        removeEmployee(id);
    }

    @org.junit.Test
    public void changeLastName() {
        register();
        EmployeeProfile employeeProfile=this.employeeParser.getEmployeeProfile(id);
        String nameBefore=employeeProfile.getLastName();
        this.employeeParser.changeLastName(nameBefore +"1",id);
        employeeProfile=this.employeeParser.getEmployeeProfile(id);
        String nameAfter=employeeProfile.getLastName();
        assertTrue(!nameAfter.equals(nameBefore));
    }

    @org.junit.Test
    public void changeTermsOfEmployment() {
        register();
        EmployeeProfile employeeProfile=this.employeeParser.getEmployeeProfile(id);
        String nameBefore=employeeProfile.getTermsOfEmployment();
        this.employeeParser.changeTermsOfEmployment(nameBefore +"1",id);
        employeeProfile=this.employeeParser.getEmployeeProfile(id);
        String nameAfter=employeeProfile.getTermsOfEmployment();
        assertTrue(!nameAfter.equals(nameBefore));
    }

    @org.junit.Test
    public void addConstraints() {
        this.employeeParser.addConstraints(1,8,15,id);
        List<EmployeeConstraints> list=this.employeeParser.getListConstraintsEmployee(id);
        assertNotNull(list);
    }
    private void removeEmployee(String id){
        this.employeeParser.removeEmployee(id);
    }
}
*/