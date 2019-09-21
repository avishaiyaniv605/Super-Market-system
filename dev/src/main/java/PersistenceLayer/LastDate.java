package PersistenceLayer;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Date;

@Entity
public class LastDate{
    @Id
    private Date lastDate;

    public LastDate() {
    }

    public LastDate(Date date) {
        this.lastDate = date;
    }

    public Date getDate() {
        return lastDate;
    }

    public void setDate(Date date) {
        this.lastDate = date;
    }
}