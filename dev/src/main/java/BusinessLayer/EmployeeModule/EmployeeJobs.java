package BusinessLayer.EmployeeModule;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="EmployeeJobs")
public class EmployeeJobs implements Serializable {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name= "ID", referencedColumnName = "ID", foreignKey = @ForeignKey(name = "ID"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private EmployeeProfile ID;

    @Id
    private String job;


    public EmployeeJobs(EmployeeProfile ID, String job) {
        this.ID = ID;
        this.job = job;
    }
    public  EmployeeJobs(){
    }

    public EmployeeProfile geteID() {
        return ID;
    }

    public void seteID(EmployeeProfile ID) {
        this.ID = ID;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    @Override
    public String toString() {
        return job;
    }
}
