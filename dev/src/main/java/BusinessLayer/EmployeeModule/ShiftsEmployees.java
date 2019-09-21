package BusinessLayer.EmployeeModule;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="ShiftsEmployees")
public class ShiftsEmployees implements Serializable {




    ///need add number of fk
    @Id
    @ManyToOne(fetch = FetchType.EAGER ,cascade = {CascadeType.ALL})
    @JoinColumns({
            @JoinColumn(
                    name = "shift_date",
                    referencedColumnName = "date"),
            @JoinColumn(
                    name = "shift_branch",
                    referencedColumnName = "nameBranch"),
            @JoinColumn(
                    name = "shift_timeShift",
                    referencedColumnName = "timeOfShift")
    })
    private Shifts shift;

    @Id
    @JoinColumn(name= "employeeProfile", referencedColumnName = "employeeProfile", foreignKey = @ForeignKey(name = "ID"))
    private EmployeeProfile employeeProfile;

    private String job;

    public ShiftsEmployees(Shifts shift, EmployeeProfile employeeProfile, String job) {
        this.shift = shift;
        this.employeeProfile = employeeProfile;
        this.job=job;
    }
    public ShiftsEmployees(Shifts shift, EmployeeProfile employeeProfile) {
        this.shift = shift;
        this.employeeProfile = employeeProfile;
    }

    public ShiftsEmployees() {
    }

    public Shifts getShift() {
        return shift;
    }

    public void setShift(Shifts shift) {
        this.shift = shift;
    }

    public EmployeeProfile getEmployeeProfile() {
        return employeeProfile;
    }

    public void setEmployeeProfile(EmployeeProfile employeeProfile) {
        this.employeeProfile = employeeProfile;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    @Override
    public String toString() {
        return
                ", employeeProfile=" + employeeProfile +
                ", job='" + job + '\'' ;
    }
}
