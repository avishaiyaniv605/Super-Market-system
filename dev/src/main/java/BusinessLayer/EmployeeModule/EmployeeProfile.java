package BusinessLayer.EmployeeModule;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import static javax.persistence.TemporalType.DATE;

@Entity
@Table(name = "EmployeeProfile")
public class EmployeeProfile implements Serializable {

    private String firstName;
    private String lastName;
    @Id
    private String ID;
    private String bankAccount;
    private Integer salary;
    private String termsOfEmployment;
    @Temporal(DATE)
    private Date startDate;
    private Integer isManager;


    public EmployeeProfile(String firstName, String lastName, String ID,String bankAccount, Integer salary,
                           String termsOfEmployment, Date startDate , Integer isManger) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.ID = ID;
        this.bankAccount=bankAccount;
        this.salary=salary;
        this.termsOfEmployment=termsOfEmployment;
        this.startDate=startDate;
        this.isManager=isManger;
    }
    public  EmployeeProfile(){

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public String getTermsOfEmployment() {
        return termsOfEmployment;
    }

    public void setTermsOfEmployment(String termsOfEmployment) {
        this.termsOfEmployment = termsOfEmployment;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Integer getIsManager() {
        return isManager;
    }

    public void setIsManager(Integer isManager) {
        this.isManager = isManager;
    }


    @Override
    public String toString() {
        return "\nEmployee Details:\n" +
                "First Name: \t\t\t" + firstName  +
                "\nLast Name: \t\t\t\t" + lastName  +
                "\nID: \t\t\t\t\t" + ID+
                "\nbankAccount: \t\t\t" + bankAccount  +
                "\nsalary: \t\t\t\t" + salary  +
                "\ntermsOfEmployment: \t\t" + termsOfEmployment  +
                "\nstartDate: \t\t\t\t" + startDate.toString();
    }

}

