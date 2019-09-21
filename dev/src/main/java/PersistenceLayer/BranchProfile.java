package PersistenceLayer;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class BranchProfile implements Serializable {
    @Id
    private String branchName;
    private String city;
    private String street;
    private int number;
    private int phoneNuber;
    private String contactName;


    public BranchProfile(String branchName, String city, String street, int number, int phoneNuber, String contactName) {
        this.branchName = branchName;
        this.city = city;
        this.street = street;
        this.number = number;
        this.phoneNuber = phoneNuber;
        this.contactName = contactName;
    }

    public BranchProfile() {}

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getPhoneNuber() {
        return phoneNuber;
    }

    public void setPhoneNuber(int phoneNuber) {
        this.phoneNuber = phoneNuber;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BranchProfile that = (BranchProfile) o;
        return getNumber() == that.getNumber() &&
                getPhoneNuber() == that.getPhoneNuber() &&
                Objects.equals(getBranchName(), that.getBranchName()) &&
                Objects.equals(getCity(), that.getCity()) &&
                Objects.equals(getStreet(), that.getStreet()) &&
                Objects.equals(getContactName(), that.getContactName());
    }

    @Override
    public String toString() {
        return this.branchName+"."+this.city;
    }
}