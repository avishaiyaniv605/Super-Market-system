package BusinessLayer.EmployeeModule;

import BusinessLayer.Enums.EnumDaysInWeek;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="EmployeeConstraints")
public class EmployeeConstraints implements Serializable {

    Integer startConstriants;

    Integer endConstriants;

    @Id
    Integer day;
    @Id
    @ManyToOne( cascade = CascadeType.REMOVE)
    @JoinColumn(name= "ID", referencedColumnName = "ID", foreignKey = @ForeignKey(name = "ID"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    EmployeeProfile ID;

    public EmployeeConstraints(Integer day ,Integer startConstriants, Integer endConstriants, EmployeeProfile ID) {
        this.startConstriants = startConstriants;
        this.endConstriants = endConstriants;
        this.ID = ID;
        this.day =day;
    }

    public EmployeeConstraints() {
    }

    public Integer getStartConstriants() {
        return startConstriants;
    }

    public void setStartConstriants(Integer startConstriants) {
        this.startConstriants = startConstriants;
    }

    public Integer getEndConstriants() {
        return endConstriants;
    }

    public void setEndConstriants(Integer endConstriants) {
        this.endConstriants = endConstriants;
    }

    public EmployeeProfile getID() {
        return ID;
    }

    public void setID(EmployeeProfile ID) {
        this.ID = ID;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    @Override
    public String toString() {
        EnumDaysInWeek[] arr=EnumDaysInWeek.values();
        return "Can work in "+arr[day-1].toString()+" hours :["+startConstriants+"-"+endConstriants+"]";
    }

    public boolean checkIfCanWork(int dayInWeek, int start, int end){
        return   start>=startConstriants && end<=endConstriants  && dayInWeek==day;
    }
}
