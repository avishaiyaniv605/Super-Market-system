package BusinessLayer.EmployeeModule;

import PersistenceLayer.BranchProfile;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name="Shifts")
public class Shifts implements Serializable {
    @Id
    Date date;
    @Id
    @JoinColumn(name= "nameBranch", referencedColumnName = "nameBranch", foreignKey = @ForeignKey(name = "nameBranch"))
    BranchProfile nameBranch;

    @JoinColumn(name= "shiftManager", referencedColumnName = "ID", foreignKey = @ForeignKey(name = "ID"))
    EmployeeProfile shiftManager;

    @OneToMany(targetEntity =ShiftsEmployees.class, fetch =FetchType.EAGER ,cascade = {CascadeType.ALL} ,mappedBy = "shift")
    private List<ShiftsEmployees> shiftsEmployees;

    @Id
    String timeOfShift;

    public Shifts(Date date, BranchProfile nameBranch, EmployeeProfile shiftManager, String timeOfShift) {
        this.nameBranch = nameBranch;
        this.shiftManager = shiftManager;
        this.date = date;
        this.timeOfShift = timeOfShift;
        shiftsEmployees = new LinkedList<ShiftsEmployees>();
    }
    public Shifts(Date date, BranchProfile nameBranch, String timeOfShift) {
        this.nameBranch = nameBranch;
        this.date = date;
        this.timeOfShift = timeOfShift;
        shiftsEmployees = new LinkedList<ShiftsEmployees>();
    }

    public Shifts(Date date, BranchProfile nameBranch, EmployeeProfile shiftManager, Set<ShiftsEmployees> shiftsEmployees, String timeOfShift) {
        this.nameBranch = nameBranch;
        this.shiftManager = shiftManager;
        this.date = date;
        this.timeOfShift = timeOfShift;
        this.shiftsEmployees = new LinkedList<>();
    }

    public Shifts() {
    }

    public BranchProfile getNameBranch() {
        return nameBranch;
    }

    public void setNameBranch(BranchProfile nameBranch) {
        this.nameBranch = nameBranch;
    }

    public EmployeeProfile getShiftManager() {
        return shiftManager;
    }

    public void setShiftManager(EmployeeProfile shiftManager) {
        this.shiftManager = shiftManager;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTimeOfShift() {
        return timeOfShift;
    }

    public void setTimeOfShift(String timeOfShift) {
        this.timeOfShift = timeOfShift;
    }

    public List<ShiftsEmployees> shiftsEmployees(){
        return this.shiftsEmployees;
    }

    public void setShiftsEmployees(List<ShiftsEmployees> shiftsEmployees) {
        this.shiftsEmployees = shiftsEmployees;
    }

    public void addShiftsEmployee(ShiftsEmployees shiftsEmployee){
        this.shiftsEmployees.add(shiftsEmployee);
    }
    public void removeShiftsEmployee(ShiftsEmployees shiftsEmployees){
        this.shiftsEmployees.remove(shiftsEmployees);
    }

    public List<ShiftsEmployees> getEmployeeOnJob(String job){
        List<ShiftsEmployees> list=new ArrayList<>();
        for(ShiftsEmployees employeeJobs:this.shiftsEmployees){
            if(employeeJobs.getJob().equals(job)){
                list.add(employeeJobs);
            }
        }
        return list;
    }
    @Override
    public String toString() {
        return "\n"+
                "nameBranch:\t" + nameBranch.getBranchName()+"\t\t" +
                "\tshiftManager:\t" + shiftManager.getFirstName()+" "+shiftManager.getLastName()+"\n" +
                "date:\t" + date +"\n" +
                "timeOfShift:\t" + timeOfShift  +"\n";
    }
}
