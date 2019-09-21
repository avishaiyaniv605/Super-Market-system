package BusinessLayer.DeliveryModule;


import BusinessLayer.EmployeeModule.EmployeeProfile;
import BusinessLayer.EmployeeModule.Shifts;
import BusinessLayer.EmployeeModule.ShiftsEmployees;
import BusinessLayer.Enums.License;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Entity(name = "DriverProfile")
public class DriverProfile  implements Serializable  {
    @Id
    @OneToOne
    @JoinColumn(name = "ID", referencedColumnName = "ID", foreignKey = @ForeignKey(name = "ID"))
    private EmployeeProfile ID;
    private License license;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Shifts> shifts;

    public DriverProfile(License license, EmployeeProfile employeeProfile) {
        this.license = license;
        this.ID = employeeProfile;
        this.shifts = new LinkedList<>();
    }

    public DriverProfile() {}


    public License getLicense() {
        return license;
    }

    public void setLicense(License license) {
        this.license = license;
    }

    public EmployeeProfile getID() {
        return ID;
    }

    public void setID(EmployeeProfile ID) {
        this.ID = ID;
    }

    public List<Shifts> getShifts() {
        return shifts;
    }

    public void setShifts(List<Shifts> shifts) {
        this.shifts = shifts;
    }

    public void addOccShift(Shifts shifts){
        this.shifts.add(shifts);
    }
}


